package yi.shi.plinth.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import org.eclipse.jetty.ee10.servlet.ListenerHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.resource.ResourceFactory;
import yi.shi.plinth.servlet.DispatcherServlet;
import yi.shi.plinth.servlet.GuiceServletCustomContextListener;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author shiyi
 *
 */
public class JettyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ServletContextHandler.class).toProvider(ServletContextHandlerProvider.class).in(Singleton.class);
		bind(Server.class).toProvider(ServerProvider.class).in(Singleton.class);
		bind(ServletContext.class).toProvider(ServletContextProvider.class).in(Singleton.class);
	}


	private static class ServletContextHandlerProvider implements Provider<ServletContextHandler> {
		@Override
		public ServletContextHandler get() {
			ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			servletContextHandler.setContextPath("/");
			servletContextHandler.addServlet(DispatcherServlet.class, "/*");
			servletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			servletContextHandler.getServletHandler().addListener(new ListenerHolder(GuiceServletCustomContextListener.class));
			servletContextHandler.setDefaultRequestCharacterEncoding("UTF-8");
			servletContextHandler.setDefaultResponseCharacterEncoding("UTF-8");
			return servletContextHandler;
		}
	}

	private static class ServletContextProvider implements Provider<ServletContext> {
		@Inject
		ServletContextHandler servletContextHandler;

		@Override
		public ServletContext get() {
			ServletContext servletContext =  servletContextHandler.getServletContext();
			return servletContext;
		}
	}

	private static class ServerProvider implements Provider<Server> {
		
		@Inject
		ServletContextHandler servletContextHandler;

		@Override
		public Server get() {
			ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
			contextHandlerCollection.addHandler(servletContextHandler);
			contextHandlerCollection.addHandler(getResourceHandler());
			Server server = new Server(Integer.parseInt(System.getProperty("server.port", "8080")));
			server.setStopAtShutdown(true);
			server.setHandler(contextHandlerCollection);
			return server;
		}

		private ServletContextHandler getResourceHandler() {
            String fileStoragePath = System.getProperty("resources.folder");
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler).newResource(fileStoragePath));
			resourceHandler.setEtags(true);
            resourceHandler.setDirAllowed(true);
			resourceHandler.setDynamic(true);
			resourceHandler.setCacheControl("max-age=3600");
			resourceHandler.setAcceptRanges(true);

			ServletContextHandler resourceHandlerContext = new ServletContextHandler();
			resourceHandlerContext.setBaseResource(resourceHandler.getBaseResource());
			resourceHandlerContext.setContextPath("/static");
			resourceHandlerContext.setDefaultRequestCharacterEncoding("UTF-8");
			resourceHandlerContext.setDefaultResponseCharacterEncoding("UTF-8");
			resourceHandlerContext.insertHandler(resourceHandler);

			List<AliasCheck> aliasChecks = resourceHandlerContext.getContext().getContextHandler().getAliasChecks();
			if(aliasChecks.isEmpty()){
				aliasChecks = new ArrayList<>();
			}
			//aliasChecks.add(new SymlinkAllowedResourceAliasChecker(resourceHandlerContext.getContext().getContextHandler()));
			AllowedResourceAliasChecker newAliasChecker = new AllowedResourceAliasChecker(resourceHandlerContext.getContext().getContextHandler(), resourceHandlerContext.getBaseResource());
			aliasChecks.add(new AllowedResourceAliasChecker(resourceHandlerContext.getContext().getContextHandler()));
			resourceHandlerContext.getContext().getContextHandler().setAliasChecks(aliasChecks);
            return resourceHandlerContext;
        }
	}

}
