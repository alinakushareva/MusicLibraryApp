/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: Playlist.java
 * Purpose: This class represents a playlist in the music library. It allows users to create playlists,
 *          add or remove songs, and retrieve the list of songs. Playlists are used to organize and
 *          manage custom collections of songs.
 */
package main.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String name;
    private final List<Song> songs;
    
    /* 
     * Constructor: Creates a new playlist with the specified name.
     * Params: name (String) - Name of the playlist
     * @pre title != null
     * Output: None (constructor)
     */
    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }
    
    /* 
     * Adds a song to the end of the playlist.
     * Params: song (Song) - Song to add
     * Output: void
     */
    public void addSong(Song song) {
        if (song != null) songs.add(song);
    }
    
    /* 
     * Removes a song from the playlist.
     * Params: song (Song) - Song to remove
     * Output: void
     */
    public void removeSong(Song song) {
        songs.remove(song);
    }
    
    
    // ================== GETTERS ================== //

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public String getName() {
        return name;
    }
}