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
    }



    /**
     * Returns all system-generated playlists.
     * 
     * @return A list of system-generated playlists.
     */
    public List<Playlist> getAutoPlaylists() {
        return new ArrayList<>(autoPlaylists.values());
    }
    
    /**
     * Returns a map of auto playlists with their names and sizes.
     * This is used by the view to display the playlists.
     * 
     * @return A map where the key is the playlist name and the value is the number of songs.
     */
    public Map<String, Integer> getAutoPlaylistInfo() {
        Map<String, Integer> playlistInfo = new HashMap<>();
        for (Map.Entry<String, Playlist> entry : autoPlaylists.entrySet()) {
            playlistInfo.put(entry.getKey(), entry.getValue().getSongs().size());
        }
        return playlistInfo;
    }
}
