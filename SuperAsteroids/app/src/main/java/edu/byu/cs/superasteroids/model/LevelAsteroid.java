package edu.byu.cs.superasteroids.model;

import android.content.Context;

import java.util.TreeMap;

/**
 * Keeps track of how many asteroids of its type need to be generated in a level.
 */
public class LevelAsteroid {
    private long id; /** The id of the LevelAsteroid in the database */
    private int number; /** The number of asteroids of this type that are generated for the level */
    private long asteroid_type_id; /** The id of the asteroid type */

    public LevelAsteroid() {
        id = -1;
    }

    public LevelAsteroid(int number, long asteroid_type_id) {
        this.number = number;
        this.asteroid_type_id = asteroid_type_id;
        id = -1;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public long getAsteroidTypeId() {
        return asteroid_type_id;
    }

    public AsteroidType getAsteroidType(TreeMap<Long,AsteroidType> map) {
        return map.get(id);
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAsteroidTypeId(long asteroid_type_id) {
        this.asteroid_type_id = asteroid_type_id;
    }


    /**
     * Checks if this object and the one provided are equal or not.
     * @param o The object being compared
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof LevelAsteroid)) return false;
        if ( ((LevelAsteroid) o).getId() != id ||
             ((LevelAsteroid) o).getNumber() != number ||
             ((LevelAsteroid)o).getAsteroidTypeId() != asteroid_type_id ) {
            return false;
        }
        return true;
    }
}
