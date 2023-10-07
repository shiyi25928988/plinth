package yi.shi.plinth.exception;

/**
 * @author shiyi
 *
 */
public class SingleRequestBodyRequiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SingleRequestBodyRequiredException() {
		super();
	}

	public SingleRequestBodyRequiredException(String message) {
		super(message);
	}
}
