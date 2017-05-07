package ca.rk.mappalinguarum.util;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import ca.rk.mappalinguarum.exceptions.BrowserNotFoundException;
import ca.rk.mappalinguarum.ui.TextConsole;
import ca.rk.mappalinguarum.util.OperatingSystem.OS;

/**
 * utility class that implements opening native browser
 * to navigate to web pages
 * 
 * this class incorporates public domain code from Bare Bones Browser Launch:
 * <a href="http://www.centerkey.com/java/browser/">www.centerkey.com/java/browser</a><br>
 * 
 * @author RK
 *
 */
public class BrowserNavigator {

	public final String[] BROWSERS = { "chrome", "chromium", "firefox", "safari", "konqueror", "epiphany",
			"seamonkey", "mozilla", "galeon", "netscape", "kazehakase",
			"lynx"};

	private OS os;
	private Desktop desktop;
	private boolean isUsingDesktop;

	/**
	 * constructs a BrowserNavigator and determines OS/desktop usage information
	 */
	public BrowserNavigator() {
		os = OperatingSystem.getOS();
		
		if ( Desktop.isDesktopSupported() ) {
			isUsingDesktop = true;
			desktop = Desktop.getDesktop();
		}
		else {
			isUsingDesktop = false;
			desktop = null;
		}
	}
	
	/**
	 * navigates to the URL in a web browser in the absence of desktop support
	 * 
	 * @see this.navigateToURL(URL url)
	 * 
	 * @param url a web address as a URL
	 */
	public void navigateToURL(URL url) {
		try {
			if (isUsingDesktop) {
				desktop.browse( url.toURI() );
			}
			else {
				navigateToURL( url.toString() );
			}
		}
		catch (URISyntaxException use) {
			TextConsole.writeLine("The URL's syntax seems to be invalid.");
		}
		catch (IOException ie) {
			TextConsole.writeLine("There was an error opening the browser.");
		}
	}
	
	/**
	 * navigates to the URL in a web browser in the absence of desktop support
	 * 
	 * supports Mac/Windows/Linux
	 * 
	 * @param url a web address as a string
	 */
	public void navigateToURL(String url) {
		try {
			if (isUsingDesktop) {
				desktop.browse( URI.create(url) );
			}
			else {
				switch (os) {
					case MAC:
						navigateOnMac(url);
						break;
					case UNIX:
						navigateOnNix(url);
						break;
					case WINDOWS:
						navigateOnWindows(url);
						break;
					default:
						TextConsole.writeLine("Your system is unsupported, sorry.");
						TextConsole.writeLine("You may have to enter the link manually to your browser.");
						break;
				}
				return;
			}
		}
		catch (BrowserNotFoundException bnfe) {
			TextConsole.writeLine("No suitable browser was found to open the webpage with.");
			TextConsole.writeLine("Is your browser on this list? " + bnfe.getMessage() );
		}
		catch (IOException ie) {
			TextConsole.writeLine("There was an error opening the browser.");
			ie.printStackTrace();
		}
		catch (Exception e) {
			TextConsole.writeLine("There was an error opening the browser.");
			e.printStackTrace();
		}
	}
	
	/**
	 * navigates to the URL in a web browser on Unix/Linux, using 'which' through runtime to find a suitable
	 * browser to run 
	 * 
	 * @param url a web address as a string
	 * @throws InterruptedException, IOException, BrowserNotFoundException
	 */
	private void navigateOnNix(String url) throws InterruptedException, IOException, BrowserNotFoundException {
		boolean isMatch = false;
		for (String browser : BROWSERS) {
			if (!isMatch) {
				isMatch = Runtime.getRuntime().exec(new String[] {"which", browser} ).waitFor() == 0;
				if (isMatch) {
					Runtime.getRuntime().exec(new String[] {browser, url} );
				}
			}
		}
		if (!isMatch) {
			throw new BrowserNotFoundException( Arrays.toString(BROWSERS) );
		}
	}
	
	/**
	 * navigates to the URL in a web browser on a Mac, using reflection to perform native operation 
	 * 
	 * @param url a web address as a string
	 * @throws ClassNotFoundException, NoSuchMethodException, SecurityException, InvocationTargetException
	 */
	private void navigateOnMac(String url) throws Exception {
		Class<?> fileManager = Class.forName("com.apple.eio.FileManager");
		Method openURL = fileManager.getDeclaredMethod("openURL", new Class[] {String.class} );
		openURL.invoke(null, new Object[] {url} );
	}

	/**
	 * navigates to the URL in a web browser on Windows, using runtime to ask the OS to handle the
	 * URL
	 * 
	 * @param url a web address as a string
	 * @throws IOException
	 */
	private void navigateOnWindows(String url) throws IOException {
		Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + url);
	}
}
