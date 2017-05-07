package ca.rk.mappalinguarum.model.phoneme;

/**
 * enumeration of possible places of articulation for consonants
 * 
 * @author RK
 *
 */

public enum PlaceOfArticulation {

	Bilabial ("Bilab"),
	Labiodental ("Labiod"),
	Dental ("Dent"),
	Alveolar ("Alv"),
	Postalveolar ("Postalv"),
	Retroflex ("Retr"),
	Palatal ("Pal"),
	Velar ("Vel"),
	Uvular ("Uv"),
	Pharyngeal ("Phar"),
	Epiglottal ("Epig"),
	Glottal ("Glot");
	
	private String shortName;
	
	/**
	 * implicitly constructs a PlaceOfArticulation with name as its short name
	 */
	private PlaceOfArticulation() {
		shortName = this.name();
	}
	
	/**
	 * constructs a PlaceOfArticulation with input as its short name
	 * 
	 * @param s a short string
	 */
	private PlaceOfArticulation(String s) {
		shortName = s;
	}
	
	/**
	 * find a PlaceOfArticulation matching an input string representation
	 * 
	 * @param s a string representation
	 * @return a matching PlaceOfArticulation, null if none exists
	 */
	public static PlaceOfArticulation fromString(String s) {
		if (s != null && !s.isEmpty() ) {
			for (PlaceOfArticulation poa : PlaceOfArticulation.values() ) {
				if ( s.equalsIgnoreCase( poa.name() ) || s.equalsIgnoreCase(poa.shortName) ) {
					return poa;
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
	
	public String getFullName() { return this.name(); }
}
