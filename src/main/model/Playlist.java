package main.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
	private final String name;
    private final List<Song> songs;


    /* 
     * Constructor: Creates a new playlist with the specified name.
     * Params: name (String) - Name of the playlist
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
        if (song != null) {
            songs.add(song);
        }
    }

    /* 
     * Removes a song from the playlist.
     * Params: song (Song) - Song to remove
     * Output: void
     */
    public void removeSong(Song song) {
        songs.remove(song);
    }

    /* 
     * Returns all songs in the playlist in their original order.
     * Params: None
     * Output: List<Song> - Unmodifiable list of songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs); 
    }

    /* 
     * Returns the name of the playlist.
     * Params: None
     * Output: String - Playlist name
     */
    public String getName() {
        return name;
    }

    /* 
     * Checks if the playlist contains a specific song.
     * Params: song (Song) - Song to check
     * Output: boolean - True if the song exists in the playlist
     */
    public boolean containsSong(Song song) {
        return songs.contains(song);
    }
}