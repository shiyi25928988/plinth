package yi.shi.plinth.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shiyi
 *
 */
@Slf4j
public class ReduplicativeMathodPathException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReduplicativeMathodPathException(String message) {
		super(message);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> log.error(message)));
		Runtime.getRuntime().exit(0);
	}
}
