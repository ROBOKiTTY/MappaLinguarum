package ca.rk.mappalinguarum.util;

import java.awt.Color;
import java.util.Iterator;

import ca.rk.mappalinguarum.util.exceptions.InvalidColourException;


/**
 * encapsulates a colour represented using the RGB colour model
 * 
 * @author RK
 *
 */
public class Colour implements Iterable<Integer>{

	private final String COLOUR_STRING_PREFIX = "0x";
	public static final int MAX_VALUE = 255;
	public static final int MIN_VALUE = 0;
	public static final int TRANSPARENCY = 200;

	private int red;
	private int green;
	private int bloo;
	
	/**
	 * constructor sets RGB values
	 * 
	 * @param r red intensity
	 * @param g green intensity
	 * @param b blue intensity
	 * @throws InvalidColourException if any of the input values is out of bound
	 */
	public Colour(int r, int g, int b) throws InvalidColourException{
		if (r > MAX_VALUE || r < MIN_VALUE ||
			g > MAX_VALUE || g < MIN_VALUE ||
			b > MAX_VALUE || b < MIN_VALUE	) {
			throw new InvalidColourException();
		}
		red = r;
		green = g;
		bloo = b;
	}
	
	/**
	 * converts a Colour object to a java.awt.Color object
	 * 
	 * @return a converted Color object
	 */
	public Color toColor() {
		return new Color(red, green, bloo, TRANSPARENCY);
	}
	
	/**
	 * converts RGB values to a hexadecimal numbar as a string
	 * e.g. 0xFFBBCC
	 */
	@Override
	public String toString() {
		StringBuilder buildar = new StringBuilder();
		buildar.append(COLOUR_STRING_PREFIX);
		
		for (Integer i : this) {
			if (i <= 0xf) {
				buildar.append("0" + Integer.toString(i, 16));
			}
			else {
				buildar.append(Integer.toString(i, 16));
			}
		}
		return buildar.toString();
	}

	/**
	 * this is just to be able to use a foreach loop to
	 * iterate through three numbars
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new ColourIterator();
	}
	
	//accessors
	public int getRed() { return red; }
	public int getGreen() { return green; }
	public int getBlue() { return bloo; }
	
	/**
	 * helper inner class that implements iterator to allow trivial
	 * iteration of a Colour object
	 * 
	 * @author RK
	 *
	 */
	private class ColourIterator implements Iterator<Integer> {
		
		private final int RED_POS = 0;
		private final int GREEN_POS = 1;
		private final int BLUE_POS = 2;
		
		private int nextColour = 0;
		
		@Override
		public boolean hasNext() {
			return nextColour <= BLUE_POS;
		}

		@Override
		public Integer next() {
			if (nextColour == RED_POS) {
				++nextColour;
				return red;
			}
			else if (nextColour == GREEN_POS) {
				++nextColour;
				return green;
			}
			else if (nextColour == BLUE_POS) {
				++nextColour;
				return bloo;
			}
			return null;
		}

		/**
		 * not supported; it does not make sense to remove one of the RGB values from a Colour.
		 * 
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"It does not make sense to remove one of the RGB values from a Colour.");
		}
	}
}
