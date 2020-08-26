/**
 * Frame buffer class skeleton
 */

public class FrameBuffer {

	//Store all pixel data in this int array.
	//NOTE: 1 int should contain red, green and blue data NOT 3!
	private int[] pixels;
	private int width;
	private int height;
	
	//Set up memory for pixel data
	public FrameBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	//A start on the point function. NOTE this is not complete!
	public void point(int xc, int yc, int r, int g, int b, int a) {
		// save point r g b a information into array
		int rgba = r;
		rgba = (rgba << 4) + g;
		rgba = (rgba << 8) + b;
		rgba = (rgba << 12) + a;
		pixels[xc+yc*width] = rgba;
		
	}

	public void lineFloat(int x1, int x2, int y1, int y2, int r, int g, int b, int a) {
		
	}
	// Definitions for the getRed, getGreen and getBlue functions. NOTE these are not complete!
	public int getRed(int xc, int yc) {
		// TODO: Implement these functions using bitwise operations and masking to retrieve individual colour components
		return 0;
	}

	public int getGreen(int xc, int yc) {
		// TODO: Implement these functions using bitwise operations and masking to retrieve individual colour components
		return 0;
	}

	public int getBlue(int xc, int yc) {
		// TODO: Implement these functions using bitwise operations and masking to retrieve individual colour components
		return 0;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getPixels() {
		return pixels;
	}
}
