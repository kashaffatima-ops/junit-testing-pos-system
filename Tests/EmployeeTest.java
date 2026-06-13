import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployeeTest {

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void testGetUsername_10001() {
        Employee instance = new Employee("10001", "Alpha Release", "Admin", "123345");
        String expResult = "10001";
        String result = instance.getUsername();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName_10002() {
        Employee instance = new Employee("10002", "Alpha Release", "Admin", "123345");
        String expResult = "Alpha Release";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPosition_10003() {
        Employee instance = new Employee("10003", "Alpha Release", "Admin", "123345");
        String expResult = "Admin";
        String result = instance.getPosition();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetPassword_10004() {
        Employee instance = new Employee("10004", "Alpha Release", "Admin", "123345");
        String expResult = "123345";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetName_10005() {
        Employee instance = new Employee("10005", "Alpha Release", "Admin", "123345");
        instance.setName("Beta Release");
        String expResult = "Beta Release";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetPosition_10006() {
        Employee instance = new Employee("10006", "Alpha Release", "Admin", "123345");
        instance.setPosition("QA");
        String expResult = "QA";
        String result = instance.getPosition();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetPassword_10007() {
        Employee instance = new Employee("10007", "Alpha Release", "Admin", "123345");
        instance.setPassword("54321");
        String expResult = "54321";
        String result = instance.getPassword();
        assertEquals(expResult, result);
    }
}
