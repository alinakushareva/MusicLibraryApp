/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: LibraryModel.java
 * Purpose: This class serves as the model for the music library, managing the user's collection of songs,
 *          albums, and playlists. It provides methods to add, remove, and search for music, as well as
 *          handle ratings and favorites. The model interacts with the MusicStore to ensure data consistency.
 */
package main.model;

import main.database.MusicStore;
import java.util.*;

public class LibraryModel {
    private final Set<Song> songLibrary = new HashSet<>();
    private final Set<Album> albumLibrary = new HashSet<>();
    private final List<Playlist> playlists = new ArrayList<>();
    private final MusicStore musicStore;
    private final PlaybackTracker playbackTracker; 
    

    /**
     * Constructs a LibraryModel with a reference to the MusicStore.
     * 
     * @param musicStore The MusicStore instance providing access to available music data.
     */
    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.playbackTracker = new PlaybackTracker(); 
    }

    /**
     * Retrieves the MusicStore associated with this library model.
     * 
     * @return The MusicStore instance.
     */
    public MusicStore getMusicStore() {
        return this.musicStore;
    }
    
    /**
     * Retrieves the PlaybackTracker associated with this library model.
     * 
     * @return The PlaybackTracker instance.
     */
    public PlaybackTracker getPlaybackTracker() {
        return this.playbackTracker;
    }

    
    // ================== LIBRARY MANAGEMENT ================== //

    
    /**
     * Adds a song to the library if it exists in the MusicStore.
     * 
     * @param song The song to add.
     */
    public void addSong(Song song) {
        if (inStore(song)) {
            songLibrary.add(song);
        }
    }

    /**
     * Removes a song from the library.
     * 
     * @param song The song to remove.
     */
    public void removeSong(Song song) {
        songLibrary.remove(song);
    }

    /**
     * Adds an album to the library and all its songs if the album exists in the MusicStore.
     * 
     * @param album The album to add.
     */
    public void addAlbum(Album album) {
        if (inStore(album)) {
            albumLibrary.add(album);
            // Add all songs from the album to the song library
            for (Song song : album.getSongs()) {
                addSong(song);
            }
        }
    }

    /**
     * Removes an album from the library and all its songs.
     * 
     * @param album The album to remove.
     */
    public void removeAlbum(Album album) {
        albumLibrary.remove(album);
        // Remove all songs from the album from the song library
        for (Song song : album.getSongs()) {
            removeSong(song);
        }
    }

    /**
     * Returns an unmodifiable view of the songs in the library.
     * 
     * @return A set of songs.
     */
    public Set<Song> getSongLibrary() {
        return Collections.unmodifiableSet(songLibrary);
    }

    /**
     * Returns an unmodifiable view of the albums in the library.
     * 
     * @return A set of albums.
     */
    public Set<Album> getAlbumLibrary() {
        return Collections.unmodifiableSet(albumLibrary);
    }

    
    // ================== SEARCH METHODS (LIBRARY) ================== //

    
    /**
     * Searches for songs in the library by title (case-insensitive).
     * 
     * @param title The title to search for.
     * @return A list of matching songs.
     */
    public List<Song> searchSongByTitle(String title) {
        List<Song> result = new ArrayList<>();
        // Loop through all songs in the library
        for (Song song : songLibrary) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                result.add(song); // Add matching song to the result list
            }
        }
        return result;
    }

    /**
     * Searches for songs in the library by artist (case-insensitive).
     * 
     * @param artist The artist to search for.
     * @return A list of matching songs.
     */
    public List<Song> searchSongByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        // Loop through all songs in the library
        for (Song song : songLibrary) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                result.add(song); // Add matching song to the result list
            }
        }
        return result;
    }

    /**
     * Searches for an album in the library by title (case-insensitive).
     * 
     * @param title The title to search for.
     * @return The matching album, or null if not found.
     */
    public Album searchAlbumByTitle(String title) {
        // Loop through all albums in the library
        for (Album album : albumLibrary) {
            // Check if the album title matches (case-insensitive)
            if (album.getTitle().equalsIgnoreCase(title)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Searches for albums in the library by artist (case-insensitive).
     * 
     * @param artist The artist to search for.
     * @return A list of matching albums.
     */
    public List<Album> searchAlbumByArtist(String artist) {
        List<Album> result = new ArrayList<>();
        // Loop through all albums in the library
        for (Album album : albumLibrary) {
            if (album.getArtist().equalsIgnoreCase(artist)) {
                result.add(album); // Add matching album to the result list
            }
        }
        return result;
    }

    /**
     * Searches for a song in the library by artist and title (case-insensitive).
     * 
     * @param artist The artist to search for.
     * @param title  The title to search for.
     * @return The matching song, or null if not found.
     */
    public Song searchSongByArtistAndTitle(String artist, String title) {
        // Loop through all songs in the library
        for (Song song : songLibrary) {
            // Check if both the artist and title match (case-insensitive)
            if (song.getArtist().equalsIgnoreCase(artist) 
                && song.getTitle().equalsIgnoreCase(title)) {
                return song;
            }
        }
        return null;
    }

    
    // ================== SEARCH METHODS (MUSIC STORE) ================== //

    
    /**
     * Searches for songs in the MusicStore by title.
     * 
     * @param title The title to search for.
     * @return A list of matching songs.
     */
    public List<Song> searchStoreSongByTitle(String title) {
        return musicStore.getSongsByTitle(title);
    }

    /**
     * Searches for songs in the MusicStore by artist.
     * 
     * @param artist The artist to search for.
     * @return A list of matching songs.
     */
    public List<Song> searchStoreSongByArtist(String artist) {
        return musicStore.getSongsByArtist(artist);
    }

    /**
     * Searches for an album in the MusicStore by title.
     * 
     * @param title The title to search for.
     * @return The matching album, or null if not found.
     */
    public Album searchStoreAlbumByTitle(String title) {
        return musicStore.getAlbumByTitle(title);
    }

    /**
     * Searches for albums in the MusicStore by artist.
     * 
     * @param artist The artist to search for.
     * @return A list of matching albums.
     */
    public List<Album> searchStoreAlbumByArtist(String artist) {
        return musicStore.getAlbumsByArtist(artist);
    }

    
    // ================== PLAYLIST MANAGEMENT ================== //

    
    /**
     * Creates a new playlist and adds it to the library.
     * 
     * @param name The name of the playlist.
     * @return The created playlist.
     */
    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.add(playlist);
        return playlist;
    }

    /**
     * Returns all playlists in the library.
     * 
     * @return A list of playlists.
     */
    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }

    /**
     * Searches for a playlist by name (case-insensitive).
     * 
     * @param name The name to search for.
     * @return The matching playlist, or null if not found.
     */
    public Playlist getPlaylistByName(String name) {
        // Loop through all playlists in the collection
        for (Playlist playlist : playlists) {
            // Check if the playlist name matches (case-insensitive)
            if (playlist.getName().equalsIgnoreCase(name)) {
                return playlist;
            }
        }
        return null;
    }

    
    // ================== RATING & FAVORITES ================== //

    
    /**
     * Rates a song (1-5). A rating of 5 marks the song as a favorite.
     * 
     * @param song   The song to rate.
     * @param rating The rating (1-5).
     * @throws IllegalArgumentException If the rating is invalid.
     */
    public void rateSong(Song song, int rating) {
        // Validate the rating is within the allowed range
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1-5");
        }
        song.rate(rating);
    }

    /**
     * Returns all favorite songs in the library.
     * 
     * @return A list of favorite songs.
     */
    public List<Song> getFavoriteSongs() {
        List<Song> favorites = new ArrayList<>();
        // Loop through all songs in the library
        for (Song song : songLibrary) {
            // Check if the song is marked as a favorite
            if (song.isFavorite()) {
                favorites.add(song);
            }
        }
        return favorites;
    }
    
    /**
     * Marks a song as a favorite.
     * 
     * @param song The song to mark as favorite.
     */
    public void markAsFavorite(Song song) {
        song.markAsFavorite();
    }

    
    // ================== HELPER METHODS ================== //

    
    /**
     * Checks if a song exists in the MusicStore.
     * 
     * @param song The song to check.
     * @return True if the song exists in the store, false otherwise.
     */
    private boolean inStore(Song song) {
        return musicStore.getSongByArtistAndTitle(song.getArtist(), song.getTitle()) != null;
    }

    /**
     * Checks if an album exists in the MusicStore.
     * 
     * @param album The album to check.
     * @return True if the album exists in the store, false otherwise.
     */
    private boolean inStore(Album album) {
        return musicStore.albumExists(album.getTitle(), album.getArtist());
    }

    /**
     * Returns a list of unique artist names in the library.
     * 
     * @return A list of artists.
     */
    public List<String> getArtists() {
        Set<String> uniqueArtists = new HashSet<>();
        // Loop through all songs in the library
        for (Song song : songLibrary) {
        	// Add artist name to the Set (duplicates are ignored)
            uniqueArtists.add(song.getArtist());
        }
        return new ArrayList<>(uniqueArtists);
    }
    
    /**
     * Adds an album directly to the user's library without checking if it exists in the MusicStore.
     * This method bypasses validation checks and is intended for loading saved user libraries.
     * 
     * @param album The album to be added to the library.
     */
    public void addAlbumDirect(Album album) {
        // Directly add album to albumLibrary without checking inStore
        albumLibrary.add(album);
        // Also add each song to the songLibrary if needed
        for (Song song : album.getSongs()) {
            songLibrary.add(song);
        }
    }
}