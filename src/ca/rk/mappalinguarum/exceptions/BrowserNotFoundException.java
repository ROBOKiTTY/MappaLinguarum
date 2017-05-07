package ca.rk.mappalinguarum.exceptions;

/**
 * this exception is thrown when the application fails to locate a browser
 * with which to open a webpage
 * 
 * @author RK
 *
 */

public class BrowserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public BrowserNotFoundException(String msg) {
		super(msg);
	}
}
