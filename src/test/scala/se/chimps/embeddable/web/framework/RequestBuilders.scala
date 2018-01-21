package se.chimps.embeddable.web.framework

import se.chimps.embeddable.web.framework.api.{Bytes, Form, HttpRequest}

trait RequestBuilders {
	def get(url:String):HttpRequest = HttpRequest("GET", url, Map(), Map(), None)

	def post(url:String, body:String):HttpRequest = HttpRequest("POST", url, Map(), Map(), Some(Bytes(body.getBytes("utf-8"))))

	def post(url:String, body:Map[String, String]):HttpRequest = HttpRequest("POST", url, Map(), Map(), Some(Form(body)))

	def put(url:String, body:String):HttpRequest = HttpRequest("PUT", url, Map(), Map(), Some(Bytes(body.getBytes("utf-8"))))

	def put(url:String, body:Map[String, String]):HttpRequest = HttpRequest("PUT", url, Map(), Map(), Some(Form(body)))

	def delete(url:String):HttpRequest = HttpRequest("DELETE", url, Map(), Map(), None)

	def head(url:String):HttpRequest = HttpRequest("HEAD", url, Map(), Map(), None)

	def patch(url:String, body:String):HttpRequest = HttpRequest("PATCH", url, Map(), Map(), Some(Bytes(body.getBytes("utf-8"))))

	def patch(url:String, body:Map[String, String]):HttpRequest = HttpRequest("PATCH", url, Map(), Map(), Some(Form(body)))
}
