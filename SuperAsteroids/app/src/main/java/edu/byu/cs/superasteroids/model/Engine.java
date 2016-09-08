package edu.byu.cs.superasteroids.model;

/**
 * An engine used for a spaceship.
 */
public class Engine {
    private long id; /** The id of the Engine in the database */
	private int base_speed; /** The base speed of the engine */
	private int base_turn_rate; /** The base speed of turning the ship */
	private Coordinate attach_point; /** Where the engine attaches to the ship */
	private ImageInfo image; /** The image for the engine */

    public Engine() {
        id = -1;
    }

    public long getId() {
        return id;
    }

    public int getBaseSpeed() {
        return base_speed;
    }

    public int getBaseTurnRate() {
        return base_turn_rate;
    }

    public Coordinate getAttachPoint() {
        return attach_point;
    }

    public ImageInfo getImage() {
        return image;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setBaseSpeed(int base_speed) {
        this.base_speed = base_speed;
    }

    public void setBaseTurnRate(int base_turn_rate) {
        this.base_turn_rate = base_turn_rate;
    }

    public void setAttachPoint(Coordinate attach_point) {
        this.attach_point = attach_point;
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
        if (!(o instanceof Engine)) return false;
        if (((Engine) o).getId() != id ||
                ((Engine)o).getBaseSpeed() != base_speed ||
                ((Engine)o).getBaseTurnRate() != base_turn_rate ||
                !attach_point.equals( ((Engine) o).getAttachPoint() ) ||
                !image.equals( ((Engine) o).getImage() )) {
            return false;
        }
        return true;
    }
}
