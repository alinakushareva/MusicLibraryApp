package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import main.model.User;
import main.model.UserManager;

class UserManagerTest {

    @Test
    void testSaveUserLibrary() {
        // Create a UserManager and a User object
        UserManager userManager = new UserManager();
        User user = new User("testUser", "password123");

        // Save the user's library
        userManager.saveUserLibrary(user);

        // Verify that the library file was created
        File libraryFile = new File("user_data/user_testUser.json");
        assertTrue(libraryFile.exists());

        // Delete the file after the test
        libraryFile.delete();
    }
}