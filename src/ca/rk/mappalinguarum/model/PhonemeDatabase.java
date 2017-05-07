package ca.rk.mappalinguarum.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ca.rk.mappalinguarum.exceptions.InvalidXMLException;
import ca.rk.mappalinguarum.model.phoneme.Consonant;
import ca.rk.mappalinguarum.model.phoneme.MannerOfArticulation;
import ca.rk.mappalinguarum.model.phoneme.Phoneme;
import ca.rk.mappalinguarum.model.phoneme.PlaceOfArticulation;
import ca.rk.mappalinguarum.model.phoneme.Vowel;
import ca.rk.mappalinguarum.model.phoneme.VowelFrontness;
import ca.rk.mappalinguarum.model.phoneme.VowelHeight;


/**
 * a database of IPA symbols and descriptions
 * 
 * @author RK
 *
 */

public class PhonemeDatabase {

	private final String DEFAULT_XML_PATH = "data/sounddescriptions.xml";
	private final DefaultHandler DEFAULT_HANDLER = new PhonemeDataHandler();
	
	private File fileToParse;
	private boolean isParsed;
	private List<Phoneme> phonemes;
	private List<String> diacritics;
	
	private String currentString;
	private Phoneme currentPhoneme;
	
	/**
	 * constructs an empty PhonemeDatabase and starts parsing phoneme data
	 * from default file path
	 */
	public PhonemeDatabase() throws ParserConfigurationException, InvalidXMLException, IOException {
		isParsed = false;
		phonemes = new ArrayList<Phoneme>();
		diacritics = new ArrayList<String>();
		fileToParse = new File(DEFAULT_XML_PATH);
		parse(fileToParse, DEFAULT_HANDLER);
	}
	
