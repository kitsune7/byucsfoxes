package edu.byu.cs.imageeditor.studentCode;

/**
 * Created by kitsune7 on 9/8/16.
 */
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

    /**
     * @return A PPM formatted string of the image
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("P3\n");
        builder.append(this.width);
        builder.append(" ");
        builder.append(this.height);
        builder.append("\n255\n");
        for (int row = 0; row < this.height; ++row) {
            for (int col = 0; col < this.width; ++col) {
                builder.append(this.data[row][col].toString());
                //builder.append("\n");
            }
        }
        return builder.toString();
    }
}

