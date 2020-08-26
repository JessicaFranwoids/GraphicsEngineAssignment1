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
                    	//0--->indexof( "(" )--->end£¬cut down space in "()"
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
                            int xc = scanner.nextInt();
                            int yc = scanner.nextInt();
                            //get the colour
                            String rgbaString = scanner.next();
                            rgbaString = rgbaString.substring(1, rgbaString.length()-1);
                            String[] color = rgbaString.split(",");                          
                            //set the color
                            int red = Integer.parseInt(color[0]);
                            int green = Integer.parseInt(color[1]);
                            int blue = Integer.parseInt(color[2]);
                            int alpha = Integer.parseInt(color[3]);
                            //Draw a point with our point method
                            buffer.point(xc, yc, red, green, blue, alpha);
                            canvas.repaint();
                            System.out.println("Drawing point: (" + xc + "," + yc + ", R=" + red + " G="+green + " B="+blue + ")");                           
                            break;
                        case "LINE_FLOAT":
                            //read end point values from next 4 ints
                            int x1 = scanner.nextInt();
                            int y1 = scanner.nextInt();
                            int x2 = scanner.nextInt();
                            int y2 = scanner.nextInt();

                            int lineRed = 255, LineGreen = 0, lineBlue = 0, lineAlpha = 255;

                            //TODO: call lineFloat method to change pixel array and update canvas
                            canvas.repaint();

                            break;
                            
                        case "LINE":
                            break;
                        case "OUTLINE_POLYGON":
                            break;
                        case "FILL_POLYGON":
                            break;
                        case "OUTLINE_CIRCLE":
                            break;
                        case "FILL_CIRCLE":
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
}

