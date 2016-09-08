package edu.byu.cs.superasteroids.model;

import android.graphics.RectF;

/**
 * A laser that the ship shoots.
 */
public class Laser extends MovingObject {
    private ImageInfo image; /** The image of the laser */
    private double angle; /** The angle of the laser. It's the same as the ship when it's first fired */
    private RectF bounds; /** The bounding box for the laser */
    private int timer; /** This timer keeps track of how long the laser stays active */

    public Laser(Coordinate position, double angle, ImageInfo image) {
        super(position);
        this.angle = angle;
        this.image = image;
        bounds = new RectF(
                position.getExactX(),
                position.getExactY(),
                (float)(position.getExactX()+image.getWidth()),
                (float)(position.getExactY()+image.getHeight())
        );
        timer = 10;
    }


    public double getAngle() {
        return angle;
    }

    public RectF getBounds() { return bounds; }

    public int getTimer() {
        return timer;
    }


    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setBounds(RectF bounds) { this.bounds = bounds; }

    public void setTimer(int timer) {
        this.timer = timer;
    }


    /**
     * Runs the timer. Every time it runs the laser has less time to live... Muahahahaha...
     */
    public void runTimer() {
        timer--;
    }
}
