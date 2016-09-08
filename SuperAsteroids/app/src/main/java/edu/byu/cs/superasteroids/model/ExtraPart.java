package edu.byu.cs.superasteroids.model;

/**
 * An extra part that attaches to the ship.
 */
public class ExtraPart {
    private long id; /** The id of the ExtraPart in the database */
    private Coordinate attach_point; /** Where the part attaches to the ship */
    private ImageInfo image; /** The image of the extra part */

    public ExtraPart() {
        id = -1;
    }

    public long getId() {
        return id;
    }

    public Coordinate getAttachPoint() {
        return attach_point;
    }

    public ImageInfo getImage() {
        return image;
    }


    public void setAttachPoint(Coordinate attach_point) {
        this.attach_point = attach_point;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }


    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ExtraPart)) return false;
        if (id != ((ExtraPart) o).getId() ||
                !attach_point.equals( ((ExtraPart) o).getAttachPoint() ) ||
                !image.equals( ((ExtraPart) o).getImage() )) {
            return false;
        }
        return true;
    }
}
