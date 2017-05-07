package ca.rk.mappalinguarum.model;

import java.util.ArrayList;
import java.util.List;

import ca.rk.mappalinguarum.util.Colour;
import ca.rk.mappalinguarum.util.RandomColourGenerator;


/**
 * class describing a language family; is-a Feature
 * 
 * @author RK
 *
 */

public class LanguageFamily extends Feature {

	private static List<LanguageFamily> listOfAll = new ArrayList<LanguageFamily>();
	private Colour colour;
	
	/**
	 * constructs a LanguageFamily identified by input string
	 * the preferred way of instantiating a LanguageFamily is through the static getFamily method
	 * 
	 * @see Feature
	 */
	private LanguageFamily(String s) {
		super(s);
		colour = RandomColourGenerator.getInstance().generateColour();
	}
	

	/**
	 * retrieve an LanguageFamily by the identifying input string, constructing one if it doesn't already exist
	 * 
	 * @param s the identifying string representation
	 * @return an existing or new LanguageFamily, or null if input is null/empty
	 */
	public static LanguageFamily getFamily(String s) {
		if (s == null || s.isEmpty() ) {
			return null;
		}
		
		for (LanguageFamily lf : listOfAll) {
			if (lf.name.equals(s) ) {
				return lf;
			}
		}
		LanguageFamily lf = new LanguageFamily(s);
		listOfAll.add(lf);
		return lf;
	}
	
	/**
	 * additionally check if input object is a LanguageFamily
	 * 
	 * @return true if super.equals(obj) returns true AND obj is a LanguageFamily
	 * @see Feature
	 */
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && obj instanceof LanguageFamily;
	}
	
	//accessors
	public Colour getColour() { return colour; }
	public static List<LanguageFamily> getAllFamilies() { return listOfAll; }
}
