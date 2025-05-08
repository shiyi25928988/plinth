package yi.shi.plinth.modules;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;


import com.google.inject.servlet.GuiceFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import org.eclipse.jetty.ee10.servlet.ListenerHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import yi.shi.plinth.servlet.DispatcherServlet;
import yi.shi.plinth.servlet.GuiceServletCustomContextListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
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
			}else{
				servletContextHandler.setContextPath("/");
				servletContextHandler.addServlet(DispatcherServlet.class, "/*");
				servletContextHandler.addFilter(
						GuiceFilter.class,
						"/*",
						EnumSet.of(DispatcherType.REQUEST));
				servletContextHandler.getServletHandler()
						.addListener(new ListenerHolder(GuiceServletCustomContextListener.class));
				//non-hybrid mode will separate api and static resource context
				ServletContextHandler resourceHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
				resourceHandler.setContextPath(System.getProperty("server.resources.context", ("/static/*")));
				resourceHandler.insertHandler(getResourceHandler());
			}

			port = Integer.parseInt(System.getProperty("server.port", "8080"));
			Server server = new Server(port);
			server.setStopAtShutdown(true);
			server.setHandler(servletContextHandler);
			return server;
		}

		private ResourceHandler getResourceHandler() {
            String fileStoragePath = System.getProperty("server.resources.folder", System.getProperty("user.dir")+ File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static");
            //Resource res = new PathResource.(Path.of(fileStoragePath));
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(fileStoragePath));
            resourceHandler.setEtags(true);
            resourceHandler.setDirAllowed(true);
			resourceHandler.setCacheControl("max-age=3600");
			resourceHandler.setAcceptRanges(true);
            return resourceHandler;
        }
	}

}
