package edu.byu.cs.superasteroids.database;
import edu.byu.cs.superasteroids.model.BackgroundObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Handles database operations for background objects.
 */
public class ObjectsDAO extends GeneralDAO {
    public ObjectsDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the BackgroundObjects to the database.
     *
     * @param object The object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(BackgroundObject object) {
        ContentValues values = new ContentValues();
        values.put("path", object.getPath());

        long id = db.insert("Objects", null, values);
        if (id >= 0) {
            object.setId(id);
            return true;
        }
        else return false;
    }

    public BackgroundObject getObjectById(long id) {
        final String SQL = "select * from Objects where id = ?";

        BackgroundObject object = new BackgroundObject();

        Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(id)});
        try {
            cursor.moveToFirst();
            object.setId(cursor.getLong(0));
            object.setPath(cursor.getString(1));
        }
        finally {
            cursor.close();
        }

        return object;
    }

    /**
     * Gets all the BackgroundObjects from the database.
     *
     * @return The set of all BackgroundObjects in the database
     */
    public ArrayList<BackgroundObject> getAll() {
        ArrayList<BackgroundObject> objects = new ArrayList<BackgroundObject>();
        Cursor cursor = db.rawQuery(
                "select * from Objects",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                BackgroundObject object = new BackgroundObject();

                object.setId(cursor.getLong(0));
                object.setPath(cursor.getString(1));

                objects.add(object);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return objects;
    }
}
