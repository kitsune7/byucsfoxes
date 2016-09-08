package edu.byu.cs.superasteroids.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.AsteroidTypesDAO;
import edu.byu.cs.superasteroids.database.CannonsDAO;
import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.EnginesDAO;
import edu.byu.cs.superasteroids.database.ExtraPartsDAO;
import edu.byu.cs.superasteroids.database.LevelsDAO;
import edu.byu.cs.superasteroids.database.MainBodiesDAO;
import edu.byu.cs.superasteroids.database.ObjectsDAO;
import edu.byu.cs.superasteroids.database.PowerCoresDAO;

/**
 * The AsteriodsGame object manages everything in the model. It keeps everything in one place and
 * makes it so the database only needs to be loaded once.
 */
public class AsteroidsGame {
    /** The main instance of the game. It's only made once */
    private static AsteroidsGame instance;

    /** A helper object to open a connection to the database */
    private DbOpenHelper dbOpenHelper; 

    /** The database object */
    private SQLiteDatabase db; 

    /** All the backgroundObjects from the database */
    private ArrayList<BackgroundObject> objects; 

    /** All the asteroidTypes from the database */
    private ArrayList<AsteroidType> asteroidTypes; 

    /** All the levels from the database */
    private ArrayList<Level> levels; 

    /** All the mainBodies from the database */
    private ArrayList<MainBody> mainBodies; 

    /** All the cannons from the database */
    private ArrayList<Cannon> cannons; 

    /** All the extraParts from the database */
    private ArrayList<ExtraPart> extraParts; 

    /** All the engines from the database */
    private ArrayList<Engine> engines; 

    /** All the powerCores from the database */
    private ArrayList<PowerCore> powerCores; 

    /** The ship that's used in the game. There is only ever one. */
    private Ship ship; 

    /**
     * Loads all everything from the database into the model.
     * @param context The database context
     */
    private AsteroidsGame(Context context) {
        dbOpenHelper = new DbOpenHelper(context);
        db = dbOpenHelper.getReadableDatabase();
        loadModel();
    }

    /**
     * Creates an asteroid game and loads the model through the constructor if it hasn't been loaded
     * yet. Otherwise, it just gets the instance.
     * @param context We need the context in order to load information from the database
     * @return An instance of AsteroidsGame
     */
    public static AsteroidsGame getInstance(Context context) {
        if (instance == null) instance = new AsteroidsGame(context);
        return instance;
    }

    /**
     * Gets all the info from the database through the DAOs and then stores it all in members of
     * this class.
     */
    public void loadModel() {
        try { db.beginTransaction(); } catch (Exception e) { e.printStackTrace(); }
        ship = new Ship(new Coordinate(), 0);

        ObjectsDAO objectsDAO = new ObjectsDAO(db);
        objects = objectsDAO.getAll();

        AsteroidTypesDAO asteroidTypesDAO = new AsteroidTypesDAO(db);
        asteroidTypes = asteroidTypesDAO.getAll();

        LevelsDAO levelsDAO = new LevelsDAO(db);
        levels = levelsDAO.getAll();
        for (int i = 0; i < levels.size(); ++i) {
            levels.get(i).loadAsteroidTypes(asteroidTypes);
        }

        MainBodiesDAO mainBodiesDAO = new MainBodiesDAO(db);
        mainBodies = mainBodiesDAO.getAll();

        CannonsDAO cannonsDAO = new CannonsDAO(db);
        cannons = cannonsDAO.getAll();

        ExtraPartsDAO extraPartsDAO = new ExtraPartsDAO(db);
        extraParts = extraPartsDAO.getAll();

        EnginesDAO enginesDAO = new EnginesDAO(db);
        engines = enginesDAO.getAll();

        PowerCoresDAO powerCoresDAO = new PowerCoresDAO(db);
        powerCores = powerCoresDAO.getAll();

        try { db.endTransaction(); } catch (Exception e) { e.printStackTrace(); }
    }


    public ArrayList<BackgroundObject> getObjects() {
        return objects;
    }

    public ArrayList<AsteroidType> getAsteroidTypes() {
        return asteroidTypes;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public ArrayList<MainBody> getMainBodies() {
        return mainBodies;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<ExtraPart> getExtraParts() {
        return extraParts;
    }

    public ArrayList<Engine> getEngines() {
        return engines;
    }

    public ArrayList<PowerCore> getPowerCores() {
        return powerCores;
    }

    public Ship getShip() { return ship; }
}
