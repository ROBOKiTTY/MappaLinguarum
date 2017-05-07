package ca.rk.mappalinguarum.ui.interfaces;

/**
 * an interface that binds any implementation to implement an observable subject
 * 
 * @author RK
 *
 */

public interface IObservable {

	/**
	 * add an IObserver to the local list of IObservers
	 * 
	 * @param obs an IObserver
	 */
	void addObserver(IObserver obs);
	
	/**
	 * remove an IObserver from local list of IObservers
	 * 
	 * @param obs an IObserver
	 */
	void removeObserver(IObserver obs);

	/**
	 * for each IObserver in the local list of IObservers, call for an update
	 */
	void notifyObservers();
}
