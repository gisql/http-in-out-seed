package com.clearscore.adtest

import akka.actor.ActorSystem
import akka.stream.Materializer
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

trait Sys {
  implicit def system: ActorSystem
  implicit lazy val dispatcher: ExecutionContextExecutor = system.dispatcher
  implicit def materializer: Materializer

  private val logger = LoggerFactory.getLogger("timer")

  def timed[T](tag: String)(task: Future[T]): Future[T] = {
    logger.debug(s"Starting action $tag")

    val start = System.currentTimeMillis()
    task onComplete {
      case Success(_) =>
        val duration = System.currentTimeMillis() - start
        logger.info(s"Action $tag took ${duration}ms")
        if (duration > 500) logger.warn(s"Long running action $tag took ${duration}ms")
      case Failure(e) =>
        val duration = System.currentTimeMillis() - start
        logger.error(s"Action $tag FAILED after ${duration}ms", e)
    }
    task
  }
}

class SysProvider(implicit val system: ActorSystem, val materializer: Materializer) extends Sys