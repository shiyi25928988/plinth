package yi.shi.plinth.demo;

import yi.shi.plinth.annotation.cache.ApiCache;
import yi.shi.plinth.annotation.http.Method.GET;

import com.google.inject.Inject;
import yi.shi.plinth.annotation.http.HttpService;
import yi.shi.plinth.annotation.http.HttpPath;
import yi.shi.plinth.http.result.JSON;
import yi.shi.plinth.jetty.JettyBootService;

@HttpService
public class HelloWord {

	@Inject
	JettyBootService jettyBootService;

	@GET
	@HttpPath(value = "/hello")
//	@AUTH
	@ApiCache(name = "hello")
	public JSON<String> hello() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new JSON<String>("Hello world");
	}
	

	@GET
	@HttpPath(value = "/SHUTDOWN")
	public JSON<String> shutdown() {
		jettyBootService.stop();
		return new JSON<String>("shutting down...");
	}

}
