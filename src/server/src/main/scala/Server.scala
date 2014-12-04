package edu.umass.cs.flapjax.server

import akka.util.{Timeout}
import scala.concurrent._
import spray.can.Http
import spray.http._
import spray.http.Uri.Path
import spray.caching.{Cache, LruCache}
import scala.concurrent.duration._
import spray.routing._
import akka.actor.ActorLogging
import java.io.{ByteArrayInputStream}
import akka.event.Logging

class StringProcessLogger extends scala.sys.process.ProcessLogger {

  private val stderrBuilder = new StringBuilder()
  private val stdoutBuilder = new StringBuilder()

  def buffer[T](f : => T) : T = f

  def appender (builder : StringBuilder, s : => String) : Unit = {
    builder.append(s).append("\n")
  }

  def err (builder : => String) = appender(stderrBuilder, builder)

  def out (builder : => String) = appender(stdoutBuilder, builder)

  def stderr () = stderrBuilder.result

  def stdout () = stdoutBuilder.result
}


class ServerActor extends HttpServiceActor with ActorLogging {

  import context.dispatcher

  implicit val timeout = Timeout(15.second)

  val cache: Cache[String] = LruCache()

  import Path.`/`

  def receive = runRoute {
    pathPrefix("fxserver") {
      (path("compile" / Rest) & post) { (key : String) =>
        entity(as[Array[Byte]]) { (prog : Array[Byte]) =>
          import scala.sys.process._
          log.info("Compiling a program:")
          val input = new ByteArrayInputStream(prog)
          val cmd = "fxc --flapjax /fx/flapjax.js --stdin --stdout --web-mode"
          val outputs = new StringProcessLogger()
          val exitCode = cmd #< input !<(outputs)
          cache(key) { outputs.stdout }
          log.info(s"Compiler exited with code ${exitCode} on stored program ${key} ")
          complete { outputs.stderr }
        }
      }  ~
      (pathPrefix("compile_expr") & post) {
        entity(as[Array[Byte]]) { (prog : Array[Byte]) =>
          complete {
            import scala.sys.process._
            val input = new ByteArrayInputStream(prog)
            val cmd = "fxc --flapjax /fx/flapjax.js --stdin --stdout --expression"
            val r = cmd #< input
            r.lines_!.mkString("\n")
          }
        }
      }  ~
      (path("getobj" / Rest) & get) { (key : String) =>
        cache.get(key) match {
          case None => complete {
            "false" // JSON false
          }
          case Some(value) => complete {
            HttpEntity(ContentType(MediaTypes.`text/html`), Await.result(value, 5.second))
          }
        }
      }  ~
      (path("setobj" / Rest)  & post) { (key : String) =>
        entity(as[String]) { (value : String) =>
          val _ = cache.remove(key)
          cache(key) { value }
          complete {
            "true" // JSON true
          }
        }
      }
    } ~
    logRequest(("invalid request", Logging.ErrorLevel)) {
      complete(StatusCodes.NotFound, "unexpected request")
    }
  }

}
