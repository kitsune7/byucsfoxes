package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;

import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.LevelAsteroid;
import edu.byu.cs.superasteroids.model.LevelObject;

/**
 * Handles the database operations for Level objects.
 */
public class LevelsDAO extends GeneralDAO {
    public LevelsDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds the level object to the database including all its LevelAsteroids and LevelObjects.
     * @param level The current level object
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(Level level) {
        ContentValues values = new ContentValues();
        values.put("number", level.getNumber());
        values.put("title", level.getTitle());
        values.put("hint", level.getHint());
        values.put("width",level.getWidth());
        values.put("height",level.getHeight());
        values.put("music",level.getMusicPath());

        long id = db.insert("Levels", null, values);
        if (id < 0) return false;
        level.setId(id);
        if (!addLevelObjects(level)) return false;
        if (!addLevelAsteroids(level)) return false;
        return true;
    }

    /**
     * Adds all the LevelObjects in the level to the database.
     * @param level The current level object
     * @return true if the data was stored in the database, false otherwise
     */
    private boolean addLevelObjects(Level level) {
        Iterator<LevelObject> it = level.getObjects().iterator();
        while (it.hasNext()) {
            LevelObject object = it.next();

            ContentValues values = new ContentValues();
            values.put("level_id", level.getId());
            values.put("position", object.getPosition().toString());
            values.put("object_id", object.getObjectId());
            values.put("scale", object.getScale());

            long id = db.insert("LevelObjects", null, values);
            if (id >= 0) object.setId(id);
            else return false;
        }
        return true;
    }

    /**
     * Adds all the LevelAsteroid objects in the level to the database.
     * @param level The current level object
     * @return true if the data was stored in the database, false otherwise
     */
    private boolean addLevelAsteroids(Level level) {
        Iterator<LevelAsteroid> it = level.getLevelAsteroids().iterator();
        while (it.hasNext()) {
            LevelAsteroid asteroid = it.next();

            ContentValues values = new ContentValues();
            values.put("level_id", level.getId());
            values.put("number", asteroid.getNumber());
            values.put("asteroid_type_id", asteroid.getAsteroidTypeId());

            long id = db.insert("LevelAsteroids", null, values);
            if (id >= 0) asteroid.setId(id);
            else return false;
        }
        return true;
    }

    /**
     * Gets every Level from the database along with the LevelObjects and LevelAsteroids.
     * @return A set of all the levels in the database
     */
    public ArrayList<Level> getAll() {
        ArrayList<Level> levels = new ArrayList<Level>();
        Cursor cursor = db.rawQuery(
                "select * from Levels",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Level level = new Level();

                level.setId(cursor.getLong(0));
                level.setNumber(cursor.getInt(1));
                level.setTitle(cursor.getString(2));
                level.setHint(cursor.getString(3));
                level.setWidth(cursor.getInt(4));
                level.setHeight(cursor.getInt(5));
                level.setMusicPath(cursor.getString(6));
                level.setObjects( getAllLevelObjects(level.getId()) );
                level.setLevelAsteroids( getAllLevelAsteroids(level.getId()) );

                levels.add(level);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return levels;
    }

    /**
     * Gets all the LevelObjects from the database that have the same id as the level that's calling
     * the function.
     * @param level_id The id of the level
     * @return All of the LevelObjects from the database with the same level id as the one that
     * was passed in.
     */
    private ArrayList<LevelObject> getAllLevelObjects(long level_id) {
        ArrayList<LevelObject> objects = new ArrayList<LevelObject>();
        Cursor cursor = db.rawQuery(
                "select * from LevelObjects where level_id=?",
                new String[] {String.valueOf(level_id)}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LevelObject object = new LevelObject();

                object.setId(cursor.getLong(0));
                // Skipping the level_id
                object.setPosition(new Coordinate(cursor.getString(2)));
                object.setObjectId(cursor.getLong(3));
                object.setScale(cursor.getFloat(4));

                objects.add(object);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return objects;
    }

    /**
     * Gets all the LevelAsteroid objects from the database that have the same id as the level
     * that's calling the function.
     * @param level_id The id of the level
     * @return All of the LevelAsteroid objects from the database with the same level id as the one
     * that was passed in.
     */
    private ArrayList<LevelAsteroid> getAllLevelAsteroids(long level_id) {
        ArrayList<LevelAsteroid> asteroids = new ArrayList<LevelAsteroid>();
        Cursor cursor = db.rawQuery(
                "select * from LevelAsteroids where level_id=?",
                new String[] {String.valueOf(level_id)}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LevelAsteroid asteroid = new LevelAsteroid();

                asteroid.setId(cursor.getLong(0));
                // Skipping the level_id
                asteroid.setNumber(cursor.getInt(2));
                asteroid.setAsteroidTypeId(cursor.getLong(3));

                asteroids.add(asteroid);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return asteroids;
    }
}
