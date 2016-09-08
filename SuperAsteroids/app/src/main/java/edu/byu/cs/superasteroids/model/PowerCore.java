package edu.byu.cs.superasteroids.model;

/**
 * The power core used inside a ship.
 */
public class PowerCore {
    private long id; /** The id of the PowerCore in the database */
	private int cannon_boost; /** The boost for the cannon */
	private int engine_boost; /** The boost for the engine */
	private String image_path; /** The filepath of the image for the power core */

    public PowerCore() {
        id = -1;
    }

    public long getId() {
        return id;
    }

    public int getCannonBoost() {
        return cannon_boost;
    }

    public int getEngineBoost() {
        return engine_boost;
    }

    public String getImagePath() {
        return image_path;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setCannonBoost(int cannon_boost) {
        this.cannon_boost = cannon_boost;
    }

    public void setEngineBoost(int engine_boost) {
        this.engine_boost = engine_boost;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }


    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PowerCore)) return false;
        if (id != ((PowerCore) o).getId() ||
                cannon_boost != ((PowerCore) o).getCannonBoost() ||
                engine_boost != ((PowerCore) o).getEngineBoost() ||
                !image_path.equals( ((PowerCore) o).getImagePath() )) {
            return false;
        }
        return true;
    }
}
