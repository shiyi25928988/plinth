package yi.shi.plinth.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaTokenContextForThreadLocal;
import cn.dev33.satoken.context.SaTokenContextForThreadLocalStorage;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.servlet.model.SaResponseForServlet;
import cn.dev33.satoken.servlet.model.SaStorageForServlet;

import yi.shi.plinth.auth.RoleStpInterface;
import yi.shi.plinth.rest.RestApiService;
import yi.shi.plinth.rest.RestApiServiceImpl;
import lombok.extern.slf4j.Slf4j;

public class DispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String METHOD_DELETE = "DELETE";
	private static final String METHOD_HEAD = "HEAD";
	private static final String METHOD_GET = "GET";
	private static final String METHOD_OPTIONS = "OPTIONS";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_TRACE = "TRACE";

	RestApiService restService = new RestApiServiceImpl();
	
	private ServletContext servletContext;

	@Override
	public void init(ServletConfig config) throws ServletException {
		servletContext = config.getServletContext();
	}

	@Override
	public void destroy() {
		ServletHelper.destory();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletHelper.init(servletContext, req, resp);
		initSaToken();//为SA-TOKEN 加载servlet context
		switch (req.getMethod()) {
		case METHOD_DELETE:
			try {
				restService.doDelete();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_HEAD:
			try {
				restService.doHead();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_GET:
			try {
				restService.doGet();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_OPTIONS:
			try {
				restService.doOptions();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_POST:
			try {
				restService.doPost();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_PUT:
			try {
				restService.doPut();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		case METHOD_TRACE:
			try {
				restService.doTrace();
			} catch (Exception e){
				throw new ServletException(e.getCause());
			}
			break;
		default:
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "HTTP METHOD NOT SUPPORT");

		}
	}

	private static void initSaToken(){
		SaRequest saRequest = new SaRequestForServlet(ServletHelper.getRequest());
		SaResponse saResponse = new SaResponseForServlet(ServletHelper.getResponse());
		SaStorage storage = new SaStorageForServlet(ServletHelper.getRequest());
		SaTokenContextForThreadLocalStorage.setBox(saRequest, saResponse, storage);
		SaManager.setSaTokenContext(new SaTokenContextForThreadLocal());
		SaManager.setStpInterface(new RoleStpInterface());
	}

}
