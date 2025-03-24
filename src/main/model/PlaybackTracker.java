/**
 * Name: Alina Kushareva
 * Class: CSC335 Spring 2025
 * Project: MusicLibraryApp
 * File: PlaybackTracker.java
 * Purpose: Tracks song playback history including recently played songs and play counts.
 *          Handles saving/loading playback data to persistent storage.
 */
package main.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaybackTracker {
    private Deque<Song> recentlyPlayed; // Stores the last 10 played songs
    private Map<Song, Integer> playCounts; // Tracks how often each song is played

    private static final int RECENTLY_PLAYED_LIMIT = 10; // Maximum number of recently played songs to store

    /**
     * Constructs a new PlaybackTracker instance.
     */
    public PlaybackTracker() {
        this.recentlyPlayed = new ArrayDeque<>(RECENTLY_PLAYED_LIMIT);
        this.playCounts = new HashMap<>();
    }

    // ================== SONG PLAYBACK ================== //

    /**
     * Tracks a song that has been played.
     *
     * @param song The song that was played.
     */
    public void playSong(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song cannot be null.");
        }

        // Remove the song from the recently played deque if it already exists
        recentlyPlayed.remove(song);

        // Add the song to the recently played deque
        if (recentlyPlayed.size() >= RECENTLY_PLAYED_LIMIT) {
            recentlyPlayed.removeLast(); // Remove the oldest song if the limit is reached
        }
        recentlyPlayed.addFirst(song);

        // Update the play count for the song
        playCounts.put(song, playCounts.getOrDefault(song, 0) + 1);
    }

    // ================== GET RECENTLY PLAYED SONGS ================== //

    /**
     * Returns the 10 most recently played songs.
     *
     * @return A list of the 10 most recently played songs.
     */
    public List<Song> getRecentlyPlayed() {
        return new ArrayList<>(recentlyPlayed);
    }

    // ================== GET MOST PLAYED SONGS ================== //

    /**
     * Returns the top 10 most played songs sorted by play count.
     *
     * @return A list of the top 10 most played songs.
     */
    public List<Song> getMostPlayed() {
        return playCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Sort by play count (descending)
                .limit(10) // Limit to top 10 songs
                .map(Map.Entry::getKey) // Extract the Song objects
                .collect(Collectors.toList());
    }
    
    
    // ================== GET PLAY COUNTS ================== //

    /**
     * Returns the map of songs and their play counts.
     *
     * @return A map where the key is the song and the value is the number of times it has been played.
     */
    public Map<Song, Integer> getPlayCounts() {
    	return Collections.unmodifiableMap(playCounts);
    }

    // ================== SAVE/LOAD PLAYBACK DATA ================== //

    /**
     * Saves playback history for a user.
     *
     * @param user The user whose playback history will be saved.
     */
    public void savePlaybackData(User user) {
        // Ensure the directory exists
        File dir = new File("user_data");
        if (!dir.exists()) {
            boolean dirCreated = dir.mkdirs(); // Create the directory if it doesn't exist
            if (!dirCreated) {
                throw new RuntimeException("Failed to create directory: user_data");
            }
        }

        // Serialize recentlyPlayed and playCounts to JSON
        JSONObject playbackData = new JSONObject();

        // Serialize recently played songs
        JSONArray recentlyPlayedArray = new JSONArray();
        for (Song song : recentlyPlayed) {
            recentlyPlayedArray.put(song.getTitle()); // Store song titles for simplicity
        }
        playbackData.put("recentlyPlayed", recentlyPlayedArray);

        // Serialize play counts
        JSONObject playCountsObject = new JSONObject();
        for (Map.Entry<Song, Integer> entry : playCounts.entrySet()) {
            playCountsObject.put(entry.getKey().getTitle(), entry.getValue());
        }
        playbackData.put("playCounts", playCountsObject);

        // Save to a file
        String fileName = "user_data/playback_" + user.getUsername() + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(playbackData.toString(4)); // To JSON
        } catch (IOException e) {
            throw new RuntimeException("Error saving playback data", e);
        }
    }

    /**
     * Loads playback history for a user.
     *
     * @param user The user whose playback history will be loaded.
     */
    public void loadPlaybackData(User user) {
        // Construct the filename using the user's username
        String fileName = "user_data/playback_" + user.getUsername() + ".json";
        File file = new File(fileName);

        // Return early if no playback data file exists for this user
        if (!file.exists()) {
            return; // No playback data exists yet
        }

        // Try-with-resources to auto-close the reader
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }

            // Parse the JSON string into a JSONObject
            JSONObject playbackData = new JSONObject(jsonData.toString());

            // Deserialize recently played songs
            JSONArray recentlyPlayedArray = playbackData.getJSONArray("recentlyPlayed");
            recentlyPlayed.clear();
            // Process each song title in the array
            for (int i = 0; i < recentlyPlayedArray.length(); i++) {
                String songTitle = recentlyPlayedArray.getString(i);
                Song song = findSongByTitle(user, songTitle); // Find matching song in library
                if (song != null) {
                    recentlyPlayed.add(song); // Add to recently played if found
                }
            }

            // Deserialize play counts
            JSONObject playCountsObject = playbackData.getJSONObject("playCounts");
            playCounts.clear();
            // Process each song entry in play counts
            for (String songTitle : playCountsObject.keySet()) {
                int count = playCountsObject.getInt(songTitle); // Get play count
                Song song = findSongByTitle(user, songTitle); // Find matching song
                if (song != null) {
                    playCounts.put(song, count); // Update play count if song exists
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading playback data", e);
        }
    }

    // ================== HELPER METHODS ================== //

    /**
     * Finds a song by its title in the user's library.
     *
     * @param user      The user whose library will be searched.
     * @param songTitle The title of the song to find.
     * @return The Song object, or null if not found.
     */
    public Song findSongByTitle(User user, String songTitle) {
        // Ensure the user’s library is initialized
        if (user.getLibrary() == null) {
            return null;
        }
        // Search all albums in user's library
        for (Album album : user.getLibrary().getAlbumLibrary()) {
            for (Song song : album.getSongs()) {
                if (song.getTitle().equalsIgnoreCase(songTitle)) {
                    return song;
                }
            }
        }
        // Search all individual songs in user's library (if applicable)
        for (Song song : user.getLibrary().getSongLibrary()) {
            if (song.getTitle().equalsIgnoreCase(songTitle)) {
                return song;
            }
        }
        return null; 
    }
}