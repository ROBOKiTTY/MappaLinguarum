package ca.rk.mappalinguarum.ui;

import javax.swing.SwingUtilities;

/**
 * application entry point
 * 
 * @author RK
 *
 */
public class Main
{
	/**
	 * program entry point
	 * 
	 * effect: bootstraps the application
	 * @param args program parameters; none is defined right now
	 */
	public static void main(String[] args) {
		try {
			Runnable r = new Runnable() {
				public void run() {
					new ApplicationFrame();
				}
			};
			SwingUtilities.invokeLater(r);
		}
		catch (Throwable e) {
			System.err.println("Unhandled error found.");
			e.printStackTrace();
		}
	}
}
