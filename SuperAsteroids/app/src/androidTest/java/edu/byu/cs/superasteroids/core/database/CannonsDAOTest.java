package edu.byu.cs.superasteroids.core.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import edu.byu.cs.superasteroids.database.DbOpenHelper;
import edu.byu.cs.superasteroids.database.CannonsDAO;
import edu.byu.cs.superasteroids.model.Cannon;
import edu.byu.cs.superasteroids.model.Coordinate;
import edu.byu.cs.superasteroids.model.ImageInfo;

/**
 * Test class for CannonsDAO.
 */
public class CannonsDAOTest extends AndroidTestCase {
    private static final String TAG = "CannonsDAOTest";

    private DbOpenHelper dbOpenHelper; /** A helper object to open a connection to the database */
    private SQLiteDatabase db; /** The database object */
    private CannonsDAO dao; /** The DAO we're testing */

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
        dao = new CannonsDAO(db);
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
     * Tests each function in the CannonsDAO.
     */
    public void testAll() {
        // Test dao.add
        Cannon cannon = new Cannon();
        cannon.setAttachPoint(new Coordinate("14,240"));
        cannon.setEmitPoint(new Coordinate("104,36"));
        cannon.setImage(new ImageInfo("images/parts/cannon1.png",160,360));
        cannon.setAttackImage(new ImageInfo("images/parts/laser.png",50,250));
        cannon.setAttackSoundPath("sounds/laser.mp3");
        cannon.setDamage(1);

        assertEquals(-1, cannon.getId());
        assertTrue(dao.add(cannon));
        assertTrue(cannon.getId() >= 0);

        // Test dao.getAll
        ArrayList<Cannon> cannons = dao.getAll();
        assertEquals(1, cannons.size());
        Cannon copy = cannons.iterator().next();
        assertEquals(cannon.getId(), copy.getId());
        assertEquals(cannon.getAttachPoint(), copy.getAttachPoint());
        assertEquals(cannon.getEmitPoint(), copy.getEmitPoint());
        assertEquals(cannon.getImage(), copy.getImage());
        assertEquals(cannon.getAttackImage(), copy.getAttackImage());
        assertEquals(cannon.getAttackSoundPath(), copy.getAttackSoundPath());
        assertEquals(cannon.getDamage(), copy.getDamage());
    }
}
