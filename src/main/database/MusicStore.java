package main.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.model.Album;
import main.model.Song;

public class MusicStore {
	private final Map<String, Album> albumsByTitle = new HashMap<>();
    private final Map<String, List<Album>> albumsByArtist = new HashMap<>();
    private final Map<String, List<Song>> songsByTitle = new HashMap<>();
    private final Set<String> genres = new HashSet<>();
    private final Set<String> artists = new HashSet<>();
    private final String basePath;
    
    /* 
     * Constructor: Initializes the music store and loads album data.
     * Params: filePath (String) - Path to the directory containing album files
     * Output: None (constructor)
     */
    public MusicStore(String filePath) {
        // Ensure base path ends with a separator
        if (filePath.endsWith(File.separator)) {
            this.basePath = filePath;
        } else {
            this.basePath = filePath + File.separator;
        }
        try {
            loadAlbums();
        } catch (IOException e) {
            // Propagate the exception to the caller
            throw new RuntimeException("Error initializing MusicStore: " + e.getMessage(), e);
        }
    }

    /* 
     * Loads album data from the albums.txt file and processes each album.
     * Params: None
     * Output: void
     */
    private void loadAlbums() throws IOException {
        File albumsFile = new File(basePath + "albums.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(albumsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    // Skip invalid entries silently
                    continue;
                }

                String albumTitle = parts[0].trim();
                String artist = parts[1].trim();
                processAlbumFile(albumTitle, artist);
            }
        } catch (IOException e) {
            // Propagate the exception to the caller
            throw new IOException("Error loading albums.txt: " + e.getMessage(), e);
        }
    }

    /* 
     * Processes an individual album file and adds its data to the store.
     * Params: albumTitle (String), artist (String)
     * Output: void
     */
    private void processAlbumFile(String albumTitle, String artist) throws IOException {
        String filename = basePath + albumTitle + "_" + artist + ".txt";
        File albumFile = new File(filename);

        if (!albumFile.exists()) {
            return; // Skip missing files
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(albumFile))) {
            // Parse header line
            String header = reader.readLine();
            if (header == null) {
                return; // Skip empty files
            }

            String[] headerParts = header.split(",", 4);
            if (headerParts.length != 4) {
                return; // Skip invalid headers
            }

            String genre = headerParts[2].trim();
            int year;
            try {
                year = Integer.parseInt(headerParts[3].trim());
            } catch (NumberFormatException e) {
                return; // Skip invalid years
            }

            // Create album and add to data structures
            Album album = new Album(albumTitle, artist, genre, year);
            albumsByTitle.put(albumTitle, album);

            // Add to albumsByArtist (store artist name in lowercase for case-insensitive lookup)
            String artistKey = artist.toLowerCase();
            if (!albumsByArtist.containsKey(artistKey)) {
                albumsByArtist.put(artistKey, new ArrayList<>());
            }
            albumsByArtist.get(artistKey).add(album);

            genres.add(genre);
            artists.add(artist);

            // Process songs in order
            String songTitle;
            while ((songTitle = reader.readLine()) != null) {
                songTitle = songTitle.trim();
                if (!songTitle.isEmpty()) {
                    Song song = new Song(songTitle, artist, album);
                    album.addSong(song); // Add songs to the album in order

                    // Add to songsByTitle
                    if (!songsByTitle.containsKey(songTitle)) {
                        songsByTitle.put(songTitle, new ArrayList<>());
                    }
                    songsByTitle.get(songTitle).add(song);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error processing album file: " + filename, e);
        }
    }
    
    /* 
     * Retrieves an album by its exact title.
     * Params: title (String) - Album title to search for
     * Output: Album (or null if not found)
     */
    public Album getAlbumByTitle(String title) {
        for (Map.Entry<String, Album> entry : albumsByTitle.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(title)) {
                return entry.getValue();
            }
        }
        return null;
    }
    
    /* 
     * Returns all albums by a specific artist
     * Params: artist (String) - Artist name to search for
     * Output: List<Album> - List of albums (empty if none found)
     */
    public List<Album> getAlbumsByArtist(String artist) {
        String artistKey = artist.toLowerCase();
        return albumsByArtist.getOrDefault(artistKey, new ArrayList<>());
    }
    
    /* 
     * Finds a specific album by artist and title combination
     * Params: artist (String) - Artist name to search for
     *         title (String) - Album title to search for
     * Output: Album - Found album or null
     */
    public Album getAlbumByArtistAndTitle(String artist, String title) {
        if (!albumsByArtist.containsKey(artist)) return null;
        
        for (Album album : albumsByArtist.get(artist)) {
            if (album.getTitle().equals(title)) {
                return album;
            }
        }
        return null;
    } 
    
    /* 
     * Retrieves all songs with a specific title
     * Params: title (String) - Song title to search for
     * Output: List<Song> - List of songs (empty if none found)
     */
    public List<Song> getSongsByTitle(String title) {
        List<Song> result = new ArrayList<>();
        for (Map.Entry<String, List<Song>> entry : songsByTitle.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(title)) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }
    
    /* 
     * Returns all songs by a specific artist
     * Params: artist (String) - Artist name to filter by
     * Output: List<Song> - List of songs (empty if none found)
     */
    public List<Song> getSongsByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        String artistKey = artist.toLowerCase();
        if (albumsByArtist.containsKey(artistKey)) {
            for (Album album : albumsByArtist.get(artistKey)) {
                result.addAll(album.getSongs()); // Songs are already in order
            }
        }
        return result;
    }
    
    /* 
     * Finds a specific song by artist and title combination
     * Params: artist (String) - Artist name to search for
     *         title (String) - Song title to search for
     * Output: Song - Found song or null
     */
    public Song getSongByArtistAndTitle(String artist, String title) {
        List<Song> songs = getSongsByTitle(title);
        for (Song song : songs) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                return song;
            }
        }
        return null;
    }

    
    /* 
     * Checks if an album exists in the music store
     * Params: album (Album) - Album object to verify
     * Output: boolean - True if album exists, false otherwise
     */
    public boolean albumExists(String title, String artist) {
        if (!albumsByTitle.containsKey(title)) {
            return false;
        }
        Album album = albumsByTitle.get(title);
        return album.getArtist().equalsIgnoreCase(artist);
    }
}