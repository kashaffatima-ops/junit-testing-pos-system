import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class PointOfSaleTest {
    private PointOfSale pointOfSale;
    private String originalDatabaseContent;
    private List<Item> originalTransactionItems;
    private static final String DATABASE_FILE = "Database/itemDatabase.txt";
    private static final String TEMP_FILE = "Database/temp.txt";
    private double originalTotalPrice;
    private String originalTempFileContent;
    private static final String TEMP_FILE_PATH = "Database/temp.txt";
    private static final String BACKUP_TEMP_FILE_PATH = "Database/temp_backup.txt";

    @Before
    public void setUp() throws IOException {
        pointOfSale = new PointOfSale() {
            @Override
            public double endPOS(String textFile) {
                return 0;
            }
            @Override
            public void deleteTempItem(int id) {}
            @Override
            public void retrieveTemp(String textFile) {}
        };

        originalDatabaseContent = readFileContent(DATABASE_FILE);
        pointOfSale.transactionItem = new ArrayList<>();
        pointOfSale.databaseItem = new ArrayList<>();
        pointOfSale.inventory.accessInventory(DATABASE_FILE, pointOfSale.databaseItem);
        originalTransactionItems = new ArrayList<>(pointOfSale.transactionItem);
        originalTotalPrice = pointOfSale.totalPrice;
        pointOfSale.totalPrice = 100.0;
        originalTempFileContent = readFileContent(TEMP_FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_TEMP_FILE_PATH))) {
            writer.write(originalTempFileContent);
        }
        int testItemId = 1010;
        int testAmount = 2;
        pointOfSale.transactionItem.add(new Item(testItemId, "TestItem", 10, testAmount));
        pointOfSale.createTemp(testItemId, testAmount);
    }

    @After
    public void tearDown() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            writer.write(originalDatabaseContent);
        }
        pointOfSale.transactionItem.clear();
        pointOfSale.transactionItem.addAll(originalTransactionItems);
        pointOfSale.totalPrice = originalTotalPrice;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE_PATH))) {
            writer.write(originalTempFileContent);
        }
        new File(BACKUP_TEMP_FILE_PATH).delete();
    }

    @Test
    public void testStartNew() throws IOException {
        boolean result = pointOfSale.startNew(DATABASE_FILE);
        assertTrue(result);
        assertFalse(pointOfSale.databaseItem.isEmpty());
        Item firstItem = pointOfSale.databaseItem.get(0);
        assertEquals(1005, firstItem.getItemID());
        assertEquals("SkirtSteak", firstItem.getItemName());
    }

    @Test
    public void testEnterItem() {
        int itemID = 1005;
        int amount = 3;
        boolean result = pointOfSale.enterItem(itemID, amount);
        assertTrue(result);
        assertFalse(pointOfSale.transactionItem.isEmpty());
        Item addedItem = pointOfSale.transactionItem.get(pointOfSale.transactionItem.size() - 1);
        assertEquals(itemID, addedItem.getItemID());
        assertEquals(amount, addedItem.getAmount());
    }

    @Test
    public void testEnterItemItemNotFound() {
        int invalidItemID = 9999;
        int amount = 2;
        boolean result = pointOfSale.enterItem(invalidItemID, amount);
        assertFalse(result);
        assertEquals(originalTransactionItems.size(), pointOfSale.transactionItem.size());
    }

    @Test
    public void testUpdateTotal() {
        int itemID = 1005;
        String itemName = "Test Item";
        float itemPrice = 10;
        int amount = 2;
        Item testItem = new Item(itemID, itemName, itemPrice, amount);
        pointOfSale.transactionItem.add(testItem);
        double updatedTotal = pointOfSale.updateTotal();
        double expectedTotal = originalTotalPrice + (itemPrice * amount);
        assertEquals(expectedTotal, updatedTotal, 0.01);
    }

    @Test
    public void testUpdateTotalWithNoItems() {
        double updatedTotal = pointOfSale.updateTotal();
        assertEquals(originalTotalPrice, updatedTotal, 0.01);
    }

    @Test
    public void testValidCoupon() {
        String validCoupon = "C005";
        boolean result = pointOfSale.coupon(validCoupon);
        assertTrue(result);
        double expectedTotalPrice = originalTotalPrice * 0.90f;
        assertEquals(expectedTotalPrice, pointOfSale.totalPrice, 0.01);
    }

    @Test
    public void testInvalidCoupon() {
        String invalidCoupon = "INVALID_COUPON";
        boolean result = pointOfSale.coupon(invalidCoupon);
        assertFalse(result);
        assertEquals(100.0, pointOfSale.totalPrice, 0.01);
    }

    @Test
    public void testCouponFileNotFound() {
        String originalCouponFile = PointOfSale.couponNumber;
        PointOfSale.couponNumber = "Database/non_existent_coupon.txt";
        String validCoupon = "C001";
        boolean result = pointOfSale.coupon(validCoupon);
        assertFalse(result);
        PointOfSale.couponNumber = originalCouponFile;
    }

    @Test
    public void testCreateTemp() throws IOException {
        int itemId = 1010;
        int amount = 5;
        pointOfSale.createTemp(itemId, amount);
        String updatedContent = readFileContent(TEMP_FILE_PATH);
        assertTrue(updatedContent.contains(itemId + " " + amount));
        removeLastTempEntry();
    }

    @Test
    public void testRemoveItems() throws IOException {
        int itemIDToRemove = 1010;
        boolean result = pointOfSale.removeItems(itemIDToRemove);
        assertFalse(containsItem(pointOfSale.transactionItem, itemIDToRemove));
        String updatedTempFileContent = readFileContent(TEMP_FILE_PATH);
        assertFalse(updatedTempFileContent.contains(String.valueOf(itemIDToRemove)));
        assertEquals(0.0, pointOfSale.totalPrice, 0.01);
        tearDown();
    }

    @Test
    public void testCreditCard() {
        assertTrue(pointOfSale.creditCard("1234567812345678"));
        assertFalse(pointOfSale.creditCard("12345678"));
        assertFalse(pointOfSale.creditCard("1234abcd5678efgh"));
        assertFalse(pointOfSale.creditCard("12345678901234567"));
    }

    @Test
    public void testLastAddedItem() {
        Item lastItem = pointOfSale.lastAddedItem();
        assertNotNull(lastItem);
        assertEquals(1010, lastItem.getItemID());
        assertEquals("TestItem", lastItem.getItemName());
        assertEquals(10.0, lastItem.getPrice(), 0.01);
    }

    @Test
    public void testGetCart() {
        List<Item> cart = pointOfSale.getCart();
        assertNotNull(cart);
        assertEquals(1, cart.size());
        assertEquals(1010, cart.get(0).getItemID());
    }

    @Test
    public void testGetCartSize() {
        int cartSize = pointOfSale.getCartSize();
        assertEquals(1, cartSize);
    }

    @Test
    public void testGetTotal() {
        double expectedTotal = 20.0 * 3;
        assertEquals(expectedTotal, pointOfSale.getTotal(), 0.01);
    }

    private boolean containsItem(List<Item> transactionItem, int itemID) {
        return transactionItem.stream().anyMatch(item -> item.getItemID() == itemID);
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    private void removeLastTempEntry() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(TEMP_FILE_PATH));
        StringBuilder content = new StringBuilder();
        String line;
        String lastLine = "";
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) lastLine = line;
            content.append(line).append(System.lineSeparator());
        }
        reader.close();
        String updatedContent = content.toString().replace(lastLine, "").trim();
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE_PATH));
        writer.write(updatedContent);
        writer.close();
    }
}
