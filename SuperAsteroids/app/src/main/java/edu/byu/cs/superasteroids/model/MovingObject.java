package edu.byu.cs.superasteroids.model;

/**
 * Anything that moves needs to keep track of it's position and handle collision detection.
 * Inheriting from this class lets it do just that.
 */
public class MovingObject {
    private Coordinate position; /** The current location of the object */
    private int velocity; /** The velocity of the moving object. */

    public MovingObject(Coordinate position) {
        this.position = position;
        this.velocity = 0;
    }

    public Coordinate getPosition() {
        return position;
    }


    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }


    /**
     * Updates the x position
     * @param x The x position
     */
    public void updateX(float x) {
        position.setX(x);
    }

    /**
     * Updates the y position
     * @param y The y position
     */
    public void updateY(float y) {
        position.setY(y);
    }

    /**
     * Gets the velocity of the object
     * @return The velocity of the object
     */
    public int getVelocity() {
        return velocity;
    }
}
