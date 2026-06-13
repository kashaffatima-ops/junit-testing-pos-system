import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class POSTest {
    private POS poh;
    private String originalTempFileContent;
    private String originalItemDatabaseContent;
    private String originalReturnSaleContent;
    private static final String TEMP_FILE_PATH = "Database/temp.txt";
    private static final String BACKUP_FILE_PATH = "Database/temp_backup.txt";
    private static final String ITEM_DATABASE_FILE = "Database/itemDatabase.txt";
    private static final String RETURN_SALE_FILE = "Database/returnSale.txt";
    private static final String BACKUP_ITEM_DATABASE_FILE = "Database/itemDatabase_backup.txt";
    private static final String BACKUP_RETURN_SALE_FILE = "Database/returnSale_backup.txt";

    @Before
    public void setUp() throws IOException {
        poh = new POS();
        originalTempFileContent = readFileContent(TEMP_FILE_PATH);
        originalItemDatabaseContent = readFileContent(ITEM_DATABASE_FILE);
        originalReturnSaleContent = readFileContent(RETURN_SALE_FILE);
        poh.transactionItem = new ArrayList<>();
        poh.transactionItem.add(new Item(1002, "SkirtSteak", 2, 15));
        poh.returnSale = true;
    }

    @After
    public void tearDown() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE_PATH))) {
            writer.write(originalTempFileContent);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ITEM_DATABASE_FILE))) {
            writer.write(originalItemDatabaseContent);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RETURN_SALE_FILE))) {
            writer.write(originalReturnSaleContent);
        }
        new File(BACKUP_FILE_PATH).delete();
        new File(BACKUP_ITEM_DATABASE_FILE).delete();
        new File(BACKUP_RETURN_SALE_FILE).delete();
    }

    @Test
    public void testDeleteTempItem() throws IOException {
        poh.deleteTempItem(1005);
        String updatedContent = readFileContent(TEMP_FILE_PATH);
        assertFalse(updatedContent.contains("1005"));
        assertTrue(updatedContent.contains("1002"));
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

    @Test
    public void testEndPOS_SumCheck() throws IOException {
        double totalPrice = poh.endPOS(ITEM_DATABASE_FILE);
        assertEquals(0.0, totalPrice, 0.01);
    }

    @Test
    public void testEndPOS_ReturnSaleUpdate() throws IOException {
        poh.endPOS(ITEM_DATABASE_FILE);
        String updatedReturnSaleContent = readFileContent(RETURN_SALE_FILE);
        assertTrue(updatedReturnSaleContent.contains("1002 SkirtSteak 2 30.0"));
    }

    @Test
    public void testEndPOS_ItemDatabaseUpdate() throws IOException {
        poh.endPOS(ITEM_DATABASE_FILE);
        String updatedItemDatabaseContent = readFileContent(ITEM_DATABASE_FILE);
        assertFalse(updatedItemDatabaseContent.contains("1005 150"));
    }

    @Test
    public void testRetrieveTemp_AmountCheck1005() throws IOException {
        poh.transactionItem = new ArrayList<>();
        poh.retrieveTemp(ITEM_DATABASE_FILE);
        assertEquals(3, poh.transactionItem.get(0).getAmount());
    }

    @Test
    public void testRetrieveTemp_AmountCheck1002() throws IOException {
        poh.transactionItem = new ArrayList<>();
        poh.retrieveTemp(ITEM_DATABASE_FILE);
        assertEquals(2, poh.transactionItem.get(1).getAmount());
    }

    @Test
    public void testRetrieveTemp_TotalPriceUpdate() throws IOException {
        poh.transactionItem = new ArrayList<>();
        poh.retrieveTemp(ITEM_DATABASE_FILE);
        assertTrue(poh.totalPrice > 0);
    }

    @Test
    public void testDefaultConstructor() {
        POS poh = new POS();
    }
}
