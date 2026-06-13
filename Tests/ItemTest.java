

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author jianing
 */
public class ItemTest {

	public ItemTest() {}
	
	 @BeforeClass
	    public static void setUpClass() {
	    }
	    
	    @AfterClass
	    public static void tearDownClass() {
	    }
	    
	    @Before
	    public void setUp() {
	    }
	    
	    @After
	    public void tearDown() {
	    }

	    @Test
	    public void testItemName() {
	        System.out.println("getUsername");
	        Item instance = new Item(1102,"Test item",100,123);
	        String expResult = "Test item";
	        String result = instance.getItemName();
	        assertEquals(expResult, result);
	    }
	    
	    @Test
	    public void testItemID() {
	        System.out.println("getItemID");
	        Item instance = new Item(1103,"Test item",100,123);
	        int expResult = 1103;
	        int result = instance.getItemID();
	        assertEquals(expResult, result);
	    }
	    
	    @Test
	    public void testItemPrice() {
	        System.out.println("getPrice");
	        Item instance = new Item(1104,"Test item",100,123);
	        float expResult = 100;
	        float result = instance.getPrice();
	        assertEquals(expResult, result,0.001);
	    }
	    
	    @Test
	    public void testAmount() {
	        System.out.println("getAmount");
	        Item instance = new Item(1105,"Test item",100,123);
	        int expResult = 123;
	        int result = instance.getAmount();
	        assertEquals(expResult, result);
	    }
	    
	    @Test
	    public void testupdateAmount() {
	        System.out.println("updateAmount");
	        Item instance = new Item(1105,"Test item",100,123);
	        int expResult = 150;
	        instance.updateAmount(150);
	        int result = instance.getAmount();
	        assertEquals(expResult, result);
	    }
}
