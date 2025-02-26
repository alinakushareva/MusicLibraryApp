package main.database;

import main.model.Album;
import main.model.Song;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicStore {
    private final Map<String, Album> albumsByTitle = new HashMap<>();
    private final Map<String, List<Album>> albumsByArtist = new HashMap<>();
    private final Map<String, List<Song>> songsByTitle = new HashMap<>();
    private final String basePath;

    /**
     * Constructs a MusicStore instance and initializes it with album data.
     * 
     * @param filePath The directory path containing album files.
     * @throws RuntimeException If an error occurs during initialization.
     */
    public MusicStore(String filePath) {
        // Making sure the base path ends with a separator
        if (filePath.endsWith(File.separator)) {
            this.basePath = filePath;
        } else {
            this.basePath = filePath + File.separator;
        }
        initializeStore();
    }

    /**
     * Initializes the music store by loading album data from files.
     */
    private void initializeStore() {
        File albumsFile = new File(basePath + "albums.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(albumsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Splitting the line into album title and artist
                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    // Skipping invalid entries
                    continue;
                }

                String albumTitle = parts[0].trim();
                String artist = parts[1].trim();
                processAlbumFile(albumTitle, artist);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading albums.txt: " + e.getMessage(), e);
        }
    }

    /**
     * Processes an individual album file and adds its data to the store.
     * 
     * @param albumTitle The title of the album.
     * @param artist The artist of the album.
     */
    private void processAlbumFile(String albumTitle, String artist) {
        String filename = basePath + albumTitle + "_" + artist + ".txt";
        File albumFile = new File(filename);

        if (!albumFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(albumFile))) {
            // Parsing header line
            String header = reader.readLine();
            if (header == null) {
                return;
            }

            // Splitting header into parts: title, artist, genre, year
            String[] headerParts = header.split(",", 4);
            if (headerParts.length != 4) {
                return; 
            }

            String genre = headerParts[2].trim();
            int year;
            try {
                year = Integer.parseInt(headerParts[3].trim());
            } catch (NumberFormatException e) {
                return; 
            }

            // Creating album and adding to data structures
            Album album = new Album(albumTitle, artist, genre, year);
            albumsByTitle.put(albumTitle.toLowerCase(), album);

            // Adding to albumsByArtist 
            String artistKey = artist.toLowerCase();
            if (!albumsByArtist.containsKey(artistKey)) {
                albumsByArtist.put(artistKey, new ArrayList<>());
            }
            albumsByArtist.get(artistKey).add(album);

            // Processing songs in order
            String songTitle;
            while ((songTitle = reader.readLine()) != null) {
                songTitle = songTitle.trim();
                if (!songTitle.isEmpty()) {
                    Song song = new Song(songTitle, artist, album);
                    album.addSong(song); // Adding songs to the album in order

                    // Adding to songsByTitle 
                    String songTitleKey = songTitle.toLowerCase();
                    if (!songsByTitle.containsKey(songTitleKey)) {
                        songsByTitle.put(songTitleKey, new ArrayList<>());
                    }
                    songsByTitle.get(songTitleKey).add(song);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing album file: " + filename, e);
        }
    }

    // ================== PUBLIC METHODS ================== //

    /**
     * Retrieves an album by its exact title (case-insensitive).
     * 
     * @param title The title of the album to search for.
     * @return The album, or null if not found.
     */
    public Album getAlbumByTitle(String title) {
        return albumsByTitle.get(title.toLowerCase());
    }

    /**
     * Retrieves all albums by a specific artist (case-insensitive).
     * 
     * @param artist The artist name to search for.
     * @return An unmodifiable list of albums (empty if none found).
     */
    public List<Album> getAlbumsByArtist(String artist) {
        String artistKey = artist.toLowerCase();
        if (albumsByArtist.containsKey(artistKey)) {
            return Collections.unmodifiableList(albumsByArtist.get(artistKey));
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves all songs with a specific title (case-insensitive).
     * 
     * @param title The title to search for.
     * @return An unmodifiable list of songs (empty if none found).
     */
    public List<Song> getSongsByTitle(String title) {
        String titleKey = title.toLowerCase();
        if (songsByTitle.containsKey(titleKey)) {
            return Collections.unmodifiableList(songsByTitle.get(titleKey));
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves all songs by a specific artist (case-insensitive).
     * 
     * @param artist The artist name to search for.
     * @return An unmodifiable list of songs (empty if none found).
     */
    public List<Song> getSongsByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        String artistKey = artist.toLowerCase();
        if (albumsByArtist.containsKey(artistKey)) {
            // Iterating through all albums by the artist and collect songs
            for (Album album : albumsByArtist.get(artistKey)) {
                result.addAll(album.getSongs());
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Retrieves a specific song by artist and title (case-insensitive).
     * 
     * @param artist The artist name to search for.
     * @param title  The song title to search for.
     * @return The song, or null if not found.
     */
    public Song getSongByArtistAndTitle(String artist, String title) {
        List<Song> songs = getSongsByTitle(title);
        // Iterating through songs to find a match with the artist
        for (Song song : songs) {
            if (song.getArtist().equalsIgnoreCase(artist)) {
                return song;
            }
        }
        return null;
    }

    /**
     * Checks if an album exists in the store (case-insensitive).
     * 
     * @param title  The title of the album.
     * @param artist The artist of the album.
     * @return True if the album exists, false otherwise.
     */
    public boolean albumExists(String title, String artist) {
        Album album = albumsByTitle.get(title.toLowerCase());
        return album != null && album.getArtist().equalsIgnoreCase(artist);
    }

    /**
     * Retrieves an album by artist and title combination (case-insensitive).
     * 
     * @param artist The artist name to search for.
     * @param title  The title of the album to search for.
     * @return The album, or null if not found.
     */
    public Album getAlbumByArtistAndTitle(String artist, String title) {
        List<Album> albums = getAlbumsByArtist(artist);
        // Iterating through albums to find a match with the title
        for (Album album : albums) {
            if (album.getTitle().equalsIgnoreCase(title)) {
                return album;
            }
        }
        return null;
    }
}