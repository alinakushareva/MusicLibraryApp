/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: UserManager.java
 * Purpose: Manages user authentication, registration, and data persistence.
 *          Handles loading/saving user credentials and library data to JSON files.
 *          Uses secure password hashing with salts for credential storage.
 */
package main.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users; // Stores all users, with username as the key
    private static final String USER_FILE = "users.json"; // File to store user credentials

    /**
     * Constructs a new UserManager instance.
     * Initializes the users map and loads existing users from the file.
     */
    public UserManager() {
        this.users = new HashMap<>();
        loadUsers(); // Load users from the file when UserManager is instantiated
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
        // Check if username already taken
        if (usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        // Create and store new user
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveUsers(); // Save the updated users map to the file
    }

    // ================== USER LOGIN ================== //

    /**
     * Authenticates a user and loads their data.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated User object.
     * @throws IllegalArgumentException If the username or password is incorrect.
     */
    public User loginUser(String username, String password) {
        // Find user by username
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Validate password
        if (!user.validatePassword(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        loadUserLibrary(user); // Load the user's library when they log in
        return user;
    }

    // ================== SAVE/LOAD USERS ================== //

    /**
     * Saves all users' credentials (username, salt, hashedPassword) to a JSON file.
     *
     * @throws IllegalStateException If an error occurs while saving the file.
     */
    public void saveUsers() {
        JSONArray usersArray = new JSONArray();
        
        // Convert each user to JSON
        for (User user : users.values()) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("salt", user.getSalt()); // Store password salt
            userJson.put("hashedPassword", user.getHashedPassword()); // Store hashed password
            usersArray.put(userJson);
        }
        // Write to file 
        try (FileWriter writer = new FileWriter(USER_FILE)) {
            writer.write(usersArray.toString(4)); // To JSON
        } catch (IOException e) {
            throw new IllegalStateException("Error saving users to file", e);
        }
    }

    /**
     * Loads all users' credentials (username, salt, hashedPassword) from a JSON file.
     *
     * @throws IllegalStateException If an error occurs while loading the file.
     */
    public void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return; // No users file exists yet
        }

        try {
            // Read entire file contents
            String jsonData = new String(Files.readAllBytes(Paths.get(USER_FILE)));
            JSONArray usersArray = new JSONArray(jsonData);

            // Process each user in the array
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);
                String username = userJson.getString("username");
                String salt = userJson.getString("salt");
                String hashedPassword = userJson.getString("hashedPassword");

                // Use the factory method to create a User object
                User user = User.fromCredentials(username, salt, hashedPassword);
                users.put(username, user);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error loading users from file", e);
        }
    }

    // ================== SAVE/LOAD USER LIBRARY ================== //

    /**
     * Saves a user's library to a JSON file.
     *
     * @param user The user whose library will be saved.
     */
    public void saveUserLibrary(User user) {
        user.saveLibraryData();
    }

    /**
     * Loads a user's library from a JSON file.
     *
     * @param user The user whose library will be loaded.
     */
    public void loadUserLibrary(User user) {
        user.loadLibraryData();
    }

    // ================== HELPER METHODS ================== //

    /**
     * Checks if a username already exists in the users map.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    private boolean usernameExists(String username) {
        return users.containsKey(username);
    }
}