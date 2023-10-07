package yi.shi.plinth.exception;

/**
 * @author shiyi
 *
 */
public class UnsupportMIMETypeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportMIMETypeException() {
		super();
	}
	
	public UnsupportMIMETypeException(String message) {
		super(message);
	}
	
	public UnsupportMIMETypeException(Exception exception) {
		super(exception);
	}
	
}
