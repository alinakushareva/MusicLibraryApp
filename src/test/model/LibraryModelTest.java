package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import main.database.MusicStore;
import main.model.Album;
import main.model.LibraryModel;
import main.model.Playlist;
import main.model.Song;

class LibraryModelTest {

    // ================== SONG/ALBUM ADDITION & REMOVAL ================== //

    @Test
    void testAddSongExistsInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Clocks");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testAddSongNotInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = new Song("Nonexistent Song", "Unknown Artist", null);
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Nonexistent Song");
        assertEquals(0, foundSongs.size());
    }

    @Test
    void testAddAlbumExistsInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(foundAlbum);
    }

    @Test
    void testAddAlbumNotInStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = new Album("Nonexistent Album", "Unknown Artist", "Unknown Genre", 2023);
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("Nonexistent Album");
        assertNull(foundAlbum);
    }

    @Test
    void testRemoveSong() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);
        libraryModel.removeSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Clocks");
        assertEquals(0, foundSongs.size());
    }

    @Test
    void testRemoveAlbum() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);
        libraryModel.removeAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A Rush of Blood to the Head");
        assertNull(foundAlbum);
    }

    // ================== SEARCH METHODS (LIBRARY) ================== //

    @Test
    void testSearchSongByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Clocks");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testSearchSongByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchSongByTitle("Nonexistent Song");
        assertEquals(0, foundSongs.size());
    }

    @Test
    void testSearchSongByArtistExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        List<Song> foundSongs = libraryModel.searchSongByArtist("Coldplay");
        assertEquals(1, foundSongs.size());
    }

    @Test
    void testSearchSongByArtistNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchSongByArtist("Unknown Artist");
        assertEquals(0, foundSongs.size());
    }

    @Test
    void testSearchAlbumByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Album foundAlbum = libraryModel.searchAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(foundAlbum);
    }

    @Test
    void testSearchAlbumByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album foundAlbum = libraryModel.searchAlbumByTitle("Nonexistent Album");
        assertNull(foundAlbum);
    }

    @Test
    void testSearchAlbumByArtistExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        List<Album> foundAlbums = libraryModel.searchAlbumByArtist("Coldplay");
        assertEquals(1, foundAlbums.size());
    }

    @Test
    void testSearchAlbumByArtistNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Album> foundAlbums = libraryModel.searchAlbumByArtist("Unknown Artist");
        assertEquals(0, foundAlbums.size());
    }

    @Test
    void testSearchSongByArtistAndTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        Song foundSong = libraryModel.searchSongByArtistAndTitle("Coldplay", "Clocks");
        assertNotNull(foundSong);
    }

    @Test
    void testSearchSongByArtistAndTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song foundSong = libraryModel.searchSongByArtistAndTitle("Unknown Artist", "Nonexistent Song");
        assertNull(foundSong);
    }

    // ================== SEARCH METHODS (MUSIC STORE) ================== //

    @Test
    void testSearchStoreSongByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchStoreSongByTitle("Clocks");
        assertFalse(foundSongs.isEmpty());
    }

    @Test
    void testSearchStoreSongByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchStoreSongByTitle("Nonexistent Song");
        assertTrue(foundSongs.isEmpty());
    }

    @Test
    void testSearchStoreSongByArtistExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchStoreSongByArtist("Coldplay");
        assertFalse(foundSongs.isEmpty());
    }

    @Test
    void testSearchStoreSongByArtistNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> foundSongs = libraryModel.searchStoreSongByArtist("Unknown Artist");
        assertTrue(foundSongs.isEmpty());
    }

    @Test
    void testSearchStoreAlbumByTitleExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album foundAlbum = libraryModel.searchStoreAlbumByTitle("A Rush of Blood to the Head");
        assertNotNull(foundAlbum);
    }

    @Test
    void testSearchStoreAlbumByTitleNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album foundAlbum = libraryModel.searchStoreAlbumByTitle("Nonexistent Album");
        assertNull(foundAlbum);
    }

    @Test
    void testSearchStoreAlbumByArtistExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Album> foundAlbums = libraryModel.searchStoreAlbumByArtist("Coldplay");
        assertFalse(foundAlbums.isEmpty());
    }

    @Test
    void testSearchStoreAlbumByArtistNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Album> foundAlbums = libraryModel.searchStoreAlbumByArtist("Unknown Artist");
        assertTrue(foundAlbums.isEmpty());
    }

    // ================== PLAYLIST MANAGEMENT ================== //

    @Test
    void testCreatePlaylist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Playlist playlist = libraryModel.createPlaylist("My Playlist");
        assertNotNull(playlist);
    }

    @Test
    void testGetPlaylists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        libraryModel.createPlaylist("Playlist 1");
        libraryModel.createPlaylist("Playlist 2");

        List<Playlist> playlists = libraryModel.getPlaylists();
        assertEquals(2, playlists.size());
    }

    @Test
    void testGetPlaylistByNameExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        libraryModel.createPlaylist("My Playlist");

        Playlist foundPlaylist = libraryModel.getPlaylistByName("My Playlist");
        assertNotNull(foundPlaylist);
    }

    @Test
    void testGetPlaylistByNameNotExists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Playlist foundPlaylist = libraryModel.getPlaylistByName("Nonexistent Playlist");
        assertNull(foundPlaylist);
    }

    // ================== RATING & FAVORITES ================== //

    @Test
    void testRateSongValid() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);
        libraryModel.rateSong(song, 5);

        assertTrue(song.isFavorite());
    }

    @Test
    void testRateSongInvalidRatingLessThan1() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        assertThrows(IllegalArgumentException.class, () -> libraryModel.rateSong(song, 0));
    }

    @Test
    void testRateSongInvalidRatingGreaterThan5() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        assertThrows(IllegalArgumentException.class, () -> libraryModel.rateSong(song, 6));
    }

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

    // ================== HELPER METHODS ================== //

    @Test
    void testGetArtists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        List<String> artists = libraryModel.getArtists();
        assertEquals(1, artists.size());
    }
    
 // ================== GETTERS ================== //

    @Test
    void testGetSongLibrary() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Song song = musicStore.getSongByArtistAndTitle("Coldplay", "Clocks");
        libraryModel.addSong(song);

        Set<Song> songLibrary = libraryModel.getSongLibrary();

        assertEquals(1, songLibrary.size());
        assertTrue(songLibrary.contains(song));

        assertThrows(UnsupportedOperationException.class, () -> songLibrary.add(new Song("New Song", "New Artist", null)));
    }

    @Test
    void testGetAlbumLibrary() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("A Rush of Blood to the Head");
        libraryModel.addAlbum(album);

        Set<Album> albumLibrary = libraryModel.getAlbumLibrary();

        assertEquals(1, albumLibrary.size());
        assertTrue(albumLibrary.contains(album));

        assertThrows(UnsupportedOperationException.class, () -> albumLibrary.add(new Album("New Album", "New Artist", "New Genre", 2023)));
    }

    @Test
    void testGetMusicStore() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        MusicStore retrievedMusicStore = libraryModel.getMusicStore();
        assertSame(musicStore, retrievedMusicStore);
    }


 // ================== SORTING AND SHUFFLING ================== //

    @Test
    void testGetSongsSortedByTitleAndArtist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album1 = new Album("Test Album 1", "Artist B", "Pop", 2020);
        Song song1 = new Song("Banana", "Artist B", album1);
        Song song2 = new Song("Apple", "Artist A", album1);
        libraryModel.addAlbumDirect(new Album("Test Album", "Artist", "Pop", 2020, List.of(song1, song2)));

        List<Song> sortedSongs = libraryModel.getSongsSortedByTitleAndArtist();
        assertEquals("Apple", sortedSongs.get(0).getTitle());
        assertEquals("Banana", sortedSongs.get(1).getTitle());
    }

    @Test
    void testGetAlbumsSortedByTitle() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album1 = new Album("Banana Album", "Artist B", "Pop", 2020);
        Album album2 = new Album("Apple Album", "Artist A", "Rock", 2019);
        libraryModel.addAlbumDirect(album1);
        libraryModel.addAlbumDirect(album2);

        List<Album> sortedAlbums = libraryModel.getAlbumsSortedByTitle();
        assertEquals("Apple Album", sortedAlbums.get(0).getTitle());
        assertEquals("Banana Album", sortedAlbums.get(1).getTitle());
    }


    @Test
    void testGetSongsSortedByRating() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = new Album("Test Album", "Test Artist", "Pop", 2020);
        Song song1 = new Song("Song 1", "Test Artist", album);
        Song song2 = new Song("Song 2", "Test Artist", album);
        song1.rate(3);
        song2.rate(5);
        libraryModel.addAlbumDirect(new Album("Test Album", "Test Artist", "Pop", 2020, List.of(song1, song2)));

        List<Song> sortedSongs = libraryModel.getSongsSortedByRating();
        assertEquals(3, sortedSongs.get(0).getRating());
        assertEquals(5, sortedSongs.get(1).getRating());
    }

    @Test
    void testGetShuffledSongs() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19");
        libraryModel.addAlbum(album);
        List<Song> originalOrder = List.copyOf(libraryModel.getSongLibrary());

        List<Song> shuffledSongs = libraryModel.getShuffledSongs();
        assertEquals(originalOrder.size(), shuffledSongs.size());
        assertTrue(shuffledSongs.containsAll(originalOrder));
    }

    @Test
    void testGetShuffledPlaylistSongs() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19");
        libraryModel.addAlbum(album);
        Playlist playlist = libraryModel.createPlaylist("Test Playlist");
        playlist.addSong(album.getSongs().get(0));
        playlist.addSong(album.getSongs().get(1));

        List<Song> shuffled = libraryModel.getShuffledPlaylistSongs("Test Playlist");
        assertEquals(2, shuffled.size());
    }

    @Test
    void testGetShuffledPlaylistSongs_NonExistentPlaylist() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> result = libraryModel.getShuffledPlaylistSongs("Nonexistent");
        assertTrue(result.isEmpty());
    }

    // ================== AUTO PLAYLISTS AND FAVORITES ================== //

    @Test
    void testMarkAsFavorite() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19");
        libraryModel.addAlbum(album);
        Song song = album.getSongs().get(0);

        libraryModel.markAsFavorite(song);
        assertTrue(song.isFavorite());
    }

    @Test
    void testGetAutoPlaylists() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19");
        libraryModel.addAlbum(album);
        libraryModel.rateSong(album.getSongs().get(0), 5);

        List<Playlist> autoPlaylists = libraryModel.getAutoPlaylists();
        assertFalse(autoPlaylists.isEmpty());
    }

    // ================== GENRE SEARCH ================== //

    @Test
    void testSearchSongByGenre() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19"); // Adele's album is Pop
        libraryModel.addAlbum(album);

        List<Song> popSongs = libraryModel.searchSongByGenre("Pop");
        assertFalse(popSongs.isEmpty());
    }

    @Test
    void testSearchSongByGenre_NonExistent() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        List<Song> result = libraryModel.searchSongByGenre("Nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchSongByGenre_CaseInsensitive() {
        MusicStore musicStore = new MusicStore("src/main/albums");
        LibraryModel libraryModel = new LibraryModel(musicStore);

        Album album = musicStore.getAlbumByTitle("19");
        libraryModel.addAlbum(album);

        List<Song> lowerCase = libraryModel.searchSongByGenre("pop");
        List<Song> upperCase = libraryModel.searchSongByGenre("POP");
        assertEquals(lowerCase.size(), upperCase.size());
    }
}