package yi.shi.plinth.http.controller;

import yi.shi.plinth.annotation.Restful;
import yi.shi.plinth.annotation.http.HttpPath;
import yi.shi.plinth.http.MimeType;
import yi.shi.plinth.http.result.BINARY;

import yi.shi.plinth.annotation.http.Method.GET;

@Restful
public class FaviconController {
    @GET
    @HttpPath("/favicon.ico")
    public BINARY getIcon(){
        BINARY binary = new BINARY();
        binary.setData(this.getClass().getResourceAsStream("/static/icon/favicon.ico"));
        binary.setMimeType(MimeType.IMAGE_ICON);
        return binary;
    }
}
