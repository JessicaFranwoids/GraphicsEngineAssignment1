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
	// method for Breseham's line
	public void bresehamLine(int x1, int y1, int x2, int y2, int r, int g, int b, int a) {
		int p,x,y;
		int dx = Math.abs(x2-x1); // dx
		int dy = Math.abs(y2-y1) ; // dy
		int twoDy = 2*dy; // 2dy
		int twoDx = 2*dx;
		int twoDyMinusDx = 2*(dy - dx); // 2dy-2dx
		int twoDyAddDx = 2*(dy + dx);
		int s1, s2, temp;
		int interchange = 0;//interchage == 1:dy>dx; interchage == 0:dx>dy
		p = 2*dy - dx;
		x = x1;
		y = y1;
//===================================================================
		if((x2-x1>0 && y2-y1>0) || (x2-x1<0 && y2-y1<0)) {
			
		}
		if((x1>x2)) {
			//draw from (x2,y2)
			x = x2; x2 = x1; y = y2; y2 = y1;
		}
		while(Math.abs(y-y2) != 0) {
			// draw current point
			point(x, y, r, g, b, a);
			//Calculate next point
			if(y<=y2) {
				//dy/dx > 0
				y++;
			}else {
				//dy/dx <= 0
				y--;
			}
			if(p<0) {
				p = p + twoDyMinusDx;
				x++;
			}else {
				p = p - twoDx;
			}
		}
//		//===========================================================
		if(x2>=x1) {
			s1 = 1;
		}else {
			s1 = -1;
		}
		if(y2>=y1) {
			s2 = 1;
		}else {
			s2 = -1;
		}
		if(dy>dx) {//exchange dx and dy
			temp = dx;
			dx = dy;
			dy = temp;
			interchange = 1;//interchage == 1:dy>dx; interchage == 0:dx>dy
		}else {
			
		}
		
		for(int i = 0; i<=dx; i++) {
			point(x,y,r,g,b,a);
			if(p>0) {
				if(interchange == 1) {
					//interchage == 1:dy>dx; interchage == 0:dx>dy
					x+=s1;
				}else {
					y+=s2;
					p-=twoDx;
				}
			}
			if(interchange == 1) {
				//interchage == 1:dy>dx; interchage == 0:dx>dy
				y+=s2;
			}else {
				x+=s1;
				p+=twoDy;
			}
		}
//	
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
