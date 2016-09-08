public class Image {
	private int width;
	private int height;
	
	public Pixel[][] data;
	
	public Image(int width, int height, Pixel[][] data) {
		this.width = width;
		this.height = height;
		this.data = data;
	}
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < this.height; ++row) {
			for (int col = 0; col < this.width; ++col) {
				builder.append(this.data[row][col].toString());
			}
		}
		return builder.toString();
	}
}
