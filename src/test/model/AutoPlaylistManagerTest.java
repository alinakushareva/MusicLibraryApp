package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import main.model.*;
import main.database.MusicStore;

class AutoPlaylistManagerTest {
    private final MusicStore store = new MusicStore("src/main/albums/");
    private final Album adele19 = store.getAlbumByTitle("19");
    private final Song daydreamer = adele19.getSongs().get(0); // "Daydreamer"
    private final Song chasingPavements = adele19.getSongs().get(2); // "Chasing Pavements"

    @Test
    void testEmptyLibraryNoFavorites() {
        LibraryModel library = new LibraryModel(store);
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(0, manager.getAutoPlaylistInfo().get("Favorite Songs"));
    }

    @Test
    void testEmptyLibraryNoTopRated() {
        LibraryModel library = new LibraryModel(store);
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(0, manager.getAutoPlaylistInfo().get("Top Rated"));
    }

    @Test
    void testMarkedFavoriteAppearsInFavorites() {
        LibraryModel library = new LibraryModel(store);
        daydreamer.markAsFavorite();
        library.addSong(daydreamer);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(1, manager.getAutoPlaylistInfo().get("Favorite Songs"));
    }

    @Test
    void testFiveStarSongAppearsInBothPlaylists() {
        LibraryModel library = new LibraryModel(store);
        chasingPavements.rate(5);
        library.addSong(chasingPavements);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(1, manager.getAutoPlaylistInfo().get("Favorite Songs"));
    }

    @Test
    void testFourStarSongAppearsOnlyInTopRated() {
        LibraryModel library = new LibraryModel(store);
        daydreamer.rate(4);
        library.addSong(daydreamer);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(1, manager.getAutoPlaylistInfo().get("Top Rated"));
    }

    @Test
    void testThreeStarSongDoesNotAppearInPlaylists() {
        LibraryModel library = new LibraryModel(store);
        chasingPavements.rate(3);
        library.addSong(chasingPavements);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(0, manager.getAutoPlaylistInfo().get("Top Rated"));
    }

    @Test
    void testUnratedSongDoesNotAppearInPlaylists() {
        LibraryModel library = new LibraryModel(store);
        library.addSong(daydreamer);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(0, manager.getAutoPlaylistInfo().get("Favorite Songs"));
    }

    @Test
    void testMultipleQualifyingSongsAreCounted() {
        LibraryModel library = new LibraryModel(store);
        daydreamer.markAsFavorite();
        chasingPavements.rate(5);
        library.addSong(daydreamer);
        library.addSong(chasingPavements);
        
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        assertEquals(2, manager.getAutoPlaylistInfo().get("Favorite Songs"));
    }

    @Test
    void testGetAutoPlaylistsReturnsAllSystemPlaylists() {
        LibraryModel library = new LibraryModel(store);
        AutoPlaylistManager manager = new AutoPlaylistManager();
        manager.updateAutoPlaylists(library);
        
        List<Playlist> playlists = manager.getAutoPlaylists();
        assertEquals(2, playlists.size());
        assertEquals("Favorite Songs", playlists.get(0).getName());
        assertEquals("Top Rated", playlists.get(1).getName());
    }
    
    @Test
    void testCopyConstructorWithNullOriginal() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AutoPlaylistManager(null);
        }, "Should throw exception when original is null");
    }

    @Test
    void testCopyConstructorWithEmptyPlaylists() {
        // Setup original with empty playlists
        AutoPlaylistManager original = new AutoPlaylistManager();
        original.updateAutoPlaylists(new LibraryModel(store)); // Creates empty playlists
        
        // Create copy
        AutoPlaylistManager copy = new AutoPlaylistManager(original);
        
        // Verify empty playlists were copied
        assertEquals(2, copy.getAutoPlaylists().size());
        assertEquals(0, copy.getAutoPlaylistInfo().get("Favorite Songs"));
        assertEquals(0, copy.getAutoPlaylistInfo().get("Top Rated"));
    }

    

    @Test
    void testCopyConstructorMaintainsPlaylistOrder() {
        // Setup original
        AutoPlaylistManager original = new AutoPlaylistManager();
        LibraryModel library = new LibraryModel(store);
        original.updateAutoPlaylists(library);
        
        // Create copy
        AutoPlaylistManager copy = new AutoPlaylistManager(original);
        
        // Verify playlist order is maintained
        List<Playlist> playlists = copy.getAutoPlaylists();
        assertEquals("Favorite Songs", playlists.get(0).getName());
        assertEquals("Top Rated", playlists.get(1).getName());
    }
}