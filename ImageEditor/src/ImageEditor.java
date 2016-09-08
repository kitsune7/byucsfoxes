/**
 * The ImageEditor program allows you to make simple transformations on your
 * image. It's a poor man's photoshop so to speak and my first real java
 * project.
 * 
 * @author	Christopher Bradshaw
 * @version 1.0
 */
import java.io.*;
import java.util.Scanner;

public class ImageEditor {
	
	public static void main(String[] args) {
		ImageEditor editor = new ImageEditor();
		editor.runEditor(args);
	}
	
	/**
	 * This parses the arguments passed in from command-line, reads the image,
	 * runs any transformations it needs, and writes the new image.
	 * 
	 * @param args	The list of arguments from command-line
	 */
	public void runEditor(String[] args) {
		// Parse command-line arguments
		if (args.length < 3) {
			System.out.println(
				"The program must be run with at least 3 arguments."
			);
			System.out.println(
				"USAGE: java ImageEditor in-file out-file (grayscale|" +
				"invert|emboss|motionblur motion-blur-length)"
			);
			return;
		}
		String source = args[0];
		String destination = args[1];
		String transform_string = args[2];
		int blur = 0;
		if (args.length == 4) blur = Integer.parseInt(args[3]);
		
		
		try {
			// Read the image
			Scanner scanner = new Scanner(new File(source)).useDelimiter(
				"(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)"
			);
		
			String magic_number = scanner.next();
			int width = Integer.parseInt(scanner.next());
			int height = Integer.parseInt(scanner.next());
			int max_color = Integer.parseInt(scanner.next());
			Pixel[][] data = new Pixel[height][width];
			
			int pixel_color_count = 3;
			for (int row = 0; row < height; ++row) {
				for (int col = 0; col < width; ++col) {
					int r = Integer.parseInt(scanner.next());
					int g = Integer.parseInt(scanner.next());
					int b = Integer.parseInt(scanner.next());
					data[row][col] = new Pixel(r, g, b);
				}
			}
			
			Image image = new Image(width, height, data);
			
			
			// Run transformation
			Transformation transformation;
			if (args.length == 4) {
				transformation = new Transformation(
					transform_string,
					image,
					blur
				);
			} else {
				transformation = new Transformation(
					transform_string,
					image
				);
			}
			Image new_image = transformation.transform();
			
			// Write the new image
			try {
				PrintWriter out = new PrintWriter(destination);
				out.println(magic_number);
				out.println(width);
				out.println(height);
				out.println(max_color);
				out.print(new_image.toString());
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
