package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.ImageInfo;
import edu.byu.cs.superasteroids.model.MainBody;

/**
 * Handles the database operations for MainBody objects.
 */
public class MainBodiesDAO extends GeneralDAO {
    public MainBodiesDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the MainBody objects to the database.
     *
     * @param body The MainBody object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(MainBody body) {
        ContentValues values = new ContentValues();
        values.put("cannon_attach", body.getCannonAttach().toString());
        values.put("engine_attach", body.getEngineAttach().toString());
        values.put("extra_attach", body.getExtraAttach().toString());
        values.put("image",body.getImage().getPath());
        values.put("image_width",body.getImage().getWidth());
        values.put("image_height",body.getImage().getHeight());

        long id = db.insert("MainBodies", null, values);
        if (id >= 0) {
            body.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the MainBody objects from the database.
     *
     * @return The set of all MainBodies in the database.
     */
    public ArrayList<MainBody> getAll() {
        ArrayList<MainBody> main_bodies = new ArrayList<MainBody>();
        Cursor cursor = db.rawQuery(
                "select * from MainBodies",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                MainBody body = new MainBody();

                body.setId(cursor.getLong(0));
                body.setCannonAttach(new Coordinate(cursor.getString(1)));
                body.setEngineAttach(new Coordinate(cursor.getString(2)));
                body.setExtraAttach(new Coordinate(cursor.getString(3)));
                body.setImage(new ImageInfo(
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));

                main_bodies.add(body);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return main_bodies;
    }
}
