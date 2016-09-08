package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Engine;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Handles the database operations for Engine objects.
 */
public class EnginesDAO extends GeneralDAO {
    public EnginesDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the Engine objects to the database.
     *
     * @param engine The engine object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(Engine engine) {
        ContentValues values = new ContentValues();
        values.put("base_speed", engine.getBaseSpeed());
        values.put("base_turn_rate", engine.getBaseTurnRate());
        values.put("attach_point", engine.getAttachPoint().toString());
        values.put("image",engine.getImage().getPath());
        values.put("image_width",engine.getImage().getWidth());
        values.put("image_height",engine.getImage().getHeight());

        long id = db.insert("Engines", null, values);
        if (id >= 0) {
            engine.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the Cannon objects from the database.
     *
     * @return The set of all Engine objects from the database
     */
    public ArrayList<Engine> getAll() {
        ArrayList<Engine> engines = new ArrayList<Engine>();
        Cursor cursor = db.rawQuery(
                "select * from Engines",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Engine engine = new Engine();

                engine.setId(cursor.getLong(0));
                engine.setBaseSpeed(cursor.getInt(1));
                engine.setBaseTurnRate(cursor.getInt(2));
                engine.setAttachPoint(new Coordinate(cursor.getString(3)));
                engine.setImage(new ImageInfo(
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));

                engines.add(engine);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return engines;
    }
}
