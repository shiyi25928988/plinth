package yi.shi.plinth.demo;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


import com.google.inject.Inject;
import yi.shi.plinth.annotation.Restful;
import yi.shi.plinth.annotation.RequestBody;
import yi.shi.plinth.http.result.JSON;
import yi.shi.plinth.jetty.JettyBootService;
import lombok.AllArgsConstructor;
import lombok.Data;

@Restful
public class HelloWord {

	@Inject
	JettyBootService jettyBootService;

	@GET
	@Path(value = "/hello")
	public JSON<String> hello() {
		return new JSON<String>("Hello world");
	}
	
	@GET
	@Path(value = "/hello2")
	public JSON<TestPojo> hello2() {
		return new JSON<TestPojo>(new TestPojo("SHIYI","30"));
	}
	
	@GET
	@Path(value = "/hello3")
	public JSON<String> hello3(@PathParam(value = "name") String name) {
		return new JSON<String>("Hello " + name);
	}

	@POST
	@Path(value = "/hello4")
	public JSON<String> hello4(@RequestBody String name) {
		return new JSON<String>("Hello " + name);
	}

	@POST
	@Path(value = "/hello5")
	public JSON<String> hello4(@RequestBody DemoObj user) {
		return new JSON<String>("Hello " + user.getName());
	}

	@GET
	@Path(value = "/SHUTDOWN")
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
