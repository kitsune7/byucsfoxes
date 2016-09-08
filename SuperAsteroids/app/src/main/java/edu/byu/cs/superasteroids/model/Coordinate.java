package edu.byu.cs.superasteroids.model;

import android.graphics.PointF;

/**
 * Marks a point in coordinate space.
 */
public class Coordinate {
    private float x; /** The x position */
    private float y; /** The y position */

    /**
     * The coordinates are initially set to (0,0) if no arguments are provided.
     */
    public Coordinate() {
        x = 0;
        y = 0;
    }

    /**
     * Parses a coordinate string and stores the x and y value.
     * @param point The coordinate string being parsed. Should be in the format: "x,y".
     */
    public Coordinate(String point) {
        int comma_index = point.indexOf(',');
        x = Float.parseFloat(point.substring(0,comma_index));
        y = Float.parseFloat(point.substring(comma_index+1));
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public float getExactX() {
        return x;
    }
    public int getX() {
        return (int)x;
    }

    public float getExactY() {
        return y;
    }
    public int getY() {
        return (int)y;
    }


    public void setX(int x) {
        this.x = (float)x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = (float)y;
    }
    public void setY(float y) {
        this.y = y;
    }


    /**
     * PointF is needed in many cases, so this function is provided for convenience and cleaner
     * code.
     * @return The coordinate point represented as a PointF instead of a Coordinate
     */
    public PointF getPointF() {
        return new PointF(x,y);
    }

    /**
     * Checks if this object and the one provided are equal or not. To be equal means they are both
     * coordinate objects and they have the same x and y coordinates.
     * @param o The object being compared
     * @return True if equal, false otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coordinate)) return false;
        if ( ((Coordinate) o).getX() != x || ((Coordinate)o).getY() != y ) return false;
        return true;
    }

    /**
     * Gets the string of the Coordinate.
     * @return A string in the format: "x,y"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(x);
        sb.append(',');
        sb.append(y);
        return sb.toString();
    }
}
