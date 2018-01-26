package se.chimps.embeddable.web.framework

import se.chimps.embeddable.web.framework.api.HttpResponse
import scala.concurrent.ExecutionContext.Implicits.global

object Defaults {

	val notFoundHtml:String =
		"""
			|<html>
			|  <head>
			|   <title>Not found.</title>
			|  </head>
			|  <body>
			|    <h1>Not found</h1>
			|    <p>%url% was not found.</p>
			|  </body>
			|</html>
		""".stripMargin

	val errorHtml:String =
		"""
			|<html>
			|		<head>
			|			<title>An error occured.</title>
			|		</head>
			|		<body>
			|			<h1>An error occured.</h1>
			|			<pre>
			|				%error%
			|			</pre>
			|		</body>
			|</html>
		""".stripMargin

	def notFound:Action = Sync { req => {
		HttpResponse(404, Map("Content-Type" -> "text/html"), notFoundHtml.toString.replace("%url%", req.url).getBytes("utf-8"))
	}}

	def errorMapping:PartialFunction[Throwable, HttpResponse] = {
		case e:Throwable => {
			val msg = "<p>" + e.getStackTrace.map(stackTrace2Line).mkString("</p><p>") + "</p>"
			HttpResponse(500, Map("Content-Type" -> "text/html"), errorHtml.toString.replace("%error%", msg).getBytes("utf-8"))
		}
	}

	def stackTrace2Line(el:StackTraceElement):String = {
		s"${el.getClassName}.${el.getMethodName} (${el.getFileName}:${el.getLineNumber})"
	}
}
