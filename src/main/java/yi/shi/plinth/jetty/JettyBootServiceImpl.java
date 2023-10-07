package yi.shi.plinth.jetty;

import org.eclipse.jetty.server.Server;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author shiyi
 *
 */
@Singleton
public class JettyBootServiceImpl implements JettyBootService {

	@Inject
	private Server server;

	@Override
	public void start() {
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
