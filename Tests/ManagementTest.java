import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagementTest {

    private String USER_DATABASE = "Database/userDatabase.txt";
    private String TEMP_USER_DATABASE = "Database/temp_userDatabase.txt";
    private Management management;

    @Before
    public void setUp() throws Exception {
        management = new Management();
        createTempDatabase();
    }

    @After
    public void tearDown() throws Exception {
        restoreOriginalDatabase();
    }

    @Test
    public void testCheckUser_UserExists() {
        Long existingUserPhone = 6096515668L;
        assertTrue(management.checkUser(existingUserPhone));
    }

    @Test
    public void testCheckUser_UserDoesNotExist() {
        Long nonExistingUserPhone = 12345678L;
        assertFalse(management.checkUser(nonExistingUserPhone));
    }

    private void createTempDatabase() throws IOException {
        try (InputStream in = new FileInputStream(USER_DATABASE);
             OutputStream out = new FileOutputStream(TEMP_USER_DATABASE)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_USER_DATABASE, true))) {
            writer.write("1234567890 2000,09/30/24,false\n");
        }
    }

    private void restoreOriginalDatabase() throws IOException {
        File tempFile = new File(TEMP_USER_DATABASE);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testGetLatestReturnDate_ValidPhone() {
        Long testPhone = 1112223334L;
        List<ReturnItem> returnItems = management.getLatestReturnDate(testPhone);
        assertNotNull(returnItems);
        assertEquals(1, returnItems.size());
        assertEquals(1013, returnItems.get(0).getItemID());
        assertTrue(returnItems.get(0).getDays() > 0);
    }

    @Test
    public void testCreateUser_ValidPhone() {
        Long phone = 1234567890L;
        boolean result = management.createUser(phone);
        assertTrue(result);
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMP_USER_DATABASE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Long.toString(phone))) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddRental_SingleItem() {
        Long phone = 1234567890L;
        management.createUser(phone);
        List<Item> rentalList = new ArrayList<>();
        rentalList.add(new Item(1111, "Test item", 100, 123));
        Management.addRental(phone, rentalList);
        verifyRentalAdded(phone);
    }

    @Test
    public void testAddRental_MultipleItems() {
        Long phone = 1234567890L;
        management.createUser(phone);
        List<Item> rentalList = new ArrayList<>();
        rentalList.add(new Item(1111, "Test item", 100, 123));
        rentalList.add(new Item(1112, "Test item 2", 150, 456));
        Management.addRental(phone, rentalList);
        verifyRentalAdded(phone);
    }

    private void verifyRentalAdded(Long phone) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEMP_USER_DATABASE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Long.toString(phone))) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateRentalStatus_SingleItem() throws IOException {
        Long phone = 9876543455L;
        management.createUser(phone);
        List<Item> rentalList = new ArrayList<>();
        rentalList.add(new Item(1115, "Test item", 100, 123));
        Management.addRental(phone, rentalList);
        List<ReturnItem> returnedList = new ArrayList<>();
        returnedList.add(new ReturnItem(1115, 2));
        management.updateRentalStatus(phone, returnedList);
        verifyRentalStatusUpdated(phone);
    }

    @Test
    public void testUpdateRentalStatus_MultipleItems() throws IOException {
        Long phone = 9876543455L;
        management.createUser(phone);
        List<Item> rentalList = new ArrayList<>();
        rentalList.add(new Item(1115, "Test item", 100, 123));
        rentalList.add(new Item(1116, "Test item 2", 150, 456));
        Management.addRental(phone, rentalList);
        List<ReturnItem> returnedList = new ArrayList<>();
        returnedList.add(new ReturnItem(1115, 2));
        returnedList.add(new ReturnItem(1116, 3));
        management.updateRentalStatus(phone, returnedList);
        verifyRentalStatusUpdated(phone);
    }

    private void verifyRentalStatusUpdated(Long phone) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Database/temp_userDatabase.txt"))) {
            String line;
            boolean updated = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(Long.toString(phone)) && line.contains("true")) {
                    updated = true;
                    break;
                }
            }
            assertTrue(updated);
        }
    }
}
