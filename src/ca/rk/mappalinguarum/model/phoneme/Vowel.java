package ca.rk.mappalinguarum.model.phoneme;

/**
 * encapsulates a phonemic vowel and its associated information
 * 
 * @author RK
 *
 */

public class Vowel extends Phoneme {

	private VowelFrontness frontness;
	private VowelHeight height;
	private boolean isRounded;
	
	/**
	 * constructs an empty Vowel
	 */
	public Vowel() {
		super();
	}
	
	/**
	 * constructs an empty Vowel represented by the input IPA symbol
	 * 
	 * @param symbol a string IPA symbol
	 */
	public Vowel(String symbol) {
		super(symbol);
	}

	public VowelFrontness getFrontness() { return frontness; }
	public VowelHeight getHeight() { return height; }
	public boolean getIsRounded() { return isRounded; }
	
	public void setVowelFrontness(VowelFrontness vf) { frontness = vf; }
	public void setVowelHeight(VowelHeight vh) { height = vh; }
	public void setIsRounded(boolean r) { isRounded = r; }
}
