package edu.byu.cs.superasteroids.model;

/**
 * This class stores information about an image used for an object in the game.
 */
public class ImageInfo {
    private String path; /** The file path of the image */
    private int width; /** The total width of the image, in pixels */
    private int height; /** The total height of the image, in pixels */

    public ImageInfo() {}

    public ImageInfo(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the center of the image.
     * @return A coordinate point of the center of the image
     */
    public Coordinate getCenter() {
        return new Coordinate(width/(float)2, height/(float)2);
    }


    /**
     * Checks whether or not the given object is equal to this ImageInfo object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ImageInfo)) return false;
        if (!path.equals( ((ImageInfo) o).getPath() ) ||
            ((ImageInfo)o).getWidth() != width ||
            ((ImageInfo)o).getHeight() != height) {
            return false;
        }
        return true;
    }
}
