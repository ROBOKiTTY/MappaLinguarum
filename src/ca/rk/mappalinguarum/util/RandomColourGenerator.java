package ca.rk.mappalinguarum.util;

import java.util.Random;

/**
 * singleton class that generates pseudorandom colours to be assigned to Locations
 * 
 * @author RK
 *
 */

public class RandomColourGenerator {

	private static RandomColourGenerator rcg;
	private final double PHI;
	
	private Random rng;
	
	/**
	 * private constructor prevents outside instantiation
	 */
	private RandomColourGenerator() {
		rng = new Random();
		PHI = (1 + Math.sqrt(5) ) / 2;
	}
	
	/**
	 * gets the instance, creating it if it's null
	 * 
	 * @return the RandomColourGenerator object
	 */
	public static RandomColourGenerator getInstance() {
		if (rcg == null) {
			rcg = new RandomColourGenerator();
		}
		return rcg;
	}
	
	/**
	 * generate a colour
	 * 
	 * @return a generated Colour object
	 */
	public Colour generateColour() {
		int r = nextInt(Colour.MAX_VALUE);
		int g = nextInt(Colour.MAX_VALUE);
		int b = nextInt(Colour.MAX_VALUE);

		return new Colour(r, g, b);
	}
	
	/**
	 * returns a mixed colour from the two input Colours, using the secondary only as
	 * a pseudorandom reference; the result should be a shade of the primary colour
	 * 
	 * @param primary a primary Colour
	 * @param secondary a secondary Colour
	 * @return a mixed Colour
	 */
	public Colour mixColours(Colour primary, Colour secondary) {
		int referenceValue = (secondary.getRed() + secondary.getGreen() + secondary.getBlue() ) / 3;
		
		int r = (primary.getRed() * 6 + referenceValue) / 7;
		int g = (primary.getGreen() * 6 + referenceValue) / 7;
		int b = (primary.getBlue() * 6 + referenceValue) / 7;
		
		return new Colour(r, g, b);
	}
	
	/**
	 * return a pseudorandom integer in normal distribution
	 * 
	 * @param upperLimit an integer indicating the largest value that can be produced (inclusive)
	 * @return an integer 0-upperLimit (inclusive)
	 */
	private int nextInt(int upperLimit) {
		double num = rng.nextDouble() + (1 / PHI);
		num -= Math.floor(num);
		
		return (int) Math.floor(num * (upperLimit + 1) );
	}
}
