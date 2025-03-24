package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import main.model.UserManager;
import main.model.User;

class UserManagerTest {
    private static final String TEST_PASSWORD = "password123";
    private static final String EXPECTED_USER_FILE = "users.json";
    
    private UserManager manager;
    private String uniqueUsername;
    
    @BeforeEach
    void setUp() {
        // Generate unique username for each test
        uniqueUsername = "testUser_" + System.currentTimeMillis();
        // Ensure clean state
        new File(EXPECTED_USER_FILE).delete();
        new File("user_data/user_" + uniqueUsername + ".json").delete();
        manager = new UserManager();
    }
    
    @AfterEach
    void tearDown() {
        // Clean up files
        new File(EXPECTED_USER_FILE).delete();
        new File("user_data/user_" + uniqueUsername + ".json").delete();
    }

    @Test
    void testRegisterNewUserCreatesUserFile() {
        manager.registerUser(uniqueUsername, TEST_PASSWORD);
        assertTrue(new File(EXPECTED_USER_FILE).exists());
    }

    @Test
    void testRegisterDuplicateUserThrowsException() {
        manager.registerUser(uniqueUsername, TEST_PASSWORD);
        assertThrows(IllegalArgumentException.class, () -> 
            manager.registerUser(uniqueUsername, "differentPassword"));
    }

    @Test
    void testLoginValidUser() {
        manager.registerUser(uniqueUsername, TEST_PASSWORD);
        User user = manager.loginUser(uniqueUsername, TEST_PASSWORD);
        assertEquals(uniqueUsername, user.getUsername());
    }

    @Test
    void testLoginInvalidUsernameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            manager.loginUser("nonexistent", TEST_PASSWORD));
    }

    @Test
    void testLoginInvalidPasswordThrowsException() {
        manager.registerUser(uniqueUsername, TEST_PASSWORD);
        assertThrows(IllegalArgumentException.class, () -> 
            manager.loginUser(uniqueUsername, "wrongPassword"));
    }

    @Test
    void testSaveUserLibraryCreatesFile() {
        User user = new User(uniqueUsername, TEST_PASSWORD);
        manager.saveUserLibrary(user);
        assertTrue(new File("user_data/user_" + uniqueUsername + ".json").exists());
    }

    @Test
    void testLoadUserLibraryWithNoFile() {
        User user = new User(uniqueUsername, TEST_PASSWORD);
        assertDoesNotThrow(() -> manager.loadUserLibrary(user));
    }

    @Test
    void testLoadUsersWithExistingFile() {
        manager.registerUser(uniqueUsername, TEST_PASSWORD);
        UserManager newManager = new UserManager(); // Should load from file
        assertDoesNotThrow(() -> newManager.loginUser(uniqueUsername, TEST_PASSWORD));
    }

    @Test
    void testLoadUsersWithNoFile() {
        new File(EXPECTED_USER_FILE).delete();
        UserManager newManager = new UserManager();
        assertThrows(IllegalArgumentException.class, () -> 
            newManager.loginUser(uniqueUsername, TEST_PASSWORD));
    }
}