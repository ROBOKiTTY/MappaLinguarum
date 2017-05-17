package ca.rk.mappalinguarum.model;

import java.util.ArrayList;
import java.util.List;

/**
 * class describing a feature of a language, in the colloquial rather than phonological sense
 * 
 * @author RK
 *
 */
public class Feature {
	protected String name;
	protected static List<Feature> listOfAll = new ArrayList<Feature>();
	
	/**
	 * constructs an Feature identified by input string;
	 * the preferred way of instantiating a Feature is through the static getFeature method
	 * 
	 * @param s a string representing the name of a feature
	 */
	protected Feature(String s) {
		name = s;
	}
	
	/**
	 * retrieve an Feature by the identifying input string, constructing one if it doesn't already exist
	 * 
	 * @param s the identifying string representation
	 * @return an existing or new Feature, or null if input is null/empty
	 */
	public static Feature getFeature(String s) {
		if (s == null || s.isEmpty() ) {
			return null;
		}
		
		for (Feature f : listOfAll) {
			if (f.name.equals(s) ) {
				return f;
			}
		}
		Feature feature = new Feature(s);
		listOfAll.add(feature);
		return feature;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * this is equal to obj if 1) obj is not null, 2) obj is a Feature,
	 * and 3) obj's string representation is equal to that of this
	 * 
	 * @param obj the Object to compare against
	 * @return true if all three conditions above are satisfied 
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj != null && obj instanceof Feature &&
				this.toString().equals(obj.toString() ) );
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	//accessors
	public String getName() { return name; }

	public static List<Feature> getAllFeatures() { return listOfAll; }
}
