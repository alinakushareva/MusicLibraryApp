package test.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.database.MusicStore;
import main.model.Album;
import main.model.Song;

class MusicStoreTest {

    private static final String TEST_ALBUMS_PATH = "src/main/albums"; 
    
    
    @Test
    void testGetAlbumByTitleValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Album album = store.getAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(album);
    }

    @Test
    void testGetAlbumByTitleInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Album album = store.getAlbumByTitle("Nonexistent Album");
        assertNull(album);
    }

    @Test
    void testGetAlbumsByArtistValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Album> albums = store.getAlbumsByArtist("Coldplay");
        assertEquals(1, albums.size());
    }

    @Test
    void testGetAlbumsByArtistInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Album> albums = store.getAlbumsByArtist("Nonexistent Artist");
        assertTrue(albums.isEmpty());
    }

    @Test
    void testGetAlbumByArtistAndTitleValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Album album = store.getAlbumByArtistAndTitle("Coldplay", "A Rush of Blood to the Head");
        assertNotNull(album);
    }

    @Test
    void testGetAlbumByArtistAndTitleInvalidArtist() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Album album = store.getAlbumByArtistAndTitle("Invalid Artist", "A Rush of Blood to the Head");
        assertNull(album);
    }

    @Test
    void testGetAlbumByArtistAndTitleInvalidTitle() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Album album = store.getAlbumByArtistAndTitle("Coldplay", "Invalid Title");
        assertNull(album);
    }

    @Test
    void testGetAlbumsByGenreInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Album> albums = store.getAlbumsByGenre("Nonexistent Genre");
        assertTrue(albums.isEmpty());
    }

    @Test
    void testGetSongsByTitleValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByTitle("Clocks");
        assertEquals(1, songs.size());
    }

    @Test
    void testGetSongsByTitleInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByTitle("Nonexistent Song");
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongsByArtistValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByArtist("Coldplay");
        assertFalse(songs.isEmpty());
    }

    @Test
    void testGetSongsByArtistInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByArtist("Nonexistent Artist");
        assertTrue(songs.isEmpty());
    }

    @Test
    void testGetSongByArtistAndTitleValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Song song = store.getSongByArtistAndTitle("Coldplay", "Clocks");
        assertNotNull(song);
    }

    @Test
    void testGetSongByArtistAndTitleInvalidArtist() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Song song = store.getSongByArtistAndTitle("Invalid Artist", "Clocks");
        assertNull(song, "Invalid artist should return null");
    }

    @Test
    void testGetSongByArtistAndTitleInvalidTitle() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        Song song = store.getSongByArtistAndTitle("Coldplay", "Invalid Title");
        assertNull(song, "Invalid title should return null");
    }

    @Test
    void testGetSongsByGenreValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByGenre("Alternative");
        assertFalse(songs.isEmpty(), "Genre with songs should return non-empty list");
    }

    @Test
    void testGetSongsByGenreInvalid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        List<Song> songs = store.getSongsByGenre("Nonexistent Genre");
        assertTrue(songs.isEmpty(), "Invalid genre should return empty list");
    }

    @Test
    void testAlbumExistsValid() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        assertTrue(store.albumExists("A Rush of Blood to the Head", "Coldplay"), "Existing album should return true");
    }

    @Test
    void testAlbumExistsInvalidTitle() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        assertFalse(store.albumExists("Invalid Title", "Coldplay"), "Invalid title should return false");
    }

    @Test
    void testAlbumExistsInvalidArtist() {
        MusicStore store = new MusicStore(TEST_ALBUMS_PATH);
        assertFalse(store.albumExists("A Rush of Blood to the Head", "Invalid Artist"), "Invalid artist should return false");
    }
}