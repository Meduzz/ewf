package se.chimps.embeddable.web.framework

import org.scalatest.Matchers
import se.chimps.embeddable.web.framework.api.HttpResponse

trait ResponseVerifier { self:Matchers =>

	def verify(resp:HttpResponse, code:Int, headers:Map[String, String], body:Array[Byte]):Unit = {
		resp.code shouldBe code
		resp.headers.size shouldBe headers.size
		headers.keySet.foreach(k => {
			resp.headers(k) shouldBe headers(k)
		})
		resp.body shouldBe body
	}

}
