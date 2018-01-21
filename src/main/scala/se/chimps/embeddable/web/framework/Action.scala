package se.chimps.embeddable.web.framework

import se.chimps.embeddable.web.framework.api.{HttpRequest, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

trait Action {
	def handle(req:HttpRequest):Future[HttpResponse]
}
trait Async extends Action
trait Sync extends Action

object Async {
	def apply(func:(HttpRequest) => Future[HttpResponse]):Async = new Async {
		override def handle(req:HttpRequest):Future[HttpResponse] = func(req)
	}
}

object Sync {
	def apply(func:(HttpRequest) => HttpResponse)(implicit ec:ExecutionContext):Sync = new Sync {
		override def handle(req:HttpRequest):Future[HttpResponse] = Future(func(req))
	}
}
