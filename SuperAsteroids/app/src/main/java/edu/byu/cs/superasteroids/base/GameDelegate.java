package edu.byu.cs.superasteroids.base;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.InputManager;
import edu.byu.cs.superasteroids.model.Asteroid;
import edu.byu.cs.superasteroids.model.AsteroidType;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.BackgroundObject;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.Ship;

/**
 * The controller for the game.
 */
public class GameDelegate implements IGameDelegate {
    private Context context; /** The context is needed to load data from the model */
    private int space_id; /** The image id of the background */
    private Level currentLevel; /** The currentLevel of the game */
    private Ship ship; /** The ship used for the game. There can be only one. */
    private Coordinate camera; /** The coordinates of the viewport */
    private ArrayList<Integer> backgroundObjectIds; /** All the background object image ids */
    private int backgroundSoundId; /** The sound id of the background */
    private int laser_counter; /** Keeps track of how long you have to wait before you can fire another laser */
    final int MAX_LASER_COUNTER = 100; /** The max counter for the laser */

    public GameDelegate(Context context) {
        this.context = context;
        camera = new Coordinate();
    }

    @Override
    public void update(double elapsedTime) {
        laser_counter = 0;
        ContentManager.getInstance().playLoop(backgroundSoundId);
        updateCamera();

        if (ship.getX() < 0) ship.getPosition().setX(0);
        if (ship.getY() < 0) ship.getPosition().setY(0);
        if (ship.getX() > currentLevel.getWidth())
            ship.getPosition().setX(currentLevel.getWidth());
        if (ship.getY() > currentLevel.getHeight())
            ship.getPosition().setY(currentLevel.getHeight());

        if (InputManager.movePoint != null) {
            //System.out.printf("Ship: (%f,%f)\n",ship.getX(),ship.getY());
            //System.out.printf("Camera: (%f,%f)\n",camera.getExactX(),camera.getExactY());
            //System.out.printf("Touching: (%f,%f)\n", InputManager.movePoint.x, InputManager.movePoint.y);
            float x = ship.getX();
            float y = ship.getY();
            GraphicsUtils.MoveObjectResult result = GraphicsUtils.moveObject(
                    new PointF(x, y),
                    ship.getBounds(),
                    ship.getSpeed(),
                    Math.atan2(
                            InputManager.movePoint.y-(ship.getY()-camera.getExactY()),
                            InputManager.movePoint.x-(ship.getX()-camera.getExactX())
                    ),
                    elapsedTime
            );
            ship.setAngle(Math.atan2(
                    InputManager.movePoint.y-(ship.getY()-camera.getExactY()),
                    InputManager.movePoint.x-(ship.getX()-camera.getExactX())
            )+Math.PI/2);

            ship.updateX(result.getNewObjPosition().x);
            ship.updateY(result.getNewObjPosition().y);
            ship.setBounds(result.getNewObjBounds());
        }

        if (InputManager.firePressed && laser_counter < 1) {
            ship.fireLaser();
            laser_counter = MAX_LASER_COUNTER;
        } else laser_counter--;

        updateLasers(elapsedTime);
        updateAsteroids(elapsedTime);
    }

    /**
     * Updates the position of the camera and keeps it in bounds
     */
    private void updateCamera() {
        camera.setX(ship.getX() - DrawingHelper.getGameViewWidth() / (float) 2);
        camera.setY(ship.getY() - DrawingHelper.getGameViewHeight() / (float) 2);

        if (camera.getExactX() < 0) camera.setX(0);
        if (camera.getExactY() < 0) camera.setY(0);
        if (camera.getExactX() > currentLevel.getWidth() - DrawingHelper.getGameViewWidth()) {
            camera.setX(currentLevel.getWidth() - DrawingHelper.getGameViewWidth());
        }
        if (camera.getExactY() > currentLevel.getHeight() - DrawingHelper.getGameViewHeight()) {
            camera.setY(currentLevel.getHeight() - DrawingHelper.getGameViewHeight());
        }
    }

    /**
     * Updates the current position of each laser as well as its lifecycle
     * @param elapsedTime The current framerate
     */
    private void updateLasers(double elapsedTime) {
        ArrayList<Integer> removeIndexes = new ArrayList<Integer>();
        for (int i = 0; i < ship.getLasers().size(); ++i) {
            ship.getLasers().get(i).runTimer();
            if (ship.getLasers().get(i).getTimer() < 0) {
                removeIndexes.add(i);
            }

            float x = ship.getLasers().get(i).getPosition().getExactX();
            float y = ship.getLasers().get(i).getPosition().getExactY();
            GraphicsUtils.MoveObjectResult result = GraphicsUtils.moveObject(
                    new PointF(x, y),
                    ship.getLasers().get(i).getBounds(),
                    ship.getSpeed()*2,
                    ship.getLasers().get(i).getAngle()-Math.PI/2,
                    elapsedTime
            );
            ship.getLasers().get(i).setBounds(result.getNewObjBounds());
            ship.getLasers().get(i).getPosition().setX(result.getNewObjPosition().x);
            ship.getLasers().get(i).getPosition().setY(result.getNewObjPosition().y);
        }
        for (int i = 0; i < removeIndexes.size(); ++i) {
            ship.getLasers().remove(removeIndexes.get(i));
        }
    }

