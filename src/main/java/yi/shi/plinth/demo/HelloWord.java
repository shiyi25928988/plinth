package yi.shi.plinth.demo;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import yi.shi.plinth.annotation.cache.LocalCache;
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
	@LocalCache(name = "hello")
	public JSON<String> hello() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new JSON<String>("Hello world");
	}

	@GET
	@HttpPath(value = "/hello2")
	public JSON<SaSession> hello2() {
		StpUtil.login("root");
		SaSession session = StpUtil.getSessionByLoginId("root", true);
		return new JSON<SaSession>(session);
	}
	

	@GET
	@HttpPath(value = "/SHUTDOWN")
	public JSON<String> shutdown() {
		jettyBootService.stop();
		return new JSON<String>("shutting down...");
	}

}
