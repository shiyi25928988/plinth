package yi.shi.plinth.demo;

import yi.shi.plinth.annotation.http.Method.GET;
import yi.shi.plinth.annotation.http.Method.POST;

import com.google.inject.Inject;
import yi.shi.plinth.annotation.http.HttpService;
import yi.shi.plinth.annotation.http.HttpBody;
import yi.shi.plinth.annotation.http.HttpParam;
import yi.shi.plinth.annotation.http.HttpPath;
import yi.shi.plinth.http.result.JSON;
import yi.shi.plinth.jetty.JettyBootService;
import lombok.AllArgsConstructor;
import lombok.Data;

@HttpService
public class HelloWord {

	@Inject
	JettyBootService jettyBootService;

	@GET
	@HttpPath(value = "/hello")
	public JSON<String> hello() {
		return new JSON<String>("Hello world");
	}
	

	@GET
	@HttpPath(value = "/SHUTDOWN")
	public JSON<String> shutdown() {
		jettyBootService.stop();
		return new JSON<String>("shutting down...");
	}

}
