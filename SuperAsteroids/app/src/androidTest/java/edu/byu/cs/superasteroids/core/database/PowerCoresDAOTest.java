package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.PowerCoresDAO;
import edu.byu.cs.superasteroids.model.PowerCore;

/**
 * Test class for PowerCoresDAO.
 */
public class PowerCoresDAOTest extends AndroidTestCase {
    private static final String TAG = "PowerCoresDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private PowerCoresDAO dao; /** The DAO we're testing */

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
        dao = new PowerCoresDAO(db);
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
     * Tests each function in the PowerCoresDAO.
     */
    public void testAll() {
        // Test dao.add
        PowerCore core = new PowerCore();
        core.setCannonBoost(10);
        core.setEngineBoost(10);
        core.setImagePath("images/Ellipse.png");

        assertEquals(-1, core.getId());
        assertTrue(dao.add(core));
        assertTrue(core.getId() >= 0);

        // Test dao.getAll
        ArrayList<PowerCore> cores = dao.getAll();
        assertEquals(1, cores.size());
        PowerCore copy = cores.iterator().next();
        assertEquals(core.getId(), copy.getId());
        assertEquals(core.getCannonBoost(), copy.getCannonBoost());
        assertEquals(core.getEngineBoost(), copy.getEngineBoost());
        assertEquals(core.getImagePath(), copy.getImagePath());
    }
}