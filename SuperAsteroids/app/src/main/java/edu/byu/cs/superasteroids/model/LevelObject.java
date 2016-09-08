package edu.byu.cs.superasteroids.model;

/**
 * A background object that is used in a level
 */
public class LevelObject {
    private long id; /** The id of the LevelObject in the database */
    private Coordinate position; /** The location of the object on the coordinate plane */
    private long object_id; /** The id of an object from the Objects table in the database */
    private float scale; /** A scalar that stretches or shrinks the object */

    public LevelObject() {
        id = -1;
    }

    public LevelObject(Coordinate position, long object_id, float scale) {
        this.position = position;
        this.object_id = object_id;
        this.scale = scale;
        id = -1;
    }

    public long getId() {
        return id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public long getObjectId() {
        return object_id;
    }

    public float getScale() {
        return scale;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public void setObjectId(long object_id) {
        this.object_id = object_id;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }


    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof LevelObject)) return false;
        if (((LevelObject) o).getId() != id ||
                !position.equals( ((LevelObject) o).getPosition() ) ||
                ((LevelObject)o).getObjectId() != object_id ||
                ((LevelObject)o).getScale() != scale) {
            return false;
        }
        return true;
    }
}
