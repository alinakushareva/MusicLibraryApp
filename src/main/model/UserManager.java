package main.model;

import java.util.Map;

import org.json.JSONObject;

import java.util.HashMap;

public class UserManager {
    private Map<String, User> users; // Stores all users, with username as the key
    private static final String USER_FILE = "users.json"; // File to store user credentials

    /**
     * Constructs a new UserManager instance.
     * Initializes the users map and loads existing users from the file.
     */
    public UserManager() {
        // Initialize the users map and load users from the file
    }

    // ================== USER REGISTRATION ================== //

    /**
     * Registers a new user with the given username and password.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @throws IllegalArgumentException If the username is already taken or if username/password is invalid.
     */
    public void registerUser(String username, String password) {
        // Check if the username already exists
        // Validate username and password
        // Create a new User object and add it to the users map
        // Save the updated users map to the file
    }

    // ================== USER LOGIN ================== //

    /**
     * Authenticates a user and loads their data.
     *
     * @param username The username of the user.
     * @return The authenticated User object.
     * @throws IllegalArgumentException If the username or password is incorrect.
     */
    public User loginUser(String username, String password) {
        // Check if the username exists in the users map
        // Validate the password using the User object's validatePassword method
        // Load the user's library data
        // Return the authenticated User object
    	return null; // just to compile
    }

    // ================== SAVE/LOAD USERS ================== //

    /**
     * Saves all users' credentials (username, salt, hashedPassword) to a JSON file.
     */
    public void saveUsers() {
        // Serialize the users map to JSON
        // Write the JSON data to the USER_FILE
    }

    /**
     * Loads all users' credentials (username, salt, hashedPassword) from a JSON file.
     */
    public void loadUsers() {
        // Read the JSON data from the USER_FILE
        // Deserialize the JSON data into User objects
        // Add the User objects to the users map
    }

    // ================== SAVE/LOAD USER LIBRARY ================== //

    /**
     * Saves a user's library to a JSON file.
     *
     * @param user The user whose library will be saved.
     */
    public void saveUserLibrary(User user) {
        // Delegate to the User object's saveLibraryData method
    }

    /**
     * Loads a user's library from a JSON file.
     *
     * @param user The user whose library will be loaded.
     */
    public void loadUserLibrary(User user) {
        // Delegate to the User object's loadLibraryData method
    }

    // ================== HELPER METHODS ================== //

    /**
     * Checks if a username already exists in the users map.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    private boolean usernameExists(String username) {
        // Check if the username exists in the users map
		return false; // just to compile
    }

    /**
     * Serializes the users map to a JSON object.
     *
     * @return A JSON object containing all users' credentials.
     */
    private JSONObject serializeUsers() {
        // Convert the users map to a JSON object
		return null; // just to compile
    }

    /**
     * Deserializes a JSON object into the users map.
     *
     * @param usersJson A JSON object containing all users' credentials.
     */
    private void deserializeUsers(JSONObject usersJson) {
        // Convert the JSON object to a users map
    }
}