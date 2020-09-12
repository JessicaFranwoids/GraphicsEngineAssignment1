import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

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
		if(xc<width && yc<height && xc>=0 && yc>=0) {
			pixels[xc+yc*width] = rgba;
		}
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
		int dx = Math.abs(x2-x1); // dx
		int dy = Math.abs(y2-y1) ; // dy
		int twoDy = 2*dy; // 2dy
		int twoDx = 2*dx;
		int twoDyMinusDx = 2*(dy - dx); // 2dy-2dx
		int twoDyAddDx = 2*(dy + dx);
		int p = 2*dy - dx;
		int x = x1;
		int y = y1;
		int xEnd = x2;
		int yEnd = y2;
		if((x1>x2)) {
			//draw from (x2,y2)
			x = x2; xEnd = x1; y = y2; yEnd = y1;
		}
		if(xEnd>=width) {xEnd = width-1;}
		if(yEnd>=height) {yEnd = height-1;}
		if(dx>dy) {
			// dx>dy, 0<k<1, so that 0< degree <45, 
			while(x <= xEnd) {
				// draw current point
				point(x, y, r, g, b, a);
				//Calculate next point
				x++;
				if(p<0) {
					p += twoDy;
				}else {
					p = p + twoDyMinusDx;
					if(y<=yEnd) {
						//
						y++;
					}else {
						//y1>y2,
						y--;
					}
				}
			}
		}else if(dx<dy){
			//dx<dy, k>1, so that degree >= 45
			while(Math.abs(y-yEnd) != 0) {
				// draw current point
				point(x, y, r, g, b, a);
				//Calculate next point
				if(y<=yEnd) {
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
		}else {
			// dx == dy
			while(Math.abs(y-yEnd) != 0) {
				// draw current point
				point(x, y, r, g, b, a);
				//Calculate next point
				x++;
				y++;
			}
		}
	}

	public void outlinePolygon(String[] nodes, int r, int g, int b, int a) {
		// n-1: rgba string
		// n-3: x2, n-2:y2
		// n-5: x1, n-4:y1
		int i = 0;
		int x1,y1,x2,y2;
		while(i<nodes.length-1) {
			if(i<=nodes.length-5) {
				x1 = Integer.parseInt(nodes[i]);
				y1 = Integer.parseInt(nodes[i+1]);
				x2 = Integer.parseInt(nodes[i+2]);
				y2 = Integer.parseInt(nodes[i+3]);
				bresehamLine(x1, y1 ,x2 , y2, r, g, b, a);
			}else {
				x1 = Integer.parseInt(nodes[i]);
				y1 = Integer.parseInt(nodes[i+1]);
				x2 = Integer.parseInt(nodes[0]);
				y2 = Integer.parseInt(nodes[1]);
				bresehamLine(x1, y1 ,x2 , y2, r, g, b, a);
			}
			i+=2;
		}
	}

	public void fillPolygon(String[] nodes, int r, int g, int b, int a) {
		//find yMin and yMax in nodes, y is in odd number
		int yMin = Integer.parseInt(nodes[1]);
		int yMax = Integer.parseInt(nodes[1]);
		int i=1;
		while(i<nodes.length -1) {
			//find yMin and yMax in nodes, y is in odd number
			if(Integer.parseInt(nodes[i])<yMin) {
				yMin = Integer.parseInt(nodes[i]);
			}
			if(Integer.parseInt(nodes[i])>yMax) {
				yMax = Integer.parseInt(nodes[i]);
			}
			i+=2;
		}
		//fill polygon: from yMin to yMax
		ActiveEdge[] newET = new ActiveEdge[yMax-yMin+1];
		ActiveEdge activeEdge;
		i=0; //even:x, odd: y; (x1:i, y1:i+1)(x2:i+2, y2:i+3)
		// n-1: rgba string
		// n-3: x2, n-2:y2
		// n-5: x1, n-4:y1
		int x1,y1,x2,y2;
		int edgeXwhenYmin, edgeYMax, edgeDx, edgeYMin;
		//insert first edge to new edge table
		while(i<nodes.length-5) {
			if(i<=nodes.length-5) {
				x1 = Integer.parseInt(nodes[i]);
				y1 = Integer.parseInt(nodes[i+1]);
				x2 = Integer.parseInt(nodes[i+2]);
				y2 = Integer.parseInt(nodes[i+3]);
			}else {
				x1 = Integer.parseInt(nodes[i]);
				y1 = Integer.parseInt(nodes[i+1]);
				x2 = Integer.parseInt(nodes[0]);
				y2 = Integer.parseInt(nodes[1]);

			}
			//calculate edge information
			edgeYMax = (y1<y2) ? y2 : y1;
			edgeYMin = (y1<y2) ? y1 : y2;
			edgeDx = Math.round((float)(y2-y1)/(x2-x1));
			edgeXwhenYmin = (y1<y2) ? x1 : x2;
			//create and insert new edge
			activeEdge = new ActiveEdge(edgeXwhenYmin, edgeDx, edgeYMax);
			if (newET[edgeYMin-yMin]==null) {
				newET[edgeYMin-yMin] = activeEdge;
			}else {
				// newET[nodeXwhenYmin]!=null
				if(newET[edgeYMin-yMin].getX()<activeEdge.getX()) {
					newET[edgeYMin-yMin].insert(activeEdge);
				}else {
					if(newET[edgeYMin-yMin].getX()==activeEdge.getX()) {

					}else {
						//newET[nodeXwhenYmin].getX()>activeEdge.getX()
						activeEdge.setNext(newET[edgeYMin-yMin]);
						newET[edgeYMin-yMin].setNext(activeEdge);
					}
				}
			}
			System.out.println(activeEdge.toString());
			i+=2;
		}
	}
	
	public void outlineCircle(int xc, int yc, double radius, int r, int g, int b, int a){
		float px = (float) ((float) 1-radius);
		int x = 0;
		int y = (int)radius;
		while(x<y) {
			//each time draw 8 direction points
			point(x+xc, y+yc, r, g, b, a);//(x,y)
			point(y+yc, x+xc, r, g, b, a);//(y,x)
			
			point(xc-x, yc-y, r, g, b, a);//(-x,-y)
			point(yc-y, xc-x, r, g, b, a);//(-y,-x)
			
			point(xc+x, yc-y, r, g, b, a);//(x,-y)
			point(yc+y, xc-x, r, g, b, a);//(y,-x)
			
			point(xc-x, yc+y, r, g, b, a);//(-x,y)
			point(yc-y, xc+x, r, g, b, a);//(-y,x)
			
			//calculate x and y by algorithm
			if(px < 0) {
				px += 2*x +3;
				x++;
			}else {
				px += 2*x - 2*y + 5;
				x++;
				y--;
			}
		}
	}
	
	public void fillCircle(int xc, int yc, double radius, int r, int g, int b, int a) {
		float px = (float) ((float) 1-radius);
		int x = 0;
		int y = (int)radius;
		while(x<y) {
			//each time draw line to 8 directions 
			bresehamLine(xc, yc, x+xc, y+yc, r, g, b, a);//(x,y)
			bresehamLine(xc, yc, y+yc, x+xc, r, g, b, a);//(y,x)
			
			bresehamLine(xc, yc, xc-x, yc-y, r, g, b, a);//(-x,-y)
			bresehamLine(xc, yc, yc-y, xc-x, r, g, b, a);//(-y,-x)
			
			bresehamLine(xc, yc, xc+x, yc-y, r, g, b, a);//(x,-y)
			bresehamLine(xc, yc, yc+y, xc-x, r, g, b, a);//(y,-x)
			
			bresehamLine(xc, yc, xc-x, yc+y, r, g, b, a);//(-x,y)
			bresehamLine(xc, yc, yc-y, xc+x, r, g, b, a);//(-y,x)
			
			//calculate x and y by algorithm
			if(px < 0) {
				px += 2*x +3;
				x++;
			}else {
				px += 2*x - 2*y + 5;
				x++;
				y--;
			}
		}
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

class ActiveEdge{
	private int x;
	private int dx; // xNext = x+dx;
	private int yMax;
	private ActiveEdge next;

	public ActiveEdge(int x, int dx, int yMax) {
		this.setX(x);
		this.dx = dx;
		this.yMax = yMax;
		this.next = null;
	}
	public String toString() {
		String str = "x: " + x
				+", dx: " + dx
				+", yMax: "+ yMax;
		if(this.next != null) {
			str += this.next.toString();
		}
		return str;
	}

	public void insert(ActiveEdge node) {
		ActiveEdge temp = this;
		if(temp.next == null) {
			temp.next = node;
			System.out.println("insert edge to the end, succeed");
		}else {
			// ascending by x, if x equals , ascending by dx
			while(temp.next.x<node.x && temp.next!=null) {
				temp = temp.next;
			}
			if(temp.x<node.x) {
				if (temp.next != null) {
					node.next = temp.next;
					temp.next = node;
				}else {
					temp.next = node;
				}
				System.out.println("insert edge in the middle, succeed");
			}else {
				//temp.x == node.x, ascending by dx
				while(temp.next.x==node.x && temp.next.dx<node.dx && temp.next!=null) {
					temp = temp.next;
				}
				if(temp.next == null) {
					temp.next = node;
				}else {
					node.next = temp.next;
					temp.next = node;
				}
				
				System.out.println("when temp.x == node.x, insert edge in the middle, succeed");
			}
		}
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getyMax() {
		return yMax;
	}

	public void setyMax(int yMax) {
		this.yMax = yMax;
	}

	public ActiveEdge getNext() {
		return next;
	}

	public void setNext(ActiveEdge next) {
		this.next = next;
	}

}
