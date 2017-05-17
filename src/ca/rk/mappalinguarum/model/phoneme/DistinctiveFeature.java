package ca.rk.mappalinguarum.model.phoneme;

/**
 * a distinctive feature is a unit of phonological structure in phonological theory
 * 
 * 
 * @author RK
 *
 */
public class DistinctiveFeature implements PhonologicalFeature {
	private String name;
	private boolean plus;
	
	/**
	 * construct a DistinctiveFeature, with name in lower case
	 * 
	 * @param s string representation
	 * @param b true is +, false is -
	 */
	public DistinctiveFeature(String s, boolean b) {
		name = s.toLowerCase();
		plus = b;
	}

	/**
	 * returns a string representation in the format of [+/-feature]
	 * 
	 * e.g. [+voice] or [-round]
	 */
	@Override
	public String toString() {
		return plus ? "[+" + name + "]" : "[-" + name + "]";
	}
	
	/**
	 * returns the feature name
	 */
	@Override
	public String getFullName() {
		return name;
	}
	
	/**
	 * @return true is +, false is -
	 */
	public boolean getBinaryValue() {
		return plus;
	}
}
