package ca.rk.mappalinguarum.util.textures;

import java.awt.image.BufferedImage;
import java.util.Random;

import ca.rk.mappalinguarum.ui.Map;
import ca.rk.mappalinguarum.ui.ViewMode;
import ca.rk.mappalinguarum.ui.interfaces.IObserver;
import ca.rk.mappalinguarum.util.Colour;

/**
 * a texture pattern that is drawn on a polygon
 * 
 * some code based on this Stack Exchange thread:
 * https://gamedev.stackexchange.com/questions/23625/how-do-you-generate-tileable-perlin-noise 
 * 
 * @author RK
 *
 */
public class TexturePattern implements IObserver {
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	private static final int NOISE_PERIOD = 32; 
	private static final int[] PERMUTATIONS = generatePermutations();
	private static final double[][] GRADIENT_DIRECTIONS = computeGradientDirections();
	private static final double FREQUENCY = 1 / 16.0;
	private static final int OCTAVES = 3;
	
	private Map map;
	private Colour background;
	private Colour familyBackground;
	private BufferedImage image;
	private BufferedImage familyImage;
	private boolean isBrightened = false;
	
	public TexturePattern(Map m, Colour background, Colour familyBackground) {
		map = m;
		this.background = background;
		this.familyBackground = familyBackground;
		image=  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		familyImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		shufflePermutations();
		generateTexturePattern();
	}
	
	/**
	 * generate an array of unsigned ints from 1~NOISE_PERIOD 
	 * 
	 * @return array of int
	 */
	private static int[] generatePermutations() {
		int[] perms = new int[NOISE_PERIOD];
		for (int i = 0; i < NOISE_PERIOD; ++i) {
			perms[i] = i;
		}
		
		return perms;
	}
	
	/**
	 * shuffle array of permutations
	 * 
	 * @return shuffled array
	 */
	private static int[] shufflePermutations() {
		int[] perms = PERMUTATIONS;
		
		int index;
		int temp;
		Random rng = new Random();
		for (int i = NOISE_PERIOD - 1; i > 0; --i) {
			index = rng.nextInt(i + 1);
			temp = perms[index];
			perms[index] = perms[i];
			perms[i] = temp;
		}
		
		return perms;
	}
	
	/**
	 * compute a two-dimensional array of double of NOISE_PERIOD
	 * 
	 * @return two-d array of double
	 */
	private static double[][] computeGradientDirections() {
		double[][] gradDirs = new double[NOISE_PERIOD][2];
		for (int i = 0; i < NOISE_PERIOD; ++i) {
			gradDirs[i][0] = Math.cos((i + 1) * 2 * Math.PI / NOISE_PERIOD);
			gradDirs[i][1] = Math.sin((i + 1) * 2 * Math.PI / NOISE_PERIOD);
		}
		
		return gradDirs;
	}
	
	private interface TwoIntsToDoubleOperator {
		double run(int x, int y);
	}
	
	/**
	 * tilable Perlin noise
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param period up to 256
	 * @return noise
	 */
	private double makeTilingNoise(double x, double y, int period) {
		TwoIntsToDoubleOperator surflet = (gridX, gridY) -> {
			double distX = Math.abs(x - gridX);
			double distY = Math.abs(y - gridY);
			double polyX = 1 - 6 * Math.pow(distX, 5) + 15 * Math.pow(distX, 4) - 10 * Math.pow(distX, 3);
			double polyY = 1 - 6 * Math.pow(distY, 5) + 15 * Math.pow(distY, 4) - 10 * Math.pow(distY, 3);
			int hashIndex = PERMUTATIONS[gridX % period] + gridY % period;
			if (hashIndex >= NOISE_PERIOD) {
				hashIndex = NOISE_PERIOD - 1;
			}
			else if (hashIndex < 0) {
				hashIndex = 0;
			}
			int hash = PERMUTATIONS[hashIndex];
			double grad = (x - gridX) * GRADIENT_DIRECTIONS[hash][0] + (y - gridY) * GRADIENT_DIRECTIONS[hash][1];
			
			return polyX * polyY * grad;
		};
		
		final int iX = (int) x;
		final int iY = (int) y;
		
		double result = surflet.run(iX + 0, iY + 0) + surflet.run(iX + 1, iY + 0) +
						surflet.run(iX + 0, iY + 1) + surflet.run(iX + 1, iY + 1);
		
		return result;
	}
	
	/**
	 * fractional brownian motion function
	 * 
	 * @param x
	 * @param y
	 * @param period
	 * @param octaves
	 * @return
	 */
	private double fBm(double x, double y, int period, int octaves) {
		double result = 0;
		for (int i = 1; i <= octaves; ++i) {
			result += Math.pow(0.5, i) *
					makeTilingNoise(x * Math.pow(2, i),
									y * Math.pow(2, i),
									period * (int) Math.pow(2, i));
		}
		
		return result;
	}
	
	/**
	 * draw texture pattern onto images
	 */
	private void generateTexturePattern() {
		Colour foreground = Colour.lightenColour(background);
		Colour familyForeground = Colour.lightenColour(familyBackground);
		double noise;
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < HEIGHT; ++y) {
				noise = fBm(x * FREQUENCY, y * FREQUENCY, (int) (WIDTH * FREQUENCY), OCTAVES);
				noise = (noise + 1) / 2.0; //normalize to 0-1
				if (noise <= 0.5) {
					image.setRGB(x, y, Colour.toInt(Colour.TRANSPARENCY,
							background.getRed(), background.getGreen(), background.getBlue()));
					familyImage.setRGB(x, y, Colour.toInt(Colour.TRANSPARENCY,
							familyBackground.getRed(), familyBackground.getGreen(), familyBackground.getBlue()));
				}
				else {
					image.setRGB(x, y, Colour.toInt(Colour.TRANSPARENCY,
							foreground.getRed(), foreground.getGreen(), foreground.getBlue()));
					familyImage.setRGB(x, y, Colour.toInt(Colour.TRANSPARENCY,
							familyForeground.getRed(), familyForeground.getGreen(), familyForeground.getBlue()));
				}
			}
		}
	}

	@Override
	public void update() {
		if (isBrightened) {
			for (int x = 0; x < WIDTH; ++x) {
				for (int y = 0; y < HEIGHT; ++y) {
					int argb = getImage().getRGB(x, y);
					argb = Colour.darkenARGB(argb);
					getImage().setRGB(x, y, argb);
				}
			}
		}
		else {
			for (int x = 0; x < WIDTH; ++x) {
				for (int y = 0; y < HEIGHT; ++y) {
					int argb = getImage().getRGB(x, y);
					argb = Colour.lightenARGB(argb);
					getImage().setRGB(x, y, argb);
				}
			}
		}
		
		isBrightened = !isBrightened;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((background == null) ? 0 : background.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + (isBrightened ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (!(obj instanceof TexturePattern)) { return false; }
		TexturePattern other = (TexturePattern) obj;
		if (background == null)
		{
			if (other.background != null) { return false; }
		}
		else if (!background.equals(other.background)) { return false; }
		if (image == null)
		{
			if (other.image != null) { return false; }
		}
		else if (!image.equals(other.image)) { return false; }
		if (isBrightened != other.isBrightened) { return false; }
		return true;
	}

	public BufferedImage getImage() {
		ViewMode viewMode = map.getViewMode();
		switch(viewMode) {
			case FAMILIES:
				return familyImage;
			case MOSAIC:
				return image;
			default:
				return image;
		}
	}
	public void setBackgroundColour(Colour background) { this.background = background; }
}
