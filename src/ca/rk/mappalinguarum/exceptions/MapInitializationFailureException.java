package ca.rk.mappalinguarum.exceptions;

/**
 * this exception is thrown if the Map component fails to initialize
 * 
 * @author RK
 *
 */
public class MapInitializationFailureException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see {@link RuntimeException}
	 */
	public MapInitializationFailureException() {
		super();
	}
	
	/**
	 * @see {@link RuntimeException}
	 * 
	 * @param t throwable object
	 */
	public MapInitializationFailureException(Throwable t) {
		super(t);
	}
}
