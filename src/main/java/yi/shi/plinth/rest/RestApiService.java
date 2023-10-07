package yi.shi.plinth.rest;

/**
 * @author yshi
 *
 */
public interface RestApiService {
	
	/**
	 * HTTP GET
	 */
	void doGet() throws Exception;
	
	/**
	 * HTTP PUT
	 */
	void doPut() throws Exception;
	
	/**
	 * HTTP POST
	 */
	void doPost() throws Exception;
	
	/**
	 * HTTP DELETE
	 */
	void doDelete() throws Exception;

	/**
	 * HTTP HEAD
	 */
	void doHead() throws Exception;

	/**
	 * HTTP OPTIONS
	 */
	void doOptions() throws Exception;

	/**
	 * HTTP TRACE
	 */
	void doTrace() throws Exception;
}
