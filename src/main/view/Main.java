package main.view;

import main.database.MusicStore;
import main.model.LibraryModel;
import main.view.LibraryView;

public class Main {
	public static void main(String[] args) {
	    // Initialize MusicStore with the path to your test data
	    MusicStore store = new MusicStore("src/main/albums");

	    // Initialize LibraryModel with the MusicStore
	    LibraryModel model = new LibraryModel(store);

	    // Initialize LibraryView with the LibraryModel
	    LibraryView view = new LibraryView(model);

	    // Start the UI
	    view.promptForCommand();
	}
}