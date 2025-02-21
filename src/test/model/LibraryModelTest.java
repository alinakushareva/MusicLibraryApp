package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.database.MusicStore;
import main.model.Album;
import main.model.LibraryModel;
import main.model.Playlist;
import main.model.Song;

class LibraryModelTest {

    // Test adding a song that exists in the MusicStore
    @Test
    void testAddSongExistsInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Clocks");
        assertEquals(1, foundSongs.size());
    }

    // Test adding a song that does NOT exist in the MusicStore
    @Test
    void testAddSongNotInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = new Song("Nonexistent Song", "Unknown Artist", null);
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Nonexistent Song");
        assertEquals(0, foundSongs.size());
    }

    // Test adding an album that exists in the MusicStore
    @Test
    void testAddAlbumExistsInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(foundAlbum);
    }

    // Test adding an album that does NOT exist in the MusicStore
    @Test
    void testAddAlbumNotInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = new Album("Nonexistent Album", "Unknown Artist", "Unknown Genre", 2023);
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("Nonexistent Album");
        assertNull(foundAlbum);
    }

    // Test searching for a song that exists in the library
    @Test
    void testSearchSongByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Clocks");
        assertEquals(1, foundSongs.size());
    }

    // Test searching for a song that does NOT exist in the library
    @Test
    void testSearchSongByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Nonexistent Song");
        assertEquals(0, foundSongs.size());
    }

    // Test searching for an album that exists in the library
    @Test
    void testSearchAlbumByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(foundAlbum);
    }

    // Test searching for an album that does NOT exist in the library
    @Test
    void testSearchAlbumByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album foundAlbum = libraryModel.searchAlbumByTitle("Nonexistent Album");
        assertNull(foundAlbum);
    }

    // Test creating a playlist
    @Test
    void testCreatePlaylist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Playlist playlist = libraryModel.createPlaylist("My Playlist");
        assertNotNull(playlist);
    }

    // Test adding a song to a playlist
    @Test
    void testAddSongToPlaylist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        Playlist playlist = libraryModel.createPlaylist("My Playlist");
        playlist.addSong(song);

        assertEquals(1, playlist.getSongs().size());
    }

    // Test removing a song from a playlist
    @Test
    void testRemoveSongFromPlaylist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        Playlist playlist = libraryModel.createPlaylist("My Playlist");
        playlist.addSong(song);
        playlist.removeSong(song);

        assertEquals(0, playlist.getSongs().size());
    }

    // Test rating a song and marking it as a favorite
    @Test
    void testRateSongAndMarkAsFavorite() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);
        libraryModel.rateSong(song, 5);

        assertTrue(song.isFavorite());
    }

    // Test getting all favorite songs
    @Test
    void testGetFavoriteSongs() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);
        libraryModel.rateSong(song, 5);

        List<Song> favoriteSongs = libraryModel.getFavoriteSongs();
        assertEquals(1, favoriteSongs.size());
    }

    // Test getting albums by genre
    @Test
    void testGetAlbumsByGenre() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        List<Album> albums = libraryModel.getAlbumsByGenre("Alternative");
        assertEquals(1, albums.size());
    }

    // Test getting all artists in the library
    @Test
    void testGetArtists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        List<String> artists = libraryModel.getArtists();
        assertEquals(1, artists.size());
    }
    
    
 // Test removing a song from a playlist (null playlist or song)
    @Test
    void testRemoveSongFromPlaylistNullInput() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        Playlist playlist = libraryModel.createPlaylist("My Playlist");

        // Test null playlist
        libraryModel.removeSongFromPlaylist(null, song);
        assertEquals(0, playlist.getSongs().size());

        // Test null song
        libraryModel.removeSongFromPlaylist(playlist, null);
        assertEquals(0, playlist.getSongs().size());
    }

    // Test searching for a song by title (case-insensitive)
    @Test
    void testSearchSongByTitleCaseInsensitive() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("CLOCKS"); // Uppercase search
        assertEquals(1, foundSongs.size());
    }

    // Test searching for an album by title (case-insensitive)
    @Test
    void testSearchAlbumByTitleCaseInsensitive() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A RUSH OF BLOOD TO THE HEAD"); // Uppercase search
        assertNotNull(foundAlbum);
    }

    // Test getting all playlists
    @Test
    void testGetPlaylists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        libraryModel.createPlaylist("Playlist 1");
        libraryModel.createPlaylist("Playlist 2");

        List<Playlist> playlists = libraryModel.getPlaylists();
        assertEquals(2, playlists.size());
    }

    // Test getting a playlist by name (case-insensitive)
    @Test
    void testGetPlaylistByNameCaseInsensitive() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        libraryModel.createPlaylist("My Playlist");

        Playlist foundPlaylist = libraryModel.getPlaylistByName("MY PLAYLIST"); // Uppercase search
        assertNotNull(foundPlaylist);
    }

    // Test getting a playlist by name that does NOT exist
    @Test
    void testGetPlaylistByNameNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Playlist foundPlaylist = libraryModel.getPlaylistByName("Nonexistent Playlist");
        assertNull(foundPlaylist);
    }

    // Test rating a song with an invalid rating (less than 1)
    @Test
    void testRateSongInvalidRatingLessThan1() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        assertThrows(IllegalArgumentException.class, () -> libraryModel.rateSong(song, 0));
    }

    // Test rating a song with an invalid rating (greater than 5)
    @Test
    void testRateSongInvalidRatingGreaterThan5() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        assertThrows(IllegalArgumentException.class, () -> libraryModel.rateSong(song, 6));
    }
}