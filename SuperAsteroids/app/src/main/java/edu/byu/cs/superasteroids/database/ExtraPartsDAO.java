package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;


import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.ExtraPart;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Handles the database operations for ExtraPart objects.
 */
public class ExtraPartsDAO extends GeneralDAO {
    public ExtraPartsDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the ExtraPart objects to the database.
     *
     * @param part The ExtraPart object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(ExtraPart part) {
        ContentValues values = new ContentValues();
        values.put("attach_point", part.getAttachPoint().toString());
        values.put("image",part.getImage().getPath());
        values.put("image_width",part.getImage().getWidth());
        values.put("image_height",part.getImage().getHeight());

        long id = db.insert("ExtraParts", null, values);
        if (id >= 0) {
            part.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the ExtraPart objects from the database.
     *
     * @return The set of all ExtraParts in the database.
     */
    public ArrayList<ExtraPart> getAll() {
        ArrayList<ExtraPart> extra_parts = new ArrayList<ExtraPart>();
        Cursor cursor = db.rawQuery(
                "select * from ExtraParts",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ExtraPart part = new ExtraPart();

                part.setId(cursor.getLong(0));
                part.setAttachPoint(new Coordinate(cursor.getString(1)));
                part.setImage(new ImageInfo(
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));

                extra_parts.add(part);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return extra_parts;
    }
}
