# Music Library Application  
*This is a simple text-based app for searching and managing music collection*  

---

## What It Does  
This Java program lets you:  
- **Search** songs/albums in a music store (loaded from text files)  
- **Build your library** by adding songs/albums  
- **Create playlists** and organize songs  
- **Rate songs** (1-5 stars) and auto-save favorites  
- View your collection by artist, album, and etc.

---

## Key Files & What They Do  

### `/src/main/database`  
- **`MusicStore.java`**  
  Loads music data from text files. Uses `albums.txt` to find albums, then reads each album file.  

### `/src/main/model`  
- **`Song.java`**, **`Album.java`**, **`Playlist.java`**  
  Basic objects for songs (title, artist, rating), albums (year, genre, tracklist), and playlists.  
- **`LibraryModel.java`**  
  Manages your library – adds songs/albums, handles searches, playlists, and ratings.  

### `/src/main/view`  
- **`LibraryView.java`**  
  Text menu system. Handles all user input/output. *AI was used to generate parts of this class, as explained in the video.*  
- **`Main.java`**  
  Starts the app.  

---

## How to Use It  
1. Compile and run the program.  
2. Follow the menu options in the console to:  
   - Search for songs/albums  
   - Add songs/albums to your library  
   - Create playlists and add songs  
   - Rate songs and view favorites  

---

## Testing  
- JUnit tests in `/src/test` (90% coverage)  

---

*AI was used only for the `LibraryView` class, and its usage is explained in the video. Core logic was implemented without AI assistance.*  