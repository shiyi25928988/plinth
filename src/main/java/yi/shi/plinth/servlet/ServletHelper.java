package yi.shi.plinth.servlet;

import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yshi
 *
 */
public final class ServletHelper {

	private static final ThreadLocal<ServletHelper> SERVLET_HELPER_HOLDER = new ThreadLocal<>();
	
	private ServletContext context;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	/**
	 * @param context
	 * @param req
	 * @param resp
	 */
	private ServletHelper(ServletContext context, HttpServletRequest req, HttpServletResponse resp) {
		this.context = context;
		this.req = req;
		this.resp = resp;
	}
	
	/**
	 * @return ServletContext
	 */
	private ServletContext getServletContext() {
		return this.context;
	}
	
	/**
	 * @return HttpServletRequest
	 */
	private HttpServletRequest getHttpServletRequest() {
		return this.req;
	}
	
	/**
	 * @return HttpServletResponse
	 */
	private HttpServletResponse getHttpServletResponse() {
		return this.resp;
	}
	
	/**
	 * @param context
	 * @param req
	 * @param resp
	 */
	public static void init(ServletContext context, HttpServletRequest req, HttpServletResponse resp) {
		SERVLET_HELPER_HOLDER.set(new ServletHelper(context, req, resp));
	}
	
	/**
	 * @param req
	 * @param resp
	 */
	public static void init(HttpServletRequest req, HttpServletResponse resp) {
		SERVLET_HELPER_HOLDER.set(new ServletHelper(null, req, resp));
	}
	
	/**
	 * 
	 */
	public static void destory() {
		SERVLET_HELPER_HOLDER.remove();
	}
	
	/**
	 * @return ServletContext
	 */
	public static ServletContext getContext() {
		return SERVLET_HELPER_HOLDER.get().getServletContext();
	}
	
	/**
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return SERVLET_HELPER_HOLDER.get().getHttpServletRequest();
	}
	
	/**
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		return SERVLET_HELPER_HOLDER.get().getHttpServletResponse();
	}
	
	/**
	 * @return
	 */
	public static String getRealPath() {
		if(Objects.nonNull(getContext())) {
			return getContext().getRealPath("/");
		}else {
			return "";
		}
	}
}
