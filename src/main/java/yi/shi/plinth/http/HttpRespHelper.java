package yi.shi.plinth.http;

import java.io.*;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import yi.shi.plinth.exception.UnsupportMIMETypeException;
import yi.shi.plinth.http.result.JSON;
import yi.shi.plinth.http.result.ReturnType;
import yi.shi.plinth.servlet.ServletHelper;
import yi.shi.plinth.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * @author yshi
 */
@Slf4j
public final class HttpRespHelper {

    /**
     * To prevent instantiated.
     */
    private HttpRespHelper() {
        throw new RuntimeException();
    }

    /**
     * @param data
     * @throws UnsupportMIMETypeException
     * @throws IOException
     */
    public static void sendResponseData(Object data, int httpStatusCode) throws IOException {
        if (Objects.nonNull(data)) {
            if (data instanceof ReturnType) {
                sendResponseData((ReturnType<?>) data, httpStatusCode);
            } else {
                sendResponseData(new JSON(data), httpStatusCode);
            }
        } else {
            log.error("RestHelper#sendResponseData send a null object, cause of a void return type method");
            throw new NullPointerException();
        }
    }

    /**
     * @param responseData
     * @throws IOException
     */
    private static void sendResponseData(final ReturnType<?> responseData, final int httpStatusCode) throws IOException {
        HttpServletResponse resp = ServletHelper.getResponse();
        String data = "";

        if (responseData.getData() instanceof String) {
            data = (String) responseData.getData();
        } else if(responseData.getData() instanceof InputStream){
            IOUtils.copy((InputStream) responseData.getData(), resp.getOutputStream());
            resp.setStatus(httpStatusCode);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType(responseData.getMimeType().getType());
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
            resp.flushBuffer();
            return;
        } else {
            try {
                data = JsonUtils.toJson(responseData.getData());
            } catch (JsonProcessingException e) {
                log.error(e.getLocalizedMessage());
                resp.setStatus(HttpStatusCode.SC_INTERNAL_SERVER_ERROR);
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                writer.write("Internal Service Error : " + e.getLocalizedMessage());
                writer.flush();
                writer.close();
                return;
            }
        }
        resp.setStatus(httpStatusCode);
        resp.setContentType(responseData.getMimeType().getType());
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.write(data);
        writer.flush();
        writer.close();
    }

}
