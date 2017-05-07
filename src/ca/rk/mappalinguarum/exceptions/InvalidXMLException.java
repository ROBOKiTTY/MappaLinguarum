package ca.rk.mappalinguarum.exceptions;

import org.xml.sax.SAXException;

/**
 * this exception is thrown when parsing fails on an XML file
 * it is dealt with at the UI level and reported as an error to the user
 * 
 * @author RK
 *
 */

public class InvalidXMLException extends SAXException {

	private static final long serialVersionUID = 1L;

}