    /**
     * Updates the position of each asteroid, keeps it in bounds, and checks for collisions
     * @param elapsedTime
     */
    private void updateAsteroids(double elapsedTime) {
        for (int i = 0; i < currentLevel.getAsteroids().size(); ++i) {
            Asteroid asteroid = currentLevel.getAsteroids().get(i);
            float x = asteroid.getPosition().getExactX();
            float y = asteroid.getPosition().getExactY();
            GraphicsUtils.MoveObjectResult result = GraphicsUtils.moveObject(
                    new PointF(x, y),
                    asteroid.getBounds(),
                    ship.getSpeed(),
                    asteroid.getAngle(),
                    elapsedTime
            );
            asteroid.setBounds(result.getNewObjBounds());
            asteroid.getPosition().setX(result.getNewObjPosition().x);
            asteroid.getPosition().setY(result.getNewObjPosition().y);

            GraphicsUtils.RicochetObjectResult ricochet_result = GraphicsUtils.ricochetObject(
                    new PointF(asteroid.getPosition().getExactX(), asteroid.getPosition().getExactY()),
                    asteroid.getBounds(),
                    asteroid.getAngle(),
                    currentLevel.getWidth(),
                    currentLevel.getHeight()
            );
            asteroid.setAngle(ricochet_result.getNewAngleRadians());
            /*if (asteroid.collides(ship)) {
                currentLevel.splitAsteroid(asteroid);
            }*/
        }
    }

