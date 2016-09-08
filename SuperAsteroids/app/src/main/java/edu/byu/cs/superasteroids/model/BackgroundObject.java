package edu.byu.cs.superasteroids.model;

/**
 * An object that shows up in the background on different levels.
 */
public class BackgroundObject {
    private long id; /** The id of the object in the database */
    private String path; /** A path to an image file */

    public BackgroundObject() {
        id = -1;
    }

    public BackgroundObject(long id, String path) {
        this.id = id;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BackgroundObject)) return false;
        if (!path.equals( ((BackgroundObject) o).getPath() ) ||
                ((BackgroundObject)o).getId() != id) {
            return false;
        }
        return true;
    }
}
