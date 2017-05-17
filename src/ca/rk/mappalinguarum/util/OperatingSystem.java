package ca.rk.mappalinguarum.util;

/**
 * finds out what operation system the application is running on
 * 
 * @author RK
 *
 */

public class OperatingSystem {

	public enum OS {
		MAC,
		WINDOWS,
		//Unix includes Linux and other Unix-likes
		UNIX,
		UNKNOWN
	}
	
	private static OS os = null;
	
	/**
	 * @return the current running operating system
	 */
	public static OS getOS() {
		if (os == null) {
			String osName = System.getProperty("os.name").toLowerCase();
			
			if ( osName.contains("mac") ) {
				os = OS.MAC;
			}
			else if ( osName.contains("win") ) {
				os = OS.WINDOWS;
			}
			else if (osName.contains("nix") || osName.contains("nux") || osName.contains("sunos") ||
					osName.contains("bsd") || osName.contains("gnu") ) {
				os = OS.UNIX;
			}
			else {
				os = OS.UNKNOWN;
			}
		}
		return os;
	}
}
