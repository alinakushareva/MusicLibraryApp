package test.model;

import main.model.PlaybackTracker;
import main.model.LibraryModel;
import main.model.Song;
import main.model.Album;
import main.database.MusicStore;
import main.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlaybackTrackerTest {

    // Initialize MusicStore and LibraryModel
    private static final MusicStore musicStore = new MusicStore("src/main/albums");
    private final LibraryModel library = new LibraryModel(musicStore);
    private final PlaybackTracker tracker = new PlaybackTracker();
    private User testUser;

    private static final String PLAYBACK_FILE_PATH = "user_data/playback_testUser.json";

    @BeforeEach
    void setUp() {
        testUser = new User("testUser", "securePassword123");

        // Ensure "Begin Again" is in the user's library
        Song song = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        assertNotNull(song);
        
        testUser.getLibrary().addSong(song); // Ensure it's in the user's library
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get(PLAYBACK_FILE_PATH)); // Clean up test file
    }

    // ================== CONSTRUCTOR TESTS ================== //

    @Test
    void testConstructor_RecentlyPlayedIsEmpty() {
        assertTrue(tracker.getRecentlyPlayed().isEmpty());
    }

    @Test
    void testConstructor_PlayCountsIsEmpty() {
        assertTrue(tracker.getMostPlayed().isEmpty());
    }

    // ================== TESTS FOR playSong() ================== //

    @Test
    void testPlaySong_AddsToRecentlyPlayed() {
        Song song = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        assertNotNull(song);
        library.addSong(song);

        tracker.playSong(song);

        assertEquals(song, tracker.getRecentlyPlayed().get(0));
    }

    @Test
    void testPlaySong_RemovesOldestWhenLimitReached() {
        List<Song> albumSongs = getSongsFromMultipleArtists(11);
        assertTrue(albumSongs.size() >= 11);

        for (int i = 0; i < 11; i++) {
            tracker.playSong(albumSongs.get(i));
        }

        assertEquals(10, tracker.getRecentlyPlayed().size());
    }

    @Test
    void testPlaySong_MostPlayedListUpdates() {
        Song song1 = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        Song song2 = musicStore.getSongByArtistAndTitle("Norah Jones", "Wintertime");

        assertNotNull(song1);
        assertNotNull(song2);
        library.addSong(song1);
        library.addSong(song2);

        tracker.playSong(song1);
        tracker.playSong(song1);
        tracker.playSong(song2);

        assertEquals(song1, tracker.getMostPlayed().get(0));
    }

    @Test
    void testPlaySong_MostPlayedListHandlesTies() {
        Song song1 = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        Song song2 = musicStore.getSongByArtistAndTitle("Norah Jones", "Wintertime");

        assertNotNull(song1);
        assertNotNull(song2);
        library.addSong(song1);
        library.addSong(song2);

        tracker.playSong(song1);
        tracker.playSong(song2);
        tracker.playSong(song1);
        tracker.playSong(song2);

        assertEquals(2, tracker.getMostPlayed().size());
    }

    @Test
    void testPlaySong_NullSongThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> tracker.playSong(null));
    }

    // ================== TESTS FOR savePlaybackData() ================== //

    @Test
    void testSavePlaybackData_CreatesFile() {
        Song song = getTestSong();
        tracker.playSong(song);

        tracker.savePlaybackData(testUser);

        File playbackFile = new File(PLAYBACK_FILE_PATH);
        assertTrue(playbackFile.exists());
    }

    @Test
    void testSavePlaybackData_StoresCorrectRecentlyPlayed() throws Exception {
        Song song = getTestSong();
        tracker.playSong(song);
        tracker.savePlaybackData(testUser);

        String content = new String(Files.readAllBytes(Paths.get(PLAYBACK_FILE_PATH)));
        assertTrue(content.contains("\"recentlyPlayed\": [\"Begin Again\"]"));
    }

    @Test
    void testSavePlaybackData_StoresCorrectPlayCounts() throws Exception {
        Song song = getTestSong();
        tracker.playSong(song);
        tracker.playSong(song);
        tracker.savePlaybackData(testUser);

        String content = new String(Files.readAllBytes(Paths.get(PLAYBACK_FILE_PATH)));
        assertTrue(content.contains("\"playCounts\": {\"Begin Again\": 2}"));
    }

    // ================== TESTS FOR loadPlaybackData() ================== //

    @Test
    void testLoadPlaybackData_RestoresRecentlyPlayed() throws Exception {
        Song song = getTestSong();
        tracker.playSong(song);
        tracker.savePlaybackData(testUser);

        PlaybackTracker newTracker = new PlaybackTracker();
        newTracker.loadPlaybackData(testUser);

        assertFalse(newTracker.getRecentlyPlayed().isEmpty());
    }

  
    @Test
    void testLoadPlaybackData_NoFile_DoesNotThrowException() {
        File playbackFile = new File(PLAYBACK_FILE_PATH);
        assertFalse(playbackFile.exists());

        PlaybackTracker newTracker = new PlaybackTracker();
        assertDoesNotThrow(() -> newTracker.loadPlaybackData(testUser));
    }

    // ================== HELPER METHODS ================== //

    private Song getTestSong() {
        Song song = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        assertNotNull(song);
        library.addSong(song);
        return song;
    }

    private List<Song> getSongsFromMultipleArtists(int minSongsNeeded) {
        String[] artists = {"Norah Jones", "Alabama Shakes"};
        List<Song> collectedSongs = new ArrayList<>();

        for (String artist : artists) {
            List<Album> albums = musicStore.getAlbumsByArtist(artist);
            for (Album album : albums) {
                collectedSongs.addAll(album.getSongs());
                if (collectedSongs.size() >= minSongsNeeded) return collectedSongs;
            }
        }
        return collectedSongs;
    }
    

    @Test
    void testFindSongByTitle_FindsSongInStandaloneLibrary() {
        Song song = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        assertNotNull(song);
        
        testUser.getLibrary().addSong(song); // Add song directly, not in an album

        Song foundSong = tracker.findSongByTitle(testUser, "Begin Again");

        assertNotNull(foundSong);
    }

    @Test
    void testFindSongByTitle_SongNotFound() {
        Song foundSong = tracker.findSongByTitle(testUser, "Nonexistent Song");

        assertNull(foundSong);
    }

    @Test
    void testFindSongByTitle_UserLibraryIsNull() {
        User newUser = new User("newUser", "password123");

        Song foundSong = tracker.findSongByTitle(newUser, "Begin Again");

        assertNull(foundSong);
    }

    @Test
    void testFindSongByTitle_HandlesDifferentCapitalization() {
        Song song = musicStore.getSongByArtistAndTitle("Norah Jones", "Begin Again");
        assertNotNull(song, "Song should exist in MusicStore.");
        
        testUser.getLibrary().addSong(song);

        Song foundSong = tracker.findSongByTitle(testUser, "BEGIN AGAIN");

        assertNotNull(foundSong);
    }

    @Test
    void testFindSongByTitle_FindsSongInSingleAlbum() {
        // Create an album and a song
        Album album = new Album("Waking Up", "OneRepublic", "Rock", 2009);
        Song song = new Song("Secrets", "OneRepublic", album);
        
        // Add song to album
        album.addSong(song);
        
        // Add album to user's library
        testUser.getLibrary().addAlbumDirect(album);

        // Search for the song
        Song foundSong = tracker.findSongByTitle(testUser, "Secrets");

        assertNotNull(foundSong);
    }

    @Test
    void testFindSongByTitle_FindsSongInMultipleAlbums() {
        // Create two albums
        Album album1 = new Album("Waking Up", "OneRepublic", "Rock", 2009);
        Album album2 = new Album("Old Ideas", "Leonard Cohen", "Singer/Songwriter", 2012);

        // Create two songs with different names
        Song song1 = new Song("Secrets", "OneRepublic", album1);
        Song song2 = new Song("Going Home", "Leonard Cohen", album2);

        // Add songs to their respective albums
        album1.addSong(song1);
        album2.addSong(song2);

        // Add both albums to user's library
        testUser.getLibrary().addAlbumDirect(album1);
        testUser.getLibrary().addAlbumDirect(album2);

        // Search for a song that exists in an album
        Song foundSong = tracker.findSongByTitle(testUser, "Going Home");

        assertNotNull(foundSong);
    }

    @Test
    void testFindSongByTitle_DoesNotFindSongInAlbums() {
        // Create an album with a different song
        Album album = new Album("Waking Up", "OneRepublic", "Rock", 2009);
        Song song = new Song("Secrets", "OneRepublic", album);
        
        // Add song to album
        album.addSong(song);

        // Add album to user's library
        testUser.getLibrary().addAlbumDirect(album);

        // Search for a song that does not exist
        Song foundSong = tracker.findSongByTitle(testUser, "Nonexistent Song");

        assertNull(foundSong);
    }

    @Test
    void testFindSongByTitle_HandlesCaseInsensitiveSearchInAlbums() {
        // Create an album and a song
        Album album = new Album("Waking Up", "OneRepublic", "Rock", 2009);
        Song song = new Song("Secrets", "OneRepublic", album);
        
        // Add song to album
        album.addSong(song);
        
        // Add album to user's library
        testUser.getLibrary().addAlbumDirect(album);

        // Search for the song with different capitalization
        Song foundSong = tracker.findSongByTitle(testUser, "secrets");

        assertNotNull(foundSong);
    }  
}