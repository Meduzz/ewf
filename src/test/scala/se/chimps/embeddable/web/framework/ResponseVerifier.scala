package se.chimps.embeddable.web.framework

import org.scalatest.Matchers
import se.chimps.embeddable.web.framework.api.HttpResponse

trait ResponseVerifier { self:Matchers =>

	def verify(resp:HttpResponse, code:Int, headers:Map[String, String], body:Response):Unit = {
		resp.code shouldBe code
		resp.headers.size shouldBe headers.size
		headers.keySet.foreach(k => {
			resp.headers(k) shouldBe headers(k)
		})
		body match {
			case Equals(str) => str.getBytes("utf-8") shouldBe resp.body
			case Contains(str) => new String(resp.body, "utf-8").contains(str) shouldBe true
			case Empty => resp.body.isEmpty shouldBe true
			case Ignore =>
		}
	}

}

trait Response
case class Equals(str:String) extends Response
case class Contains(str:String) extends Response
case object Empty extends Response
case object Ignore extends Response
