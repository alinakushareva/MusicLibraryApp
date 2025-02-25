package main.model;

import main.database.MusicStore;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryModel {
    private final Set<Song> songLibrary = new HashSet<>();
    private final Set<Album> albumLibrary = new HashSet<>();
    private final List<Playlist> playlists = new ArrayList<>();
    private final MusicStore musicStore;

    /**
     * Constructor: Initializes the model with a reference to the MusicStore.
     * @param musicStore The music store to validate songs/albums against.
     */
    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
    }

    /**
     * Provides access to the MusicStore for validation purposes.
     * @return The MusicStore instance associated with this library.
     */
    public MusicStore getMusicStore() {
        return this.musicStore;
    }

    // ================== LIBRARY MANAGEMENT ================== //

    /**
     * Adds a song to the user's library if it exists in the MusicStore.
     * @param song The song to add.
     */
    public void addSong(Song song) {
        if (inStore(song)) {
            songLibrary.add(song);
        }
    }

    /**
     * Adds an album to the user's library if it exists in the MusicStore.
     * @param album The album to add.
     */
    public void addAlbum(Album album) {
        if (inStore(album)) {
            albumLibrary.add(album);
            album.getSongs().forEach(this::addSong);
        }
    }

    /**
     * Returns all songs in the user's library.
     * @return A set of songs in the library.
     */
    public Set<Song> getSongLibrary() {
        return Collections.unmodifiableSet(songLibrary);
    }

    /**
     * Returns all albums in the user's library.
     * @return A set of albums in the library.
     */
    public Set<Album> getAlbumLibrary() {
        return Collections.unmodifiableSet(albumLibrary);
    }

    // ================== SEARCH METHODS (LIBRARY) ================== //

    /**
     * Searches for songs in the user's library by title (case-insensitive).
     * @param title The title to search for.
     * @return A list of matching songs (empty if none found).
     */
    public List<Song> searchSongByTitle(String title) {
        return songLibrary.stream()
            .filter(song -> song.getTitle().equalsIgnoreCase(title))
            .collect(Collectors.toList());
    }

    /**
     * Searches for songs in the user's library by artist (case-insensitive).
     * @param artist The artist to search for.
     * @return A list of matching songs (empty if none found).
     */
    public List<Song> searchSongByArtist(String artist) {
        return songLibrary.stream()
            .filter(song -> song.getArtist().equalsIgnoreCase(artist))
            .collect(Collectors.toList());
    }

    /**
     * Searches for an album in the user's library by title (case-insensitive).
     * @param title The title to search for.
     * @return The matching album or null.
     */
    public Album searchAlbumByTitle(String title) {
        return albumLibrary.stream()
            .filter(album -> album.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
    }

    /**
     * Searches for albums in the user's library by artist (case-insensitive).
     * @param artist The artist to search for.
     * @return A list of matching albums (empty if none found).
     */
    public List<Album> searchAlbumByArtist(String artist) {
        return albumLibrary.stream()
            .filter(album -> album.getArtist().equalsIgnoreCase(artist))
            .collect(Collectors.toList());
    }

    /**
     * Searches for a specific song in the user's library by artist and title (case-insensitive).
     * @param artist The artist to search for.
     * @param title The title to search for.
     * @return The matching song or null.
     */
    public Song searchSongByArtistAndTitle(String artist, String title) {
        return songLibrary.stream()
            .filter(song -> song.getArtist().equalsIgnoreCase(artist) 
                         && song.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
    }

    // ================== SEARCH METHODS (MUSIC STORE) ================== //

    /**
     * Searches the entire MusicStore for songs by title (case-insensitive).
     * @param title The title to search for.
     * @return A list of matching songs from the store.
     */
    public List<Song> searchStoreSongByTitle(String title) {
        return musicStore.getSongsByTitle(title);
    }

    /**
     * Searches the entire MusicStore for songs by artist (case-insensitive).
     * @param artist The artist to search for.
     * @return A list of matching songs from the store.
     */
    public List<Song> searchStoreSongByArtist(String artist) {
        return musicStore.getSongsByArtist(artist);
    }

    /**
     * Searches the entire MusicStore for an album by title (case-insensitive).
     * @param title The title to search for.
     * @return The matching album or null.
     */
    public Album searchStoreAlbumByTitle(String title) {
        return musicStore.getAlbumByTitle(title);
    }

    /**
     * Searches the entire MusicStore for albums by artist (case-insensitive).
     * @param artist The artist to search for.
     * @return A list of matching albums from the store.
     */
    public List<Album> searchStoreAlbumByArtist(String artist) {
        return musicStore.getAlbumsByArtist(artist);
    }

    // ================== PLAYLIST MANAGEMENT ================== //

    /**
     * Creates a new playlist with the given name.
     * @param name The name of the playlist.
     * @return The newly created playlist.
     */
    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.add(playlist);
        return playlist;
    }

    /**
     * Returns all playlists in the library.
     * @return A list of playlists (empty if none exist).
     */
    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }

    /**
     * Finds a playlist by name (case-insensitive).
     * @param name The name of the playlist.
     * @return The matching playlist or null.
     */
    public Playlist getPlaylistByName(String name) {
        return playlists.stream()
            .filter(playlist -> playlist.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    // ================== RATING & FAVORITES ================== //

    /**
     * Rates a song and automatically marks it as a favorite if rated 5.
     * @param song The song to rate.
     * @param rating The rating value (1-5).
     * @throws IllegalArgumentException If the rating is not between 1 and 5.
     */
    public void rateSong(Song song, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1-5");
        }
        song.rate(rating);
    }

    /**
     * Returns all songs marked as favorites in the library.
     * @return A list of favorite songs (empty if none exist).
     */
    public List<Song> getFavoriteSongs() {
        return songLibrary.stream()
            .filter(Song::isFavorite)
            .collect(Collectors.toList());
    }

    // ================== HELPER METHODS ================== //

    /**
     * Checks if a song exists in the MusicStore.
     * @param song The song to validate.
     * @return True if the song exists in the store.
     */
    private boolean inStore(Song song) {
        return musicStore.getSongByArtistAndTitle(song.getArtist(), song.getTitle()) != null;
    }

    /**
     * Checks if an album exists in the MusicStore.
     * @param album The album to validate.
     * @return True if the album exists in the store.
     */
    private boolean inStore(Album album) {
        return musicStore.albumExists(album.getTitle(), album.getArtist());
    }

    /**
     * Returns a list of all unique artists in the library.
     * @return A list of artist names (empty if none exist).
     */
    public List<String> getArtists() {
        return songLibrary.stream()
            .map(Song::getArtist)
            .distinct()
            .collect(Collectors.toList());
    }
}