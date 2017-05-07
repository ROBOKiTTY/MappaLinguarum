package ca.rk.mappalinguarum.model.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.rk.mappalinguarum.exceptions.InvalidXMLException;
import ca.rk.mappalinguarum.model.Location;
import ca.rk.mappalinguarum.model.MapData;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


/**
 * test class for MapData class
 * 
 * @author RK
 *
 */

public class MapDataTest {

	MapData data;
	
	/**
	 * instantiate a MapData for testing
	 */
	@Before
	public void testMapData() {
		try {
			data = new MapData();
		}
		catch (ParserConfigurationException pce) {
			fail("An error in parser configuration is found. Aborting test.");
		}
		catch (InvalidXMLException e) {
			fail("XML file or parser need to be checked.");
		}
		catch (IOException ie) {
			fail("An IO exception happened. Test cannot continue.");
		}
	}
	
	/**
	 * test whether MapData's properties are valid 
	 */
	@Test
	public void testGetHandler() {
		Object handler = data.getHandler();
		assertNotNull(handler);
	}
	
	@Test
	public void testGetLocations() {
		Object locs = data.getLocations();
		assertNotNull(locs);
	}
	
	/**
	 * test handler functionalities, whether it's assigning and mapping values correctly
	 */
	@Test
	public void testHandler() {
		data = data.getParsedData();
		
		List<Location> locs = data.getLocations();

		for (Location loc : locs) {
			assertNotNull(loc.getLanguage() );
			assertFalse( loc.getLanguage().getFeatures().contains(null) );
		}
	}
}
