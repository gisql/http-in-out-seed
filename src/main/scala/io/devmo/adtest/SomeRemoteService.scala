package io.devmo.adtest

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}

import scala.concurrent.Future

trait SomeRemoteService {
  def resourceSize(url: String): Future[(String, Int)]
  def multiple: Future[Map[String, Int]]
}

trait SomeRemoteServiceSlice {
  this: Sys =>

  val someService: SomeRemoteService = new SomeRemoteService {
    override def resourceSize(url: String): Future[(String, Int)] = timed(s"$url") {
      Http().singleRequest(HttpRequest(uri = url)) flatMap {
        case HttpResponse(StatusCodes.OK, _, entity, _) =>
          entity.dataBytes.runFold(0)({ case (acc, x) => acc + x.size }).map(size => url -> size)
        case _ => Future(url -> -1)
      }
    }
    private def summary(in: List[(String, Int)]): Map[String, Int] =
      (("total", in.map(_._2).sum) :: in).toMap
    override def multiple: Future[Map[String, Int]] =
      Future.sequence(List(
        "https://www.google.com/",
        "https://www.facebook.com/",
        "https://www.bbc.co.uk/",
        "https://www.clearscore.com/",
        "https://api.dev.clearscore.io/app1",
        //        "http://ipv4.download.thinkbroadband.com/5MB.zip",
        "https://clearscore.com/caesium-api/version"
      ).map(resourceSize)).map(summary)
  }
}
