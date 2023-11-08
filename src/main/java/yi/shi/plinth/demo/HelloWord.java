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
	@HttpPath(value = "/hello2")
	public JSON<TestPojo> hello2() {
		return new JSON<TestPojo>(new TestPojo("SHIYI","30"));
	}
	
	@GET
	@HttpPath(value = "/hello3")
	public JSON<String> hello3(@HttpParam(value = "name") String name) {
		return new JSON<String>("Hello " + name);
	}

	@POST
	@HttpPath(value = "/hello4")
	public JSON<String> hello4(@HttpBody String name) {
		return new JSON<String>("Hello " + name);
	}

	@POST
	@HttpPath(value = "/hello5")
	public JSON<String> hello4(@HttpBody DemoObj user) {
		return new JSON<String>("Hello " + user.getName());
	}

	@GET
	@HttpPath(value = "/SHUTDOWN")
	public JSON<String> shutdown() {
		jettyBootService.stop();
		return new JSON<String>("shutting down...");
	}



	@Data
	@AllArgsConstructor
	class TestPojo{
		String name;
		String age;
	}
}
