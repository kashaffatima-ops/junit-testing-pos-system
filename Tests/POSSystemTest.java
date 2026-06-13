import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.*;
import static org.junit.Assert.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class POSSystemTest {

    private POSSystem posSystem;
    private String tempFilePath = "Database/temp.txt";
    private String originalTempFileContent;
    private static final String EMPLOYEE_DB = "Database/employeeDatabase.txt";
    private static final String TEMP_EMPLOYEE_DB = "Database/temp_employeeDatabase.txt";

    @Before
    public void setUp() throws IOException {
        posSystem = new POSSystem();
        Files.copy(Paths.get(EMPLOYEE_DB), Paths.get(TEMP_EMPLOYEE_DB), StandardCopyOption.REPLACE_EXISTING);
        File tempFile = new File(tempFilePath);
        if (tempFile.exists()) {
            originalTempFileContent = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Return\n");
            writer.write("1234567892\n");
            writer.write("1005 1\n");
            writer.write("1002 3\n");
            writer.write("1005 1\n");
        }
        posSystem.readFile();
    }

    @Test
    public void testContinueFromTemp_Return() {
        long phone = 1234567892L;
        String result = posSystem.continueFromTemp(phone);
        assertEquals("Return", result);
    }

    @Test
    public void testContinueFromTemp_Sale() {
        long phone = 1234567892L;
        String result = posSystem.continueFromTemp(phone);
        assertEquals("Sale", result);
    }

    @Test
    public void testContinueFromTemp_Rental() {
        long phone = 1234567892L;
        String result = posSystem.continueFromTemp(phone);
        assertEquals("Rental", result);
    }

    @Test
    public void testCheckTemp_True() {
        boolean result = posSystem.checkTemp();
        assertTrue(result);
    }

    @Test
    public void testCheckTemp_False() {
        File tempFile = new File(tempFilePath);
        tempFile.delete();
        boolean result = posSystem.checkTemp();
        assertFalse(result);
    }

    @Test
    public void testLogIn_ValidCashier() {
        int result = posSystem.logIn("110002", "lehigh2016");
        assertEquals(1, result);
    }

    @Test
    public void testLogIn_ValidAdmin() {
        int result = posSystem.logIn("110001", "1");
        assertEquals(1, result);
    }

    @Test
    public void testLogIn_InvalidUsername() {
        int result = posSystem.logIn("112", "lehigh2016");
        assertEquals(0, result);
    }

    @Test
    public void testLogIn_InvalidPassword() {
        int result = posSystem.logIn("110002", "lehig");
        assertEquals(0, result);
    }

    @Test
    public void testLogOut() {
        posSystem.logIn("110002", "lehigh2016");
        posSystem.logOut("Cashier");
    }

    @After
    public void tearDown() throws IOException {
        if (originalTempFileContent != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))) {
                writer.write(originalTempFileContent);
            }
        } else {
            new File(tempFilePath).delete();
        }
        Files.copy(Paths.get(TEMP_EMPLOYEE_DB), Paths.get(EMPLOYEE_DB), StandardCopyOption.REPLACE_EXISTING);
    }
}
