package yi.shi.plinth.jetty;

import com.google.inject.ImplementedBy;

/**
 * @author shiyi
 *
 */
@ImplementedBy(value=JettyBootServiceImpl.class)
public interface JettyBootService {
	
	void start();
	void stop();
}
