package edu.byu.cs.imageeditor.studentCode;

/**
 * Created by kitsune7 on 9/8/16.
 */
public class Pixel {
    private int r;
    private int g;
    private int b;

    public Pixel() {}

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() { return this.r; }
    public int getG() { return this.g; }
    public int getB() { return this.b; }

    public void setR(int r) { this.r = r; }
    public void setG(int g) { this.g = g; }
    public void setB(int b) { this.b = b; }

    public String toString() {
        return String.format("%d\n%d\n%d\n", this.r, this.g, this.b);
    }
}

