package yi.shi.plinth.http;

import yi.shi.plinth.http.result.ErrorMessage;

import java.io.IOException;

public final class HttpErrorRespHelper {


    /**
     * 401 Unauthorized
     * */
    public static void send401() throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("401 Unauthorized", HttpStatusCode.SC_UNAUTHORIZED);
        HttpRespHelper.sendResponseData(errorMessage, HttpStatusCode.SC_UNAUTHORIZED);
    }
    public static void send401Page() {
    }

    /**
     * 403 Forbidden
     * */
    public static void send403(String msg) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("403 Forbidden ".concat(msg), HttpStatusCode.SC_FORBIDDEN);
        HttpRespHelper.sendResponseData(errorMessage, HttpStatusCode.SC_FORBIDDEN);
    }
    public static void send403Page() {
    }

    /**
     * 404 not found
     * */
    public static void send404() throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("404 Not Found", HttpStatusCode.SC_NOT_FOUND);
        HttpRespHelper.sendResponseData(errorMessage, HttpStatusCode.SC_NOT_FOUND);
    }
    public static void send404Page() {

    }

    /**
     * 405 method not allowed
     * */
    public static void send405() throws IOException {
        ErrorMessage errorMessage = new ErrorMessage("405 method not allowed", HttpStatusCode.SC_METHOD_NOT_ALLOWED);
        HttpRespHelper.sendResponseData(errorMessage, HttpStatusCode.SC_METHOD_NOT_ALLOWED);
    }

    public static void send500(String errMessage) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(errMessage, HttpStatusCode.SC_INTERNAL_SERVER_ERROR);
        HttpRespHelper.sendResponseData(errorMessage, HttpStatusCode.SC_INTERNAL_SERVER_ERROR);
    }
    public static void send500Page(String errMessage){}


}
