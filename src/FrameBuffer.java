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
		int rgba = getRGBA(r, g, b, a);
		pixels[xc+yc*width] = rgba;

	}

	public void lineFloat(int x1, int y1, int x2, int y2, int r, int g, int b, int a) {
		int x,y;
		if (x1 == x2) {
			// draw vertical line
			if(y1<y2) {
				for (y = y1; y<=y2; y++) {
					point(x1, y, r, g, b, a);
				}
			}else{  // y2<=y1
				for (y = y2; y<=y1; y++) {
					point(x1, y, r, g, b, a);
				}
			}
		}else if(y1 == y2) {
			// draw horizontal line
			if(x1 < x2) {
				for (x = x1; x<=x2; x++) {
					point(x, y1, r, g, b, a);
				}
			}else{  // x2 <= x1
				for (x = x2; x<=x1; x++) {
					point(x, y1, r, g, b, a);
				}
			}
		}else{
			float m = (float)(y2-y1)/(x2-x1);  // (dy/dx)
			float c = y1-m*x1;
			int rgba = getRGBA(r, g, b, a);
			if(x1>x2) {
				for(x = x2+1; x<x1; x++) {
					y = (int) (m*x + c);
					point(x, y, r, g, b, a);
				}
			}else if (x2>x1) {
				for(x = x1+1; x<x2; x++) {
					y = (int) (m*x + c);
					point(x, y, r, g, b, a);
				}
			}
			point(x1, y1, r, g, b, a);
			point(x2, y2, r, g, b, a);
		}

	}

	public void bresehamLine(int x1, int y1, int x2, int y2, int r, int g, int b, int a) {
		int p,x,y;
		int deltaX = x2-x1; // dx
		int deltaY = y2-y1; // dy
		int twoDy = 2*deltaY; // 2dy
		int twoDx = 2*deltaX;
		int twoDyMinusDx = 2*(deltaY - deltaX); // 2dy-2dx
		int twoDyAddDx = 2*(deltaY + deltaX);// 2dx + 2dy
		x = x1;
		y = y1;
		p = 2*deltaY - deltaX;
		if(x1<x2) {
			if(y1<y2) {
				// x1<x2, y1<y2
				while(x <= x2) {
					// draw current point
					point(x, y, r, g, b, a);
					//Calculate next point
					x++;
					if(p<0) {
						p = p + twoDy;
					}else {
						p = p + twoDyMinusDx;
						y++;
					}
				}
				while(x <= x2) {
					// draw current point
					point(x, y, r, g, b, a);
					//Calculate next point
					x++;
					if(p<0) {
						p = p - twoDy;
					}else {
						p = p + twoDyMinusDx;
						y++;
					}
				}
			}else{
				// x1<x2, y1>=y2
				while(x <= x2) {
					// draw current point
					point(x, y, r, g, b, a);
					//Calculate next point
					x++;
					if(p<0) {
						p = p - twoDy;
					}else {
						p = p - twoDyAddDx;
						y--;
					}
				}
			}
		}else{
			if(y1<y2) {
				// x1>=x2, y1<y2
				while(x >= x2) {
					// draw current point
					point(x, y, r, g, b, a);
					//Calculate next point
					x--;
					if(p<0) {
						p = p - twoDyAddDx;
						y--;
					}else {
						p = p - twoDy;
					}
				}
			}else{
				// x1>=x2, y1>=y2
				while(x >= x2) {
					// draw current point
					point(x, y, r, g, b, a);
					//Calculate next point
					x--;
					if(p<0) {
						p = p - twoDy;
					}else {
						p = p + twoDyAddDx;
						y++;
					}
				}
			}
		}
	}


	// method for Breseham's line
	public void moveUP(int x, int y, int r, int g, int b, int a) {
		point(x, y-1, r, g, b, a);
	}
	public void moveRight(int x, int y, int r, int g, int b, int a) {
		point(x+1, y, r, g, b, a);
	}
	public void moveDown(int x, int y, int r, int g, int b, int a) {
		point(x, y+1, r, g, b, a);
	}


	// Definitions for the getRed, getGreen and getBlue functions. NOTE these are not complete!
	public int getRed(int xc, int yc) {
		return pixels[xc+yc*width] >> 24 & 0xFF;
	}

	public int getGreen(int xc, int yc) {
		return pixels[xc+yc*width] >> 16 & 0xFF;
	}

	public int getBlue(int xc, int yc) {
		return pixels[xc+yc*width] >> 8 & 0xFF;
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

	private int getRGBA (int r, int g, int b, int a) {
		int rgba = r;
		rgba = (rgba << 8) + g;
		rgba = (rgba << 8) + b;
		// TODO save alpha value in rgba using int
		return rgba;
	}
}
