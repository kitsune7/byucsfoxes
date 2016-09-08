package edu.byu.cs.superasteroids.core.importer;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.importer.GameDataImporter;
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
 * Tests the GameDataImporter class.
 */
public class GameDataImporterTest extends AndroidTestCase {
    private static final String TAG = "GameDataImporterTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private GameDataImporter importer; /** The importer object we use to test the class */

    /**
     * Gets the database ready.
     * @throws Exception An exception is thrown if there's a database error.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        importer = new GameDataImporter(getContext());
    }

    /**
     * Tests each method in the GameDataImporter class. Since there is only the import data method,
     * it is the only method we need to test.
     */
    public void testAll() {
        Resources res = getContext().getResources();
        AssetManager am = res.getAssets();
        try {
            // Load JSON into the database
            importer.importData(new InputStreamReader(
                    new BufferedInputStream(am.open("easy_win.json"))));

            // Load the data from the database into the model
            AsteroidsGame instance = AsteroidsGame.getInstance(getContext());
            ArrayList<BackgroundObject> db_objects = instance.getObjects();
            ArrayList<AsteroidType> db_asteroidTypes = instance.getAsteroidTypes();
            ArrayList<Level> db_levels = instance.getLevels();
            ArrayList<MainBody> db_mainBodies = instance.getMainBodies();
            ArrayList<Cannon> db_cannons = instance.getCannons();
            ArrayList<ExtraPart> db_extraParts = instance.getExtraParts();
            ArrayList<Engine> db_engines = instance.getEngines();
            ArrayList<PowerCore> db_powerCores = instance.getPowerCores();
            assertEquals(12,db_objects.size());
            assertEquals(db_asteroidTypes.size(),3);
            assertEquals(db_levels.size(),2);
            assertEquals(db_mainBodies.size(),2);
            assertEquals(db_cannons.size(),2);
            assertEquals(db_extraParts.size(),2);
            assertEquals(db_engines.size(),2);
            assertEquals(db_powerCores.size(),2);

            // Create data manually from json to test against the database data
            ArrayList<BackgroundObject> objects = new ArrayList<BackgroundObject>();
            objects.add(new BackgroundObject(db_objects.get(0).getId(),"images/planet1.png"));
            objects.add(new BackgroundObject(db_objects.get(1).getId(),"images/planet2.png"));
            objects.add(new BackgroundObject(db_objects.get(2).getId(),"images/planet3.png"));
            objects.add(new BackgroundObject(db_objects.get(3).getId(),"images/planet4.png"));
            objects.add(new BackgroundObject(db_objects.get(4).getId(),"images/planet5.png"));
            objects.add(new BackgroundObject(db_objects.get(5).getId(),"images/planet6.png"));
            objects.add(new BackgroundObject(db_objects.get(6).getId(),"images/station.png"));
            objects.add(new BackgroundObject(db_objects.get(7).getId(),"images/nebula1.png"));
            objects.add(new BackgroundObject(db_objects.get(8).getId(),"images/nebula2.png"));
            objects.add(new BackgroundObject(db_objects.get(9).getId(),"images/nebula3.png"));
            objects.add(new BackgroundObject(db_objects.get(10).getId(),"images/nebula4.png"));
            objects.add(new BackgroundObject(db_objects.get(11).getId(),"images/nebula5.png"));

            ArrayList<AsteroidType> asteroidTypes = new ArrayList<AsteroidType>();
            AsteroidType type = new AsteroidType();
            type.setId(db_asteroidTypes.get(0).getId());
            type.setType("regular");
            type.setImage(new ImageInfo("images/asteroids/asteroid.png", 169, 153));
            asteroidTypes.add(type);

            type = new AsteroidType();
            type.setId(db_asteroidTypes.get(1).getId());
            type.setType("growing");
            type.setImage(new ImageInfo("images/asteroids/blueasteroid.png", 161, 178));
            asteroidTypes.add(type);

            type = new AsteroidType();
            type.setId(db_asteroidTypes.get(2).getId());
            type.setType("octeroid");
            type.setImage(new ImageInfo("images/asteroids/asteroid.png", 169, 153));
            asteroidTypes.add(type);

            ArrayList<Level> levels = new ArrayList<Level>();
            Level level = new Level();
            level.setId(db_levels.get(0).getId());
            level.setNumber(1);
            level.setTitle("Level 1");
            level.setHint("Destroy 1 Asteroid");
            level.setWidth(3000);
            level.setHeight(3000);
            level.setMusicPath("sounds/SpyHunter.ogg");
            level.setObjects(new ArrayList<LevelObject>());
            level.getObjects().add(new LevelObject(new Coordinate("1000,1000"), 1, (float) 1.5));
            level.getObjects().get(0).setId(1);
            level.getObjects().add(new LevelObject(new Coordinate("2000,2000"), 2, (float) 1.0));
            level.getObjects().get(1).setId(2);
            level.setLevelAsteroids(new ArrayList<LevelAsteroid>());
            level.getLevelAsteroids().add(new LevelAsteroid(1, 1));
            level.getLevelAsteroids().get(0).setId(1);
            levels.add(level);

            level = new Level();
            level.setId(db_levels.get(1).getId());
            level.setNumber(2);
            level.setTitle("Level 2");
            level.setHint("Destroy 1 Asteroid");
            level.setWidth(3000);
            level.setHeight(3000);
            level.setMusicPath("sounds/SpyHunter.ogg");
            level.setObjects(new ArrayList<LevelObject>());
            level.setLevelAsteroids(new ArrayList<LevelAsteroid>());
            level.getLevelAsteroids().add(new LevelAsteroid(1,1));
            level.getLevelAsteroids().get(0).setId(
                    db_levels.get(1).getLevelAsteroids().get(0).getId()
            );
            levels.add(level);

            ArrayList<MainBody> mainBodies = new ArrayList<MainBody>();
            MainBody body = new MainBody();
            body.setId(db_mainBodies.get(0).getId());
            body.setCannonAttach(new Coordinate("190,227"));
            body.setEngineAttach(new Coordinate("102,392"));
            body.setExtraAttach(new Coordinate("6,253"));
            body.setImage(new ImageInfo("images/parts/mainbody1.png", 200, 400));
            mainBodies.add(body);

            body = new MainBody();
            body.setId(db_mainBodies.get(1).getId());
            body.setCannonAttach(new Coordinate("143,323"));
            body.setEngineAttach(new Coordinate("85,459"));
            body.setExtraAttach(new Coordinate("26,323"));
            body.setImage(new ImageInfo("images/parts/mainbody2.png", 156, 459));
            mainBodies.add(body);

            ArrayList<Cannon> cannons = new ArrayList<Cannon>();
            Cannon cannon = new Cannon();
            cannon.setId(db_cannons.get(0).getId());
            cannon.setAttachPoint(new Coordinate("14,240"));
            cannon.setEmitPoint(new Coordinate("104,36"));
            cannon.setImage(new ImageInfo("images/parts/cannon1.png", 160, 360));
            cannon.setAttackImage(new ImageInfo("images/parts/laser.png", 50, 250));
            cannon.setAttackSoundPath("sounds/laser.mp3");
            cannon.setDamage(1);
            cannons.add(cannon);

            cannon = new Cannon();
            cannon.setId(db_cannons.get(1).getId());
            cannon.setAttachPoint(new Coordinate("19,137"));
            cannon.setEmitPoint(new Coordinate("184,21"));
            cannon.setImage(new ImageInfo("images/parts/cannon2.png",325,386));
            cannon.setAttackImage(new ImageInfo("images/parts/laser2.png",105,344));
            cannon.setAttackSoundPath("sounds/laser.mp3");
            cannon.setDamage(2);
            cannons.add(cannon);

            ArrayList<ExtraPart> extraParts = new ArrayList<ExtraPart>();
            ExtraPart part = new ExtraPart();
            part.setId(db_extraParts.get(0).getId());
            part.setAttachPoint(new Coordinate("312,94"));
            part.setImage(new ImageInfo("images/parts/extrapart1.png",320,240));
            extraParts.add(part);

            part = new ExtraPart();
            part.setId(db_extraParts.get(1).getId());
            part.setAttachPoint(new Coordinate("310,124"));
            part.setImage(new ImageInfo("images/parts/extrapart2.png",331,309));
            extraParts.add(part);

            ArrayList<Engine> engines = new ArrayList<Engine>();
            Engine engine = new Engine();
            engine.setId(db_engines.get(0).getId());
            engine.setBaseSpeed(350);
            engine.setBaseTurnRate(270);
            engine.setAttachPoint(new Coordinate("106,6"));
            engine.setImage(new ImageInfo("images/parts/engine1.png",220,160));
            engines.add(engine);

            engine = new Engine();
            engine.setId(db_engines.get(1).getId());
            engine.setBaseSpeed(500);
            engine.setBaseTurnRate(360);
            engine.setAttachPoint(new Coordinate("107,7"));
            engine.setImage(new ImageInfo("images/parts/engine2.png", 208, 222));
            engines.add(engine);

            ArrayList<PowerCore> powerCores = new ArrayList<PowerCore>();
            PowerCore core = new PowerCore();
            core.setId(db_powerCores.get(0).getId());
            core.setCannonBoost(10);
            core.setEngineBoost(10);
            core.setImagePath("images/Ellipse.png");
            powerCores.add(core);

            core = new PowerCore();
            core.setId(db_powerCores.get(1).getId());
            core.setCannonBoost(10);
            core.setEngineBoost(10);
            core.setImagePath("images/Triangle.png");
            powerCores.add(core);

            // Verify the data is the same
            assertEquals(db_objects, objects);
            assertEquals(db_asteroidTypes,asteroidTypes);
            assertEquals(db_levels,levels);
            assertEquals(db_mainBodies,mainBodies);
            assertEquals(db_cannons,cannons);
            assertEquals(db_extraParts,extraParts);
            assertEquals(db_engines,engines);
            assertEquals(db_powerCores,powerCores);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}