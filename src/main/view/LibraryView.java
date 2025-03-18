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
    private final UserManager userManager; 
    private User currentUser; 

    /**
     * Constructs a LibraryView instance.
     * 
     * @param model The LibraryModel to interact with.
     */
    public LibraryView(LibraryModel model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
        this.userManager = new UserManager(); // Initialize UserManager
        this.currentUser = null; // No user logged in initially
    }

    // ================== USER REGISTRATION & LOGIN ================== //

    /**
     * Handles user registration.
     */
    private void handleRegister() {
        System.out.print("Enter username: ");
        String username = getUserInput();
        System.out.print("Enter password: ");
        String password = getUserInput();

        try {
            userManager.registerUser(username, password);
            System.out.println("User registered successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles user login.
     */
    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = getUserInput();
        System.out.print("Enter password: ");
        String password = getUserInput();

        try {
            currentUser = userManager.loginUser(username, password);
            System.out.println("Login successful!");
            promptForCommand(); // Proceed to the main Music Library Manager menu
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ================== MAIN MENU ================== //

    /**
     * Displays the main menu options.
     */
    public void displayMainMenu() {
        if (currentUser == null) {
            // Show registration/login menu if no user is logged in
            System.out.println("\n=== Welcome to the Music Library Manager ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
        } else {
            // Show the regular Music Library Manager menu if a user is logged in
            System.out.println("\n=== Music Library Manager ===");
            System.out.println("1. Search Music Store");
            System.out.println("2. My Library");
            System.out.println("3. Playlists");
            System.out.println("4. Manage Ratings & Favorites");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
        }
    }

    /**
     * Handles user input for the main menu.
     */
    public void promptForCommand() {
        while (true) {
            displayMainMenu();
            String input = getUserInput();

            try {
                int choice = Integer.parseInt(input);
                if (currentUser == null) {
                    // Handle registration/login menu
                    switch (choice) {
                        case 1:
                            handleRegister(); // Register a new user
                            break;
                        case 2:
                            handleLogin(); // Login an existing user
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid choice. Try again.");
                    }
                } else {
                    // Handle the regular Music Library Manager menu
                    switch (choice) {
                        case 1:
                            handleStoreSearch(); // Navigate to the store search functionality
                            break;
                        case 2:
                            handleLibraryMenu(); // Navigate to the library menu
                            break;
                        case 3:
                            handlePlaylistMenu(); // Navigate to the playlist menu
                            break;
                        case 4:
                            handleRatingMenu(); // Navigate to the song rating menu
                            break;
                        case 5:
                            userManager.saveUserLibrary(currentUser); // Add this line to save playback data
                            currentUser = null;
                            System.out.println("Logged out successfully!");
                            break;
                        default:
                            System.out.println("Invalid choice. Try again.");
                    }
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
        	// Display the search menu options
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
                        handleSongSearchByTitle(); // Search for a song by its title
                        break;
                    case 2:
                        handleSongSearchByArtist(); // Search for songs by an artist
                        break;
                    case 3:
                        handleAlbumSearchByTitle(); // Search for an album by its title
                        break;
                    case 4:
                        handleAlbumSearchByArtist(); // Search for albums by an artist
                        break;
                    case 5:
                        handleAddSong(); // Add a song to the library
                        break;
                    case 6:
                        handleAddAlbum(); // Add an album to the library
                        break;
                    case 7:
                        return; // Exit the search menu and return to the main menu
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
        while (true) {
            System.out.println("\n=== My Library ===");
            System.out.println("1. View All Songs");
            System.out.println("2. View All Albums");
            System.out.println("3. View All Artists");
            System.out.println("4. Search Library");
            System.out.println("5. View Playlists");
            System.out.println("6. View Favorites");
            System.out.println("7. Remove Song from Library");
            System.out.println("8. Remove Album from Library");
            System.out.println("9. Play a Song");
            System.out.println("10. View Recently Played Songs");
            System.out.println("11. View Most Played Songs");
            System.out.println("12. View Songs by Rating"); 
            System.out.println("13. Shuffle Songs"); 
            System.out.println("14. Return to Main Menu"); 
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        displayLibrarySongs(); // Show all songs (sorted by title and artist)
                        break;
                    case 2:
                        displayLibraryAlbums(); // Show all albums (sorted by title)
                        break;
                    case 3:
                        displayLibraryArtists(); // Show all artists (sorted alphabetically)
                        break;
                    case 4:
                        handleLibrarySearch(); // Handle searching
                        break;
                    case 5:
                        displayPlaylists(); // Show all playlists
                        break;
                    case 6:
                        displayFavorites(); // Show favorite songs
                        break;
                    case 7:
                        handleRemoveSong(); // Remove a song from the library
                        break;
                    case 8:
                        handleRemoveAlbum(); // Remove an album from the library
                        break;
                    case 9:
                        handlePlaySong(); // Play a song
                        break;
                    case 10:
                        displayRecentlyPlayedSongs(); // View recently played songs
                        break;
                    case 11:
                        displayMostPlayedSongs(); // View most played songs
                        break;
                    case 12:
                        displaySongsByRating(); //  View songs sorted by rating
                        break;
                    case 13:
                    	handleShuffleSongs(); //  Shuffle songs in the library
                        break;
                    case 14:
                        return; // Exit to main menu
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void handleShuffleSongs() {
        System.out.println("\n=== Shuffled Songs ===");
        List<Song> shuffledSongs = model.getShuffledSongs();
        if (shuffledSongs.isEmpty()) {
            System.out.println("Your library has no songs yet.");
        } else {
            for (Song song : shuffledSongs) {
                printSongWithRating(song);
            }
        }
    }
    
 // ================== PLAYBACK FUNCTIONALITY ================== //

    /**
     * Handles playing a song.
     */
    private void handlePlaySong() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        // Search for the song in the library
        Song song = model.searchSongByArtistAndTitle(artist, title);
        if (song == null) {
            System.out.println("Song not found in your library.");
            return;
        }

        // Simulate playing the song
        currentUser.getLibrary().getPlaybackTracker().playSong(song);
        System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
    }

    /**
     * Displays the 10 most recently played songs.
     */
    private void displayRecentlyPlayedSongs() {
        List<Song> recentlyPlayed = currentUser.getLibrary().getPlaybackTracker().getRecentlyPlayed();
        if (recentlyPlayed.isEmpty()) {
            System.out.println("\nYou haven't played any songs yet.");
        } else {
            System.out.println("\n=== Recently Played Songs ===");
            for (Song song : recentlyPlayed) {
                printSongWithRating(song);
            }
        }
    }

    /**
     * Displays the 10 most frequently played songs.
     */
    private void displayMostPlayedSongs() {
        List<Song> mostPlayed = currentUser.getLibrary().getPlaybackTracker().getMostPlayed();
        if (mostPlayed.isEmpty()) {
            System.out.println("\nYou haven't played any songs yet.");
        } else {
            System.out.println("\n=== Most Played Songs ===");
            for (Song song : mostPlayed) {
                printSongWithRating(song);
            }
        }
    }
    
    // ================== LIBRARY SEARCH MENU ================== //
    /**
     * AI generated (design)!
     * Handles library search operations.
     */
    private void handleLibrarySearch() {
        while (true) {
            System.out.println("\n=== Search Library ===");
            System.out.println("1. Search Songs by Title");
            System.out.println("2. Search Songs by Artist");
            System.out.println("3. Search Albums by Title");
            System.out.println("4. Search Albums by Artist");
            System.out.println("5. Return to Library Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        handleLibrarySongByTitle(); // Search for songs by title
                        break;
                    case 2:
                        handleLibrarySongByArtist(); // Search for songs by artist
                        break;
                    case 3:
                        handleLibraryAlbumByTitle(); // Search for albums by title
                        break;
                    case 4:
                        handleLibraryAlbumByArtist(); // Search for albums by artist
                        break;
                    case 5:
                        return; // Exit the search menu and return to the library menu
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    
    // ================== LIBRARY SEARCH HANDLERS ================== //
    
    
    /**
     * Handles song search by title in the library (no sorting).
     */
    private void handleLibrarySongByTitle() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        List<Song> results = model.searchSongByTitle(title);
        displaySearchResults(results); 
    }

    /**
     * Handles song search by artist in the library (no sorting).
     */
    private void handleLibrarySongByArtist() {
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        List<Song> results = model.searchSongByArtist(artist);
        displaySearchResults(results); 
    }
    
    
    /**
     * Handles album search by title in the library.
     */
    private void handleLibraryAlbumByTitle() {
        // Prompt to user 
        System.out.print("Enter album title: ");
        String title = getUserInput();

        // Search for the album in the library by title
        Album album = model.searchAlbumByTitle(title);

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
     * Handles album search by artist in the library.
     */
    private void handleLibraryAlbumByArtist() {
        // Prompt to user 
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        
        // Search for albums in the library by artist
        List<Album> results = model.searchAlbumByArtist(artist);
        displaySearchResults(results);
    }
    

    // ================== REMOVE SONGS & ALBUMS FROM LIBRARY ================== //

    
    /**
     * Handles removing a song from the library.
     */
    private void handleRemoveSong() {
        // Prompt user to enter the song title
        System.out.print("Enter song title: ");
        String title = getUserInput();
        
        // Prompt user to enter the artist's name
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Song song = model.searchSongByArtistAndTitle(artist, title);
        
        // If the song exists, remove it from the library
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
        // Prompt user to enter the album title
        System.out.print("Enter album title: ");
        String title = getUserInput();
        
        // Prompt user to enter the artist's name
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Album album = model.searchAlbumByTitle(title);
        
        // Check if the album exists and matches the artist's name before removing
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
        List<Song> songs = model.getSongsSortedByTitleAndArtist();
        
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
        
        // Check if there are any favorite songs
        if (favorites.isEmpty()) {
            System.out.println("\nYou have no favorite songs yet.");
        } else {
            System.out.println("\n=== Your Favorites ===");
            // Loop through each favorite song and print its details
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
        List<Album> albums = model.getAlbumsSortedByTitle();
        
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
        List<String> artists = model.getArtistsSorted();
        
        if (artists.isEmpty()) {
            System.out.println("\nYour library has no artists yet.");
        } else {
            System.out.println("\n=== Your Artists ===");
            for (String artist : artists) {
                System.out.println("- " + artist);
            }
        }
    }
    
    
    /**
     * Displays all songs in the library sorted by rating (ascending).
     */
    private void displaySongsByRating() {
        List<Song> songs = model.getSongsSortedByRating();
        
        if (songs.isEmpty()) {
            System.out.println("\nYour library has no songs yet.");
        } else {
            System.out.println("\n=== Songs by Rating ===");
            for (Song song : songs) {
                printSongWithRating(song);
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
            // Display the playlist management menu options
            System.out.println("\n=== Playlist Management ===");
            System.out.println("1. Create Playlist");
            System.out.println("2. Add Song to Playlist");
            System.out.println("3. Remove Song from Playlist");
            System.out.println("4. View Playlists");
            System.out.println("5. Shuffle Playlist"); 
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        createPlaylist(); // Create a new playlist
                        break;
                    case 2:
                        addSongToPlaylist(); // Add a song to an existing playlist
                        break;
                    case 3:
                        removeSongFromPlaylist(); // Remove a song from a playlist
                        break;
                    case 4:
                        displayPlaylists(); // Display all available playlists
                        break;
                    case 5:
                        handleShufflePlaylist();
                    case 6:
                        return; // Exit the menu and return to the main menu
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private void handleShufflePlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = getUserInput();
        List<Song> shuffledSongs = model.getShuffledPlaylistSongs(playlistName);
        
        if (shuffledSongs.isEmpty()) {
            System.out.println("Playlist not found or is empty.");
        } else {
            System.out.println("\n=== Shuffled Playlist: " + playlistName + " ===");
            for (Song song : shuffledSongs) {
                printSongWithRating(song);
            }
        }
    }

    /**
     * Creates a new playlist.
     */
    private void createPlaylist() {
        // Prompt user to enter the name for the new playlist
        System.out.print("Enter playlist name: ");
        String name = getUserInput();
        
        // Create the playlist using the model
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
        Song song = model.searchSongByArtistAndTitle(artist, songTitle);

        if (playlist != null && song != null) {
            // Check if the song is actually in the playlist
            if (playlist.getSongs().contains(song)) {
                playlist.removeSong(song);
                System.out.println("Song removed from playlist!");
            } else {
                System.out.println("Song not found in the playlist.");
            }
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
                
                // Iterating through songs in playlist
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
     * Handles rating and favorite management operations.
     */
    private void handleRatingMenu() {
        while (true) {
            System.out.println("\n=== Manage Ratings & Favorites ===");
            System.out.println("1. Rate a Song");
            System.out.println("2. Mark Song as Favorite");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1:
                        handleSongRating();  // Existing rating functionality
                        break;
                    case 2:
                        handleMarkAsFavorite();  // New favorite marking functionality
                        break;
                    case 3:
                        return;  // Exit to main menu
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    /**
     * Handles marking a song as a favorite.
     */
    private void handleMarkAsFavorite() {
        System.out.print("\nEnter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Song song = model.searchSongByArtistAndTitle(artist, title);
        if (song == null) {
            System.out.println("Song not found in your library.");
            return;
        }

        model.markAsFavorite(song);
        System.out.println("★ Song marked as favorite!");
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
     * Handles song rating operations (renamed from handleRatingMenu).
     */
    private void handleSongRating() {
        System.out.print("\nEnter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Song song = model.searchSongByArtistAndTitle(artist, title);
        if (song == null) {
            System.out.println("Song not found in your library.");
            return;
        }

        System.out.print("Enter rating (1-5): ");
        try {
            int rating = Integer.parseInt(getUserInput());
            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
                return;
            }
            model.rateSong(song, rating);
            System.out.println(rating == 5 ? "★ Favorite added!" : "Rating updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating. Must be a number between 1 and 5.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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