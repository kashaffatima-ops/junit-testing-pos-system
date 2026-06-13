import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class InventoryTest {
    private Inventory inventory;
    private List<Item> databaseItem;
    private String databaseFile;
    private List<Item> transactionItem;
    private String tempDatabaseFile;

    @Before
    public void setUp() throws IOException {
        inventory = Inventory.getInstance();
        databaseFile = "Database/itemDatabase.txt";
        transactionItem = new ArrayList<>();
        tempDatabaseFile = "Database/itemDatabase_temp.txt";
        createTempDatabase(databaseFile, tempDatabaseFile);
    }

    private void createTempDatabase(String sourceFile, String tempFile) throws IOException {
        File source = new File(sourceFile);
        File temp = new File(tempFile);
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(temp)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    @After
    public void tearDown() throws IOException {
        File tempFile = new File(tempDatabaseFile);
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    public void testGetInstance() {
        Inventory instance1 = Inventory.getInstance();
        Inventory instance2 = Inventory.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testGetInstanceIsNotNull() {
        Inventory instance = Inventory.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testAccessInventory() {
        databaseItem = new ArrayList<>();
        boolean result = inventory.accessInventory(tempDatabaseFile, databaseItem);
        assertTrue(result);
        assertEquals(101, databaseItem.size());

        Item item1 = databaseItem.get(0);
        assertEquals(1000, item1.getItemID());
        assertEquals("Potato", item1.getItemName());
        assertEquals(1.0, item1.getPrice(), 0.01);
        assertEquals(249, item1.getAmount());

        Item item2 = databaseItem.get(10);
        assertEquals(1010, item2.getItemID());
        assertEquals("Applesauce", item2.getItemName());
        assertEquals(2.5, item2.getPrice(), 0.01);
        assertEquals(100, item2.getAmount());

        Item item3 = databaseItem.get(100);
        assertEquals(1100, item3.getItemID());
        assertEquals("IceCreamToppings", item3.getItemName());
        assertEquals(6.7, item3.getPrice(), 0.01);
        assertEquals(100, item3.getAmount());
    }

    @Test
    public void testUpdateInventorySale_Potato() {
        databaseItem = new ArrayList<>();
        boolean result = inventory.accessInventory(tempDatabaseFile, databaseItem);
        databaseItem.add(new Item(1000, "Potato", 1.0f, 249));
        transactionItem.add(new Item(1000, "Potato", 1.0f, 49));
        inventory.updateInventory(tempDatabaseFile, transactionItem, databaseItem, true);
        assertEquals(200, databaseItem.get(0).getAmount());
    }

    @Test
    public void testUpdateInventorySale_PlasticCup() {
        databaseItem = new ArrayList<>();
        boolean result = inventory.accessInventory(tempDatabaseFile, databaseItem);
        databaseItem.add(new Item(1001, "PlasticCup", 0.5f, 376));
        transactionItem.add(new Item(1001, "PlasticCup", 0.5f, 100));
        inventory.updateInventory(tempDatabaseFile, transactionItem, databaseItem, true);
        assertEquals(276, databaseItem.get(1).getAmount());
    }

    @Test
    public void testUpdateInventoryReturn_SkirtSteak() {
        databaseItem = new ArrayList<>();
        boolean result = inventory.accessInventory(tempDatabaseFile, databaseItem);
        databaseItem.add(new Item(1002, "SkirtSteak", 15.0f, 1055));
        transactionItem.add(new Item(1002, "SkirtSteak", 15.0f, 55));
        inventory.updateInventory(tempDatabaseFile, transactionItem, databaseItem, false);
        assertEquals(1110, databaseItem.get(2).getAmount());
    }

    @Test
    public void testUpdateInventoryReturn_PotatoChips() {
        databaseItem = new ArrayList<>();
        boolean result = inventory.accessInventory(tempDatabaseFile, databaseItem);
        databaseItem.add(new Item(1003, "PotatoChips", 1.2f, 168));
        transactionItem.add(new Item(1003, "PotatoChips", 1.2f, 32));
        inventory.updateInventory(tempDatabaseFile, transactionItem, databaseItem, false);
        assertEquals(200, databaseItem.get(3).getAmount());
    }
}
