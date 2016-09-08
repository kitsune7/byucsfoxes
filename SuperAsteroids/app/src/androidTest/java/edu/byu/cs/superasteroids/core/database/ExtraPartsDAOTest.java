package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.ExtraPartsDAO;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.ExtraPart;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Test class for ExtraPartsDAO.
 */
public class ExtraPartsDAOTest extends AndroidTestCase {
    private static final String TAG = "ExtraPartsDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private ExtraPartsDAO dao; /** The DAO we're testing */

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
        dao = new ExtraPartsDAO(db);
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
     * Tests each function in the ExtraPartsDAO.
     */
    public void testAll() {
        // Test dao.add
        ExtraPart part = new ExtraPart();
        part.setAttachPoint(new Coordinate("312,94"));
        part.setImage(new ImageInfo("images/parts/extrapart1.png",320,240));

        assertEquals(-1, part.getId());
        assertTrue(dao.add(part));
        assertTrue(part.getId() >= 0);

        // Test dao.getAll
        ArrayList<ExtraPart> parts = dao.getAll();
        assertEquals(1, parts.size());
        ExtraPart copy = parts.iterator().next();
        assertEquals(part.getId(), copy.getId());
        assertEquals(part.getAttachPoint(), copy.getAttachPoint());
        assertEquals(part.getImage(), copy.getImage());
    }
}