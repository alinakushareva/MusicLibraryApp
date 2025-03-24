package test.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.File;
import org.json.JSONObject;
import org.json.JSONArray;
import main.model.User;
import main.model.Album;
import main.model.Song;
import main.model.Playlist;
import main.database.MusicStore;

public class UserTest {
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "password123";
    private User user;
    private File testFile;
    private MusicStore musicStore;
    private Album adele19;

    @BeforeEach
    void setUp() {
        user = new User(TEST_USERNAME, TEST_PASSWORD);
        testFile = new File("user_data/user_" + TEST_USERNAME + ".json");
        
        // Initialize music store and load Adele's 19 album
        musicStore = new MusicStore("src/main/albums/");
        adele19 = musicStore.getAlbumByTitle("19");
        
        // Clean up any existing test files
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    // ================== TEST WITH REAL ALBUM DATA ================== //

    @Test
    void testSerializeLibrary_WithRealAlbum_ContainsCorrectData() {
        // Add real album to library
        user.getLibrary().addAlbum(adele19);
        
        JSONObject libraryJson = user.serializeLibrary();
        JSONArray albumsArray = libraryJson.getJSONArray("albums");
        
        assertEquals(1, albumsArray.length());
        assertEquals("19", albumsArray.getJSONObject(0).getString("title"));
        assertEquals("Adele", albumsArray.getJSONObject(0).getString("artist"));
    }

    @Test
    void testDeserializeLibrary_WithRealAlbumData_LoadsCorrectly() {
        // Create JSON structure mimicking real album
        JSONObject albumJson = new JSONObject();
        albumJson.put("title", "19");
        albumJson.put("artist", "Adele");
        albumJson.put("genre", "Pop");
        albumJson.put("year", 2008);

        JSONArray songsArray = new JSONArray();
        JSONObject songJson = new JSONObject();
        songJson.put("title", "Daydreamer");
        songJson.put("artist", "Adele");
        songJson.put("rating", 5);
        songJson.put("isFavorite", true);
        songsArray.put(songJson);

        albumJson.put("songs", songsArray);

        JSONArray albumsArray = new JSONArray();
        albumsArray.put(albumJson);

        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", albumsArray);
        libraryJson.put("playlists", new JSONArray());

        user.deserializeLibrary(libraryJson);
        
        Album loadedAlbum = user.getLibrary().getAlbumLibrary().iterator().next();
        assertEquals("19", loadedAlbum.getTitle());
        assertEquals(1, loadedAlbum.getSongs().size());
        assertEquals("Daydreamer", loadedAlbum.getSongs().get(0).getTitle());
    }

    

    @Test
    void testCreatePlaylist_WithRealSongs_SerializesCorrectly() {
        // Add real album to library
        user.getLibrary().addAlbum(adele19);
        
        // Create playlist with real songs
        Playlist playlist = user.getLibrary().createPlaylist("Adele Favorites");
        playlist.addSong(adele19.getSongs().get(0)); // Daydreamer
        playlist.addSong(adele19.getSongs().get(2)); // Chasing Pavements
        
        JSONObject libraryJson = user.serializeLibrary();
        JSONArray playlistsArray = libraryJson.getJSONArray("playlists");
        
        assertEquals(1, playlistsArray.length());
        assertEquals(2, playlistsArray.getJSONObject(0).getJSONArray("songs").length());
    }

    @Test
    void testSaveAndLoadLibrary_WithRealAlbum_PreservesData() {
        Album testAlbum = new Album("19", "Adele", "Pop", 2008);
        Song testSong = new Song("Daydreamer", "Adele", testAlbum);
        testAlbum.addSong(testSong);
        
        user.getLibrary().addAlbum(testAlbum);
        testSong.markAsFavorite();
        testSong.rate(5);
        user.saveLibraryData();
        User loadedUser = new User(TEST_USERNAME, TEST_PASSWORD);
        loadedUser.loadLibraryData();
        
        Album loadedAlbum = loadedUser.getLibrary().getAlbumLibrary().iterator().next();
        assertEquals("19", loadedAlbum.getTitle());
        assertTrue(loadedAlbum.getSongs().get(0).isFavorite());
        assertEquals(5, loadedAlbum.getSongs().get(0).getRating());
    }

    @Test
    void testDeserializeLibrary_WithRealPlaylist_LoadsCorrectly() {
        // Create JSON structure with real playlist data
        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Adele Mix");

        JSONArray playlistSongs = new JSONArray();
        playlistSongs.put("Daydreamer");
        playlistSongs.put("Chasing Pavements");
        playlistJson.put("songs", playlistSongs);

        JSONArray playlistsArray = new JSONArray();
        playlistsArray.put(playlistJson);

        // Add album data first
        JSONObject albumJson = new JSONObject();
        albumJson.put("title", "19");
        albumJson.put("artist", "Adele");
        albumJson.put("genre", "Pop");
        albumJson.put("year", 2008);
        
        JSONArray songsArray = new JSONArray();
        for (Song song : adele19.getSongs()) {
            JSONObject songJson = new JSONObject();
            songJson.put("title", song.getTitle());
            songJson.put("artist", song.getArtist());
            songJson.put("rating", 1); // Set valid minimum rating instead of 0
            songJson.put("isFavorite", false);
            songsArray.put(songJson);
        }
        albumJson.put("songs", songsArray);

        JSONArray albumsArray = new JSONArray();
        albumsArray.put(albumJson);

        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", albumsArray);
        libraryJson.put("playlists", playlistsArray);

        user.deserializeLibrary(libraryJson);
        
        assertEquals(1, user.getLibrary().getPlaylists().size());
        assertEquals(2, user.getLibrary().getPlaylists().get(0).getSongs().size());
        
        // Verify songs have valid ratings
        for (Song song : user.getLibrary().getAlbumLibrary().iterator().next().getSongs()) {
            assertTrue(song.getRating() >= 1 && song.getRating() <= 5);
        }
    }

    // ================== TEST RATING SYSTEM WITH REAL SONGS ================== //

    @Test
    void testSongRating_PersistsThroughSaveLoad() {
        Album testAlbum = new Album("19", "Adele", "Pop", 2008);
        Song testSong = new Song("Daydreamer", "Adele", testAlbum);
        testAlbum.addSong(testSong);
        
        user.getLibrary().addAlbum(testAlbum);
        testSong.rate(5);
        user.saveLibraryData();
        User loadedUser = new User(TEST_USERNAME, TEST_PASSWORD);
        loadedUser.loadLibraryData();
        
        Album loadedAlbum = loadedUser.getLibrary().getAlbumLibrary().iterator().next();
        assertEquals(5, loadedAlbum.getSongs().get(0).getRating());
    }

    // ================== TEST EDGE CASES WITH REAL DATA ================== //

    @Test
    void testDeserializeLibrary_WithMissingSongInPlaylist_HandlesGracefully() {
        // Create playlist referencing non-existent song
        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Missing Songs");
        playlistJson.put("songs", new JSONArray().put("Nonexistent Song"));

        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", new JSONArray());
        libraryJson.put("playlists", new JSONArray().put(playlistJson));

        assertDoesNotThrow(() -> user.deserializeLibrary(libraryJson));
        assertEquals(1, user.getLibrary().getPlaylists().size());
        assertEquals(0, user.getLibrary().getPlaylists().get(0).getSongs().size());
    }
}