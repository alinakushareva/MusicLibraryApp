package main.model;

import java.util.ArrayList;
import java.util.List;

public class Album {
	private final String title;
    private final String artist;
    private final String genre;
    private final int year;
    private final List<Song> songs = new ArrayList<>();
    
    /* 
     * Constructor: Creates a new Album instance
     * Params: title (String) - Album title
     *         artist (String) - Album artist
     *         genre (String) - Music genre
     *         year (int) - Release year
     * Output: None (constructor)
     */
    public Album(String title, String artist, String genre, int year) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
    }
    
    /* 
     * Adds a song to the album
     * Params: song (Song) - Song to add
     * Output: void
     */
    public void addSong(Song song) {
        songs.add(song);
    }
    
    // Getters
    public String getTitle() { 
    	return title; 
    }
    
    public String getArtist() { 
    	return artist;
    }
    
    public String getGenre() {
    	return genre; 
    }
    
    public int getYear() { 
    	return year; 
    }
    
    public List<Song> getSongs() { 
    	return new ArrayList<>(songs);
    }
}