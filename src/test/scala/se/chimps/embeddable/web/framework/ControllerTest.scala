package se.chimps.embeddable.web.framework

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSuite, Matchers}
import se.chimps.embeddable.web.framework.api.{Bytes, Form, HttpRequest, ResponseBuilders}

import scala.concurrent.ExecutionContext.Implicits.global

class ControllerTest extends FunSuite with Matchers with RequestBuilders with ResponseBuilders with ResponseVerifier with ScalaFutures {

	test("methods") {
		val subject = Controller()
	  	.GET("/", Sync { req => ok("get", "") })
	  	.POST("/", Sync { req => ok("post", "") })
	  	.PUT("/", Sync { req => ok("put", "") })
	  	.DELETE("/", Sync { req => ok("delete", "") })
	  	.HEAD("/", Sync { req => ok("head", "") })
	  	.PATCH("/", Sync { req => ok("patch", "") })
	  	.other("ASDF", "/", Sync { req => ok("asdf", "") })

		val getRes = subject.handle(get("/"))
		val postRes = subject.handle(post("/", ""))
		val putRes = subject.handle(put("/", ""))
		val deleteRes = subject.handle(delete("/"))
		val headRes = subject.handle(head("/"))
		val patchRes = subject.handle(patch("/", ""))
		val asdfRes = subject.handle(HttpRequest("ASDF", "/", Map(), Map(), None))

		whenReady(getRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "get".getBytes("utf-8"))
		}

		whenReady(postRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "post".getBytes("utf-8"))
		}

		whenReady(putRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "put".getBytes("utf-8"))
		}

		whenReady(deleteRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "delete".getBytes("utf-8"))
		}

		whenReady(headRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "head".getBytes("utf-8"))
		}

		whenReady(patchRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "patch".getBytes("utf-8"))
		}

		whenReady(asdfRes) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "asdf".getBytes("utf-8"))
		}
	}

	test("routing") {
		val subject = Controller()
	  	.GET("/:a/:b/:c", Sync { req =>
			  val a = req.params("a")
			  val b = req.params("b")
			  val c = req.params("c") // query param c=1 will be overwritten by path param c=c

			  ok(s"$a $b $c", "text/plain")
		  })

		val response = subject.handle(get("/a/b/c").withParam("c", "1"))
		val notfound = subject.handle(post("/", "test"))

		whenReady(response) { res =>
			verify(res, 200, Map("Content-Type" -> "text/plain"), "a b c".getBytes("utf-8"))
		}

		whenReady(notfound) { res =>
			verify(res, 404, Map("Content-Type" -> "text/plain"), "Not found.".getBytes("utf-8"))
		}
	}

	test("posting") {
		val subject = Controller()
			.POST("/halp", Sync { req =>
				req.body match {
					case Some(body) => body match {
						case Form(data) => ok(s"HALP me ${data("name")}", "")
						case Bytes(bs) => ok(s"HALP me ${new String(bs, "utf-8")}", "")
					}
					case None => badRequest()
				}
			})

		val formPost = subject.handle(post("/halp", Map("name" -> "form")))
		val bytesPost = subject.handle(post("/halp", "bytes"))

		whenReady(formPost) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "HALP me form".getBytes("utf-8"))
		}

		whenReady(bytesPost) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "HALP me bytes".getBytes("utf-8"))
		}
	}

	test("putting") {
		val subject = Controller()
			.PUT("/halp", Sync { req =>
				req.body match {
					case Some(body) => body match {
						case Form(data) => ok(s"HALP me ${data("name")}", "")
						case Bytes(bs) => ok(s"HALP me ${new String(bs, "utf-8")}", "")
					}
					case None => badRequest()
				}
		})

		val formPut = subject.handle(put("/halp", Map("name" -> "form")))
		val bytesPut = subject.handle(put("/halp", "bytes"))

		whenReady(formPut) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "HALP me form".getBytes("utf-8"))
		}

		whenReady(bytesPut) { res =>
			verify(res, 200, Map("Content-Type" -> ""), "HALP me bytes".getBytes("utf-8"))
		}
	}
}
