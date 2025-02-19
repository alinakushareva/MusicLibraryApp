package main.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.model.Album;
import main.model.Song;

public class MusicStore {
	private final Map<String, Album> albumsByTitle = new HashMap<>();
    private final Map<String, List<Album>> albumsByArtist = new HashMap<>();
    private final Map<String, List<Song>> songsByTitle = new HashMap<>();
    private final Set<String> genres = new HashSet<>();
    private final Set<String> artists = new HashSet<>();
    private final String basePath;
    
    /* 
     * Constructor: Initializes the music store and loads album data.
     * Params: filePath (String) - Path to the directory containing album files
     * Output: None (constructor)
     */
    public MusicStore(String filePath) {
        this.basePath = filePath;
        loadAlbums();
    }
    
    /* 
     * Loads album data from the albums.txt file and processes each album.
     * Params: None
     * Output: void
     */
    private void loadAlbums() {
    	
    	// TODO here!!
    }
    
    /* 
     * Processes an individual album file and adds its data to the store.
     * Params: albumTitle (String), artist (String)
     * Output: void
     */
    private void processAlbumFile(String albumTitle, String artist) {
    	
    	// TODO here!!

    }
    
    /* 
     * Retrieves an album by its exact title.
     * Params: title (String) - Album title to search for
     * Output: Album (or null if not found)
     */
    public Album getAlbumByTitle(String title) {
        return albumsByTitle.get(title);
    }
    
    /* 
     * Returns all albums by a specific artist
     * Params: artist (String) - Artist name to search for
     * Output: List<Album> - List of albums (empty if none found)
     */
    public List<Album> getAlbumsByArtist(String artist) {
        List<Album> result = new ArrayList<>();
        if (albumsByArtist.containsKey(artist)) {
            result.addAll(albumsByArtist.get(artist));
        }
        return result;
    }
    
    /* 
     * Finds a specific album by artist and title combination
     * Params: artist (String) - Artist name to search for
     *         title (String) - Album title to search for
     * Output: Album - Found album or null
     */
    public Album getAlbumByArtistAndTitle(String artist, String title) {
        if (!albumsByArtist.containsKey(artist)) return null;
        
        for (Album album : albumsByArtist.get(artist)) {
            if (album.getTitle().equals(title)) {
                return album;
            }
        }
        return null;
    }
    
    /* 
     * Returns all albums in a specific genre
     * Params: genre (String) - Genre to filter by
     * Output: List<Album> - List of albums (empty if none found)
     */
    public List<Album> getAlbumsByGenre(String genre) {
        List<Album> result = new ArrayList<>();
        for (Album album : albumsByTitle.values()) {
            if (album.getGenre().equals(genre)) {
                result.add(album);
            }
        }
        return result;
    }
    
    /* 
     * Retrieves all songs with a specific title
     * Params: title (String) - Song title to search for
     * Output: List<Song> - List of songs (empty if none found)
     */
    public List<Song> getSongsByTitle(String title) {
        List<Song> result = new ArrayList<>();
        if (songsByTitle.containsKey(title)) {
            result.addAll(songsByTitle.get(title));
        }
        return result;
    }
    
    /* 
     * Returns all songs by a specific artist
     * Params: artist (String) - Artist name to filter by
     * Output: List<Song> - List of songs (empty if none found)
     */
    public List<Song> getSongsByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        for (List<Song> songList : songsByTitle.values()) {
            for (Song song : songList) {
                if (song.getArtist().equals(artist)) {
                    result.add(song);
                }
            }
        }
        return result;
    }
    
    /* 
     * Finds a specific song by artist and title combination
     * Params: artist (String) - Artist name to search for
     *         title (String) - Song title to search for
     * Output: Song - Found song or null
     */
    public Song getSongByArtistAndTitle(String artist, String title) {
        List<Song> songs = getSongsByTitle(title);
        for (Song song : songs) {
            if (song.getArtist().equals(artist)) {
                return song;
            }
        }
        return null;
    }
    
    /* 
     * Retrieves all songs in a specific genre.
     * Params: genre (String) - Genre to filter by (e.g., "Pop", "Rock")
     * Output: List<Song> - List of songs in the specified genre (empty if none found)
     */
    public List<Song> getSongsByGenre(String genre) {
        List<Song> result = new ArrayList<>();
        for (Album album : albumsByTitle.values()) {
            if (album.getGenre().equalsIgnoreCase(genre)) {
                result.addAll(album.getSongs());
            }
        }
        return result;
    }
    
    /* 
     * Checks if an album exists in the music store
     * Params: album (Album) - Album object to verify
     * Output: boolean - True if album exists, false otherwise
     */
    public boolean albumExists(String title, String artist) {
        if (!albumsByTitle.containsKey(title)) {
            return false;
        }
        Album album = albumsByTitle.get(title);
        return album.getArtist().equals(artist);
    }
}