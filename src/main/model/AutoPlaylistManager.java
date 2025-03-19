/**
 * Name: 
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: AutoPlaylistManager.java
 * Purpose: This class manages system-generated playlists based on the user's library.
 *          It creates and updates playlists for favorite songs and top-rated songs.
 */
package main.model;

import java.util.*;
public class AutoPlaylistManager {
    private final Map<String, Playlist> autoPlaylists;

    /**
     * Constructs an AutoPlaylistManager instance.
     */
    public AutoPlaylistManager() {
        this.autoPlaylists = new HashMap<>();
    }

    /**
     * Updates the system-generated playlists based on the user's library.
     * 
     * @param userLibrary The user's library model.
     */
    public void updateAutoPlaylists(LibraryModel userLibrary) {
        // Clear existing auto playlists
        autoPlaylists.clear();

        // Get all songs from the user's library
        Set<Song> songs = userLibrary.getSongLibrary();

        // Create Favorite Songs playlist
        Playlist favoriteSongsPlaylist = new Playlist("Favorite Songs");
        for (Song song : songs) {
            if (song.isFavorite() || song.getRating() == 5) {
                favoriteSongsPlaylist.addSong(song);
            }
        }
        autoPlaylists.put("Favorite Songs", favoriteSongsPlaylist);

        // Create Top Rated playlist
        Playlist topRatedPlaylist = new Playlist("Top Rated");
        for (Song song : songs) {
            if (song.getRating() >= 4) {
                topRatedPlaylist.addSong(song);
            }
        }
        autoPlaylists.put("Top Rated", topRatedPlaylist);

        // Log auto playlists
        System.out.println("\n=== Auto Playlists ===");
        for (Map.Entry<String, Playlist> entry : autoPlaylists.entrySet()) {
            System.out.printf("- %s: %d songs\n", entry.getKey(), entry.getValue().getSongs().size());
        }
    }


    /**
     * Returns all system-generated playlists.
     * 
     * @return A list of system-generated playlists.
     */
    public List<Playlist> getAutoPlaylists() {
        return new ArrayList<>(autoPlaylists.values());
    }
}
