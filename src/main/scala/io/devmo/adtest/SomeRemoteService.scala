package com.clearscore.adtest

import scala.concurrent.Future

trait SomeRemoteService {
  def resourceSize(url: String): Future[Map[String, Int]]
}

trait SomeRemoteServiceSlice {
  this: Sys =>

  val someService: SomeRemoteService = (url: String) => {
    Future(Map(url -> 333))
  }
}