	/**
	 * Interfaces SAXParser, passing it a file to parse and a DefaultHandler to handle the data
	 * 
	 * @param file a file path
	 * @param dh a DefaultHandler where parsed data is stored
	 * @throws ParserConfigurationException the exception is propagated from SAXParser 
	 * @throws InvalidXMLException the exception is propagated
	 * @throws IOException the exception is propagated
	 */
	private void parse(File file, DefaultHandler dh)
			throws ParserConfigurationException, InvalidXMLException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(file, dh);
			isParsed = true;
		}
		catch (SAXException se) {
			se.printStackTrace();
			throw new InvalidXMLException();
		}
	}
	
	/**
	 * add an input Phoneme if it's not null or already present in the database
	 * 
	 * @param p a Phoneme
	 */
	private void addPhoneme(Phoneme p) {
		if (p == null || phonemes.contains(p) ) {
			return;
		}
		phonemes.add(p);
	}
	
	/**
	 * checks whether the database contains a given phoneme
	 * 
	 * @param p a Phoneme
	 * @return true if database contains given phoneme, otherwise false
	 */
	public boolean contains(Phoneme p) {
		return phonemes.contains(p);
	}
	
	/**
	 * return a phoneme whose string representation matches input
	 * 
	 * @param s an input string representation
	 * @return a Phoneme, null if no match is found
	 */
	public Phoneme getPhoneme(String s) {
		if (s == null || s.isEmpty() ) {
			return null;
		}
		if ( s.equals(currentString) ) {
			return currentPhoneme;
		}
		
		currentString = s;
		
		String subbedString = s;
		if (subbedString.length() > 1) {
			for (String diacritic : diacritics) {
				if ( subbedString.contains(diacritic) ) {
					subbedString = subbedString.replace(diacritic, "");
				}
			}
		}
		
		for (Phoneme p : phonemes) {
			if ( p.getIPASymbol().equals(subbedString) ) {
				currentPhoneme = p;
				return currentPhoneme;
			}
		}
		
		currentString = null;
		currentPhoneme = null;
		return null;
	}
	
	/**
	 * finds the PlaceOfArticulation of a consonant within the database
	 * 
	 * @param s an IPA symbol as a string
	 * @return a PlaceOfArticulation, null if consonant not in database
	 */
	public PlaceOfArticulation getPlaceOfArticulation(String s) {
		if (s == null) {
			return null;
		}
		
		Consonant c;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			c = (Consonant) currentPhoneme;
		}
		else {
			c = (Consonant) getPhoneme(s);
		}
		
		if (c == null) {
			return null;
		}

		return c.getPlaceOfArticulation();
	}
	
	/**
	 * finds the secondary PlaceOfArticulation of a consonant within the database
	 * 
	 * @param s an IPA symbol as a string
	 * @return a PlaceOfArticulation, null if consonant lacks secondary POA or is not in database
	 */
	public PlaceOfArticulation getSecondaryPOA(String s) {
		if (s == null) {
			return null;
		}
		
		Consonant c;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			c = (Consonant) currentPhoneme;
		}
		else {
			c = (Consonant) getPhoneme(s);
		}
		
		if (c == null) {
			return null;
		}

		return c.getSecondaryPOA();
	}
	
	/**
	 * finds the MannerOfArticulation of a consonant within the database
	 * 
	 * @param s an IPA symbol as a string
	 * @return a MannerOfArticulation, null if consonant not in database
	 */
	public MannerOfArticulation getMannerOfArticulation(String s) {
		if (s == null) {
			return null;
		}
		
		Consonant c;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			c = (Consonant) currentPhoneme;
		}
		else {
			c = (Consonant) getPhoneme(s);
		}
		
		if (c == null) {
			return null;
		}

		return c.getMannerOfArticulation();
	}
	
	/**
	 * finds whether a given consonant in the database is voiced
	 * 
	 * @param s an IPA symbol as a string
	 * @return true if voiced, false otherwise
	 * @throws IllegalArgumentException if s is null or consonant is not found in database
	 */
	public boolean getIsVoiced(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		Consonant c;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			c = (Consonant) currentPhoneme;
		}
		else {
			c = (Consonant) getPhoneme(s);
		}
		
		if (c == null) {
			System.out.println(s);
			throw new IllegalArgumentException();
		}

		return c.getIsVoiced();
	}
	
	/**
	 * finds the frontness of a vowel within the database
	 * 
	 * @param s an IPA symbol as a string
	 * @return a VowelFrontness, null if vowel not in database
	 */
	public VowelFrontness getVowelFrontness(String s) {
		if (s == null) {
			return null;
		}
		
		Vowel v;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			v = (Vowel) currentPhoneme;
		}
		else {
			v = (Vowel) getPhoneme(s);
		}

		if (v == null) {
			return null;
		}
		
		return v.getFrontness();
	}

	/**
	 * finds the height of a vowel within the database
	 * 
	 * @param s an IPA symbol as a string
	 * @return a VowelHeight, null if vowel not in database
	 */
	public VowelHeight getVowelHeight(String s) {
		if (s == null) {
			return null;
		}
		
		Vowel v;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			v = (Vowel) currentPhoneme;
		}
		else {
			v = (Vowel) getPhoneme(s);
		}
		
		if (v == null) {
			return null;
		}
		
		return v.getHeight();
	}
	
	/**
	 * finds whether a given vowel in the database is rounded
	 * 
	 * @param s an IPA symbol as a string
	 * @return true if rounded, false otherwise
	 * @throws IllegalArgumentException if vowel is not found in database
	 */
	public boolean getIsRounded(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		
		Vowel v;
		if ( s.equals(currentString) && currentPhoneme != null ) {
			v = (Vowel) currentPhoneme;
		}
		else {
			v = (Vowel) getPhoneme(s);
		}
		
		if (v == null) {
			throw new IllegalArgumentException();
		}
		
		return v.getIsRounded();
	}
	
	public boolean getIsParsed() { return isParsed; }
	public List<Phoneme> getPhonemes() { return phonemes; }
	
	/**
	 * This helper class by default handles parsed XML data from SAXParser
	 * 
	 * @author RK
	 *
	 */
	private class PhonemeDataHandler extends DefaultHandler {
		
		private StringBuilder charsToStringBuildar;
		
		private boolean isReadingConsonant = false;
		private boolean isReadingVowel = false;
		
		private boolean isReadingSymbol = false;
		
		private boolean isReadingCVoice = false;
		private boolean isReadingCPlace = false;
		private boolean isReadingCManner = false;

		private boolean isReadingVFrontness = false;
		private boolean isReadingVHeight = false;
		private boolean isReadingVRounded = false;
		
		private boolean isReadingDiacritic = false;
		
		private Consonant consonant;
		private Vowel vowel;
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			charsToStringBuildar = new StringBuilder();
			if (qName.equalsIgnoreCase("consonant") ) {
				consonant = new Consonant();
				isReadingConsonant = true;
			}
			else if (qName.equalsIgnoreCase("vowel") ) {
				vowel = new Vowel();
				isReadingVowel = true;
			}
			else if (qName.equalsIgnoreCase("symbol") ) {
				isReadingSymbol = true;
			}
			else if (qName.equalsIgnoreCase("voice") ) {
				isReadingCVoice = true;
			}
			else if (qName.equalsIgnoreCase("place") ) {
				isReadingCPlace = true;
			}
			else if (qName.equalsIgnoreCase("manner") ) {
				isReadingCManner = true;
			}
			else if (qName.equalsIgnoreCase("frontness") ) {
				isReadingVFrontness = true;
			}
			else if (qName.equalsIgnoreCase("height") ) {
				isReadingVHeight = true;
			}
			else if (qName.equalsIgnoreCase("rounded") ) {
				isReadingVRounded = true;
			}
			else if (qName.equalsIgnoreCase("diacritic") ) {
				isReadingDiacritic = true;
			}
		}
		
		@Override
		public void characters(char[] chars, int start, int length) throws SAXException {
			if (isReadingSymbol || isReadingCVoice || isReadingCPlace || isReadingCManner ||
				isReadingVFrontness || isReadingVHeight || isReadingVRounded || isReadingDiacritic) {
				charsToStringBuildar.append(chars, start, length);
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String stringifiedChars = charsToStringBuildar.toString();
			//boolean isEmpty = stringifiedChars.isEmpty() || stringifiedChars.equals("N/A");
			
			if (isReadingConsonant) {
				if ( qName.equalsIgnoreCase("consonant") ) {
					addPhoneme(consonant);
					isReadingConsonant = false;
				}
				else if ( qName.equalsIgnoreCase("symbol") ) {
					consonant.setIPASymbol(stringifiedChars);
					isReadingSymbol = false;
				}
				else if ( qName.equalsIgnoreCase("voice") ) {
					consonant.setIsVoiced( Boolean.parseBoolean(stringifiedChars) );
					isReadingCVoice = false;
				}
				else if ( qName.equalsIgnoreCase("place") ) {
					String[] places = stringifiedChars.split(" ");
					if (places.length > 1) {
						consonant.setPlaceOfArticulation( PlaceOfArticulation.fromString(places[0]) );
						consonant.setSecondaryPOA( PlaceOfArticulation.fromString(places[1]) );
					}
					else {
						consonant.setPlaceOfArticulation( PlaceOfArticulation.fromString(stringifiedChars) );
					}

					isReadingCPlace = false;
				}
				else if ( qName.equalsIgnoreCase("manner") ) {
					consonant.setMannerOfArticulation( MannerOfArticulation.fromString(stringifiedChars) );
					isReadingCManner = false;
				}
			}
			else if (isReadingVowel) {
				if ( qName.equalsIgnoreCase("vowel") ) {
					addPhoneme(vowel);
					isReadingVowel = false;
				}
				else if ( qName.equalsIgnoreCase("symbol") ) {
					vowel.setIPASymbol(stringifiedChars);
					isReadingSymbol = false;
				}
				else if ( qName.equalsIgnoreCase("frontness") ) {
					vowel.setVowelFrontness( VowelFrontness.fromString(stringifiedChars) );
					isReadingVFrontness = false;
				}
				else if ( qName.equalsIgnoreCase("height") ) {
					vowel.setVowelHeight( VowelHeight.fromString(stringifiedChars) );
					isReadingVHeight = false;
				}
				else if ( qName.equalsIgnoreCase("rounded") ) {
					vowel.setIsRounded( Boolean.parseBoolean(stringifiedChars) );
					isReadingVRounded = false;
				}
			}
			else if ( qName.equalsIgnoreCase("diacritic") ) {
				diacritics.add(stringifiedChars);
				isReadingDiacritic = false;
			}
		}
	}
}
