package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.LevelsDAO;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.LevelAsteroid;
import edu.byu.cs.superasteroids.model.LevelObject;

/**
 * Test class for LevelsDAO.
 */
public class LevelsDAOTest extends AndroidTestCase {
    private static final String TAG = "LevelsDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private LevelsDAO dao; /** The DAO we're testing */

    /**
     * Gets the database ready and makes a new instance of the DAO.
     * @throws Exception An exception is thrown if there's a database error.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        dbOpenHelper = new DbOpenHelper(getContext());

        db = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.onCreate(db); // Clears the database
        db.beginTransaction();
        dao = new LevelsDAO(db);
    }


    /**
     * Cleans up the database
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        db.endTransaction();
        db = null;

        dbOpenHelper.close();
        dbOpenHelper = null;
    }

    /**
     * Tests each function in the LevelsDAO.
     */
    public void testAll() {
        // Test dao.add
        Level level = new Level();
        level.setNumber(1);
        level.setTitle("Level 1");
        level.setHint("Destroy 1 Asteroid");
        level.setWidth(3000);
        level.setHeight(3000);
        level.setMusicPath("sounds/SpyHunter.ogg");

        ArrayList<LevelObject> objects = new ArrayList<LevelObject>();
        objects.add(new LevelObject(new Coordinate("1000,1000"),1,(float)1.5));
        level.setObjects(objects);

        ArrayList<LevelAsteroid> level_asteroids = new ArrayList<LevelAsteroid>();
        level_asteroids.add(new LevelAsteroid(4, 1));
        level_asteroids.add(new LevelAsteroid(4, 2));
        level.setLevelAsteroids(level_asteroids);

        assertEquals(-1, level.getId());
        assertTrue(dao.add(level));
        assertTrue(level.getId() >= 0);

        // Test dao.getAll
        ArrayList<Level> levels = dao.getAll();
        assertEquals(1, levels.size());
        Level copy = levels.iterator().next();
        assertEquals(level.getId(), copy.getId());
        assertEquals(level.getNumber(), copy.getNumber());
        assertEquals(level.getTitle(), copy.getTitle());
        assertEquals(level.getHint(), copy.getHint());
        assertEquals(level.getWidth(), copy.getWidth());
        assertEquals(level.getHeight(), copy.getHeight());
        assertEquals(level.getMusicPath(), copy.getMusicPath());
        assertEquals(level.getObjects(), copy.getObjects());
        assertEquals(level.getLevelAsteroids(), copy.getLevelAsteroids());
        assertEquals(level.getAsteroids(), copy.getAsteroids());
    }
}