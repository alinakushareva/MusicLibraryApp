/**
 * Name: 	Lindsay D. Davis
 * Class: 	CSC 335 Spring 2025
 * Project: Long Assignment 2 - MusicLibraryApp
 * File: 	UserManager.java
 * Purpose: Class manages access to users and user libraries
 */
package main.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class UserManager {

	private HashMap<String, User> users;
	private final String USER_FILE = "users.json";
	
	
	/**
     * Constructor for a UserManager instance
     * 
     * @param 	none
     * 
     * @return 	none
     */
	public UserManager() {
		this.users = new HashMap<String, User>();
	}
	
	
	/**
	 * Takes String username and String password and registers a new user
	 * if a user does not already exist with the indicated username
     * 
     * @param 	username 	String, intended username
     * @param 	password 	String, intended password
     * 
     * @return 	none
     * 
     * @throws 	IllegalArgumentException if username exists
     * @throws 	IllegalArgumentException if username is not a valid input (e.g., null)
     */
	public void registerUser(String username, String password) {
		
		try{
			if(users.containsKey(username)) {
				throw new IllegalArgumentException("Username already exists");
			}
			else {
				User user = new User(username, password);
				users.put(username, user);
			}
		}
		catch(IllegalArgumentException e){
			System.out.println("Please input valid username");
		}
	}
	
	
	/**
	 * Saves user data as a JSON file with each user represented by an
	 * individual JSON object
     * 
     * @param 	none
     * 
     * @return 	none
     * 
     * @throws 	IllegalArgumentException if "users.json" not found
     */
	
	public void saveUsers() {
        try {
        	FileWriter file = new FileWriter(USER_FILE);
			for( Map.Entry<String, User> entry : users.entrySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("username", entry.getKey());
				jsonObject.put("salt", entry.getValue().getSalt());
				jsonObject.put("hashedPassword", entry.getValue().getHashedPassword());
				
				file.write(jsonObject.toString());
			}
			file.close();
		}
        catch (IOException e) {
	         e.printStackTrace();
	    }
        
	}
	
	
	/**
	 * Saves library data of indicated user
	 * 
     * @param 	user 		User, user to save library of
     * 
     * @return 	none
     * 
     * @throws 	IllegalArgumentException if user does not exist
     */
	
	void saveUserLibrary(User user) {
		try {
			users.get(user.getUsername()).saveLibraryData();
		}
		catch(IllegalArgumentException e){
			System.out.println("User does not exist");
		}
	}
	
	
	/**
	 * Attempts to login a user and returns user to access data if
	 * login attempt is successful
	 * 
     * @param 	user 		String, username of user
     * @param 	password 	String, password of user
     * 
     * @return 	user, should the username-password combination match records
     * 
     * @throws 	IllegalArgumentException if user does not exist
     * @throws 	Exception if password is incorrect
     */
	
	User loginUser(String username, String password) throws Exception {
		if(users.containsKey(username)) {
			if(users.get(username).validatePassword(password)) {
				return users.get(username);
			}
			else {
				throw new Exception("Password incorrect");
			}
		}
		else {
			throw new IllegalArgumentException("User "+username+" does not exist");
		}
	}
}
