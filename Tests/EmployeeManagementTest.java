import java.io.*;
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployeeManagementTest {

    private EmployeeManagement testobj;
    private List<String> addedUsernames;

    public EmployeeManagementTest() {}

    @BeforeClass
    public static void setUpClass() {}

    @AfterClass
    public static void tearDownClass() {}

    @Before
    public void setUp() {
        testobj = new EmployeeManagement();
        addedUsernames = new ArrayList<>();
    }

    @After
    public void tearDown() throws IOException {
        for (String username : addedUsernames) {
            testobj.delete(username);
        }
        addedUsernames.clear();
    }

    @Test
    public void testAdd_Cashier() throws IOException {
        List<Employee> instancelist = testobj.getEmployeeList();
        int instancesize = instancelist.size();
        testobj.add("Alpha Release", "123345", true);
        addedUsernames.add("Alpha Release");
        List<Employee> newlist = testobj.getEmployeeList();
        assertEquals(instancesize + 1, newlist.size());
        Employee latestaddition = newlist.get(newlist.size() - 1);
        assertEquals("Alpha Release", latestaddition.getName());
        assertEquals("123345", latestaddition.getPassword());
        assertEquals("Cashier", latestaddition.getPosition());
    }

    @Test
    public void testAdd_Admin() throws IOException {
        List<Employee> instancelist = testobj.getEmployeeList();
        int instancesize = instancelist.size();
        testobj.add("Alpha Release2", "123345", false);
        addedUsernames.add("Alpha Release2");
        List<Employee> newlist = testobj.getEmployeeList();
        assertEquals(instancesize + 1, newlist.size());
        Employee latestaddition = newlist.get(newlist.size() - 1);
        assertEquals("Alpha Release2", latestaddition.getName());
        assertEquals("123345", latestaddition.getPassword());
        assertEquals("Admin", latestaddition.getPosition());
    }

    @Test
    public void testDelete_ExistingEmployee() throws IOException {
        testobj.add("DeleteTest Employee", "testpass123", true);
        addedUsernames.add("DeleteTest Employee");
        List<Employee> employeeListBeforeDelete = testobj.getEmployeeList();
        int initialSize = employeeListBeforeDelete.size();
        String lastAddedUsername = employeeListBeforeDelete.get(initialSize - 1).getUsername();
        boolean isDeleted = testobj.delete(lastAddedUsername);
        assertTrue(isDeleted);
        List<Employee> employeeListAfterDelete = testobj.getEmployeeList();
        assertEquals(initialSize - 1, employeeListAfterDelete.size());
        for (Employee emp : employeeListAfterDelete) {
            assertNotEquals(lastAddedUsername, emp.getUsername());
        }
    }

    @Test
    public void testDelete_NonExistingEmployee() throws IOException {
        testobj.add("DeleteTest Employee", "testpass123", true);
        addedUsernames.add("DeleteTest Employee");
        String nonExistentUsername = "Nonexistent employee";
        boolean isDeleted = testobj.delete(nonExistentUsername);
        assertFalse(isDeleted);
    }

    @Test
    public void testUpdate_ValidUpdate() throws IOException {
        testobj.add("UpdateTest Employee", "originalPass123", true);
        addedUsernames.add("UpdateTest Employee");
        List<Employee> employeeListBeforeUpdate = testobj.getEmployeeList();
        String lastAddedUsername = employeeListBeforeUpdate.get(employeeListBeforeUpdate.size() - 1).getUsername();
        int result = testobj.update(lastAddedUsername, "newPass456", "Admin", "UpdatedTest Employee");
        assertEquals(0, result);
        List<Employee> employeeListAfterUpdate = testobj.getEmployeeList();
        Employee updatedEmployee = null;
        for (Employee emp : employeeListAfterUpdate) {
            if (emp.getUsername().equals(lastAddedUsername)) {
                updatedEmployee = emp;
                break;
            }
        }
        assertNotNull(updatedEmployee);
        assertEquals("UpdatedTest Employee", updatedEmployee.getName());
        assertEquals("newPass456", updatedEmployee.getPassword());
        assertEquals("Admin", updatedEmployee.getPosition());
    }

    @Test
    public void testUpdate_InvalidUsername() throws IOException {
        int result = testobj.update("99999", "newPass456", "Admin", "NonExistent Employee");
        assertEquals(-1, result);
    }

    @Test
    public void testUpdate_InvalidPosition() throws IOException {
        testobj.add("PositionTest Employee", "testpass123", true);
        addedUsernames.add("PositionTest Employee");
        List<Employee> employeeListBeforeUpdate = testobj.getEmployeeList();
        String lastAddedUsername = employeeListBeforeUpdate.get(employeeListBeforeUpdate.size() - 1).getUsername();
        int result = testobj.update(lastAddedUsername, "newPass456", "InvalidPosition", "UpdatedTest Employee");
        assertEquals(-2, result);
    }
}
