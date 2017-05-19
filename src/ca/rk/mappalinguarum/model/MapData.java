package ca.rk.mappalinguarum.model;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import ca.rk.mappalinguarum.exceptions.*;
import ca.rk.mappalinguarum.model.phoneme.Consonant;
import ca.rk.mappalinguarum.model.phoneme.PhonemeInventory;
import ca.rk.mappalinguarum.model.phoneme.Vowel;

/**
 * Data is parsed, handled, and stored in here
 * 
 * @author RK
 */
public class MapData {

	private final String DEFAULT_XML_PATH = "data/languagedata.xml";
	private final DefaultHandler DEFAULT_HANDLER = new LanguageDataHandler();
	
	private File fileToParse;
	private List<Location> locations = new ArrayList<Location>();
	private DefaultHandler handler;
	private PhonemeDatabase phonemeDatabase;
	private boolean isParsed = false;

	/**
	 * default constructor builds data from default file path
	 * 
	 * @throws ParserConfigurationException the exception is propagated from SAXParser
	 * @throws InvalidXMLException the exception is propagated
	 * @throws IOException the exception is propagated
	 */
	public MapData() throws ParserConfigurationException, InvalidXMLException, IOException {
		fileToParse = new File(DEFAULT_XML_PATH);
		handler = DEFAULT_HANDLER;
		phonemeDatabase = new PhonemeDatabase();
		parse(fileToParse, handler);
	}
	
	/**
	 * overloaded constructor builds data from parsing specified file using specified handler
	 * 
	 * @param file File to be parsed
	 * @param dh handler to use
	 * @throws ParserConfigurationException the exception is propagated from SAXParser
	 * @throws InvalidXMLException the exception is propagated
	 * @throws IOException the exception is propagated
	 */
	public MapData(File file, DefaultHandler dh)
			throws ParserConfigurationException, InvalidXMLException, IOException {
		fileToParse = file;
		handler = dh;
		phonemeDatabase = new PhonemeDatabase();
		parse(fileToParse, handler);
	}
	
	/**
	 * overloaded constructor builds data from parsing specified file using default handler
	 * 
	 * @param file File to be parsed
	 * @throws ParserConfigurationException the exception is propagated from SAXParser
	 * @throws InvalidXMLException the exception is propagated
	 * @throws IOException the exception is propagated
	 */
	public MapData(File file)
			throws ParserConfigurationException, InvalidXMLException, IOException {
		fileToParse = file;
		handler = DEFAULT_HANDLER;
		phonemeDatabase = new PhonemeDatabase();
		parse(fileToParse, handler);
	}
	
	/**
	 * overloaded constructor builds data from parsing a file with the input filename using default
	 * handler
	 * 
	 * @param filename path and name of File to be parsed
	 * @throws ParserConfigurationException the exception is propagated from SAXParser
	 * @throws InvalidXMLException the exception is propagated
	 * @throws IOException the exception is propagated
	 */
	public MapData(String filename)
			throws ParserConfigurationException, InvalidXMLException, IOException {
		fileToParse = new File(filename);
		handler = DEFAULT_HANDLER;
		phonemeDatabase = new PhonemeDatabase();
		parse(fileToParse, handler);
	}
	
	/**
	 * Interfaces SAXParser, passing it a file to parse and a DefaultHandler to handle the data
	 * 
	 * Modifies: this
	 * Effect: use SAXParser to parse a file, handling output data to a DefaultHandler; sets isParsed
	 * to true if parsing finishes successfully; intercepts SAXException and rethrow it as InvalidXMLException
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
	 * Returns post-processed data
	 * 
	 * Modifies: nothing
	 * Effect: returns parsed data, null if parsing is not done or successful
	 * 
	 * @return MapData containing data after parsing
	 */
	public MapData getParsedData() {
		if (!isParsed) {
			return null;
		}
		else {
			return this;
		}
	}

	//accessors
	public DefaultHandler getHandler() { return handler; }
	public List<Location> getLocations() { return locations; }
	public List<Feature> getAllFeatures() { return Feature.getAllFeatures(); }
	public List<LanguageFamily> getAllFamilies() { return LanguageFamily.getAllFamilies(); }
	
	/**
	 * This helper class by default handles parsed XML data from SAXParser
	 * 
	 * @author RK
	 *
	 */
	private class LanguageDataHandler extends DefaultHandler {
		
		private StringBuilder charsToStringBuildar;
		private boolean isReadingName = false;
		private boolean isReadingFeature = false;
		private boolean isReadingFamily = false;
		private boolean isReadingLocation = false;
		private boolean isReadingPhonemeConsonants = false;
		private boolean isReadingPhonemeVowels = false;
		private boolean isReadingInformation = false;
		private boolean isReadingLink = false;
		private boolean isReadingDialect = false;
		private Location loc;
		private Language lang;
		private Dialect dialect;
		private PhonemeInventory langPhonemeInv;
		private PhonemeInventory dialectPhonemeInv;

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			charsToStringBuildar = new StringBuilder();
			
			if (qName.equalsIgnoreCase("language") ) {
				lang = new Language();
				loc = new Location();
				langPhonemeInv = new PhonemeInventory();
			}
			else if (qName.equalsIgnoreCase("name") ) {
				isReadingName = true;
			}
			else if (qName.equalsIgnoreCase("feature") ) {
				isReadingFeature = true;
			}
			else if (qName.equalsIgnoreCase("family") ) {
				isReadingFamily = true;
			}
			else if (qName.equalsIgnoreCase("phoneme-consonants") ) {
				isReadingPhonemeConsonants = true;
			}
			else if (qName.equalsIgnoreCase("phoneme-vowels") ) {
				isReadingPhonemeVowels = true;
			}
			else if (qName.equalsIgnoreCase("location") ) {
				isReadingLocation = true;
			}
			else if (qName.equalsIgnoreCase("information") ) {
				isReadingInformation = true;
			}
			else if (qName.equalsIgnoreCase("link") ) {
				isReadingLink = true;
			}
			else if (qName.equalsIgnoreCase("dialect") ) {
				isReadingDialect = true;
				dialect = new Dialect(lang);
				dialectPhonemeInv = new PhonemeInventory();
			}
		}

