package yi.shi.plinth.http.controller;

import yi.shi.plinth.annotation.Restful;
import yi.shi.plinth.http.MimeType;
import yi.shi.plinth.http.result.BINARY;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Restful
public class FaviconController {
    @GET
    @Path("/favicon.ico")
    public BINARY getIcon(){
        BINARY binary = new BINARY();
        binary.setData(this.getClass().getResourceAsStream("/static/icon/favicon.ico"));
        binary.setMimeType(MimeType.IMAGE_ICON);
        return binary;
    }
}
