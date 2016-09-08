package edu.byu.cs.superasteroids.model;

import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * An asteroid that flies around in a level. This is the actual object that moves around in the
 * game.
 */
public class Asteroid extends MovingObject {
    private AsteroidType type; /** The type of asteroid */
    private double angle; /** The angle that the asteroid is going */
    private RectF bounds; /** The bounds of the asteroid */
    private int health; /** If the health reaches 0, the asteroid is destroyed */

    public Asteroid(AsteroidType type) {
        super(new Coordinate());
        this.type = type;
        this.bounds = new RectF(
            getPosition().getExactX(),
            getPosition().getExactY(),
            getPosition().getExactX() + type.getImage().getHeight(),
            getPosition().getExactX() + type.getImage().getHeight()
        );
        randomizeAngle();
        health = 3;
    }

    public AsteroidType getType() {
        return type;
    }

    public double getAngle() {
        return angle;
    }

    public RectF getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }


    public void setType(AsteroidType type) {
        this.type = type;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setBounds(RectF bounds) {
        this.bounds = bounds;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    /**
     * Changes the angle of the asteroid to a random one.
     */
    public void randomizeAngle() {
        angle = Math.toRadians(Math.random() * 360);
    }

    @Override
    public void setPosition(Coordinate position) {
        super.setPosition(position);
        resetBounds();
    }

    /**
     * Recalculates the bounds of the asteroid based on where it's at and the height of the
     * asteroid.
     */
    public void resetBounds() {
        this.bounds = new RectF(
                getPosition().getExactX(),
                getPosition().getExactY(),
                getPosition().getExactX() + type.getImage().getHeight(),
                getPosition().getExactX() + type.getImage().getHeight()
        );
    }

    /**
     * Whether or not the asteroid is colliding with something.
     * @param ship The ship is needed to test for collision along with getting its lasers
     * @return Whether or not the ship collides with something.
     */
    public boolean collides(Ship ship) {
        Rect ship_bounds = new Rect();
        ship.getBounds().round(ship_bounds);

        ArrayList<Laser> lasers = ship.getLasers();

        Rect asteroid_bounds = new Rect();
        bounds.round(asteroid_bounds);

        for (int i = 0; i < lasers.size(); ++i) {
            Rect laser_bounds = new Rect();
            lasers.get(i).getBounds().round(laser_bounds);
            if (Rect.intersects(asteroid_bounds,laser_bounds)) {
                lasers.get(i).setTimer(0);
                return true;
            }
        }

        if (Rect.intersects(asteroid_bounds,ship_bounds)) {
            ship.hurt();
            return true;
        }

        return false;
    }

    /**
     * Hurts the asteroid.
     */
    public void hurt() {
        health--;
    }

    /**
     * Checks if this object and the one provided are equal or not.
     * @param o The object being compared
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Asteroid)) return false;
        if (!type.equals(((Asteroid) o).getType())) return false;
        return true;
    }
}