		@Override
		public void characters(char[] chars, int start, int length) throws SAXException {
			if (isReadingName || isReadingFeature || isReadingFamily ||
				isReadingPhonemeConsonants || isReadingPhonemeVowels ||
				isReadingLocation || isReadingInformation || isReadingLink || isReadingDialect) {
				charsToStringBuildar.append(chars, start, length);
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("language") ) {
				lang.setPhonemeInventory(langPhonemeInv);
				loc.setLanguage(lang);
				locations.add(loc);
			}

			String stringifiedChars = charsToStringBuildar.toString();
			//#if DEBUG
			//System.out.println(stringifiedChars);
			//#endif
			boolean isEmpty = stringifiedChars.isEmpty() || stringifiedChars.equals("N/A");
			
			if (isReadingName) {
				if (isReadingDialect) {
					dialect.addName(stringifiedChars);
				}
				else {
					lang.addName(stringifiedChars);
				}
				isReadingName = false;
			}
			else if (isReadingFeature) {
				Feature currentFeature = Feature.getFeature(stringifiedChars);
				if (currentFeature != null) {
					lang.addFeature(currentFeature);
				}
				isReadingFeature = false;
			}
			else if (isReadingFamily) {
				LanguageFamily family = LanguageFamily.getFamily(stringifiedChars);
				if (family != null) {
					lang.addFamily(family);
				}
				isReadingFamily = false;
			}
			else if (isReadingPhonemeConsonants) {
				String[] rawConsonants = stringifiedChars.split("\\s");
				for (String eachRawConsonant : rawConsonants) {
					if ( eachRawConsonant.matches("\\s") ) {
						continue;
					}
					Consonant c = new Consonant();
					c.setIPASymbol(eachRawConsonant);
					c.setIsVoiced( phonemeDatabase.getIsVoiced(eachRawConsonant) );
					c.setPlaceOfArticulation( phonemeDatabase.getPlaceOfArticulation(eachRawConsonant) );
					c.setSecondaryPOA(phonemeDatabase.getSecondaryPOA(eachRawConsonant) );
					c.setMannerOfArticulation( phonemeDatabase.getMannerOfArticulation(eachRawConsonant) );
					if (isReadingDialect) {
						dialectPhonemeInv.addPhoneme(c);
					}
					else {
						langPhonemeInv.addPhoneme(c);
					}
				}
				isReadingPhonemeConsonants = false;
			}
			else if (isReadingPhonemeVowels) {
				String[] rawVowels = stringifiedChars.split(" ");
				for (String eachRawVowel : rawVowels) {
					Vowel v = new Vowel();
					v.setIPASymbol(eachRawVowel);
					v.setVowelFrontness( phonemeDatabase.getVowelFrontness(eachRawVowel) );
					v.setVowelHeight( phonemeDatabase.getVowelHeight(eachRawVowel) );
					v.setIsRounded( phonemeDatabase.getIsRounded(eachRawVowel) );
					if (isReadingDialect) {
						dialectPhonemeInv.addPhoneme(v);
					}
					else {
						langPhonemeInv.addPhoneme(v);
					}
				}
				isReadingPhonemeVowels = false;
			}
			else if (isReadingLocation) {
				if (!isEmpty) {
					/* Google KML coordinate format:
					 * longitude,latitude,altitude longitude,latitude,altitude
					 * 
					 * e.g.
					 * -126.0021,53.11,0 -121.0042,51.22,0
					 * 
					 * since altitude is never used in our 2d map, it's ignored
					 * 
					 * this weird format is a legacy remnant from when Mappa Linguarum used Google Maps
					 * */
					String[] rawLocs = stringifiedChars.split(",0 ");
					double[] lons = new double[rawLocs.length];
					double[] lats = new double[rawLocs.length];
					int index = 0;
					for (String rawLoc : rawLocs) {
						String[] lonLat = rawLoc.split(",");
						if (lonLat.length < 2 || lonLat.length > 3) {
							/* #if DEBUG
							System.out.println( lang.getName() );
							for (String s : lonLat) {
								System.out.print(s + " ");
							}
							System.out.println();
							System.out.println(rawLoc);
								#endif
							*/
							throw new InvalidXMLException();
						}
						lons[index] = Double.parseDouble(lonLat[0]);
						lats[index] = Double.parseDouble(lonLat[1]);
						++index;
					}
					loc.addLatLongs(lons, lats);
				}
				else {
					loc.addLatLongs( new double[] {0}, new double[] {0} );
				}
				isReadingLocation = false;
			}
			else if (isReadingInformation) {
				lang.setInformation(stringifiedChars);
				isReadingInformation = false;
			}
			else if (isReadingLink) {
				if (!isEmpty) {
					if (!stringifiedChars.startsWith("http") ) {
						stringifiedChars = "http://" + stringifiedChars;
					}
					lang.addLink(stringifiedChars);
				}
				isReadingLink = false;
			}
			else if (isReadingDialect) {
				dialect.setPhonemeInventory(dialectPhonemeInv);
				lang.addDialect(dialect);
				isReadingDialect = false;
			}
		}
	}
}
