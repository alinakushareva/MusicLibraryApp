/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: Main.java
 * Purpose: This is the entry point of the MusicLibraryApp. It initializes the MusicStore, LibraryModel,
 *          and LibraryView, and starts the application by launching the user interface. The main method
 *          sets up the necessary components for the application to run.
 */
package main.view;

import main.database.MusicStore;
import main.model.LibraryModel;
import main.view.LibraryView;

public class Main {
	public static void main(String[] args) {
	    // Initializing MusicStore with the path to the test data
	    MusicStore store = new MusicStore("src/main/albums");

	    // Initializing LibraryModel with the MusicStore
	    LibraryModel model = new LibraryModel(store);

	    // Initializing LibraryView with the LibraryModel
	    LibraryView view = new LibraryView(model);

	    // Starting the UI
	    view.promptForCommand();
	}
}