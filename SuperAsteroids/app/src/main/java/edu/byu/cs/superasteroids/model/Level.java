package edu.byu.cs.superasteroids.model;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * A level in the game.
 */
public class Level {
    private long id; /** The id of the Level in the database */
    private int number; /** The level number */
    private String title; /** The name of the level */
    private String hint; /** A description for the level */
    private int width; /** The total width of the level */
    private int height; /** The total height of the level */
    private String music_path; /** The file path to the music file */
    private ArrayList<LevelObject> objects; /** All of the objects in the level */
    private ArrayList<LevelAsteroid> levelAsteroids; /** The asteroids that need to be generated */
    private ArrayList<Asteroid> asteroids; /** The asteroids that move around the level */
    private TreeMap<Long,AsteroidType> asteroidTypes; /** Maps the id of the asteroidType to the actual AsteroidType */

    public Level() {
        id = -1;
        asteroids = new ArrayList<Asteroid>();
        asteroidTypes = new TreeMap<Long,AsteroidType>();
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getMusicPath() {
        return music_path;
    }

    public ArrayList<LevelObject> getObjects() {
        return objects;
    }

    public ArrayList<LevelAsteroid> getLevelAsteroids() {
        return levelAsteroids;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public TreeMap<Long,AsteroidType> getAsteroidTypes() {
        return asteroidTypes;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMusicPath(String music_path) {
        this.music_path = music_path;
    }

    public void setObjects(ArrayList<LevelObject> objects) {
        this.objects = objects;
    }

    public void setLevelAsteroids(ArrayList<LevelAsteroid> levelAsteroids) {
        this.levelAsteroids = levelAsteroids;
    }

    public void setAsteroids(ArrayList<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    public void setAsteroidTypes(TreeMap<Long,AsteroidType> types) { asteroidTypes = types; }

    /**
     * Gets all of the asteriodTypes and loads them into the asteroidTypes map.
     * @param types Every AsteroidType from the database
     */
    public void loadAsteroidTypes(ArrayList<AsteroidType> types) {
        for (int i = 0; i < types.size(); ++i) {
            asteroidTypes.put(types.get(i).getId(),types.get(i));
        }
    }

    /**
     * Generates all the asteroids in the level. It uses the levelAsteroids to know what asteroids
     * to make and how many of them to make.
     */
    public void generateAsteroids() {
        for (int i = 0; i < levelAsteroids.size(); ++i) {
            Asteroid a = new Asteroid(levelAsteroids.get(i).getAsteroidType(asteroidTypes));
            a.setPosition(getRandomPosition());
            a.resetBounds();
            asteroids.add(a);
        }
    }

    /**
     * This function is useful for finding where to put the asteroids in a level.
     * @return a random coordinate in the level
     */
    public Coordinate getRandomPosition() {
        int x = (int)(Math.random() * width);
        int y = (int)(Math.random() * height);
        return new Coordinate(x,y);
    }

    /**
     * Splits an asteroid in two if it's big enough. Otherwise it destroys it.
     * @param asteroid The asteroid to split.
     */
    public void splitAsteroid(Asteroid asteroid) {
        asteroid.hurt();
        if (asteroid.getHealth() < 1) {
            asteroids.remove(asteroid);
        } else {
            Asteroid clone1 = new Asteroid(asteroid.getType());
            Asteroid clone2 = new Asteroid(asteroid.getType());
            clone1.setPosition(asteroid.getPosition());
            clone2.setPosition(asteroid.getPosition());
            clone1.randomizeAngle();
            clone2.randomizeAngle();
            asteroids.remove(asteroid);
            asteroids.add(clone1);
            asteroids.add(clone2);
        }
    }

    /**
     * Checks whether or not the given object is equal to this object.
     * @param o The object being compared to this.
     * @return True if equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Level)) return false;

        if (id != ((Level) o).getId()) return false;
        if (number != ((Level) o).getNumber()) return false;
        if (!title.equals(((Level) o).getTitle())) return false;
        if (!hint.equals(((Level) o).getHint())) return false;
        if (width != ((Level) o).getWidth()) return false;
        if (height != ((Level) o).getHeight()) return false;
        if (!music_path.equals(((Level) o).getMusicPath())) return false;
        if (objects != null) {
            if (!objects.equals(((Level) o).getObjects())) return false;
        }
        if (levelAsteroids != null) {
            if (!levelAsteroids.equals(((Level) o).getLevelAsteroids())) return false;
        }
        if (asteroids != null) {
            if (!asteroids.equals(((Level) o).getAsteroids())) return false;
        }
        return true;
    }
}
