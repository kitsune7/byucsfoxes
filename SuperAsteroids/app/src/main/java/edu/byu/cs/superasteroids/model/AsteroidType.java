package edu.byu.cs.superasteroids.model;

/**
 * A type of asteroid. This class is used to limit the number of times an image is loaded for an
 * asteroid. We only want to load one image for every type of image to avoid reloading the same
 * image over and over again.
 */
public class AsteroidType {
    private long id; /** The id of the AsteroidType in the database */
    private String type; /** The string representing the type of asteroid */
    private ImageInfo image; /** The image used for this type of asteroid */
    private int imageId; /** The id of the image for this type of asteroid that's in the ContentManager */

    public AsteroidType() {
        id = -1;
        imageId = -1;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ImageInfo getImage() {
        return image;
    }

    public int getImageId() {
        return imageId;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    /**
     * Checks if this object and the one provided are equal or not.
     * @param o The object being compared
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AsteroidType)) return false;
        if ( ((AsteroidType) o).getId() != id ||
                !type.equals( ((AsteroidType) o).getType() ) ||
                !image.equals(((AsteroidType) o).getImage()) ) {
            return false;
        }
        return true;
    }
}