    @Override
    public void loadContent(ContentManager content) {
        AsteroidsGame instance = AsteroidsGame.getInstance(context);
        currentLevel = instance.getLevels().get(0);
        space_id = content.loadImage("images/space.bmp");
        loadShip(content);
        loadBackgroundObjects(content);
        loadAsteroids(content);
        try {
            backgroundSoundId = content.loadSound(currentLevel.getMusicPath());
            ship.setLaserSoundId( content.loadSound(ship.getCannon().getAttackSoundPath()) );
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Loads the backgroundObjects. The resulting image ids are stored in backgroundImageIds.
     * @param content The content manager is used for loading the backgroundObjects
     */
    private void loadBackgroundObjects(ContentManager content) {
        AsteroidsGame instance = AsteroidsGame.getInstance(context);
        backgroundObjectIds = new ArrayList<Integer>();
        ArrayList<String> object_images = new ArrayList<String>();
        ArrayList<BackgroundObject> objects = instance.getObjects();

        for (BackgroundObject object: objects) {
            object_images.add(object.getPath());
        }
        content.loadImages(object_images);

        for (String path: object_images) {
            backgroundObjectIds.add(content.getImageId(path));
        }
    }

    /**
     * Loads each part of the ship.
     * @param content The content manager
     */
    private void loadShip(ContentManager content) {
        AsteroidsGame instance = AsteroidsGame.getInstance(context);
        ship = instance.getShip();
        ship.setBounds(new RectF(
            ship.getX(),
            ship.getY(),
            ship.getX()+ship.getBody().getImage().getHeight(),
            ship.getY()+ship.getBody().getImage().getHeight()
        ));
        ship.updateX(currentLevel.getWidth() / (float) 2);
        ship.updateY(currentLevel.getHeight() / (float) 2);

        ArrayList<String> ship_images = new ArrayList<String>();
        ship_images.add(ship.getBody().getImage().getPath());
        ship_images.add(ship.getCannon().getImage().getPath());
        ship_images.add(ship.getExtra().getImage().getPath());
        ship_images.add(ship.getEngine().getImage().getPath());
        content.loadImages(ship_images);

        ship.setBodyImageId(content.getImageId(ship.getBody().getImage().getPath()));
        ship.setCannonImageId(content.getImageId(ship.getCannon().getImage().getPath()));
        ship.setExtraImageId(content.getImageId(ship.getExtra().getImage().getPath()));
        ship.setEngineImageId(content.getImageId(ship.getEngine().getImage().getPath()));
        ship.setLaserImageId(content.loadImage(ship.getCannon().getAttackImage().getPath()));
    }

    /**
     * Loads all the asteroid images.
     * @param content The content manager
     */
    private void loadAsteroids(ContentManager content) {
        currentLevel.generateAsteroids();
        Collection<AsteroidType> types = currentLevel.getAsteroidTypes().values();
        for (AsteroidType type: types) {
            type.setImageId( content.loadImage(type.getImage().getPath()) );
        }
    }

    @Override
    public void unloadContent(ContentManager content) {}

    @Override
    public void draw() {
        drawSpace();
        drawBackgroundObjects();
        drawAsteroids();
        drawLasers();
        drawShip();
    }

    /**
     * Draws the background
     */
    private void drawSpace() {
        DrawingHelper.drawImage(
                space_id,
                new Rect(
                        (int) camera.getExactX(),
                        (int) camera.getExactY(),
                        (int) camera.getExactX() + DrawingHelper.getGameViewWidth(),
                        (int) camera.getExactY() + DrawingHelper.getGameViewHeight()
                ),
                new Rect(0, 0, currentLevel.getWidth(), currentLevel.getHeight())
        );
    }

    /**
     * Draws the background objects
     */
    private void drawBackgroundObjects() {
        for (int i = 0; i < currentLevel.getObjects().size(); ++i) {
            Coordinate p = currentLevel.getObjects().get(i).getPosition();
            DrawingHelper.drawImage(
                    backgroundObjectIds.get(i),
                    p.getExactX()-camera.getExactX(),
                    p.getExactY()-camera.getExactY(),
                    0,
                    1,
                    1,
                    255
            );
        }
    }

    /**
     * Draws all the asteroids
     */
    private void drawAsteroids() {
        for (int i = 0; i < currentLevel.getAsteroids().size(); ++i) {
            Coordinate p = currentLevel.getAsteroids().get(i).getPosition();
            DrawingHelper.drawImage(
                    currentLevel.getAsteroids().get(i).getType().getImageId(),
                    p.getExactX()-camera.getExactX(),
                    p.getExactY()-camera.getExactY(),
                    (float)GraphicsUtils.radiansToDegrees(currentLevel.getAsteroids().get(i).getAngle()),
                    ship.getScale()*currentLevel.getAsteroids().get(i).getHealth(),
                    ship.getScale()*currentLevel.getAsteroids().get(i).getHealth(),
                    255
            );
        }
    }

    /**
     * Draws all the lasers
     */
    private void drawLasers() {
        for (int i = 0; i < ship.getLasers().size(); ++i) {
            DrawingHelper.drawImage(
                    ship.getLaserImageId(),
                    ship.getLasers().get(i).getPosition().getExactX() - camera.getExactX(),
                    ship.getLasers().get(i).getPosition().getExactY() - camera.getExactY(),
                    (float)GraphicsUtils.radiansToDegrees(ship.getLasers().get(i).getAngle())+(float)Math.PI/2,
                    ship.getScale(),
                    ship.getScale(),
                    255
            );
        }
    }

    /**
     * Draws the ship with all its parts
     */
    private void drawShip() {
        DrawingHelper.drawImage(
                ship.getBodyImageId(),
                ship.getX()-camera.getExactX(),
                ship.getY()-camera.getExactY(),
                (float)GraphicsUtils.radiansToDegrees(ship.getAngle()),
                ship.getScale(),
                ship.getScale(),
                255
        );
        DrawingHelper.drawImage(
                ship.getEngineImageId(),
                ship.getX() - camera.getExactX() + ship.getRotatedEngineOffset().getExactX(),
                ship.getY() - camera.getExactY() + ship.getRotatedEngineOffset().getExactY(),
                (float) GraphicsUtils.radiansToDegrees(ship.getAngle()),
                ship.getScale(),
                ship.getScale(),
                255
        );
        DrawingHelper.drawImage(
                ship.getCannonImageId(),
                ship.getX()-camera.getExactX()+ship.getRotatedCannonOffset().getExactX(),
                ship.getY()-camera.getExactY()+ship.getRotatedCannonOffset().getExactY(),
                (float)GraphicsUtils.radiansToDegrees(ship.getAngle()),
                ship.getScale(),
                ship.getScale(),
                255
        );
        DrawingHelper.drawImage(
                ship.getExtraImageId(),
                ship.getX()-camera.getExactX()+ship.getRotatedExtraPartOffset().getExactX(),
                ship.getY()-camera.getExactY()+ship.getRotatedExtraPartOffset().getExactY(),
                (float)GraphicsUtils.radiansToDegrees(ship.getAngle()),
                ship.getScale(),
                ship.getScale(),
                255
        );
    }
}
