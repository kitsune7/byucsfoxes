package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.ObjectsDAO;
import edu.byu.cs.superasteroids.model.BackgroundObject;

/**
 * Test class for ObjectsDAO.
 */
public class ObjectsDAOTest extends AndroidTestCase {
    private static final String TAG = "ObjectsDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private ObjectsDAO dao; /** The DAO we're testing */

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
        dao = new ObjectsDAO(db);
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
     * Tests each function in the ObjectsDAO.
     */
    public void testAll() {
        // Test dao.add
        BackgroundObject object = new BackgroundObject();
        object.setPath("images/planet1.png");

        assertEquals(-1, object.getId());
        assertTrue(dao.add(object));
        assertTrue(object.getId() >= 0);

        // Test dao.getAll
        ArrayList<BackgroundObject> objects = dao.getAll();
        assertEquals(1, objects.size());
        BackgroundObject copy = objects.iterator().next();
        assertEquals(object.getId(), copy.getId());
        assertEquals(object.getPath(), copy.getPath());
    }
}