package yi.shi.plinth.http.result;


import yi.shi.plinth.http.MimeType;

/**
 * @author SHIYI
 *
 */
public interface ReturnType<T> {

	MimeType getMimeType();
	T getData();
}
