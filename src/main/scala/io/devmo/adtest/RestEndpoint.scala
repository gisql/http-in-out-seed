package io.devmo.adtest

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.Flow

trait RestEndpoint {
  this: Sys with SomeRemoteServiceSlice =>

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import akka.http.scaladsl.server.Directives._
  import spray.json.DefaultJsonProtocol._

  def routes: Flow[HttpRequest, HttpResponse, Any] = logRequestResult("http-logger") {
    baseRoute
  }

  private lazy val baseRoute = get {
    path("single") {
      complete {
        timed("GET single") {
          someService.resourceSize("https://www.clearscore.com/")
        }
      }
    } ~ path("multiple") {
      complete {
        timed("GET multiple") {
          someService.multiple
        }
      }
    }
  }
}
