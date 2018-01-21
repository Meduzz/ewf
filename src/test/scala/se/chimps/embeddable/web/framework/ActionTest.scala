package se.chimps.embeddable.web.framework

import org.scalatest.{FunSuite, Matchers}
import se.chimps.embeddable.web.framework.api.{HttpResponse, ResponseBuilders}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ActionTest extends FunSuite with Matchers with ResponseBuilders with RequestBuilders {

	test("async is async") {

		val action = Async { req =>
			Future(created())
		}

		val request = get("/")
		val response = action.handle(request)

		response.isInstanceOf[Future[HttpResponse]] shouldBe true
	}

	test("sync is async (under the hood)") {

		val action = Sync { req =>
			created()
		}

		val request = get("/")
		val response = action.handle(request)

		response.isInstanceOf[Future[HttpResponse]] shouldBe true
	}
}
