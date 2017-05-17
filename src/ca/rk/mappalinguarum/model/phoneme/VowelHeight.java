package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of vowel heights
 * 
 * @author RK
 *
 */

public enum VowelHeight implements PhonologicalFeature {
	
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
	 * @param full a full string representation
	 * @param shrt a short string representation
	 */
	private VowelHeight(String full, String shrt) {
		fullName = full;
		shortName = shrt;
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
	 * @return a VowelHeight raised one level from this, without wrapping around
	 */
	public VowelHeight raised() {
		if (this.equals(VowelHeight.Low)) {
			return VowelHeight.Near_Low;
		}
		else if (this.equals(VowelHeight.Near_Low)) {
			return VowelHeight.Mid_Low;
		}
		else if (this.equals(VowelHeight.Mid_Low)) {
			return VowelHeight.Mid_High;
		}
		else if (this.equals(VowelHeight.Mid_High)) {
			return VowelHeight.Near_High;
		}
		else if (this.equals(VowelHeight.Near_High)) {
			return VowelHeight.High;
		}
		else if (this.equals(VowelHeight.High)) {
			return VowelHeight.High;
		}
		
		throw new RuntimeException("VowelHeight none of the above in raised()");
	}
	
	/**
	 * @return a VowelHeight lowered one level from this, without wrapping around
	 */
	public VowelHeight lowered() {
		if (this.equals(VowelHeight.High)) {
			return VowelHeight.Near_High;
		}
		else if (this.equals(VowelHeight.Near_High)) {
			return VowelHeight.Mid_High;
		}
		else if (this.equals(VowelHeight.Mid_High)) {
			return VowelHeight.Mid_Low;
		}
		else if (this.equals(VowelHeight.Mid_Low)) {
			return VowelHeight.Near_Low;
		}
		else if (this.equals(VowelHeight.Near_Low)) {
			return VowelHeight.Low;
		}
		else if (this.equals(VowelHeight.Low)) {
			return VowelHeight.Low;
		}
		
		throw new RuntimeException("VowelHeight none of the above in lowered()");
	}

	/**
	 * returns a short string representation
	 */
	@Override
	public String toString() {
		return shortName;
	}
	
	@Override
	public String getFullName() { return fullName; }
}
