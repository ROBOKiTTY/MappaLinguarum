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
	
	public Vowel setVowelFrontness(VowelFrontness vf) { frontness = vf; return this; }
	public Vowel setVowelHeight(VowelHeight vh) { height = vh; return this; }
	public Vowel setIsRounded(boolean r) { isRounded = r; return this; }
}
