package edu.byu.cs.superasteroids.model;

import java.util.jar.Manifest;

/**
 * The main body of a ship.
 */
public class MainBody {
    private long id; /** The id of the MainBody in the database */
    private Coordinate cannon_attach; /** Where the cannon attaches */
    private Coordinate engine_attach; /** Where the engine attaches */
    private Coordinate extra_attach; /** Where the extra part attaches */
    private ImageInfo image; /** The image for the main body */

    public MainBody() {
        id = -1;
    }

    public long getId() {
        return id;
    }

    public Coordinate getCannonAttach() {
        return cannon_attach;
    }

    public Coordinate getEngineAttach() {
        return engine_attach;
    }

    public Coordinate getExtraAttach() {
        return extra_attach;
    }

    public ImageInfo getImage() {
        return image;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setCannonAttach(Coordinate cannon_attach) {
        this.cannon_attach = cannon_attach;
    }

    public void setEngineAttach(Coordinate engine_attach) {
        this.engine_attach = engine_attach;
    }

    public void setExtraAttach(Coordinate extra_attach) {
        this.extra_attach = extra_attach;
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
        if (!(o instanceof MainBody)) return false;
        if (id != ((MainBody) o).getId() ||
                !cannon_attach.equals( ((MainBody) o).getCannonAttach() ) ||
                !engine_attach.equals( ((MainBody) o).getEngineAttach() ) ||
                !extra_attach.equals( ((MainBody) o).getExtraAttach() ) ||
                !image.equals( ((MainBody) o).getImage() )) {
            return false;
        }
        return true;
    }
}
