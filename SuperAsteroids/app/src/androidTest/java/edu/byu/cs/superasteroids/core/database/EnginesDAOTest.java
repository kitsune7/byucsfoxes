package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.EnginesDAO;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.Engine;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Test class for EnginesDAO.
 */
public class EnginesDAOTest extends AndroidTestCase {
    private static final String TAG = "EnginesDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private EnginesDAO dao; /** The DAO we're testing */

    /**
     * Gets the database ready and makes a new instance of the DAO.
     * @throws Exception An exception is thrown if there's a database error.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        dbOpenHelper = new DbOpenHelper(getContext());

        db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        dao = new EnginesDAO(db);
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
     * Tests each function in the EnginesDAO.
     */
    public void testAll() {
        // Test dao.add
        Engine engine = new Engine();
        engine.setBaseSpeed(350);
        engine.setBaseTurnRate(270);
        engine.setAttachPoint(new Coordinate("106,6"));
        engine.setImage(new ImageInfo("images/parts/engine1.png",220,160));

        assertEquals(-1, engine.getId());
        assertTrue(dao.add(engine));
        assertTrue(engine.getId() >= 0);

        // Test dao.getAll
        ArrayList<Engine> engines = dao.getAll();
        assertEquals(1, engines.size());
        Engine copy = engines.iterator().next();
        assertEquals(engine.getId(), copy.getId());
        assertEquals(engine.getBaseSpeed(), copy.getBaseSpeed());
        assertEquals(engine.getBaseTurnRate(), copy.getBaseTurnRate());
        assertEquals(engine.getAttachPoint(), copy.getAttachPoint());
        assertEquals(engine.getImage(), copy.getImage());
    }
}

