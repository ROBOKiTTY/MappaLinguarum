package ca.rk.mappalinguarum.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e) {
			System.out.println("UI Look and Feel not found, defaulting to system L&F.");
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (ClassNotFoundException ee) {
				ee.printStackTrace();
			}
			catch (InstantiationException ee) {
				ee.printStackTrace();
			}
			catch (IllegalAccessException ee) {
				ee.printStackTrace();
			}
			catch (UnsupportedLookAndFeelException ee) {
				ee.printStackTrace();
			}
		}
		
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
