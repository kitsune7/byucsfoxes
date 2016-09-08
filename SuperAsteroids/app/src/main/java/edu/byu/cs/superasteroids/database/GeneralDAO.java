package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * A general DAO class to handle operations in common with all DAOs.
 */
public class GeneralDAO {
    protected SQLiteDatabase db; /** The database object */

    public GeneralDAO(SQLiteDatabase db) {
        this.db = db;
    }
}
