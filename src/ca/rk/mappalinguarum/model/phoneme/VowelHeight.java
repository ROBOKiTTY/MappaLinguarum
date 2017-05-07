package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of vowel heights
 * 
 * @author RK
 *
 */

public enum VowelHeight {
	
	High ("High", "High"),
	Near_High("Near-High", "NH"),
	Mid_High("Mid-High", "M-H"),
	Mid ("Mid", "Mid"),
	Mid_Low("Mid-Low", "M-L"),
	Near_Low ("Near-Low", "NL"),
	Low ("Low", "Low");
	
	private String fullName;
	private String shortName;
	
	/**
	 * construct a VowelHeight whose string representations are the same as its name
	 */
	private VowelHeight() {
		fullName = this.name();
		shortName = fullName;
	}
	
	/**
	 * construct a VowelHeight whose string representations are the same as input
	 * 
	 * @param s a string representation
	 */
	private VowelHeight(String s) {
		fullName = s;
		shortName = s;
	}
	
	/**
	 * construct a VowelHeight whose string representations are the same as input
	 * 
	 * @param s1 a full string representation
	 * @param s2 a short string representation
	 */
	private VowelHeight(String s1, String s2) {
		fullName = s1;
		shortName = s2;
	}
	
	/**
	 * find a VowelHeight option matching an input string representation
	 * 
	 * @param s a string representation
	 * @return a matching VowelHeight option, null if none exists
	 */
	public static VowelHeight fromString(String s) {
		if (s != null && !s.isEmpty() ) {
			for (VowelHeight vh : VowelHeight.values() ) {
				if ( s.equalsIgnoreCase(vh.fullName) || s.equalsIgnoreCase(vh.shortName) ) {
					return vh;
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
