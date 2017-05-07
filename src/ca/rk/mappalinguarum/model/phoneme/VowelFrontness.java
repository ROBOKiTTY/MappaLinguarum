package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of tongue positions for vowels
 * 
 * @author RK
 *
 */

public enum VowelFrontness {
	
	Front ("Front", "Fr"),
	Near_Front ("Near-Front", "N-Fr"),
	Central ("Central", "Cent"),
	Near_Back ("Near-Back", "N-Back"),
	Back;
	
	private String fullName;
	private String shortName;
	
	/**
	 * construct a VowelFrontness whose string representations are the same as its name
	 */
	private VowelFrontness() {
		fullName = this.name();
		shortName = fullName;
	}
	
	/**
	 * construct a VowelFrontness whose string representations are the same as input
	 * 
	 * @param s a string representation
	 */
	private VowelFrontness(String s) {
		fullName = s;
		shortName = s;
	}
	
	/**
	 * construct a VowelFrontness whose string representations are the same as input
	 * 
	 * @param s1 a full string representation
	 * @param s2 a short string representation
	 */
	private VowelFrontness(String s1, String s2) {
		fullName = s1;
		shortName = s2;
	}
	
	/**
	 * find a VowelFrontness option matching an input string representation
	 * 
	 * @param s a string representation
	 * @return a matching VowelFrontness option, null if none exists
	 */
	public static VowelFrontness fromString(String s) {
		if (s != null && !s.isEmpty() ) {
			for (VowelFrontness vf : VowelFrontness.values() ) {
				if ( s.equalsIgnoreCase(vf.fullName) || s.equalsIgnoreCase(vf.shortName) ) {
					return vf;
				}
			}
		}
		return null;
	}

	/**
	 * returns a short string representation
	 */
	@Override
	public String toString() {
		return shortName;
	}
	
	public String getFullName() { return fullName; }
}
