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

class ServerActor extends HttpServiceActor with ActorLogging {

  import context.dispatcher

  implicit val timeout = Timeout(15.second)

  val cache: Cache[String] = LruCache()

  import Path.`/`

  def receive = runRoute {
    pathPrefix("fxserver") {
      (pathPrefix("compile") & post) {
        entity(as[Array[Byte]]) { (prog : Array[Byte]) =>
          complete {
            import scala.sys.process._
            log.info("Compiling a program:")

            val input = new ByteArrayInputStream(prog)
            val cmd = "fxc --flapjax /fx/flapjax.js --stdin --stdout --web-mode"
            val r = cmd #< input
            r.lines_!.mkString("\n")
          }
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
            value
          }
        }
      }  ~
      (path("setobj" / Rest)  & post) { (key : String) =>
        entity(as[String]) { (value : String) =>
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
