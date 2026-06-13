import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnItemTest {

    public ReturnItemTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void testGetItemID_10001() {
        ReturnItem instance = new ReturnItem(10001, 12);
        int expResult = 10001;
        int result = instance.getItemID();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetItemID_10002() {
        ReturnItem instance = new ReturnItem(10002, 12);
        int expResult = 10002;
        int result = instance.getItemID();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDays_12() {
        ReturnItem instance = new ReturnItem(10001, 12);
        int expResult = 12;
        int result = instance.getDays();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetDays_5() {
        ReturnItem instance = new ReturnItem(10001, 5);
        int expResult = 5;
        int result = instance.getDays();
        assertEquals(expResult, result);
    }
}
