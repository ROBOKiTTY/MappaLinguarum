package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of tongue positions for vowels
 * 
 * @author RK
 *
 */

public enum VowelFrontness implements PhonologicalFeature {
	
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
	 * @param full a full string representation
	 * @param shrt a short string representation
	 */
	private VowelFrontness(String full, String shrt) {
		fullName = full;
		shortName = shrt;
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
	 * @return a VowelFrontness advanced one level of this, so back goes to near-back, etc,
	 * without wrapping around
	 */
	public VowelFrontness advanced() {
		if (this.equals(VowelFrontness.Back)) {
			return VowelFrontness.Near_Back;
		}
		else if (this.equals(VowelFrontness.Near_Back)) {
			return VowelFrontness.Central;
		}
		else if (this.equals(VowelFrontness.Central)) {
			return VowelFrontness.Near_Front;
		}
		else if (this.equals(VowelFrontness.Near_Front)) {
			return VowelFrontness.Front;
		}
		else if (this.equals(VowelFrontness.Front)) {
			return VowelFrontness.Front;
		}
		
		throw new RuntimeException("VowelFrontness none of the above in advanced()");
	}
	
	/**
	 * @return a VowelFrontness retracted one level of this, so front goes to near-front, etc,
	 * without wrapping around
	 */
	public VowelFrontness retracted() {
		if (this.equals(VowelFrontness.Front)) {
			return VowelFrontness.Near_Front;
		}
		else if (this.equals(VowelFrontness.Near_Front)) {
			return VowelFrontness.Central;
		}
		else if (this.equals(VowelFrontness.Central)) {
			return VowelFrontness.Near_Back;
		}
		else if (this.equals(VowelFrontness.Near_Back)) {
			return VowelFrontness.Back;
		}
		else if (this.equals(VowelFrontness.Back)) {
			return VowelFrontness.Back;
		}
		
		throw new RuntimeException("VowelFrontness none of the above in retracted()");
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
