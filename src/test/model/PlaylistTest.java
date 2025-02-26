package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.model.Playlist;
import main.model.Song;

class PlaylistTest {

    @Test
    void testConstructorSetsNameCorrectly() {
        Playlist playlist = new Playlist("My Playlist");
        assertEquals("My Playlist", playlist.getName());
    }

    @Test
    void testConstructorInitializesEmptySongList() {
        Playlist playlist = new Playlist("My Playlist");
        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    void testAddSongIncreasesSize() {
        Playlist playlist = new Playlist("My Playlist");
        Song song = new Song("Molodost", "Max Korzh", null);
        playlist.addSong(song);
        assertEquals(1, playlist.getSongs().size());
    }

    @Test
    void testAddNullSongDoesNotIncreaseSize() {
        Playlist playlist = new Playlist("My Playlist");
        playlist.addSong(null);
        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    void testRemoveSongDecreasesSize() {
        Playlist playlist = new Playlist("My Playlist");
        Song song = new Song("Molodost", "Max Korzh", null);
        playlist.addSong(song);
        playlist.removeSong(song);
        assertTrue(playlist.getSongs().isEmpty());
    }

    @Test
    void testRemoveSongNotInListDoesNotChangeSize() {
        Playlist playlist = new Playlist("My Playlist");
        Song song1 = new Song("Molodost", "Max Korzh", null);
        Song song2 = new Song("Dagestan", "Sabina", null);
        playlist.addSong(song1);
        playlist.removeSong(song2);
        assertEquals(1, playlist.getSongs().size());
    }

    @Test
    void testRemoveNullSongDoesNotChangeSize() {
        Playlist playlist = new Playlist("My Playlist");
        Song song = new Song("Molodost", "Max Korzh", null);
        playlist.addSong(song);
        playlist.removeSong(null);
        assertEquals(1, playlist.getSongs().size());
    }

    @Test
    void testGetSongsReturnsCopy() {
        Playlist playlist = new Playlist("My Playlist");
        Song song = new Song("Molodost", "Max Korzh", null);
        playlist.addSong(song);
        List<Song> songs = playlist.getSongs();
        songs.remove(0); // Modifying the returned list
        assertEquals(1, playlist.getSongs().size()); // Original list remains unchanged
    }
}