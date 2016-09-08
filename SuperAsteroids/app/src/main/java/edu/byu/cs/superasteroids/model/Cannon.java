package edu.byu.cs.superasteroids.model;

/**
 * A cannon that attaches to the main body of a ship.
 */
public class Cannon {
	private long id; /** The id of the Cannon in the database */
    private Coordinate attach_point; /** Where the cannon attaches to the main body */
    private Coordinate emit_point; /** Where the cannon shoots */
    private ImageInfo image; /** The image for the cannon */
    private ImageInfo attack_image; /** The image for the cannon when it's attacking */
    private String attack_sound_path; /** The filepath to the attack sound file */
    private int damage; /** The amount of damage each shot inflicts */

    public Cannon() {
        id = -1;
    }

    public long getId() {
        return id;
    }

    public Coordinate getAttachPoint() {
        return attach_point;
    }

    public Coordinate getEmitPoint() {
        return emit_point;
    }

    public ImageInfo getImage() {
        return image;
    }

    public ImageInfo getAttackImage() {
        return attack_image;
    }

    public String getAttackSoundPath() {
        return attack_sound_path;
    }

    public int getDamage() {
        return damage;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setAttachPoint(Coordinate attach_point) {
        this.attach_point = attach_point;
    }

    public void setEmitPoint(Coordinate emit_point) {
        this.emit_point = emit_point;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }

    public void setAttackImage(ImageInfo attack_image) {
        this.attack_image = attack_image;
    }

    public void setAttackSoundPath(String attack_sound_path) {
        this.attack_sound_path = attack_sound_path;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Cannon)) return false;
        if (((Cannon)o).getId() != id ||
                !attach_point.equals( ((Cannon) o).getAttachPoint() ) ||
                !emit_point.equals( ((Cannon) o).getEmitPoint() ) ||
                !image.equals( ((Cannon) o).getImage() ) ||
                !attack_image.equals( ((Cannon) o).getAttackImage() ) ||
                !attack_sound_path.equals( ((Cannon) o).getAttackSoundPath() ) ||
                ((Cannon) o).getDamage() != damage) {
            return false;
        }
        return true;
    }
}
