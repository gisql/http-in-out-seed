package io.devmo.adtest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object Main extends App {
  implicit lazy val system = ActorSystem()
  implicit lazy val materializer = ActorMaterializer()
  implicit lazy val executor = system.dispatcher

  private val logger = LoggerFactory.getLogger("main")
  private val port = 5000
  try {
    logger.info(s"Starting App Dynamics Test")

    logger.info(s"Starting HTTP service on port: $port")
    val app = new SysProvider with RestEndpoint with SomeRemoteServiceSlice
    Http().bindAndHandle(app.routes, "0.0.0.0", port) onComplete {
      case Failure(e) =>
        logger.error("Failed to bind HTTP service.  Shutting down.", e)
        system.terminate()
      case Success(_) =>
        logger.info("HTTP Startup complete")
    }
  } catch {
    case NonFatal(e) =>
      logger.error("Error during initialisation.  Shutting down.", e)
      system.terminate()
  }
}