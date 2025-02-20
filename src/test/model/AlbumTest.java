package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.database.MusicStore;
import main.model.Album;
import main.model.Song;

class AlbumTest {

    @Test
    void testAlbumTitle() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        assertEquals("19", album.getTitle());
    }

    @Test
    void testAlbumArtist() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        assertEquals("Adele", album.getArtist());
    }

    @Test
    void testAlbumGenre() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        assertEquals("Pop", album.getGenre());
    }

    @Test
    void testAlbumYear() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        assertEquals(2008, album.getYear());
    }

    @Test
    void testAlbumSongsOrder() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        List<Song> songs = album.getSongs();
        assertEquals("Daydreamer", songs.get(0).getTitle());
        assertEquals("Hometown Glory", songs.get(11).getTitle());
    }

    @Test
    void testAlbumSongsCount() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        List<Song> songs = album.getSongs();
        assertEquals(12, songs.size());
    }

    @Test
    void testGetSongsReturnsCopy() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        List<Song> songs = album.getSongs();
        songs.clear(); // Modifying the returned list
        assertEquals(12, album.getSongs().size()); // Original list should remain unchanged
    }

    @Test
    void testAlbumNotFound() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("Nonexistent Album");
        assertNull(album);
    }

    @Test
    void testAlbumSongsArtistAssociation() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        List<Song> songs = album.getSongs();
        for (Song song : songs) {
            assertEquals("Adele", song.getArtist());
        }
    }

    @Test
    void testAlbumSongsAlbumAssociation() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("19");
        List<Song> songs = album.getSongs();
        for (Song song : songs) {
            assertEquals(album, song.getAlbum());
        }
    }
}