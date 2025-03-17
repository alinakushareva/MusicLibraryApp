package test.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

import main.model.User;
import main.model.LibraryModel;
import main.model.Album;
import main.model.Song;
import main.model.Playlist;
import org.json.JSONObject;
import org.json.JSONArray;

public class UserTest {

    // ================== TEST CONSTRUCTOR ================== //

    @Test
    void testConstructor_ValidInput_CreatesUser() {
        User user = new User("testUser", "password123");
        assertNotNull(user);
    }

    @Test
    void testConstructor_NullUsername_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "password123"),
            "Constructor should throw IllegalArgumentException for null username.");
    }

    @Test
    void testConstructor_EmptyUsername_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new User("", "password123"),
            "Constructor should throw IllegalArgumentException for empty username.");
    }

    @Test
    void testConstructor_NullPassword_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new User("testUser", null),
            "Constructor should throw IllegalArgumentException for null password.");
    }

    @Test
    void testConstructor_EmptyPassword_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new User("testUser", ""),
            "Constructor should throw IllegalArgumentException for empty password.");
    }

    // ================== TEST validatePassword() ================== //

    @Test
    void testValidatePassword_CorrectPassword_ReturnsTrue() {
        User user = new User("testUser", "password123");
        assertTrue(user.validatePassword("password123"));
    }

    @Test
    void testValidatePassword_IncorrectPassword_ReturnsFalse() {
        User user = new User("testUser", "password123");
        assertFalse(user.validatePassword("wrongpassword"));
    }

    @Test
    void testValidatePassword_NullPassword_ThrowsIllegalArgumentException() {
        User user = new User("testUser", "password123");
        assertThrows(IllegalArgumentException.class, () -> user.validatePassword(null),
            "Validating a null password should throw IllegalArgumentException.");
    }

    @Test
    void testValidatePassword_EmptyPassword_ThrowsIllegalArgumentException() {
        User user = new User("testUser", "password123");
        assertThrows(IllegalArgumentException.class, () -> user.validatePassword(""),
            "Validating an empty password should throw IllegalArgumentException.");
    }

    // ================== TEST getLibrary() ================== //

    @Test
    void testGetLibrary_ReturnsLibraryModel() {
        User user = new User("testUser", "password123");
        assertNotNull(user.getLibrary());
    }

    // ================== TEST saveLibraryData() ================== //

    @Test
    void testSaveLibraryData_CreatesFile() {
        User user = new User("testUser", "password123");
        user.saveLibraryData();

        File file = new File("user_data/user_testUser.json");
        assertTrue(file.exists());
        file.delete(); // Clean up after the test
    }

    // ================== TEST loadLibraryData() ================== //

    @Test
    void testLoadLibraryData_LoadsUsername() {
        User user = new User("testUser", "password123");
        user.saveLibraryData();

        User loadedUser = new User("testUser", "password123");
        loadedUser.loadLibraryData();

        assertEquals("testUser", loadedUser.getUsername());
    }

    @Test
    void testLoadLibraryData_LoadsSalt() {
        User user = new User("testUser", "password123");
        user.saveLibraryData();

        User loadedUser = new User("testUser", "password123");
        loadedUser.loadLibraryData();

        assertEquals(user.getSalt(), loadedUser.getSalt());
    }

    @Test
    void testLoadLibraryData_LoadsHashedPassword() {
        User user = new User("testUser", "password123");
        user.saveLibraryData();

        User loadedUser = new User("testUser", "password123");
        loadedUser.loadLibraryData();

        assertEquals(user.getHashedPassword(), loadedUser.getHashedPassword());
    }

    @Test
    void testLoadLibraryData_FileDoesNotExist_NoExceptionThrown() {
        User user = new User("nonExistentUser", "password123");
        assertDoesNotThrow(() -> user.loadLibraryData());
    }

    // ================== TEST serializeLibrary() ================== //

    @Test
    void testSerializeLibrary_ReturnsValidJSON() {
        User user = new User("testUser", "password123");
        JSONObject libraryJson = user.serializeLibrary();

        assertNotNull(libraryJson);
    }

    @Test
    void testSerializeLibrary_ContainsAlbumsArray() {
        User user = new User("testUser", "password123");
        JSONObject libraryJson = user.serializeLibrary();

        assertTrue(libraryJson.has("albums"));
    }

    @Test
    void testSerializeLibrary_ContainsPlaylistsArray() {
        User user = new User("testUser", "password123");
        JSONObject libraryJson = user.serializeLibrary();

        assertTrue(libraryJson.has("playlists"));
    }

    // ================== TEST deserializeLibrary() ================== //

    @Test
    void testDeserializeLibrary_LoadsAlbums() {
        JSONObject albumJson = new JSONObject();
        albumJson.put("title", "Test Album");
        albumJson.put("artist", "Test Artist");
        albumJson.put("genre", "Test Genre");
        albumJson.put("year", 2023);

        JSONArray songsArray = new JSONArray();
        JSONObject songJson = new JSONObject();
        songJson.put("title", "Song 1");
        songJson.put("artist", "Test Artist");
        songJson.put("rating", 4);
        songJson.put("isFavorite", false);
        songsArray.put(songJson);

        albumJson.put("songs", songsArray);

        JSONArray albumsArray = new JSONArray();
        albumsArray.put(albumJson);

        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", albumsArray);

        User user = new User("testUser", "password123");
        user.deserializeLibrary(libraryJson);

        assertEquals(1, user.getLibrary().getAlbumLibrary().size());
    }

    @Test
    void testDeserializeLibrary_LoadsSongs() {
        // Create a JSON object representing an album with songs
        JSONObject albumJson = new JSONObject();
        albumJson.put("title", "Test Album");
        albumJson.put("artist", "Test Artist");
        albumJson.put("genre", "Test Genre");
        albumJson.put("year", 2023);

        JSONArray songsArray = new JSONArray();
        JSONObject songJson = new JSONObject();
        songJson.put("title", "Song 1");
        songJson.put("artist", "Test Artist");
        songJson.put("rating", 4);
        songJson.put("isFavorite", false);
        songsArray.put(songJson);

        albumJson.put("songs", songsArray);

        JSONArray albumsArray = new JSONArray();
        albumsArray.put(albumJson);

        // Create an empty playlists array (required by deserializeLibrary)
        JSONArray playlistsArray = new JSONArray();

        // Create the library JSON object
        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", albumsArray); // Add albums array
        libraryJson.put("playlists", playlistsArray); // Add empty playlists array

        // Create a user and deserialize the library
        User user = new User("testUser", "password123");
        user.deserializeLibrary(libraryJson);

        // Verify the album and songs are deserialized correctly
        Album album = user.getLibrary().getAlbumLibrary().iterator().next();
        assertEquals(1, album.getSongs().size());
    }

    @Test
    void testDeserializeLibrary_LoadsPlaylists() {
        // Create a JSON object representing a playlist
        JSONObject playlistJson = new JSONObject();
        playlistJson.put("name", "Test Playlist");

        JSONArray playlistSongs = new JSONArray();
        playlistSongs.put("Song 1");
        playlistJson.put("songs", playlistSongs);

        JSONArray playlistsArray = new JSONArray();
        playlistsArray.put(playlistJson);

        // Create an empty albums array (required by deserializeLibrary)
        JSONArray albumsArray = new JSONArray();

        // Create the library JSON object
        JSONObject libraryJson = new JSONObject();
        libraryJson.put("albums", albumsArray); // Add empty albums array
        libraryJson.put("playlists", playlistsArray); // Add playlists array

        // Create a user and deserialize the library
        User user = new User("testUser", "password123");
        user.deserializeLibrary(libraryJson);

        // Verify the playlist is deserialized correctly
        assertEquals(1, user.getLibrary().getPlaylists().size());
    }

    // ================== TEST GETTERS ================== //

    @Test
    void testGetUsername_ReturnsUsername() {
        User user = new User("testUser", "password123");
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testGetSalt_ReturnsSalt() {
        User user = new User("testUser", "password123");
        assertNotNull(user.getSalt());
    }

    @Test
    void testGetHashedPassword_ReturnsHashedPassword() {
        User user = new User("testUser", "password123");
        assertNotNull(user.getHashedPassword());
    }
    
}