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
	
	public void setPlaceOfArticulation(PlaceOfArticulation poa) { place = poa; }
	public void setSecondaryPOA(PlaceOfArticulation poa) { secondaryPlace = poa; }
	public void setMannerOfArticulation(MannerOfArticulation moa) { manner = moa; }
	public void setIsVoiced(boolean v) { isVoiced = v; }
}
