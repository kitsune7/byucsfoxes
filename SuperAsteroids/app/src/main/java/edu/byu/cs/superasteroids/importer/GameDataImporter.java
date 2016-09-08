package edu.byu.cs.superasteroids.importer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;

import java.io.InputStreamReader;
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
import edu.byu.cs.superasteroids.model.AsteroidType;
import edu.byu.cs.superasteroids.model.AsteroidsGame;
import edu.byu.cs.superasteroids.model.BackgroundObject;
import edu.byu.cs.superasteroids.model.Cannon;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Engine;
import edu.byu.cs.superasteroids.model.ExtraPart;
import edu.byu.cs.superasteroids.model.ImageInfo;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.LevelAsteroid;
import edu.byu.cs.superasteroids.model.LevelObject;
import edu.byu.cs.superasteroids.model.MainBody;
import edu.byu.cs.superasteroids.model.PowerCore;

/**
 * Handles importing data from a json file to the database.
 */
public class GameDataImporter implements IGameDataImporter {
    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private Context context;

    public GameDataImporter(Context context) {
        this.context = context;
        dbOpenHelper = new DbOpenHelper(context);
    }

    /**
     * Closes the database connection
     */
    public void tearDown() {
        try {
            db.setTransactionSuccessful();
            db.endTransaction();
            db = null;

            dbOpenHelper.close();
            dbOpenHelper = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Imports the data from the .json file the given InputStreamReader is connected to. Imported data
     * should be stored in a SQLite database for use in the ship builder and the game.
     * @param dataInputReader The InputStreamReader connected to the .json file needing to be imported.
     * @return TRUE if the data was imported successfully, FALSE if the data was not imported due
     * to any error.
     */
    public boolean importData(InputStreamReader dataInputReader) {
        try {
            db = dbOpenHelper.getWritableDatabase();
            dbOpenHelper.onCreate(db);
            db.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonReader reader = new JsonReader(dataInputReader);
        try {
            reader.beginObject();
            reader.nextName(); // Skip asteroidsGame label
            reader.beginObject();

            getObjects(reader);
            getAsteroids(reader);
            getLevels(reader);
            getMainBodies(reader);
            getCannons(reader);
            getExtraParts(reader);
            getEngines(reader);
            getPowerCores(reader);

            reader.endObject();
            reader.endObject();
        } catch (Exception e) {
            tearDown();
            return false;
        }
        tearDown();
        AsteroidsGame.getInstance(context).loadModel();
        return true;
    }

    /**
     * Imports data from the "objects" json to the database.
     * @param reader The JsonReader object. The "objects" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getObjects(JsonReader reader) throws Exception {
        ObjectsDAO dao = new ObjectsDAO(db);
        reader.nextName(); // Skip objects label
        reader.beginArray();
        while (reader.hasNext()) {
            BackgroundObject object = new BackgroundObject();
            object.setPath(reader.nextString());
            dao.add(object);
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "asteroids" json to the database.
     * @param reader The JsonReader object. The "asteroids" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getAsteroids(JsonReader reader) throws Exception {
        AsteroidTypesDAO dao = new AsteroidTypesDAO(db);
        reader.nextName(); // Skip asteroids label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            AsteroidType type = new AsteroidType();
            ImageInfo image = new ImageInfo();

            reader.nextName(); // name
            type.setType(reader.nextString());

            reader.nextName(); // image
            image.setPath(reader.nextString());

            reader.nextName(); // imageWidth
            image.setWidth(reader.nextInt());

            reader.nextName(); // imageHeight
            image.setHeight(reader.nextInt());

            reader.nextName(); // Skip type
            reader.nextString();

            type.setImage(image);
            dao.add(type);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "levels" json to the database.
     * @param reader The JsonReader object. The "levels" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getLevels(JsonReader reader) throws Exception {
        LevelsDAO dao = new LevelsDAO(db);
        reader.nextName(); // Skip levels label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            Level level = new Level();

            reader.nextName(); // number
            level.setNumber(reader.nextInt());

            reader.nextName(); // title
            level.setTitle(reader.nextString());

            reader.nextName(); // hint
            level.setHint(reader.nextString());

            reader.nextName(); // width
            level.setWidth(reader.nextInt());

            reader.nextName(); // height
            level.setHeight(reader.nextInt());

            reader.nextName(); // music
            level.setMusicPath(reader.nextString());

            reader.nextName(); // levelObjects
            level.setObjects(new ArrayList<LevelObject>());
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();

                LevelObject object = new LevelObject();

                reader.nextName(); // position
                object.setPosition(new Coordinate(reader.nextString()));

                reader.nextName(); // objectId
                object.setObjectId(reader.nextLong());

                reader.nextName(); // scale
                object.setScale((float)reader.nextDouble());

                level.getObjects().add(object);

                reader.endObject();
            }
            reader.endArray();

            reader.nextName(); // levelAsteroids
            level.setLevelAsteroids(new ArrayList<LevelAsteroid>());
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();

                LevelAsteroid asteroid = new LevelAsteroid();

                reader.nextName(); // number
                asteroid.setNumber(reader.nextInt());

                reader.nextName(); // asteroidId
                asteroid.setAsteroidTypeId(reader.nextLong());

                level.getLevelAsteroids().add(asteroid);

                reader.endObject();
            }
            reader.endArray();

            dao.add(level);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "mainBodies" json to the database.
     * @param reader The JsonReader object. The "mainBodies" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getMainBodies(JsonReader reader) throws Exception {
        MainBodiesDAO dao = new MainBodiesDAO(db);
        reader.nextName(); // Skip mainBodies label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            MainBody body = new MainBody();
            ImageInfo image = new ImageInfo();

            reader.nextName(); // cannonAttach
            body.setCannonAttach(new Coordinate(reader.nextString()));

            reader.nextName(); // engineAttach
            body.setEngineAttach(new Coordinate(reader.nextString()));

            reader.nextName(); // extraAttach
            body.setExtraAttach(new Coordinate(reader.nextString()));

            reader.nextName(); // image
            image.setPath(reader.nextString());

            reader.nextName(); // imageWidth
            image.setWidth(reader.nextInt());

            reader.nextName(); // imageHeight
            image.setHeight(reader.nextInt());

            body.setImage(image);
            dao.add(body);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "cannons" json to the database.
     * @param reader The JsonReader object. The "cannons" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getCannons(JsonReader reader) throws Exception {
        CannonsDAO dao = new CannonsDAO(db);
        reader.nextName(); // Skip cannons label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            Cannon cannon = new Cannon();
            ImageInfo image = new ImageInfo();
            ImageInfo attack_image = new ImageInfo();

            reader.nextName(); // attachPoint
            cannon.setAttachPoint(new Coordinate(reader.nextString()));

            reader.nextName(); // emitPoint
            cannon.setEmitPoint(new Coordinate(reader.nextString()));

            reader.nextName(); // image
            image.setPath(reader.nextString());

            reader.nextName(); // imageWidth
            image.setWidth(reader.nextInt());

            reader.nextName(); // imageHeight
            image.setHeight(reader.nextInt());

            reader.nextName(); // attackImage
            attack_image.setPath(reader.nextString());

            reader.nextName(); // attackImageWidth
            attack_image.setWidth(reader.nextInt());

            reader.nextName(); // attackImageHeight
            attack_image.setHeight(reader.nextInt());

            reader.nextName(); // attackSound
            cannon.setAttackSoundPath(reader.nextString());

            reader.nextName(); // damage
            cannon.setDamage(reader.nextInt());

            cannon.setImage(image);
            cannon.setAttackImage(attack_image);
            dao.add(cannon);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "extraParts" json to the database.
     * @param reader The JsonReader object. The "extraParts" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getExtraParts(JsonReader reader) throws Exception {
        ExtraPartsDAO dao = new ExtraPartsDAO(db);
        reader.nextName(); // Skip extraParts label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            ExtraPart part = new ExtraPart();
            ImageInfo image = new ImageInfo();

            reader.nextName(); // attachPoint
            part.setAttachPoint(new Coordinate(reader.nextString()));

            reader.nextName(); // image
            image.setPath(reader.nextString());

            reader.nextName(); // imageWidth
            image.setWidth(reader.nextInt());

            reader.nextName(); // imageHeight
            image.setHeight(reader.nextInt());

            part.setImage(image);
            dao.add(part);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "engines" json to the database.
     * @param reader The JsonReader object. The "engines" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getEngines(JsonReader reader) throws Exception {
        EnginesDAO dao = new EnginesDAO(db);
        reader.nextName(); // Skip engines label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            Engine engine = new Engine();
            ImageInfo image = new ImageInfo();

            reader.nextName(); // baseSpeed
            engine.setBaseSpeed(reader.nextInt());

            reader.nextName(); // baseTurnRate
            engine.setBaseTurnRate(reader.nextInt());

            reader.nextName(); // attachPoint
            engine.setAttachPoint(new Coordinate(reader.nextString()));

            reader.nextName(); // image
            image.setPath(reader.nextString());

            reader.nextName(); // imageWidth
            image.setWidth(reader.nextInt());

            reader.nextName(); // imageHeight
            image.setHeight(reader.nextInt());

            engine.setImage(image);
            dao.add(engine);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }

    /**
     * Imports data from the "powerCores" json to the database.
     * @param reader The JsonReader object. The "powerCores" label should be next up.
     * @return true if the data was imported successfully, false if the data wasn't imported due to
     * an error.
     */
    private boolean getPowerCores(JsonReader reader) throws Exception {
        PowerCoresDAO dao = new PowerCoresDAO(db);
        reader.nextName(); // Skip powerCores label
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();

            PowerCore core = new PowerCore();

            reader.nextName(); // cannonBoost
            core.setCannonBoost(reader.nextInt());

            reader.nextName(); // engineBoost
            core.setEngineBoost(reader.nextInt());

            reader.nextName(); // image
            core.setImagePath(reader.nextString());

            dao.add(core);

            reader.endObject();
        }
        reader.endArray();
        return true;
    }
}
