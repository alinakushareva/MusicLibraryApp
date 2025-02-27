/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: LibraryView.java
 * Purpose: This class handles the user interface (UI) for managing and interacting with the music library.
 * 
 *          The design of the UI, displaying logic, and some display methods were assisted by AI.
 *          AI was mainly used to help structure the menus, improve the overall look, and handle parts
 *          of the display logic. All other logic, implementation, and integration were completed manually
 *          to ensure the application meets the project requirements and functions as intended.
 */
package main.view;

import main.model.*;
import main.database.MusicStore;
import java.util.*;

public class LibraryView {
    private final LibraryModel model;
    private final Scanner scanner;

    /**
     * Constructs a LibraryView instance.
     * 
     * @param model The LibraryModel to interact with.
     */
    public LibraryView(LibraryModel model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    // ================== MAIN MENU ================== //

    /**
     * AI generated!
     * Displays the main menu options. 
     */
    public void displayMainMenu() {
        System.out.println("\n=== Music Library Manager ===");
        System.out.println("1. Search Music Store");
        System.out.println("2. My Library");
        System.out.println("3. Playlists");
        System.out.println("4. Rate Songs & Favorites");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * AI generated!
     * Handles user input for the main menu.
     */
    public void promptForCommand() {
        while (true) {
            displayMainMenu();
            String input = getUserInput();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        handleStoreSearch();
                        break;
                    case 2:
                        handleLibraryMenu();
                        break;
                    case 3:
                        handlePlaylistMenu();
                        break;
                    case 4:
                        handleRatingMenu();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // ================== STORE SEARCH ================== //

    /**
     * AI generated!
     * Handles music store search operations.
     */
    private void handleStoreSearch() {
        while (true) {
            System.out.println("\n=== Search Music Store ===");
            System.out.println("1. Search Songs by Title");
            System.out.println("2. Search Songs by Artist");
            System.out.println("3. Search Albums by Title");
            System.out.println("4. Search Albums by Artist");
            System.out.println("5. Add Song to Library");
            System.out.println("6. Add Album to Library");
            System.out.println("7. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        handleSongSearchByTitle();
                        break;
                    case 2:
                        handleSongSearchByArtist();
                        break;
                    case 3:
                        handleAlbumSearchByTitle();
                        break;
                    case 4:
                        handleAlbumSearchByArtist();
                        break;
                    case 5:
                        handleAddSong();
                        break;
                    case 6:
                        handleAddAlbum();
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // ================== SEARCH HANDLERS ================== //

    /**
     * Handles song search by title in the music store.
     */
    private void handleSongSearchByTitle() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        // Search for matching songs in the store
        List<Song> results = model.searchStoreSongByTitle(title);
        displaySearchResults(results);
    }

    /**
     * Handles song search by artist in the music store.
     */
    private void handleSongSearchByArtist() {
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        // Searching for matching songs in the store
        List<Song> results = model.searchStoreSongByArtist(artist);
        displaySearchResults(results);
    }

    /**
     * Handles album search by title in the music store.
     */
    private void handleAlbumSearchByTitle() {
        System.out.print("Enter album title: ");
        String title = getUserInput();
        
        // Searching for the album in the music store
        Album album = model.searchStoreAlbumByTitle(title);
        
        List<Album> results;
        if (album != null) {
            // If the album is found, creating a list with the single album
            results = List.of(album);
        } else {
            // If the album is not found, using an empty list
            results = Collections.emptyList();
        }
        displaySearchResults(results);
    }

    /**
     * Handles album search by artist in the music store.
     */
    private void handleAlbumSearchByArtist() {
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        // Searching for albums by the artist
        List<Album> results = model.searchStoreAlbumByArtist(artist);
        displaySearchResults(results);
    }

    // ================== ADD SONGS & ALBUMS TO LIBRARY ================== //

    /**
     * Handles adding a song to the library.
     */
    private void handleAddSong() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        
        // Search for the song in the music store
        Song song = model.getMusicStore().getSongByArtistAndTitle(artist, title);
        
        // If the song exists, adding it to the library
        if (song != null) {
            model.addSong(song);
            System.out.println("Song added to library!");
        } else {
            System.out.println("Song not found in MusicStore.");
        }
    }

    /**
     * Handles adding an album to the library.
     */
    private void handleAddAlbum() {
        System.out.print("Enter album title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        
        // Search for the album in the music store
        Album album = model.getMusicStore().getAlbumByArtistAndTitle(artist, title);
        
        // If the album exists, adding it to the library
        if (album != null) {
            model.addAlbum(album);
            System.out.println("Album added to library!");
        } else {
            System.out.println("Album not found in MusicStore.");
        }
    }

    // ================== LIBRARY MANAGEMENT ================== //

    /**
     * AI generated!
     * Handles library menu operations.
     */
    private void handleLibraryMenu() {
    	// Infinite loop to keep displaying the menu until user exits
        while (true) {
            System.out.println("\n=== My Library ===");
            System.out.println("1. View All Songs");
            System.out.println("2. View All Albums");
            System.out.println("3. View All Artists");
            System.out.println("4. View Playlists");
            System.out.println("5. View Favorites");
            System.out.println("6. Remove Song from Library");
            System.out.println("7. Remove Album from Library");
            System.out.println("8. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
            	// Reading user input and converting to integer
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        displayLibrarySongs(); // Show all songs in the library
                        break;
                    case 2:
                        displayLibraryAlbums(); // Show all albums in the library
                        break;
                    case 3:
                        displayLibraryArtists(); // Show all artists in the library
                        break;
                    case 4:
                        displayPlaylists(); // Show all playlists
                        break;
                    case 5:
                        displayFavorites(); // Show favorite songs
                        break;
                    case 6:
                        handleRemoveSong(); // Remove a song from the library
                        break;
                    case 7:
                        handleRemoveAlbum(); // Remove an album from the library
                        break;
                    case 8:
                        return; // Exiting menu and returning to the main menu
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // ================== REMOVE SONGS & ALBUMS FROM LIBRARY ================== //

    /**
     * Handles removing a song from the library.
     */
    private void handleRemoveSong() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Song song = model.searchSongByArtistAndTitle(artist, title);
        if (song != null) {
            model.removeSong(song);
            System.out.println("Song removed from library!");
        } else {
            System.out.println("Song not found in your library.");
        }
    }

    /**
     * Handles removing an album from the library.
     */
    private void handleRemoveAlbum() {
        System.out.print("Enter album title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Album album = model.searchAlbumByTitle(title);
        if (album != null && album.getArtist().equalsIgnoreCase(artist)) {
            model.removeAlbum(album);
            System.out.println("Album removed from library!");
        } else {
            System.out.println("Album not found in your library.");
        }
    }

    // ================== DISPLAY METHODS ================== //

    /**
     * AI generated!
     * Displays all songs in the library.
     */
    private void displayLibrarySongs() {
        Set<Song> songs = model.getSongLibrary();
        if (songs.isEmpty()) {
            System.out.println("\nYour library has no songs yet.");
        } else {
            System.out.println("\n=== Your Songs ===");
            for (Song song : songs) {
                printSongWithRating(song);
            }
        }
    }

    /**
     * AI generated!
     * Displays favorite songs in the library.
     */
    private void displayFavorites() {
        List<Song> favorites = model.getFavoriteSongs();
        if (favorites.isEmpty()) {
            System.out.println("\nYou have no favorite songs yet.");
        } else {
            System.out.println("\n=== Your Favorites ===");
            for (Song song : favorites) {
                printSongWithRating(song);
            }
        }
    }

    /**
     * AI generated!
     * Displays all albums in the library.
     */
    private void displayLibraryAlbums() {
        Set<Album> albums = model.getAlbumLibrary();
        if (albums.isEmpty()) {
            System.out.println("\nYour library has no albums yet.");
        } else {
            System.out.println("\n=== Your Albums ===");
            for (Album album : albums) {
                System.out.printf("- %s (%d) by %s\n",
                    album.getTitle(), album.getYear(), album.getArtist());
            }
        }
    }

    /**
     * AI generated!
     * Displays all artists in the library.
     */
    private void displayLibraryArtists() {
        List<String> artists = model.getArtists();
        if (artists.isEmpty()) {
            System.out.println("\nYour library has no artists yet.");
        } else {
            System.out.println("\n=== Your Artists ===");
            for (String artist : artists) {
                System.out.println("- " + artist);
            }
        }
    }

    // ================== PLAYLIST MANAGEMENT ================== //

    /**
     * AI generated!
     * Handles playlist menu operations.
     */
    private void handlePlaylistMenu() {
        while (true) {
            System.out.println("\n=== Playlist Management ===");
            System.out.println("1. Create Playlist");
            System.out.println("2. Add Song to Playlist");
            System.out.println("3. Remove Song from Playlist");
            System.out.println("4. View Playlists");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        createPlaylist();
                        break;
                    case 2:
                        addSongToPlaylist();
                        break;
                    case 3:
                        removeSongFromPlaylist();
                        break;
                    case 4:
                        displayPlaylists();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Creates a new playlist.
     */
    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String name = getUserInput();
        model.createPlaylist(name);
        System.out.println("Playlist created!");
    }

    /**
     * Adds a song to a playlist.
     */
    private void addSongToPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = getUserInput();
        System.out.print("Enter song title: ");
        String songTitle = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Playlist playlist = model.getPlaylistByName(playlistName);
        // Searching for the song by title and artist
        Song song = model.searchSongByArtistAndTitle(artist, songTitle);

        // Checking if both the playlist and song exist
        if (playlist != null && song != null) {
            playlist.addSong(song);
            System.out.println("Song added to playlist!");
        } else {
            System.out.println("Playlist or song not found.");
        }
    }

    /**
     * Removes a song from a playlist.
     */
    private void removeSongFromPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = getUserInput();
        System.out.print("Enter song title: ");
        String songTitle = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Playlist playlist = model.getPlaylistByName(playlistName);
        // Searching for the song by title and artist
        Song song = model.searchSongByArtistAndTitle(artist, songTitle);

        // Checking if both the playlist and song exist
        if (playlist != null && song != null) {
            playlist.removeSong(song);
            System.out.println("Song removed from playlist!");
        } else {
            System.out.println("Playlist or song not found.");
        }
    }

    /**
     * AI generated!
     * Displays all playlists in the library.
     */
    private void displayPlaylists() {
        List<Playlist> playlists = model.getPlaylists();
        // Checking if there are no playlists and notifying the user
        if (playlists.isEmpty()) {
            System.out.println("\nYou have no playlists yet.");
        } else {
            System.out.println("\n=== Your Playlists ===");
            // Iterating through each playlist
            for (Playlist playlist : playlists) {
                System.out.printf("%s (%d songs):\n", playlist.getName(), playlist.getSongs().size());
                for (Song song : playlist.getSongs()) {
                    String ratingStars = "";
                    if (song.getRating() > 0) {
                        ratingStars = " " + getRatingStars(song.getRating());
                    }
                    System.out.printf(" - %s by %s%s\n",
                        song.getTitle(), song.getArtist(), ratingStars);
                }
            }
        }
    }

    // ================== RATING SYSTEM ================== //

    /**
     * Handles song rating operations.
     */
    private void handleRatingMenu() {
        System.out.print("\nEnter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        // Search for the song in the library
        Song song = model.searchSongByArtistAndTitle(artist, title);
        if (song == null) {
            System.out.println("Song not found in your library.");
            return;
        }

        System.out.print("Enter rating (1-5): ");
        try {
            int rating = Integer.parseInt(getUserInput());
            // Validating the rating input
            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
                return;
            }
            // Updating the song's rating
            model.rateSong(song, rating);
            if (rating == 5) {
                System.out.println("★ Favorite added!");
            } else {
                System.out.println("Rating updated!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating. Must be a number between 1 and 5.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ================== HELPER METHODS ================== //

    /**
     * Gets user input from the console.
     * @return Trimmed user input string.
     */
    public String getUserInput() {
        return scanner.nextLine().trim();
    }

    /**
     * AI generated!
     * Displays search results in appropriate format.
     * @param results The results to display (List or single object).
     */
    public void displaySearchResults(Object results) {
        if (results instanceof List) {
        	// Casting results to a generic List
            List<?> items = (List<?>) results;
            if (items.isEmpty()) {
                System.out.println("No results found.");
                return;
            }

            // Handling song results
            if (!items.isEmpty() && items.get(0) instanceof Song) {
                System.out.println("\n=== Songs ===");
                for (Object item : items) {
                    Song song = (Song) item;
                    String ratingStars = "";
                    if (song.getRating() > 0) {
                        ratingStars = " " + getRatingStars(song.getRating());
                    }
                    System.out.printf("- %s by %s (Album: %s)%s\n",
                        song.getTitle(), song.getArtist(), song.getAlbum().getTitle(), ratingStars);
                }
            }
            // Handling album results
            else if (!items.isEmpty() && items.get(0) instanceof Album) {
                System.out.println("\n=== Albums ===");
                for (Object item : items) {
                	// Casting each item to an Album
                    Album album = (Album) item;
                    System.out.printf("%s (%d) - %s [%s]\nSongs:\n",
                        album.getTitle(), album.getYear(), album.getArtist(), album.getGenre());
                    // Iterating through the songs in the album and printing their details
                    for (Song song : album.getSongs()) {
                        String ratingStars = "";
                        if (song.getRating() > 0) {
                            ratingStars = " " + getRatingStars(song.getRating());
                        }
                        System.out.printf(" - %s%s\n", song.getTitle(), ratingStars);
                    }
                }
            }
        }
    }

    /**
     * AI generated (design of the stars)
     * Generates star rating string.
     * @param rating The numerical rating (1-5).
     * @return String of star characters representing the rating.
     */
    private String getRatingStars(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    /**
     * Prints song details with rating stars.
     * @param song The song to display.
     */
    private void printSongWithRating(Song song) {
        String ratingStars = "";
        if (song.getRating() > 0) {
            ratingStars = " " + getRatingStars(song.getRating());
        }
        // Printing  song details in the specified format
        System.out.printf("- %s by %s (Album: %s)%s\n",
            song.getTitle(), song.getArtist(), song.getAlbum().getTitle(), ratingStars);
    }
}