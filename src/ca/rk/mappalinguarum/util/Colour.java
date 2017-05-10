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
	private static final Colour REF_WHITE = new Colour(255, 255, 255);
	private static final Colour REF_BLACK = new Colour(0, 0, 0);

	private int red;
	private int green;
	private int bloo;
	private int alpha;
	
	/**
	 * constructor sets RGB values
	 * 
	 * @param r red intensity
	 * @param g green intensity
	 * @param b blue intensity
	 * @throws InvalidColourException if any of the input values is out of bound
	 */
	public Colour(int r, int g, int b) throws InvalidColourException {
		this(r, g, b, TRANSPARENCY);
	}
	
	/**
	 * constructor sets RGBA values
	 * 
	 * @param r red intensity
	 * @param g green intensity
	 * @param b blue intensity
	 * @param a alpha
	 * @throws InvalidColourException if any of the input values is out of bound
	 */
	public Colour(int r, int g, int b, int a) throws InvalidColourException {
		if (r > MAX_VALUE || r < MIN_VALUE ||
			g > MAX_VALUE || g < MIN_VALUE ||
			b > MAX_VALUE || b < MIN_VALUE ||
			a > MAX_VALUE || a < MIN_VALUE) {
				throw new InvalidColourException();
			}
			red = r;
			green = g;
			bloo = b;
			alpha = a;
	}
	
	/**
	 * makes a copy of another colour
	 * 
	 * @param other other colour to copy
	 */
	public Colour(Colour other) {
		red = other.red;
		green = other.green;
		bloo = other.bloo;
		alpha = other.alpha;
	}
	
	/**
	 * make a Colour from a java.awt.Color object
	 * 
	 * @param other a java.awt.Color
	 */
	public Colour(Color other) {
		red = other.getRed();
		green = other.getGreen();
		bloo = other.getBlue();
		alpha = other.getAlpha();
	}
	
	/**
	 * converts a Colour object to a java.awt.Color object
	 * 
	 * @return a converted Color object
	 */
	public Color toColor() {
		return new Color(red, green, bloo, alpha);
	}
	
	/**
	 * return a lightened shade of the input colour by mixing it with an internal
	 * reference colour (REF_WHITE), which is white
	 * 
	 * @param colour the base colour to work with as argb int
	 * @return a lighter colour as argb int
	 */
	public static int lightenARGB(int argb) {
		int a = (argb >> 24) & 0xff;
		int r = (argb >> 16) & 0xff;
		int g = (argb >> 8) & 0xff;
		int b = argb & 0xff;
		
		r = (r + REF_WHITE.red) / 2;
		g = (g + REF_WHITE.green) / 2;
		b = (b + REF_WHITE.bloo) / 2;
		
		return toInt(a, r, g, b);
	}
	
	/**
	 * return a darkened shade of the input colour by mixing it with an internal
	 * reference colour (REF_BLACK), which is black
	 * 
	 * @param colour the base colour to work with as argb int
	 * @return a darker colour as argb int
	 */
	public static int darkenARGB(int argb) {
		int a = (argb >> 24) & 0xff;
		int r = (argb >> 16) & 0xff;
		int g = (argb >> 8) & 0xff;
		int b = argb & 0xff;
		
		r = (r + REF_BLACK.red) / 2;
		g = (g + REF_BLACK.green) / 2;
		b = (b + REF_BLACK.bloo) / 2;
		
		return toInt(a, r, g, b);
	}
	
	/**
	 * return a lightened shade of the input colour by mixing it with an internal
	 * reference colour (REF_WHITE), which is white
	 * 
	 * @param colour the base Colour to work with
	 * @return a lighter Colour
	 */
	public static Colour lightenColour(Colour colour) {
		int r = (colour.red + REF_WHITE.getRed() ) / 2;
		int g = (colour.green + REF_WHITE.getGreen() ) / 2;
		int b = (colour.bloo + REF_WHITE.getBlue() ) / 2;

		return new Colour(r, g, b);
	}
	
	/**
	 * return a darkened shade of the input colour by mixing it with an internal
	 * reference colour (REF_BLACK), which is black
	 * 
	 * @param colour the base Colour to work with
	 * @return a darker Colour
	 */
	public static Colour darkenColour(Colour colour) {
		int r = (colour.red + REF_BLACK.getRed() ) / 2;
		int g = (colour.green + REF_BLACK.getGreen() ) / 2;
		int b = (colour.bloo + REF_BLACK.getBlue() ) / 2;

		return new Colour(r, g, b);
	}
	
	/**
	 * converts a HSL int to a colour
	 * 
	 * @param h hue as 8-bit int
	 * @param s saturation as 8-bit int
	 * @param l luminosity as 8-bit int
	 * @return a converted colour
	 */
	public static Colour fromHSL(int h, int s, int l) {
		int rgb = Color.HSBtoRGB(h / (float) 255, s / (float) 255, l / (float) 255);
		return fromInt(rgb);
	}
	
	/**
	 * converts RGB values to HSL values 
	 * 
	 * @param r red as 8-bit int
	 * @param g green as 8-bit int
	 * @param b blue as 8-bit int
	 * @return HSL values as an array of float
	 */
	public static float[] rgbToHSL(int r, int g, int b) {
		return Color.RGBtoHSB(r, g, b, null);
	}
	
	/**
	 * packs three 8-bit RGB values into an int
	 * @return a 32-bit int
	 */
	public static int toInt(int red, int green, int blue) {
		return (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
	}
	
	/**
	 * packs three 8-bit ARGB values into an int
	 * @return a 32-bit int
	 */
	public static int toInt(int alpha, int red, int green, int blue) {
		return (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
	}
	
	/**
	 * unpacks a 32-bit ARGB int into a Colour
	 * @param i a packed integer
	 * @return a colour object
	 */
	public static Colour fromInt(int i) {
		int a = (i >> 24) & 0xff;
		int r = (i >> 16) & 0xff;
		int g = (i >> 8) & 0xff;
		int b = i & 0xff;
		
		return new Colour(r, g, b, a);
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
