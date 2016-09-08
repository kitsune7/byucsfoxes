package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.AsteroidType;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Handles the database operations for AsteroidType objects.
 */
public class AsteroidTypesDAO extends GeneralDAO {
    public AsteroidTypesDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the AsteroidType objects to the database.
     *
     * @param type The asteroid type being added
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(AsteroidType type) {
        ContentValues values = new ContentValues();
        values.put("type", type.getType());
        values.put("image",type.getImage().getPath());
        values.put("image_width",type.getImage().getWidth());
        values.put("image_height",type.getImage().getHeight());

        long id = db.insertOrThrow("AsteroidTypes", null, values);
        if (id >= 0) {
            type.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the AsteroidType objects from the database and stores them in the model.
     *
     * @return A set of all the AsteroidTypes in the database
     */
    public ArrayList<AsteroidType> getAll() {
        ArrayList<AsteroidType> types = new ArrayList<AsteroidType>();
        Cursor cursor = db.rawQuery(
                "select * from AsteroidTypes",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AsteroidType type = new AsteroidType();

                type.setId(cursor.getLong(0));
                type.setType(cursor.getString(1));
                type.setImage(new ImageInfo(
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));

                types.add(type);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return types;
    }
}
