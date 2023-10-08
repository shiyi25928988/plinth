package yi.shi.plinth.http;

import yi.shi.plinth.servlet.ServletHelper;
import yi.shi.plinth.utils.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public final class HttpReqHelper {

    /**
     * @param clazz
     * @return
     * @throws IOException
     */
    public static Object getRequestPostBody(Class<?> clazz) throws IOException {
        HttpServletRequest request = ServletHelper.getRequest();
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return JsonUtils.fromJson(buffer, clazz);
    }
}
