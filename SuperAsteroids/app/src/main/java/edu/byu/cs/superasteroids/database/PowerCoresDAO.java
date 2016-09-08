package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.model.PowerCore;

/**
 * Handles the database operations for PowerCore objects.
 */
public class PowerCoresDAO extends GeneralDAO {
    public PowerCoresDAO(SQLiteDatabase db) {
        super(db);
    }

    /**
     * Adds all the PowerCore objects to the database.
     *
     * @param power_core The PowerCore object being added to the database
     * @return true if the data was stored in the database, false otherwise
     */
    public boolean add(PowerCore power_core) {
        ContentValues values = new ContentValues();
        values.put("cannon_boost", power_core.getCannonBoost());
        values.put("engine_boost", power_core.getEngineBoost());
        values.put("image",power_core.getImagePath());

        long id = db.insert("PowerCores", null, values);
        if (id >= 0) {
            power_core.setId(id);
            return true;
        }
        else return false;
    }

    /**
     * Gets all the PowerCore objects from the database.
     *
     * @return The set of all PowerCores in the database.
     */
    public ArrayList<PowerCore> getAll() {
        ArrayList<PowerCore> power_cores = new ArrayList<PowerCore>();
        Cursor cursor = db.rawQuery(
                "select * from PowerCores",
                new String[] {}
        );

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PowerCore power_core = new PowerCore();

                power_core.setId(cursor.getLong(0));
                power_core.setCannonBoost(cursor.getInt(1));
                power_core.setEngineBoost(cursor.getInt(2));
                power_core.setImagePath(cursor.getString(3));

                power_cores.add(power_core);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return power_cores;
    }
}
