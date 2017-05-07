package ca.rk.mappalinguarum.model.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.rk.mappalinguarum.model.Feature;


/**
 * test class for Feature
 * 
 * @author RK
 *
 */

public class FeatureTest {

	private Feature feature;
	private final String CREE = "Cree";
	
	/**
	 * set a feature for testing
	 */
	@Before
	public void testFeature() {
		feature = Feature.getFeature(CREE);
	}
	
	/**
	 * test whether string returns correct value
	 */
	@Test
	public void testToString() {
		String name = feature.toString();
		assertEquals(name, CREE);
	}
	
	/**
	 * test getFeature
	 */
	@Test
	public void testGetFeature() {
		assertTrue(Feature.getAllFeatures().size() == 1);
		assertEquals(Feature.getFeature(CREE), feature);
		assertNull(Feature.getFeature("") );
		assertNull(Feature.getFeature(null) );
		System.out.println(Feature.getAllFeatures().size());
		for (Feature f : Feature.getAllFeatures() ) {
			System.out.println(f.toString());
		}
		assertTrue(Feature.getAllFeatures().size() == 1);
	}
	
	/**
	 * test equals
	 */
	@Test
	public void testEquals() {
		//7 A's
		Feature testF1 = Feature.getFeature("AAAAAAA");
		Feature testF2 = Feature.getFeature("AAAAAAA");
		//should equal itself
		assertTrue(testF1.equals(testF1) );

		assertTrue(testF1.equals(testF2) );
		Feature testF3 = Feature.getFeature("BBBBB");
		assertFalse(testF1.equals(testF3) );
		//7 A's too
		String s = "AAAAAAA";
		assertFalse(testF2.equals(s) );
	}
}
