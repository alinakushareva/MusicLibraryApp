package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.database.MusicStore;
import main.model.Album;
import main.model.Song;

class SongTest {
	
	
    @Test
    void testSongTitle() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertEquals("Rolling in the Deep", song.getTitle());
    }

    @Test
    void testSongArtist() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertEquals("Adele", song.getArtist());
    }

    @Test
    void testSongAlbumAssociation() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertEquals(album, song.getAlbum());
    }

    @Test
    void testDefaultRating() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertEquals(0, song.getRating());
    }

    @Test
    void testDefaultFavoriteStatus() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertFalse(song.isFavorite());
    }

    @Test
    void testValidRatingSetTo2() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(2);
        assertEquals(2, song.getRating());
    }

    @Test
    void testValidMinimumRating1() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(1);
        assertEquals(1, song.getRating());
    }

    @Test
    void testValidMaximumRating5() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(5);
        assertEquals(5, song.getRating());
    }

    @Test
    void testRating0ThrowsException() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertThrows(IllegalArgumentException.class, () -> song.rate(0));
    }

    @Test
    void testRating7ThrowsException() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertThrows(IllegalArgumentException.class, () -> song.rate(7));
    }

    @Test
    void testNegativeRatingThrowsException() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        assertThrows(IllegalArgumentException.class, () -> song.rate(-7));
    }

    @Test
    void testAutoFavoriteAtRating5() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(5);
        assertTrue(song.isFavorite());
    }

    @Test
    void testNoAutoFavoriteAtRating4() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(4);
        assertFalse(song.isFavorite());
    }

    @Test
    void testExplicitFavoriteMarking() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.markAsFavorite();
        assertTrue(song.isFavorite());
    }

    @Test
    void testFavoriteRemainsAfterRatingChange() {
        MusicStore store = new MusicStore("src/main/albums");
        Album album = store.getAlbumByTitle("21");
        List<Song> songs = album.getSongs();
        Song song = songs.get(0);
        song.rate(5);
        song.rate(4);
        assertTrue(song.isFavorite());
    }
}