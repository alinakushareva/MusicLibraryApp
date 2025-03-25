# Music Library Application  
*This is a simple text-based app for searching and managing a personalized music collection.*

---

## 🎵 What It Does  
This Java program lets each **registered user** manage their own music library with features such as:  
- **Create and log into an account** with secure password hashing (PBKDF2 + salt)  
- **Search** for songs and albums from a music store loaded from files  
- **Add** songs or albums to a personal library  
- **Create and manage playlists**, shuffle them, and view their contents  
- **Rate songs** (1–5 stars) and **mark favorites**  
- **View recently played** and **most played** songs (automatically tracked)  
- **Explore auto-generated playlists** such as:  
  - Top Rated Songs  
  - Favorite Songs  
- **Persistent storage** of all user data (library + playback history)

---

## 📁 Directory Structure & Class Overview  

### `/src/main/database/`  
- **MusicStore.java** – Loads music from `albums.txt` and album files, supports store-level searches  

### `/src/main/security/`  
- **PasswordUtil.java** – Generates salts, hashes passwords, and validates them securely  

### `/src/main/view/`  
- **LibraryView.java** – Handles all user input/output via a text-based menu system  
- **Main.java** – Launches the application and connects the components  
> *AI was used to assist in writing repetitive parts of `LibraryView.java`. Core logic was implemented manually. Details are explained in the video.*

### `/src/main/model/`  
- **Song.java** – Represents individual songs  
- **Album.java** – Represents albums and their metadata  
- **Playlist.java** – User-created playlists  
- **LibraryModel.java** – Manages a user's personal music library (songs, albums, playlists)  
- **AutoPlaylistManager.java** – Automatically generates dynamic playlists (favorites, top rated, genre-based)  
- **PlaybackTracker.java** – Tracks song play history (recently played & most played)  
- **User.java** – Represents a user and their saved music library  
- **UserManager.java** – Handles user registration, login, and file-based data saving/loading  

---


## How to Use It  
1. Compile and run the program.  
2. When prompted, **register** or **log into your account** (secure password handling is built-in).  
3. Once logged in, follow the menu options in the console to:  
   -- Search for songs or albums from the music store  
   -- Add songs or albums to your personal library  
   -- Create playlists and add songs to them  
   -- Rate songs (1–5 stars) and mark your favorites  
   -- Shuffle songs or entire playlists  
   -- View recently played and most played songs (automatically tracked)  
   -- Access auto-generated playlists such as favorites and top-rated songs  
4. Your data (library) will be automatically saved and loaded per user session.


---

## Testing  
- JUnit tests in `/src/test` (90% coverage)  
- Tests cover key logic including library management, searching, playlist creation, user accounts, and playback tracking  

---

*AI was used only for the `LibraryView` class, and its usage is explained in the video. Core logic was implemented without AI assistance.*