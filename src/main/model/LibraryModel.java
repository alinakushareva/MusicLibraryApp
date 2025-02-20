package main.model;  

import main.database.MusicStore;  
import java.util.ArrayList;  
import java.util.HashSet;  
import java.util.List;  
import java.util.Set;  
import java.util.stream.Collectors;  

public class LibraryModel {  
    private final Set<Song> songLibrary = new HashSet<>();  
    private final Set<Album> albumLibrary = new HashSet<>();  
    private final List<Playlist> playlists = new ArrayList<>();  
    private final MusicStore musicStore;  

    /*  
     * Constructor: Initializes the model with a reference to the MusicStore.  
     * Params: musicStore (MusicStore) - The music store to validate songs/albums against  
     * Output: None (constructor)  
     */  
    public LibraryModel(MusicStore musicStore) {  
        this.musicStore = musicStore;  
    }  

    /*  
     * Adds a song to the user's library if it exists in the MusicStore.  
     * Params: song (Song) - Song to add  
     * Output: void  
     */  
    public void addSong(Song song) {  
        if (inStore(song)) {  
            songLibrary.add(song);  
        }  
    }  

    /*  
     * Adds an album to the user's library if it exists in the MusicStore.  
     * Params: album (Album) - Album to add  
     * Output: void  
     */  
    public void addAlbum(Album album) {  
    	// TODO !!! 
    }  
    
    /*  
     * Removes a song from a playlist.  
     * Params: playlist (Playlist) - Playlist to remove the song from  
     *         song (Song) - Song to remove  
     * Output: void  
     */  
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        if (playlist != null && song != null) {
            playlist.removeSong(song);
        }
    }

    /*  
     * Creates a new playlist with the given name.  
     * Params: name (String) - Name of the playlist  
     * Output: Playlist - The newly created playlist  
     */  
    public Playlist createPlaylist(String name) {  
    	// TODO !!! 
    	return null; // Temporary return to avoid errors  
    }  

    /*  
     * Searches for songs in the library by title (case-insensitive).  
     * Params: title (String) - Title to search for  
     * Output: List<Song> - List of matching songs (empty if none found)  
     */  
    public List<Song> searchSongByTitle(String title) {  
        List<Song> results = new ArrayList<>();  
        for (Song song : songLibrary) {  
            if (song.getTitle().equalsIgnoreCase(title)) {  
                results.add(song);  
            }  
        }  
        return results;  
    }  

    /*  
     * Searches for an album in the library by title (case-insensitive).  
     * Params: title (String) - Title to search for  
     * Output: Album - Found album or null  
     */  
    public Album searchAlbumByTitle(String title) {  
    	// TODO !!! 
    	return null; // Temporary return to avoid errors
    }  

    /*  
     * Returns all playlists in the library.  
     * Params: None  
     * Output: List<Playlist> - List of playlists (empty if none exist)  
     */  
    public List<Playlist> getPlaylists() {  
        return new ArrayList<>(playlists);  
    }  

    /*  
     * Finds a playlist by name (case-insensitive).  
     * Params: name (String) - Playlist name to search for  
     * Output: Playlist - Found playlist or null  
     */  
    public Playlist getPlaylistByName(String name) {  
    	// TODO !!! 
    	return null; // Temporary return to avoid errors  
    }  

    /*  
     * Returns all albums in the library of a specific genre (case-insensitive).  
     * Params: genre (String) - Genre to filter by  
     * Output: List<Album> - List of matching albums (empty if none found)  
     */  
    public List<Album> getAlbumsByGenre(String genre) {  
    	// TODO !!! 
    	return null; // Temporary return to avoid errors  
    }  

    /*  
     * Returns a list of all unique artists in the library.  
     * Params: None  
     * Output: List<String> - List of artist names (empty if none exist)  
     */  
    public List<String> getArtists() {  
        Set<String> artists = new HashSet<>();  
        for (Song song : songLibrary) {  
            artists.add(song.getArtist());  
        }  
        return new ArrayList<>(artists);  
    }  

    /*  
     * Rates a song and automatically marks it as a favorite if rated 5.  
     * Params: song (Song) - Song to rate  
     *         rating (int) - Rating value (1-5)  
     * Output: void  
     */  
    public void rateSong(Song song, int rating) {  
        if (rating < 1 || rating > 5) {  
            throw new IllegalArgumentException("Rating must be 1-5");  
        }  
        song.rate(rating);  
    }  

    /*  
     * Returns all songs marked as favorites in the library.  
     * Params: None  
     * Output: List<Song> - List of favorite songs (empty if none exist)  
     */  
    public List<Song> getFavoriteSongs() {  
    	// TODO !!! 
    	return null; // Temporary return to avoid errors  
    }  

    // Helper Methods  

    /*  
     * Checks if a song exists in the MusicStore.  
     * Params: song (Song) - Song to validate  
     * Output: boolean - True if the song exists in the store  
     */  
    private boolean inStore(Song song) {  
        return musicStore.getSongByArtistAndTitle(song.getArtist(), song.getTitle()) != null;  
    }  

    /*  
     * Checks if an album exists in the MusicStore.  
     * Params: album (Album) - Album to validate  
     * Output: boolean - True if the album exists in the store  
     */  
    private boolean inStore(Album album) {  
    	// TODO !!! 
    	return true; // Temporary return to avoid errors  
    }  
}  