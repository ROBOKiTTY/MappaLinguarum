package ca.rk.mappalinguarum.ui;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import ca.rk.mappalinguarum.util.BrowserNavigator;


/**
 * this class handles links
 * 
 * @author RK
 * @see HyperlinkListener
 *
 */

public class HtmlLinkListener implements HyperlinkListener {

	private BrowserNavigator browserNavigator;

	/**
	 * constructs an HtmlLinkListener with a BrowserNavigator
	 * 
	 */
	public HtmlLinkListener() {		
		browserNavigator = new BrowserNavigator();
	}
	
	/**
	 * event handler for when a link is clicked
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent hle) {
		HyperlinkEvent.EventType type = hle.getEventType();

		if (type == HyperlinkEvent.EventType.ACTIVATED) {
			if ( !hle.getDescription().startsWith("http") ) {
				return;
			}
			browserNavigator.navigateToURL( hle.getURL() );
		}
	}
}
