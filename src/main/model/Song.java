package main.model;

public class Song {
    private final String title;
    private final String artist;
    private final Album album;
    private int rating;
    private boolean isFavorite;
    
    /**
     * Constructs a new Song instance.
     * 
     * Params: title  The title of the song (cannot be null or empty).
     * Params: artist The artist of the song (cannot be null or empty).
     * Params: album  The album the song belongs to (cannot be null).
     * @pre title != null && artist != null && album != null
     * Output: None (constructor)
     */
    public Song(String title, String artist, Album album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
    }
    
    /**
     * Rates the song on a scale of 1 to 5.
     * If the rating is 5, the song is automatically marked as a favorite.
     * 
     * Params: rating The rating value (must be between 1 and 5).
     * @throws IllegalArgumentException If the rating is not between 1 and 5.
     * Output: void
     */
    public void rate(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1-5");
        }
        this.rating = rating;
        if (rating == 5) markAsFavorite();
    }
    
    /* 
     * Marks the song as a favorite
     * Params: None
     * Output: void
     */
    public void markAsFavorite() { 
        this.isFavorite = true;
    }

    
    // ================== GETTERS ================== //
    
    public String getTitle() {
    	return title; 
    }
    
    public String getArtist() {
    	return artist; 
    }
    
    public Album getAlbum() { 
    	return album; 
    }
    
    public int getRating() { 
    	return rating;
    }
    
    public boolean isFavorite() { 
    	return isFavorite; 
    }
}