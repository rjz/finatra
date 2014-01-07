package com.twitter.finatra

import com.twitter.finatra.test.FlatSpecHelper

class RequestResponseSpec extends FlatSpecHelper {

  class ExampleApp extends Controller {
    get("/") { request =>
      request.response.setStatusCode(429)
      render.plain("hello world").toFuture
    }
  }

  val server = new FinatraServer
  server.register(new ExampleApp)

  "Response" should "be tied to the request" in {
    get("/")
    response.code should equal (429)
  }

}
