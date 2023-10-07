package yi.shi.plinth.servlet;

import java.util.Objects;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import yi.shi.plinth.modules.ServletModule;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yshi
 *
 */
public class GuiceServletCustomContextListener extends GuiceServletContextListener {

	/**
	 * Injector
	 */
	private static volatile Injector injector = null;
	
	public GuiceServletCustomContextListener() {
	}


	/* (non-Javadoc)
	 * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
	 */
	@Override
	protected synchronized Injector getInjector() {
		if (Objects.isNull(injector)) {
			injector = Guice.createInjector(new ServletModule());
		}
		return injector;
	}

	/**
	 * @return
	 * @throws NullPointerException
	 */
	public static Injector getInjectorInstance() throws NullPointerException {
		if (Objects.isNull(injector)) {
			throw new NullPointerException();
		}
		return injector;
	}
}
