# The embeddable web framework
Is more than a lame name.

## Usage

### In your sbt:

    resolvers += "se.chimps.ewf" at "https://yamr.kodiak.se/maven"
    libraryDependencies += "se.chimps.ewf" %% "ewf" % "20180121"

### In your "server" code:

Enjoy full routing with :routing parameters in your apis or simple servers.

    		val subject = Controller()
    	  	.GET("/:a/:b/:c", Sync { req =>
    			  val a = req.params("a")
    			  val b = req.params("b")
    			  val c = req.params("c") // query param c=1 will be overwritten by path param c=c
    
    			  ok(s"$a $b $c", "text/plain")
    		  })


### Calling:

Just map your old ugly or poor http api to a request:HttpRequest and call:

    val response:Future[HttpResponse] = subject.handle(request)