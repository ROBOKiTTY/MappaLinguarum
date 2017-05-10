package ca.rk.mappalinguarum.util.textures;

import java.awt.image.BufferedImage;
import java.util.Random;

import ca.rk.mappalinguarum.ui.interfaces.IObserver;
import ca.rk.mappalinguarum.util.Colour;

/**
 * a texture pattern that is drawn on a polygon
 * 
 * @author RK
 *
 */
public class TexturePattern implements IObserver {
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;
	
	private Colour background;
	private BufferedImage image;
	private boolean isBrightened = false;
	
	public TexturePattern(Colour background) {
		this.background = background;
		image=  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		generateTexturePattern();
	}
	
	/**
	 * draw texture pattern onto image
	 */
	private void generateTexturePattern() {
		int r = background.getRed();
		int g = background.getGreen();
		int b = background.getBlue();
		float[] hsl = Colour.rgbToHSL(r, g, b);
		
		int luminosity;
		double[][] noise = generateNoise();
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < HEIGHT; ++y) {
				luminosity = 192 + (int) turbulence(x, y, 64, noise) / 4;
				Colour colour = Colour.fromHSL( (int)(hsl[0] * 255), (int)(hsl[1] * 255), luminosity);
				image.setRGB(x, y,
						Colour.toInt(Colour.TRANSPARENCY, colour.getRed(),  colour.getGreen(),  colour.getBlue() ) );
			}
		}
	}
	
	/**
	 * @return a noise map WIDTH x HEIGHT
	 */
	private double[][] generateNoise() {
		Random rng = new Random();
		
		double[][] noise = new double[WIDTH][HEIGHT];
		
		for (int y = 0; y < HEIGHT; ++y) {
			for (int x = 0; x < WIDTH; ++x) {
				noise[y][x] = rng.nextGaussian();
			}
		}
		
		return noise;
	}
	
	/**
	 * smooth out noise using bilinear interpolation
	 * @return average of four neighbouring pixels
	 */
	private double smoothNoise(double x, double y, double[][] noise) {
		double fractX = x - (int)x;
		double fractY = y - (int)y;
		
		int x1 = ((int)x + WIDTH) % WIDTH;
		int y1 = ((int)y + HEIGHT) % HEIGHT;
		
		int x2 = (x1 + WIDTH - 1) % WIDTH;
		int y2 = (y1 + HEIGHT - 1) % HEIGHT;
		
		double avg = 0;
		avg += fractX * fractY * noise[y1][x1];
		avg += (1 - fractX) * fractY * noise[y1][x2];
		avg += fractX * (1 - fractY) * noise[y2][x1];
		avg += (1 - fractX) * (1 - fractY) * noise[y2][x2];
		
		return avg;
	}
	
	private double turbulence(double x, double y, double size, double[][] noise) {
		double value = 0;
		double initialSize = size;
		
		while (size >= 1) {
			value += smoothNoise(x / size, y / size, noise) * size;
			size /= 2;
		}
		
		return 128 * value / initialSize;
	}

	@Override
	public void update() {
		//TODO: make this work maybe
//		if (isBrightened) {
//			for (int x = 0; x < WIDTH; ++x) {
//				for (int y = 0; y < HEIGHT; ++y) {
//					int argb = image.getRGB(x, y);
//					argb = Colour.darkenARGB(argb);
//					image.setRGB(x, y, argb);
//				}
//			}
//		}
//		else {
//			for (int x = 0; x < WIDTH; ++x) {
//				for (int y = 0; y < HEIGHT; ++y) {
//					int argb = image.getRGB(x, y);
//					argb = Colour.lightenARGB(argb);
//					image.setRGB(x, y, argb);
//				}
//			}
//		}
		
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

	public BufferedImage getImage() { return image; }
	public void setBackgroundColour(Colour background) { this.background = background; }
}
