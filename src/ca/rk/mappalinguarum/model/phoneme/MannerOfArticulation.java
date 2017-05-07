package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of possible manners of articulation for consonants
 * 
 * @author RK
 *
 */

public enum MannerOfArticulation {
	
	Nasal ("Nasal", "Nas"),
	Plosive ("Plosive", "Plos"),
	Fricative ("Fricative", "Fric"),
	Affricate ("Affricate", "Aff"),
	Approximant ("Approximant", "Appr"),
	Trill ("Trill", "Trl"),
	Flap_tap("Flap"),
	Lateral_Fricative("Lateral fricative", "LatFric"),
	Lateral_Approximant("Lateral approximant", "LatAppr"),
	Lateral_Flap("Lateral flap", "LatFlp"),
	Coarticulated_Fricative ("Coarticulated fricative", "CoFric"),
	Coarticulated_Approximant ("Coarticulated approximant", "CoAppr"),
	Coarticulated_Stop ("Coarticulated stop", "CoStp"),
	Click ("Click", "Clk"),
	Implosive ("Implosive", "Impl");
	
	private String fullName;
	private String shortName;
	
	/**
	 * construct a MannerOfArticulation whose string representations are the same as its name
	 */
	private MannerOfArticulation() {
		fullName = this.name();
		shortName = fullName;
	}
	
	/**
	 * construct a MannerOfArticulation whose string representations are the same as input
	 * 
	 * @param s string representation
	 */
	private MannerOfArticulation(String s) {
		fullName = s;
		shortName = s;
	}
	
	/**
	 * construct a MannerOfArticulation whose string representations are the same as input
	 * 
	 * @param s1 full display name
	 * @param s2 shortened name
	 */
	private MannerOfArticulation(String s1, String s2) {
		fullName = s1;
		shortName = s2;
	}
	
	/**
	 * find a MannerOfArticulation matching an input string representation
	 * 
	 * @param s a string representation
	 * @return a matching MannerOfArticulation, null if none exists
	 */
	public static MannerOfArticulation fromString(String s) {
		if (s != null && !s.isEmpty() ) {
			for (MannerOfArticulation moa : MannerOfArticulation.values() ) {
				if ( s.equalsIgnoreCase(moa.fullName) || s.equalsIgnoreCase(moa.shortName) ) {
					return moa;
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
