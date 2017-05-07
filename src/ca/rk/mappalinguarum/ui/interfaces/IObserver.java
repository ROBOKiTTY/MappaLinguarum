package ca.rk.mappalinguarum.ui.interfaces;

/**
 * an interface that binds any implementation to implement an observer
 * 
 * @author RK
 *
 */
public interface IObserver {

	/**
	 * an IObserver updates when notified of changes
	 */
	public void update();
}
