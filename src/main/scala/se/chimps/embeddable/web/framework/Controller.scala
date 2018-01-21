package se.chimps.embeddable.web.framework

import se.chimps.embeddable.web.framework.api.{HttpRequest, HttpResponse, ResponseBuilders}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

case class Controller(routes:Seq[Route] = Seq()) extends ResponseBuilders {
	def GET(url:String, action:Action):Controller = {
		copy(routes = routes :+ register("GET", url, action))
	}

	def POST(url:String, action:Action):Controller = {
		copy(routes = routes :+ register("POST", url, action))
	}

	def PUT(url:String, action:Action):Controller = {
		copy(routes = routes :+ register("PUT", url, action))
	}

	def DELETE(url:String, action:Action):Controller = {
		copy(routes = routes :+ register("DELETE", url, action))
	}

	def HEAD(url:String, action: Action):Controller = {
		copy(routes = routes :+ register("HEAD", url, action))
	}

	def PATCH(url:String, action: Action):Controller = {
		copy(routes = routes :+ register("PATCH", url, action))
	}

	def other(method:String, url:String, action:Action):Controller = {
		copy(routes = routes :+ register(method, url, action))
	}

	def handle(request:HttpRequest)(implicit ec:ExecutionContext):Future[HttpResponse] = {
		routes
			.filter(r => r.method == request.method)
			.find(r => {
				val matcher = r.regex.pattern.matcher(request.url)
				matcher.matches()
			}) match {
			case Some(r) => {
				val matcher = r.regex.pattern.matcher(request.url)

				if (matcher.matches()) {
					val groupVals = (1 to matcher.groupCount()).map(i => matcher.group(i))
					val params = r.params.zip(groupVals).toMap
					val req = params.foldLeft(request)((a,b) => a.withParam(b._1, b._2))
					r.action.handle(req)
				} else {
					Future(notFound())
				}
			}
			case None => {
				Future(notFound())
			}
		}
	}

	private def register(method:String, url:String, handler:Action):Route = {
		val (regex, params) = regexify(url)
		Route(method, regex, params, handler)
	}

	private def regexify(url:String):(Regex, Seq[String]) = {
		if (url.contains(":")) {
			var paramNames = Seq[String]()
			val r2 = ":([a-zA-Z0-9]*|[a-zA-Z0-9]*(.*))".r

			val newItems = url.substring(1).split("/").map {
				case r2(paramName, regex) => {
					val index = paramName.indexOf("(")
					val name = if (index != -1) {
						paramName.substring(0, index)
					} else {
						paramName
					}

					paramNames = paramNames ++ Seq(name)
					if (regex != null) {
						regex
					} else {
						"([a-zA-Z0-9]+)"
					}
				}
				case r2(name) => {
					"([a-zA-Z0-9]+)"
				}
				case u => u
			}

			(s"/${newItems.mkString("/")}".r, paramNames)
		} else {
			(url.r, Seq())
		}
	}
}

case class Route(method:String, regex:Regex, params:Seq[String], action:Action)