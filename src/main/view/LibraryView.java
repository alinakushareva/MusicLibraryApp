package main.view;

import main.model.*;
import main.database.MusicStore;
import java.util.*;

public class LibraryView {
    private final LibraryModel model;
    private final Scanner scanner;

    public LibraryView(LibraryModel model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    // ================== MAIN MENU ================== //

    public void displayMainMenu() {
        System.out.println("\n=== Music Library Manager ===");
        System.out.println("1. Search Music Store");
        System.out.println("2. My Library");
        System.out.println("3. Playlists");
        System.out.println("4. Rate Songs & Favorites");
        System.out.println("5. Exit");
        System.out.print("Enter choice: ");
    }

    public void promptForCommand() {
        while (true) {
            displayMainMenu();
            String input = getUserInput();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> handleStoreSearch();
                    case 2 -> handleLibraryMenu();
                    case 3 -> handlePlaylistMenu();
                    case 4 -> handleRatingMenu();
                    case 5 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // ================== STORE SEARCH ================== //

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
                    case 1 -> handleSongSearchByTitle();
                    case 2 -> handleSongSearchByArtist();
                    case 3 -> handleAlbumSearchByTitle();
                    case 4 -> handleAlbumSearchByArtist();
                    case 5 -> handleAddSong();
                    case 6 -> handleAddAlbum();
                    case 7 -> { return; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void handleSongSearchByTitle() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        List<Song> results = model.searchStoreSongByTitle(title);
        displaySearchResults(results);
    }

    private void handleSongSearchByArtist() {
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        List<Song> results = model.searchStoreSongByArtist(artist);
        displaySearchResults(results);
    }

    private void handleAlbumSearchByTitle() {
        System.out.print("Enter album title: ");
        String title = getUserInput();
        Album album = model.searchStoreAlbumByTitle(title);
        displaySearchResults(album != null ? List.of(album) : Collections.emptyList());
    }

    private void handleAlbumSearchByArtist() {
        System.out.print("Enter artist: ");
        String artist = getUserInput();
        List<Album> results = model.searchStoreAlbumByArtist(artist);
        displaySearchResults(results);
    }

    // ================== ADD SONGS & ALBUMS TO LIBRARY ================== //

    private void handleAddSong() {
        System.out.print("Enter song title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Song song = model.getMusicStore().getSongByArtistAndTitle(artist, title);
        if (song != null) {
            model.addSong(song);
            System.out.println("Song added to library!");
        } else {
            System.out.println("Song not found in MusicStore.");
        }
    }

    private void handleAddAlbum() {
        System.out.print("Enter album title: ");
        String title = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Album album = model.getMusicStore().getAlbumByArtistAndTitle(artist, title);
        if (album != null) {
            model.addAlbum(album);
            System.out.println("Album added to library!");
        } else {
            System.out.println("Album not found in MusicStore.");
        }
    }

    // ================== MY LIBRARY ================== //

    private void handleLibraryMenu() {
        while (true) {
            System.out.println("\n=== My Library ===");
            System.out.println("1. View All Songs");
            System.out.println("2. View All Albums");
            System.out.println("3. View All Artists");
            System.out.println("4. View Playlists");
            System.out.println("5. View Favorites");
            System.out.println("6. Search in My Library");
            System.out.println("7. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1 -> displayLibrarySongs();
                    case 2 -> displayLibraryAlbums();
                    case 3 -> displayLibraryArtists();
                    case 4 -> displayPlaylists();
                    case 5 -> displayFavorites();
                    case 6 -> handleLibrarySearch();
                    case 7 -> { return; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void displayLibrarySongs() {
        Set<Song> songs = model.getSongLibrary();
        if (songs.isEmpty()) {
            System.out.println("\nYour library has no songs yet.");
        } else {
            System.out.println("\n=== Your Songs ===");
            songs.forEach(song -> {
                String ratingStars = (song.getRating() > 0) 
                    ? " " + getRatingStars(song.getRating()) 
                    : ""; // No rating
                System.out.printf("- %s by %s (Album: %s)%s\n",
                    song.getTitle(), song.getArtist(), song.getAlbum().getTitle(), ratingStars);
            });
        }
    }

    private void displayLibraryAlbums() {
        Set<Album> albums = model.getAlbumLibrary();
        if (albums.isEmpty()) {
            System.out.println("\nYour library has no albums yet.");
        } else {
            System.out.println("\n=== Your Albums ===");
            albums.forEach(album -> System.out.printf("- %s (%d) by %s\n",
                album.getTitle(), album.getYear(), album.getArtist()));
        }
    }

    private void displayLibraryArtists() {
        List<String> artists = model.getArtists();
        if (artists.isEmpty()) {
            System.out.println("\nYour library has no artists yet.");
        } else {
            System.out.println("\n=== Your Artists ===");
            artists.forEach(artist -> System.out.println("- " + artist));
        }
    }

    private void displayFavorites() {
        List<Song> favorites = model.getFavoriteSongs();
        if (favorites.isEmpty()) {
            System.out.println("\nYou have no favorite songs yet.");
        } else {
            System.out.println("\n=== Your Favorites ===");
            favorites.forEach(song -> {
                String ratingStars = (song.getRating() > 0) 
                    ? " " + getRatingStars(song.getRating()) 
                    : ""; // No rating
                System.out.printf("- %s by %s (Album: %s)%s\n",
                    song.getTitle(), song.getArtist(), song.getAlbum().getTitle(), ratingStars);
            });
        }
    }

    private void handleLibrarySearch() {
        System.out.println("\n=== Search My Library ===");
        System.out.println("1. Search Songs by Title");
        System.out.println("2. Search Songs by Artist");
        System.out.println("3. Search Albums by Title");
        System.out.println("4. Search Albums by Artist");
        System.out.print("Enter choice: ");

        try {
            int choice = Integer.parseInt(getUserInput());
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter song title: ");
                    String title = getUserInput();
                    List<Song> results = model.searchSongByTitle(title);
                    displaySearchResults(results);
                }
                case 2 -> {
                    System.out.print("Enter artist: ");
                    String artist = getUserInput();
                    List<Song> results = model.searchSongByArtist(artist);
                    displaySearchResults(results);
                }
                case 3 -> {
                    System.out.print("Enter album title: ");
                    String albumTitle = getUserInput();
                    Album album = model.searchAlbumByTitle(albumTitle);
                    displaySearchResults(album != null ? List.of(album) : Collections.emptyList());
                }
                case 4 -> {
                    System.out.print("Enter artist: ");
                    String artist = getUserInput();
                    List<Album> results = model.searchAlbumByArtist(artist);
                    displaySearchResults(results);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // ================== PLAYLIST MANAGEMENT ================== //

    private void handlePlaylistMenu() {
        while (true) {
            System.out.println("\n=== Playlist Management ===");
            System.out.println("1. Create Playlist");
            System.out.println("2. Add Song to Playlist");
            System.out.println("3. View Playlists");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(getUserInput());
                switch (choice) {
                    case 1 -> createPlaylist();
                    case 2 -> addSongToPlaylist();
                    case 3 -> displayPlaylists();
                    case 4 -> { return; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String name = getUserInput();
        model.createPlaylist(name);
        System.out.println("Playlist created!");
    }

    private void addSongToPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = getUserInput();
        System.out.print("Enter song title: ");
        String songTitle = getUserInput();
        System.out.print("Enter artist: ");
        String artist = getUserInput();

        Playlist playlist = model.getPlaylistByName(playlistName);
        Song song = model.searchSongByArtistAndTitle(artist, songTitle);

        if (playlist != null && song != null) {
            playlist.addSong(song);
            System.out.println("Song added to playlist!");
        } else {
            System.out.println("Playlist or song not found.");
        }
    }

    private void displayPlaylists() {
        List<Playlist> playlists = model.getPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("\nYou have no playlists yet.");
        } else {
            System.out.println("\n=== Your Playlists ===");
            playlists.forEach(playlist -> {
                System.out.printf("%s (%d songs):\n", playlist.getName(), playlist.getSongs().size());
                playlist.getSongs().forEach(song -> {
                    String ratingStars = (song.getRating() > 0) 
                        ? " " + getRatingStars(song.getRating()) 
                        : ""; // No rating
                    System.out.printf(" - %s by %s%s\n",
                        song.getTitle(), song.getArtist(), ratingStars);
                });
            });
        }
    }

    // ================== RATING & FAVORITES ================== //

    private void handleRatingMenu() {
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
            model.rateSong(song, rating);
            System.out.println(rating == 5 ? "★ Favorite added!" : "Rating updated!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating. Must be a number between 1 and 5.");
        }
    }

    // ================== HELPER METHODS ================== //

    public String getUserInput() {
        return scanner.nextLine().trim();
    }

    public void displaySearchResults(Object results) {
        if (results instanceof List) {
            List<?> items = (List<?>) results;
            if (items.isEmpty()) {
                System.out.println("No results found.");
                return;
            }

            if (items.get(0) instanceof Song) {
                System.out.println("\n=== Songs ===");
                for (Object item : items) {
                    Song song = (Song) item;
                    String ratingStars = (song.getRating() > 0) 
                        ? " " + getRatingStars(song.getRating()) 
                        : ""; // No rating
                    System.out.printf("- %s by %s (Album: %s)%s\n",
                        song.getTitle(), song.getArtist(), song.getAlbum().getTitle(), ratingStars);
                }
            } else if (items.get(0) instanceof Album) {
                System.out.println("\n=== Albums ===");
                for (Object item : items) {
                    Album album = (Album) item;
                    System.out.printf("%s (%d) - %s [%s]\nSongs:\n",
                        album.getTitle(), album.getYear(), album.getArtist(), album.getGenre());
                    album.getSongs().forEach(song -> {
                        String ratingStars = (song.getRating() > 0) 
                            ? " " + getRatingStars(song.getRating()) 
                            : ""; // No rating
                        System.out.printf(" - %s%s\n", song.getTitle(), ratingStars);
                    });
                }
            } else if (items.get(0) instanceof Playlist) {
                System.out.println("\n=== Playlists ===");
                for (Object item : items) {
                    Playlist playlist = (Playlist) item;
                    System.out.printf("%s (%d songs):\n",
                        playlist.getName(), playlist.getSongs().size());
                    playlist.getSongs().forEach(song -> {
                        String ratingStars = (song.getRating() > 0) 
                            ? " " + getRatingStars(song.getRating()) 
                            : ""; // No rating
                        System.out.printf(" - %s by %s%s\n", 
                            song.getTitle(), song.getArtist(), ratingStars);
                    });
                }
            }
        } else {
            System.out.println("\nResult: " + results);
        }
    }

    // Helper method to convert rating to stars
    private String getRatingStars(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (i <= rating) {
                stars.append("★"); // Filled star
            } else {
                stars.append("☆"); // Empty star
            }
        }
        return stars.toString();
    }
}