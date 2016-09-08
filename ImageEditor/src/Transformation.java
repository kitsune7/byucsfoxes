public class Transformation {
	// Data members
	private String transform_string;
	private Image image;
	private int blur;
	
	
	// Public Methods
	
	public Transformation(String t_string, Image image) {
		this.transform_string = t_string;
		this.image = image;
	}
	
	public Transformation(String t_string, Image image, int blur) {
		this.transform_string = t_string;
		this.image = image;
		this.blur = blur;
	}
	
	public Image transform() {
		switch (transform_string) {
			case "invert":
				invert();
				break;
			case "grayscale":
				grayscale();
				break;
			case "emboss":
				emboss();
				break;
			case "motionblur":
				motionBlur();
				break;
			default:
				System.out.println(
					"Invalid string provided for the type of transformation."
				);
				System.out.println(
					"USAGE: java ImageEditor in-file out-file (grayscale|" +
					"invert|emboss|motionblur motion-blur-length)"
				);
				return null;
		}
		return this.image;
	}
	
	
	// Private Methods
	
	private void invert() {
		Pixel[][] data = this.image.data;
		int width = this.image.getHeight();
		int height = this.image.getWidth();
		
		for (int row = 0; row < width; ++row) {
			for (int col = 0; col < height; ++col) {
				int r = data[row][col].getR();
				int g = data[row][col].getG();
				int b = data[row][col].getB();
				
				r -= 255;
				g -= 255;
				b -= 255;
				
				data[row][col].setR( Math.abs(r) );
				data[row][col].setG( Math.abs(g) );
				data[row][col].setB( Math.abs(b) );
			}
		}
	}
	
	private void grayscale() {
		Pixel[][] data = this.image.data;
		int width = this.image.getHeight();
		int height = this.image.getWidth();
		
		for (int row = 0; row < width; ++row) {
			for (int col = 0; col < height; ++col) {
				int r = data[row][col].getR();
				int g = data[row][col].getG();
				int b = data[row][col].getB();
				int average = (r+g+b)/3;
				
				r = average;
				g = average;
				b = average;
				
				data[row][col].setR(r);
				data[row][col].setG(g);
				data[row][col].setB(b);
			}
		}
	}
	
	private void emboss() {
		Pixel[][] data = this.image.data;
		int width = this.image.getHeight();
		int height = this.image.getWidth();
		
		for (int row = width-1; row > 0; --row) {
			for (int col = height-1; col > 0; --col) {
				int r = data[row][col].getR();
				int g = data[row][col].getG();
				int b = data[row][col].getB();
				
				int r_diff = r - data[row-1][col-1].getR();
				int g_diff = g - data[row-1][col-1].getG();
				int b_diff = b - data[row-1][col-1].getB();
				
				int v = r_diff;
				if ( Math.abs(g_diff) > Math.abs(v) ) v = g_diff;
				if ( Math.abs(b_diff) > Math.abs(v) ) v = b_diff;
				
				v += 128;
				
				if (v > 255) v = 255;
				if (v < 0) v = 0;
				
				data[row][col].setR(v);
				data[row][col].setG(v);
				data[row][col].setB(v);
			}
		}
		
		for (int row = 0; row < width; ++row) {
			data[row][0].setR(128);
			data[row][0].setG(128);
			data[row][0].setB(128);
		}
		for (int col = 1; col < height; ++col) {
			data[0][col].setR(128);
			data[0][col].setG(128);
			data[0][col].setB(128);
		}
		
	}
	
	private void motionBlur() {
		Pixel[][] data = this.image.data;
		int width = this.image.getHeight();
		int height = this.image.getWidth();
		
		if (this.blur <= 0) {
			System.out.println("The blur value must be greater than 0.");
			return;
		}
		
		for (int row = 0; row < width; ++row) {
			for (int col = 0; col < height; ++col) {
				int r = data[row][col].getR();
				int g = data[row][col].getG();
				int b = data[row][col].getB();
				
				int r_average = 0;
				int g_average = 0;
				int b_average = 0;
				
				int start = col;
				int end = start + this.blur;
				int count = 0;
				if (end > height) end = height;
				for (int i = start; i < end; i++) {
					r_average += data[row][i].getR();
					g_average += data[row][i].getG();
					b_average += data[row][i].getB();
					count++;
				}
				r_average /= count;
				g_average /= count;
				b_average /= count;
				
				data[row][col].setR(r_average);
				data[row][col].setG(g_average);
				data[row][col].setB(b_average);
			}
		}
	}
}
