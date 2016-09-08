package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.AsteroidTypesDAO;
import edu.byu.cs.superasteroids.model.AsteroidType;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Test class for AsteroidTypesDAO.
 */
public class AsteroidTypesDAOTest extends AndroidTestCase {
    private static final String TAG = "AsteroidTypesDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private AsteroidTypesDAO dao; /** The DAO we're testing */

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
        dao = new AsteroidTypesDAO(db);
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
    * Tests each function in the AsteroidsTypeDAO.
    */
    public void testAll() {
        // Test dao.add
        AsteroidType type = new AsteroidType();
        type.setType("regular");
        type.setImage(new ImageInfo("images/asteroids/asteroid.png", 169, 153));

        assertEquals(-1, type.getId());
        assertTrue(dao.add(type));
        assertTrue(type.getId() >= 0);

        // Test dao.getAll
        ArrayList<AsteroidType> types = dao.getAll();
        assertEquals(1, types.size());
        AsteroidType copy = types.iterator().next();
        assertEquals(type,copy);
    }
}
