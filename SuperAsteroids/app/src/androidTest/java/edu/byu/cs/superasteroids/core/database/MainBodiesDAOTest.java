package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.MainBodiesDAO;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.MainBody;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Test class for MainBodiesDAO.
 */
public class MainBodiesDAOTest extends AndroidTestCase {
    private static final String TAG = "MainBodiesDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private MainBodiesDAO dao; /** The DAO we're testing */

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
        dao = new MainBodiesDAO(db);
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
     * Tests each function in the MainBodiesDAO.
     */
    public void testAll() {
        // Test dao.add
        MainBody body = new MainBody();
        body.setCannonAttach(new Coordinate("190,227"));
        body.setEngineAttach(new Coordinate("102,392"));
        body.setExtraAttach(new Coordinate("6,253"));
        body.setImage(new ImageInfo("images/parts/mainbody1.png",200,400));

        assertEquals(-1, body.getId());
        assertTrue(dao.add(body));
        assertTrue(body.getId() >= 0);

        // Test dao.getAll
        ArrayList<MainBody> bodies = dao.getAll();
        assertEquals(1, bodies.size());
        MainBody copy = bodies.iterator().next();
        assertEquals(body.getId(), copy.getId());
        assertEquals(body.getCannonAttach(), copy.getCannonAttach());
        assertEquals(body.getEngineAttach(), copy.getEngineAttach());
        assertEquals(body.getExtraAttach(), copy.getExtraAttach());
        assertEquals(body.getImage(), copy.getImage());
    }
}