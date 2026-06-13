import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class PORTest {
    private POR poh;
    private String originalTempFileContent;
    private String originalItemDatabaseContent;
    private String originalReturnSaleContent;
    private static final String TEMP_FILE_PATH = "Database/temp.txt";
    private static final String ITEM_DATABASE_FILE = "Database/itemDatabase.txt";
    private static final String RETURN_SALE_FILE = "Database/returnSale.txt";

    @Before
    public void setUp() throws IOException {
        poh = new POR(1234567892L);
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
    }

    @Test
    public void testDeleteTempItem_1005() throws IOException {
        poh.deleteTempItem(1005);
        String updatedContent = readFileContent(TEMP_FILE_PATH);
        assertFalse(updatedContent.contains("1005"));
        assertTrue(updatedContent.contains("1002"));
    }

    @Test
    public void testEndPOS_ItemDatabase() throws IOException {
        double totalPrice = poh.endPOS(ITEM_DATABASE_FILE);
        assertEquals(0.0, totalPrice, 0.01);
        String updatedReturnSaleContent = readFileContent(RETURN_SALE_FILE);
        assertTrue(updatedReturnSaleContent.contains("1002 SkirtSteak 2 30.0"));
        String updatedItemDatabaseContent = readFileContent(ITEM_DATABASE_FILE);
        assertFalse(updatedItemDatabaseContent.contains("1005 150"));
    }

    @Test
    public void testRetrieveTemp_1005() throws IOException {
        poh.transactionItem = new ArrayList<>();
        poh.retrieveTemp(ITEM_DATABASE_FILE);
        assertEquals(3, poh.transactionItem.get(0).getAmount());
    }

    @Test
    public void testRetrieveTemp_1002() throws IOException {
        poh.transactionItem = new ArrayList<>();
        poh.retrieveTemp(ITEM_DATABASE_FILE);
        assertEquals(2, poh.transactionItem.get(1).getAmount());
        assertTrue(poh.totalPrice > 0);
    }

    @Test
    public void testDefaultConstructor() {
        POR poh = new POR(0);
        assertEquals(0, poh.phoneNum);
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
}
