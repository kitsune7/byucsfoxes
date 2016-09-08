package edu.byu.cs.superasteroids.database;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.*;

/**
 * Manages the connection to the database. Tables are added to the database if the database is
 * empty.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AsteroidsGame.sqlite"; /** The filename of the database*/
    private static final int DB_VERSION = 1; /** The database version */

    private final String CREATE_OBJECTS = "create table Objects (id integer primary key autoincrement not null unique, path varchar not null);";
    private final String CREATE_ASTEROID_TYPES = "create table AsteroidTypes (id integer primary key autoincrement not null unique, type varchar not null, image varchar not null, image_width integer not null, image_height integer not null);";
    private final String CREATE_LEVELS = "create table Levels (id integer primary key autoincrement not null unique, number integer not null, title varchar not null, hint varchar not null, width integer not null, height integer not null, music varchar not null);";
    private final String CREATE_LEVEL_OBJECTS = "create table LevelObjects (id integer primary key autoincrement not null unique, level_id integer not null, position varchar not null, object_id integer not null, scale real not null);";
    private final String CREATE_LEVEL_ASTEROIDS = "create table LevelAsteroids (id integer primary key autoincrement not null unique, level_id integer not null, number integer not null, asteroid_type_id integer not null);";
    private final String CREATE_MAIN_BODIES = "create table MainBodies (id integer primary key autoincrement not null unique, cannon_attach varchar not null, engine_attach varchar not null, extra_attach varchar not null, image varchar not null, image_width integer not null, image_height integer not null);";
    private final String CREATE_CANNONS = "create table Cannons (id integer primary key autoincrement not null unique, attach_point varchar not null, emit_point varchar not null, image varchar not null, image_width integer not null, image_height integer not null, attack_image varchar not null, attack_image_width integer not null, attack_image_height integer not null, attack_sound varchar not null, damage integer not null);";
    private final String CREATE_EXTRA_PARTS = "create table ExtraParts (id integer primary key autoincrement not null unique, attach_point varchar not null, image varchar not null, image_width integer not null, image_height integer not null);";
    private final String CREATE_ENGINES = "create table Engines (id integer primary key autoincrement not null unique, base_speed integer not null, base_turn_rate integer not null, attach_point varchar not null, image varchar not null, image_width integer not null, image_height integer not null);";
    private final String CREATE_POWER_CORES = "create table PowerCores (id integer primary key autoincrement not null unique, cannon_boost integer not null, engine_boost integer not null, image varchar not null);";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * The function that creates the tables in the database if it's empty
     *
     * @param db The database object
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists Objects;");
        db.execSQL("drop table if exists AsteroidTypes;");
        db.execSQL("drop table if exists Levels;");
        db.execSQL("drop table if exists LevelObjects;");
        db.execSQL("drop table if exists LevelAsteroids;");
        db.execSQL("drop table if exists MainBodies;");
        db.execSQL("drop table if exists Cannons;");
        db.execSQL("drop table if exists ExtraParts;");
        db.execSQL("drop table if exists Engines;");
        db.execSQL("drop table if exists PowerCores;");
        db.execSQL(CREATE_OBJECTS);
        db.execSQL(CREATE_ASTEROID_TYPES);
        db.execSQL(CREATE_LEVELS);
        db.execSQL(CREATE_LEVEL_OBJECTS);
        db.execSQL(CREATE_LEVEL_ASTEROIDS);
        db.execSQL(CREATE_MAIN_BODIES);
        db.execSQL(CREATE_CANNONS);
        db.execSQL(CREATE_EXTRA_PARTS);
        db.execSQL(CREATE_ENGINES);
        db.execSQL(CREATE_POWER_CORES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
}