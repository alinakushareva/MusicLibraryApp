package main.model;

import main.database.MusicStore;
import main.security.PasswordUtil;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.util.List;

/**
 * Represents an individual user with a unique library.
 * This class handles user authentication and library data persistence.
 */
public class User {
    private String username;
    private String salt;
    private String hashedPassword;
    private LibraryModel library;

    private static final String LIBRARY_DIR = "user_data/";

    /**
     * Constructs a new User instance
     * 
     * @param username The username for the user.
     * @param password The plaintext password for the user.
     * @throws IllegalArgumentException If username or password is null or empty.
     */
    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        this.username = username;
        this.salt = PasswordUtil.generateSalt(); // Generate a random salt
        this.hashedPassword = PasswordUtil.hashPassword(password, this.salt); // Hash the password
        this.library = new LibraryModel(new MusicStore("src/main/albums/"));
    }

    /**
     * Validates a given password against the stored hash.
     * 
     * @param password The plaintext password to validate.
     * @return True if the password matches the stored hash, false otherwise.
     * @throws IllegalArgumentException If password is null or empty.
     */
    public boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        return PasswordUtil.validatePassword(password, this.salt, this.hashedPassword);
    }

    /**
     * Returns the user's personalized music library.
     * 
     * @return The user's LibraryModel instance.
     */
    public LibraryModel getLibrary() {
        return this.library;
    }

    /**
     * Saves the user's library data to a JSON file.
     */
    public void saveLibraryData() {
        // Ensure the directory exists
        File dir = new File(LIBRARY_DIR);
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdirs(); // Create the directory if it doesn't exist
            if (!dirCreated) {
                throw new RuntimeException("Failed to create directory: " + LIBRARY_DIR);
            }
        }

        JSONObject userJson = new JSONObject();
        userJson.put("username", this.username);
        userJson.put("salt", this.salt);
        userJson.put("hashedPassword", this.hashedPassword);

        // Serialize the library to JSON
        JSONObject libraryJson = serializeLibrary();
        userJson.put("library", libraryJson);

        // Construct the file path correctly
        File file = new File(dir, "user_" + this.username + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(userJson.toString(4)); 
        } catch (IOException e) {
            throw new RuntimeException("Error saving user library data", e);
        }
    }

    /**
     * Serializes the library (songs, albums, playlists) into a JSONObject.
     * 
     * @return JSONObject containing library data.
     */
    public JSONObject serializeLibrary() {
        JSONObject libraryJson = new JSONObject();

        // Serialize albums
        JSONArray albumsArray = new JSONArray();
        for (Album album : this.library.getAlbumLibrary()) {
            JSONObject albumJson = new JSONObject();
            albumJson.put("title", album.getTitle());
            albumJson.put("artist", album.getArtist());
            albumJson.put("genre", album.getGenre());
            albumJson.put("year", album.getYear());

            // Serialize songs in the album
            JSONArray songsArray = new JSONArray();
            for (Song song : album.getSongs()) {
                JSONObject songJson = new JSONObject();
                songJson.put("title", song.getTitle());
                songJson.put("artist", song.getArtist());
                songJson.put("rating", song.getRating());
                songJson.put("isFavorite", song.isFavorite());
                songsArray.put(songJson);
            }
            albumJson.put("songs", songsArray);
            albumsArray.put(albumJson);
        }
        libraryJson.put("albums", albumsArray);

        // Serialize playlists
        JSONArray playlistsArray = new JSONArray();
        for (Playlist playlist : this.library.getPlaylists()) {
            JSONObject playlistJson = new JSONObject();
            playlistJson.put("name", playlist.getName());

            // Serialize songs in the playlist
            JSONArray playlistSongs = new JSONArray();
            for (Song song : playlist.getSongs()) {
                playlistSongs.put(song.getTitle()); // Store only song titles for simplicity
            }
            playlistJson.put("songs", playlistSongs);
            playlistsArray.put(playlistJson);
        }
        libraryJson.put("playlists", playlistsArray);

        return libraryJson;
    }

    /**
     * Loads the user's library data from a JSON file.
     */
    public void loadLibraryData() {
        File file = new File(LIBRARY_DIR + "user_" + this.username + ".json");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }

                JSONObject userJson = new JSONObject(jsonData.toString());
                this.username = userJson.getString("username");
                this.salt = userJson.getString("salt");
                this.hashedPassword = userJson.getString("hashedPassword");

                // Deserialize the library from JSON
                JSONObject libraryJson = userJson.getJSONObject("library");
                deserializeLibrary(libraryJson);

            } catch (IOException e) {
                throw new RuntimeException("Error loading user library data", e);
            }
        } else {
            // If the file doesn't exist, initialize an empty library
            this.library = new LibraryModel(new MusicStore("src/main/albums/"));
        }
    }

    /**
     * Deserializes the library from a JSONObject.
     * 
     * @param libraryJson JSONObject containing library data.
     */
    public void deserializeLibrary(JSONObject libraryJson) {
        // Deserialize albums
        if (libraryJson.has("albums")) {
            JSONArray albumsArray = libraryJson.getJSONArray("albums");
            for (int i = 0; i < albumsArray.length(); i++) {
                JSONObject albumJson = albumsArray.getJSONObject(i);
                String title = albumJson.getString("title");
                String artist = albumJson.getString("artist");
                String genre = albumJson.getString("genre");
                int year = albumJson.getInt("year");
                Album album = new Album(title, artist, genre, year);

                // Deserialize songs in the album
                JSONArray songsArray = albumJson.getJSONArray("songs");
                for (int j = 0; j < songsArray.length(); j++) {
                    JSONObject songJson = songsArray.getJSONObject(j);
                    String songTitle = songJson.getString("title");
                    String songArtist = songJson.getString("artist");
                    int rating = songJson.getInt("rating");
                    boolean isFavorite = songJson.getBoolean("isFavorite");

                    Song song = new Song(songTitle, songArtist, album);
                    song.rate(rating);
                    if (isFavorite) song.markAsFavorite();
                    album.addSong(song);
                }
                this.library.addAlbumDirect(album);  // Add the album to the library
            }
        }

        // Deserialize playlists
        if (libraryJson.has("playlists")) {
            JSONArray playlistsArray = libraryJson.getJSONArray("playlists");
            for (int i = 0; i < playlistsArray.length(); i++) {
                JSONObject playlistJson = playlistsArray.getJSONObject(i);
                String name = playlistJson.getString("name");

                // Use createPlaylist to add the playlist to the library
                Playlist playlist = this.library.createPlaylist(name);

                // Deserialize songs in the playlist
                JSONArray playlistSongs = playlistJson.getJSONArray("songs");
                for (int j = 0; j < playlistSongs.length(); j++) {
                    String songTitle = playlistSongs.getString(j);

                    // Use searchSongByTitle to find the song
                    List<Song> matchingSongs = this.library.searchSongByTitle(songTitle);
                    if (!matchingSongs.isEmpty()) {
                        // Add the first matching song to the playlist
                        playlist.addSong(matchingSongs.get(0));
                    }
                }
            }
        }
    }
    
    /**
     * Creates a User object from pre-existing credentials.
     * This method is used when loading user data from a file.
     *
     * @param username The username of the user.
     * @param salt The salt used for password hashing.
     * @param hashedPassword The hashed password.
     * @return A User object with the provided credentials.
     */
    public static User fromCredentials(String username, String salt, String hashedPassword) {
    	// Using a dummy password to avoid exposure of salt and hashing in the user class constructor
        User user = new User(username, "dummyPassword"); 
        user.salt = salt; // Set the salt
        user.hashedPassword = hashedPassword; // Set the hashed password
        return user;
    }

    // ================== GETTERS ================== //

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}