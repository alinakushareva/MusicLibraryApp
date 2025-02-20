package main.model;

public class Song {
	private final String title;
    private final String artist;
    private final Album album;
    private int rating;
    private boolean isFavorite;
    
    /* 
     * Constructor: Creates a new Song instance
     * Params: title (String) - Song title
     *         artist (String) - Song artist
     *         album (Album) - Parent album
     * Output: None (constructor)
     */
    public Song(String title, String artist, Album album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    /* 
     * Rates the song on a scale of 1 to 5 and marks it as a favorite if rated 5
     * Params: rating (int) - Rating value (1-5)
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

    // Getters
    
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