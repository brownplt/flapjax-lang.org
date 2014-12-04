package edu.umass.cs.flapjax.server

import akka.util.Timeout
import akka.actor.{ActorSystem, Props, Actor, ActorRef}
import akka.io.IO
import spray.can.Http
import scala.concurrent._
import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("flapjax-server")
  implicit val log = system.log
  import system.dispatcher
  
  implicit val timeout = Timeout(120.second)

  lazy val server = system.actorOf(Props(classOf[ServerActor]), "server")

  args match {
    case Array("serve") =>
      IO(Http) ! Http.Bind(server, interface = "127.0.0.1", port = 8081)
    case args =>
      import scala.collection.JavaConversions._
      println(s"Invalid arguments: ${args.toList}")
      system.shutdown
  }

}
