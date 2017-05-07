package ca.rk.mappalinguarum.model.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ca.rk.mappalinguarum.model.Feature;
import ca.rk.mappalinguarum.model.Language;

import java.util.List;


/**
 * test class for Language class
 * 
 * @author RK
 *
 */

public class LanguageTest {

	Language language;
	
	/**
	 * instantiate a language for testing
	 */
	@Before
	public void testLanguage() {
		language = new Language();
		assertTrue( language.getNames().isEmpty() );
		assertTrue( language.getFamilies().isEmpty() );
		assertTrue( language.getDialects().isEmpty() );
		assertTrue( language.getFeatures().isEmpty() );
		assertTrue( language.getLinks().isEmpty() );
	}
	
	/**
	 * test addFeature(), wheter it adds correctly and handles edge cases
	 * as expected
	 */
	@Test
	public void testAddFeatures() {
		List<Feature> testedFeatures = language.getFeatures();
		
		//should be empty to begin with
		Feature someFeature = Feature.getFeature("OooOoo");
		assertFalse( testedFeatures.contains(someFeature) );
		
		//adding a single feature, is it correctly stored?
		language.addFeature(someFeature);
		assertTrue( ( testedFeatures = language.getFeatures() ).contains(someFeature) );
		
		assertTrue(language.getFeatures().size() == 1);
		//duplicate
		language.addFeature(someFeature);
		assertFalse(language.getFeatures().size() == 2);
		assertTrue(language.getFeatures().size() == 1);
	}
	
	@Test
	public void testAddName() {
		assertTrue(language.getNames().isEmpty() );
		
		String randomText = "Elvish";
		
		language.addName(randomText);
		assertTrue(language.getNames().contains(randomText));
		
		randomText = "";
		language.addName(randomText);
		assertFalse(language.getNames().contains(randomText));
	}
	
	/**
	 * test addLink()
	 */
	@Test
	public void testAddLink() {
		List<String> links = null;
		
		//add some nonsense text
		String randomText = "waaah";
		language.addLink(randomText);
		assertTrue( ( links = language.getLinks() ).contains(randomText) );
		
		//note how many elements are in links
		int tempNumbarOfLinks = links.size();
		
		language.addLink("http://waaah.com");
		//after adding, the number should go up by 1
		assertNotSame(tempNumbarOfLinks, ( links = language.getLinks() ).size() );
		assertTrue( links.size() - tempNumbarOfLinks == 1 );
	}
	
	/**
	 * test getters and setters
	 */
	@Test
	public void testProperties() {
		String randomText = "Elvish";
		
		language.addName(randomText);
		assertTrue(language.getNames().contains(randomText));
		
		randomText = "Elvish is a conlang.";
		language.setInformation(randomText);
		assertTrue( language.getInformation().equals(randomText) );
		
		randomText = "Elvish is not a conlang.";
		assertFalse( language.getInformation().equals(randomText) );
	}
}
