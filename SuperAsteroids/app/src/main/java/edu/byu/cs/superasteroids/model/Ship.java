package edu.byu.cs.superasteroids.model;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;

/**
 * The ship that plays the game. This class organizes all the parts and functions of the ship.
 */
public class Ship extends MovingObject {
    private MainBody body; /** The body that was chosen for the ship */
    private Cannon cannon; /** The cannon that was chosen for the ship */
    private ExtraPart extra; /** The wings that were chosen for the ship */
    private Engine engine; /** The engine that was chosen for the ship */
    private PowerCore core; /** The power core that was chosen for the ship */

    private double angle; /** The angle of the ship/the direction the ship is facing in radians*/
    private float scale; /** A scale to size the ship appropriately */
    private int bodyImageId; /** The image id of the bainBody of the ship */
    private int cannonImageId; /** The image id of the cannon of the ship */
    private int extraImageId; /** The image id of the extraPart of the ship */
    private int engineImageId; /** The image id of the engine of the ship */
    private int laserSoundId; /** The sound id for the laser */
    private int laserImageId; /** The image id for the laser */
    private int speed; /** The speed of the ship */
    private ArrayList<Laser> lasers; /** The list of all the lasers that are still active */
    private RectF bounds; /** The bounds for the ship */
    private int health; /** The health of the ship. If it gets to 0, it's game over. */

    public Ship(Coordinate position, double angle) {
        super(position);
        this.angle = angle;
        scale = (float).4;
        bodyImageId = -1;
        cannonImageId = -1;
        extraImageId = -1;
        engineImageId = -1;
        laserSoundId = -1;
        laserImageId = -1;
        speed = 300;
        lasers = new ArrayList<Laser>();
        bounds = new RectF();
        health = 10;
    }

    public MainBody getBody() {
        return body;
    }

    public Cannon getCannon() {
        return cannon;
    }

    public ExtraPart getExtra() {
        return extra;
    }

    public Engine getEngine() {
        return engine;
    }

    public PowerCore getCore() {
        return core;
    }

    public double getAngle() {
        return angle;
    }

    public float getScale() { return scale; }

    public int getBodyImageId() {
        return bodyImageId;
    }

    public int getCannonImageId() {
        return cannonImageId;
    }

    public int getExtraImageId() {
        return extraImageId;
    }

    public int getEngineImageId() {
        return engineImageId;
    }

    public int getSpeed() {
        return speed;
    }

    public float getX() { return getPosition().getExactX(); }

    public float getY() { return getPosition().getExactY(); }

    public int getLaserSoundId() {
        return laserSoundId;
    }

    public ArrayList<Laser> getLasers() {
        return lasers;
    }

    public int getLaserImageId() {
        return laserImageId;
    }

    public RectF getBounds() { return bounds; }


    public void setBody(MainBody body) {
        this.body = body;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public void setExtra(ExtraPart extra) {
        this.extra = extra;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setCore(PowerCore core) {
        this.core = core;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setBodyImageId(int bodyImageId) {
        this.bodyImageId = bodyImageId;
    }

    public void setCannonImageId(int cannonImageId) {
        this.cannonImageId = cannonImageId;
    }

    public void setExtraImageId(int extraImageId) {
        this.extraImageId = extraImageId;
    }

    public void setEngineImageId(int engineImageId) {
        this.engineImageId = engineImageId;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setLaserSoundId(int laserSoundId) {
        this.laserSoundId = laserSoundId;
    }

    public void setLaserImageId(int laserImageId) {
        this.laserImageId = laserImageId;
    }

    public void setLasers(ArrayList<Laser> lasers) {
        this.lasers = lasers;
    }

    public void setBounds(RectF bounds) {
        this.bounds = bounds;
    }


    /**
     * Hurts the ship.
     */
    public void hurt() {
        --health;
    }

    /**
     * Fires a laser from the cannon
     */
    public void fireLaser() {
        lasers.add(new Laser(
            new Coordinate(getX(), getY()),
            angle,
            cannon.getAttackImage()
        ));
        System.out.printf("CannonAttachX: %d\n", getBody().getCannonAttach().getX());
        System.out.printf("CannonAttachY: %d\n", getBody().getCannonAttach().getY());
        System.out.printf("ShipX: %d ShipY: %d\n\n", getX(), getY());

        ContentManager.getInstance().playSound(laserSoundId, 1, 1);
    }

    /**
     * Gets the offset of the cannon in relation to the ship
     * @return The offset of the cannon
     */
    public Coordinate getCannonOffset() {
        return new Coordinate(
                (body.getCannonAttach().getX()-body.getImage().getCenter().getX()+
                        cannon.getImage().getCenter().getX()-cannon.getAttachPoint().getX())*scale,
                (body.getCannonAttach().getY()-body.getImage().getCenter().getY()+
                        cannon.getImage().getCenter().getY()-cannon.getAttachPoint().getY())*scale
        );
    }

    /**
     * Same as getCannonOffset except that it takes the current angle into account
     * @return The rotated offset of the cannon
     */
    public Coordinate getRotatedCannonOffset() {
        Coordinate point = getCannonOffset();
        PointF rotated = GraphicsUtils.rotate(
                new PointF(point.getExactX(),point.getExactY()),angle
        );
        return new Coordinate(rotated.x,rotated.y);
    }

    /**
     * Gets the offset of the extraPart in relation to the ship
     * @return The offset of the extraPart
     */
    public Coordinate getExtraPartOffset() {
        return new Coordinate(
                (body.getExtraAttach().getX()-body.getImage().getCenter().getX()+
                        extra.getImage().getCenter().getX()-extra.getAttachPoint().getX())*scale,
                (body.getExtraAttach().getY()-body.getImage().getCenter().getY()+
                        extra.getImage().getCenter().getY()-extra.getAttachPoint().getY())*scale
        );
    }

    /**
     * It's the same as getExtraPartOffset except that it takes the current angle into account
     * @return The rotated offset of the extraPart
     */
    public Coordinate getRotatedExtraPartOffset() {
        Coordinate point = getExtraPartOffset();
        PointF rotated = GraphicsUtils.rotate(
                new PointF(point.getExactX(),point.getExactY()),angle
        );
        return new Coordinate(rotated.x,rotated.y);
    }

    /**
     * Gets the offset of the engine in relation to the ship
     * @return The offset of the engine
     */
    public Coordinate getEngineOffset() {
        return new Coordinate(
                (body.getEngineAttach().getX()-body.getImage().getCenter().getX()+
                        engine.getImage().getCenter().getX()-engine.getAttachPoint().getX())*scale,
                (body.getEngineAttach().getY()-body.getImage().getCenter().getY()+
                        engine.getImage().getCenter().getY()-engine.getAttachPoint().getY())*scale
        );
    }

    /**
     * It's the same as getEngineOffset except that it takes the current angle of the ship into
     * account.
     * @return The rotated offset of the engine
     */
    public Coordinate getRotatedEngineOffset() {
        Coordinate point = getEngineOffset();
        PointF rotated = GraphicsUtils.rotate(
                new PointF(point.getExactX(),point.getExactY()),angle
        );
        return new Coordinate(rotated.x,rotated.y);
    }
}
