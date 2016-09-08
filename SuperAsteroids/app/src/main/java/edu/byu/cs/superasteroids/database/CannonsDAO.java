package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Cannon;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Handles the database operations for Cannon objects.
 */
public class CannonsDAO extends GeneralDAO {
    public CannonsDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the Cannon objects to the database.
     *
     * @param cannon The cannon object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(Cannon cannon) {
        ContentValues values = new ContentValues();
        values.put("attach_point", cannon.getAttachPoint().toString());
        values.put("emit_point", cannon.getEmitPoint().toString());
        values.put("image",cannon.getImage().getPath());
        values.put("image_width",cannon.getImage().getWidth());
        values.put("image_height",cannon.getImage().getHeight());
        values.put("attack_image",cannon.getAttackImage().getPath());
        values.put("attack_image_width",cannon.getAttackImage().getWidth());
        values.put("attack_image_height",cannon.getAttackImage().getHeight());
        values.put("attack_sound",cannon.getAttackSoundPath());
        values.put("damage", cannon.getDamage());

        long id = db.insert("Cannons", null, values);
        if (id >= 0) {
            cannon.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the Cannons from the database
     *
     * @return A set of all the Cannons from the database
     */
    public ArrayList<Cannon> getAll() {
        ArrayList<Cannon> cannons = new ArrayList<Cannon>();
        Cursor cursor = db.rawQuery(
                "select * from Cannons",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Cannon cannon = new Cannon();

                cannon.setId(cursor.getLong(0));
                cannon.setAttachPoint(new Coordinate(cursor.getString(1)));
                cannon.setEmitPoint(new Coordinate(cursor.getString(2)));
                cannon.setImage(new ImageInfo(
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(5)
                ));
                cannon.setAttackImage(new ImageInfo(
                        cursor.getString(6),
                        cursor.getInt(7),
                        cursor.getInt(8)
                ));
                cannon.setAttackSoundPath(cursor.getString(9));
                cannon.setDamage(cursor.getInt(10));

                cannons.add(cannon);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return cannons;
    }
}
