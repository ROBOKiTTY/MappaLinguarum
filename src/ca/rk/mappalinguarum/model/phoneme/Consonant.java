package ca.rk.mappalinguarum.model.phoneme;

/**
 * encapsulates a phonemic consonant and its associated information
 * 
 * @author RK
 *
 */

public class Consonant extends Phoneme {

	private PlaceOfArticulation place;
	private PlaceOfArticulation secondaryPlace;
	private MannerOfArticulation manner;
	private boolean isVoiced;
	
	/**
	 * constructs an empty Consonant
	 */
	public Consonant() {
		super();
	}
	
	/**
	 * constructs an empty Consonant represented by the input IPA symbol
	 * 
	 * @param symbol a string IPA symbol
	 */
	public Consonant(String symbol) {
		super(symbol);
	}

	public PlaceOfArticulation getPlaceOfArticulation() { return place; }
	public PlaceOfArticulation getSecondaryPOA() { return secondaryPlace; }
	public MannerOfArticulation getMannerOfArticulation() { return manner; }
	public boolean getIsVoiced() { return isVoiced; }
	
	public Consonant setPlaceOfArticulation(PlaceOfArticulation poa) { place = poa; return this; }
	public Consonant setSecondaryPOA(PlaceOfArticulation poa) { secondaryPlace = poa; return this; }
	public Consonant setMannerOfArticulation(MannerOfArticulation moa) { manner = moa; return this; }
	public Consonant setIsVoiced(boolean v) { isVoiced = v; return this; }
}
