package yi.shi.plinth.modules;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;

import com.google.inject.servlet.GuiceFilter;
import yi.shi.plinth.servlet.DispatcherServlet;
import yi.shi.plinth.servlet.GuiceServletCustomContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * @author shiyi
 *
 */
public class JettyModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(ServletContextHandler.class).toProvider(ServletContextHandlerProvider.class);
		bind(Server.class).toProvider(ServerProvider.class);
		bind(ServletContext.class).toProvider(ServletContextProvider.class);
	}
	private static class ServletContextHandlerProvider implements Provider<ServletContextHandler> {
		@Override
		public ServletContextHandler get() {
			return new ServletContextHandler(ServletContextHandler.SESSIONS);
		}
	}

	private static class ServletContextProvider implements Provider<ServletContext> {
		@Inject
		ServletContextHandler servletContextHandler;

		@Override
		public ServletContext get() {
			return servletContextHandler.getServletContext();
		}
	}

	private static class ServerProvider implements Provider<Server> {
		
		private int port;

		@Inject
		ServletContextHandler servletContextHandler;

		@Override
		public Server get() {
			String _hybrid = System.getProperty("server.hybrid", "false");
			Boolean hybrid = Boolean.parseBoolean(_hybrid);
			HandlerList handlerList = new HandlerList();
			if(hybrid){
				//within hybrid mode , only 'server.resources.folder' works, api path will share context path with staic resource
				servletContextHandler.setContextPath("/");
				servletContextHandler.addServlet(DispatcherServlet.class, "/*");
				servletContextHandler.addFilter(
						GuiceFilter.class,
						"/*",
						EnumSet.of(DispatcherType.REQUEST));
				servletContextHandler.getServletHandler()
						.addListener(new ListenerHolder(GuiceServletCustomContextListener.class));
				servletContextHandler.insertHandler(getResourceHandler());
				handlerList.addHandler(servletContextHandler);
			}else{
				servletContextHandler.setContextPath("/");
				servletContextHandler.addServlet(DispatcherServlet.class, "/*");
				servletContextHandler.addFilter(
						GuiceFilter.class,
						"/*",
						EnumSet.of(DispatcherType.REQUEST));
				servletContextHandler.getServletHandler()
						.addListener(new ListenerHolder(GuiceServletCustomContextListener.class));
				handlerList.addHandler(servletContextHandler);
				//non-hybrid mode will separate api and static resource context
				ServletContextHandler resourceHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
				resourceHandler.setContextPath(System.getProperty("server.resources.context", ("/static/*")));
				resourceHandler.insertHandler(getResourceHandler());
				handlerList.prependHandler(resourceHandler);
			}

			port = Integer.parseInt(System.getProperty("server.port", "8080"));
			Server server = new Server(port);
			server.setStopAtShutdown(true);
			server.setHandler(handlerList);
			return server;
		}

		private ResourceHandler getResourceHandler() {
			try {
				String fileStoragePath = System.getProperty("server.resources.folder", System.getProperty("user.dir")+ File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static");
				Resource res = Resource.newResource(fileStoragePath, false);
				ResourceHandler resourceHandler = new ResourceHandler();
				resourceHandler.setDirectoriesListed(true);
				resourceHandler.setBaseResource(res);
				resourceHandler.setDirAllowed(true);
				return resourceHandler;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

}
