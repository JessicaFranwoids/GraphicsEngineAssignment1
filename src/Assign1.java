import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Assign1 {
	public Assign1() {

	}
	public static void main(String[] args) {

		System.out.println("Starting 2D engine...");
		//Buffer, Canvas and Frame containers.
		FrameBuffer buffer = null;
		MainCanvas canvas = null;
		JFrame frame = null;

		//Tools to read command line and text file instructions
		BufferedReader reader = null;
		Scanner scanner;
		String command;
		int lineNumber = 0;
		String trimmedLine;

		// graphic variables
		int xc,yc,x1,y1,x2,y2;//points
		int red, green, blue, alpha;//color
		String rgbaString;
		int[] color;
		String str;//temp string
		String[] nodes;//polygon nods, nods[n-1]:color string of the polygon
		double radius;
		//Look at each file
		for (int i = 0; i < args.length; i++) {
			try {

				//Open file and read line
				reader = new BufferedReader(new FileReader(args[i]));
				String line = reader.readLine();

				//Process and read every line
				while (line != null) {

					//Clean up any extra spaces etc.
					trimmedLine = line;
					while (trimmedLine != null && trimmedLine.trim().equals("")) {
						line = reader.readLine();
						trimmedLine = line;
					}
					lineNumber++;

					//ignore empty
					if(line == null){
						break;
					}

					//Filter comments
					if (line.contains("#")) {
						String noComments;
						noComments = line.substring(0, line.indexOf("#"));
						//if there is text before comment, process it, otherwise ignore the line
						if (noComments.length() > 0) {
							line = noComments; //save the text before comment
						} else {
							line = reader.readLine();
							continue;
						}
					}

					if (line.contains("(")) {
						//strip spaces from after opening bracket - ensure scanner reads RGB values together with next()
						//0--->indexof( "(" )--->end��cut down space in "()"
						line = line.substring(0, line.indexOf("(")) + line.substring(line.indexOf("("), line.length()).replace(" ", "");
					}

					//Store command
					command = line.substring(0, line.indexOf(" "));

					//Read all the arguments into a scanner
					scanner = new Scanner(line.substring(line.indexOf(" "), line.length()));

					switch (command) {
					case "INIT":
						//Create the buffer and canvas
						int width = scanner.nextInt();
						int height = scanner.nextInt();
						buffer = new FrameBuffer(width, height);
						canvas = new MainCanvas(buffer);

						//Set up a swing frame
						frame = new JFrame();
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setSize(width, height);
						frame.add(canvas);
						frame.setTitle("Assign1");
						frame.setVisible(true);
						System.out.println("Creating canvas, frame and buffer of size " + width + "x" + height);
						break;

					case "POINT":
						//Get x and y location
						xc = scanner.nextInt();
						yc = scanner.nextInt();
						//get the colour
						rgbaString = scanner.next();

						//set the color,using catchRGBA function
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						//Draw a point with our point method
						buffer.point(xc, yc, red, green, blue, alpha);
						canvas.repaint();
						System.out.println("Drawing point: (" + xc + "," + yc + ", R=" + red + " G="+green + " B="+blue + " A="+alpha + ")");                           
						break;

					case "LINE_FLOAT":
						//read end point values from next 4 ints
						x1 = scanner.nextInt();
						y1 = scanner.nextInt();
						x2 = scanner.nextInt();
						y2 = scanner.nextInt();
						// read and save rgba color
						rgbaString = scanner.next();
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];

						// call lineFloat method to change pixel array and update canvas
						buffer.lineFloat(x1, y1, x2, y2, red, green, blue, alpha);
						canvas.repaint();
						System.out.println("Drawing line: (" + x1 + "," + y1 
								+ "), (" + x2 + "," + y2 
								+ ") , R=" + red + " G="+green + " B="+blue + " A="+ alpha + ")");
						break;

					case "LINE":
						//read end point values from next 4 ints
						x1 = scanner.nextInt();
						y1 = scanner.nextInt();
						x2 = scanner.nextInt();
						y2 = scanner.nextInt();
						//read and save rgba color
						rgbaString = scanner.next();
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						// draw Bresenham's line
						buffer.bresehamLine(x1, y1, x2, y2, red, green, blue, alpha);
						System.out.println("Drawing Bresenham's line: (" + x1 + "," + y1 
								+ "), (" + x2 + "," + y2 
								+ ") , R=" + red + " G="+green + " B="+blue + " A="+ alpha + ")");
						break;
					case "LINE_WIDTH":
						//TODO draw uk_flag
						break;
					case "OUTLINE_POLYGON":
						//read in data
						str = scanner.nextLine();
						str = str.substring(1);
						nodes = str.split(" ");
						//get r g b a of polygon
						rgbaString = nodes[nodes.length-1];
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						buffer.outlinePolygon(nodes, red, green, blue, alpha);
						System.out.println("Drawing OUTLINE_POLYGON: (" + nodes.toString()
							+ ") , R=" + red + " G="+green + " B="+blue + " A="+ alpha + ")");
						
						break;
					case "FILL_POLYGON":
						//read in data
						str = scanner.nextLine();
						str = str.substring(1);
						nodes = str.split(" ");
						//get r g b a of polygon
						rgbaString = nodes[nodes.length-1];
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						buffer.fillPolygon(nodes, red, green, blue, alpha);
						System.out.println("Drawing FILL_POLYGON: (" + str
						+ ") , R=" + red + " G="+green + " B="+blue + " A="+ alpha + ")");					
						break;
					case "OUTLINE_CIRCLE":
						//read information
						xc = scanner.nextInt();
						yc = scanner.nextInt();
						radius = scanner.nextDouble();
						//get r g b a of circle
						rgbaString = scanner.next();
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						buffer.outlineCircle( xc, yc, radius, red, green, blue, alpha);
						System.out.println("Drawing OUTLINE_CIRCLE: (" + xc + "," + yc + "Radius= "+ radius+ ", R=" + red + " G="+green + " B="+blue + " A="+alpha + ")");		
						
						break;
						
					case "FILL_CIRCLE":
						//read information
						xc = scanner.nextInt();
						yc = scanner.nextInt();
						radius = scanner.nextDouble();
						//get r g b a of circle
						rgbaString = scanner.next();
						color = catchRGBA(rgbaString);
						red = color[0];
						green = color[1];
						blue = color[2];
						alpha = color[3];
						buffer.fillCircle( xc, yc, radius, red, green, blue, alpha);
						System.out.println("Drawing FILL_CIRCLE: (" + xc + "," + yc + "Radius= "+ radius+ ", R=" + red + " G="+green + " B="+blue + " A="+alpha + ")");
						break;
					case "PAUSE":
						int millis = scanner.nextInt();
						System.out.println("Pause: " + millis + " milliseconds");
						try {
							Thread.sleep(millis);
						} catch (InterruptedException e) {
							System.out.println("Problem with sleep...");
							e.printStackTrace();
						}
						break;
					case "SAVE":

						String fileName = scanner.next();
						canvas.save(fileName);
						System.out.println("Saving file:" + fileName);

						break;
					default:
						System.out.println("Unknown command at line:  " + lineNumber);
					}
					line = reader.readLine();

				}

			} catch (FileNotFoundException e) {
				System.out.println("Error: File could not be found: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Error: Unexpected IO exception encountered");
			}

		}
	}

	public static int[] catchRGBA(String rgbaString) {
		rgbaString = rgbaString.substring(1, rgbaString.length()-1);
		String[] temp = rgbaString.split(",");  
		int[] color = {Integer.parseInt(temp[0]),
				Integer.parseInt(temp[1]),
				Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3])};
		if(color[0]>255 || color[0]<0) {color[0] = 0;}
		if(color[1]>255 || color[1]<0) {color[1] = 0;}
		if(color[2]>255 || color[2]<0) {color[2] = 0;}
		if(color[3]>255 || color[3]<0) {color[3] = 0;}
		return color;
	}
}

