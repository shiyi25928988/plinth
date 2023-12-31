package yi.shi.plinth.http.controller;

import yi.shi.plinth.annotation.http.HttpService;
import yi.shi.plinth.annotation.http.HttpPath;
import yi.shi.plinth.http.result.HTML;

import yi.shi.plinth.annotation.http.Method.GET;
import java.io.IOException;

@HttpService
public class HttpStatusController {


    @GET
    @HttpPath(value = "/404")
    public HTML notFound() throws IOException {
        return new HTML().setUrl("/static/http/404.html");
    }

}
