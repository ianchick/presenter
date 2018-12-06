package app;

import app.models.Song;

/**
 * Singleton class for storing the session values.
 */
public class Session {

    private static Session session;
    private Song selectedSong;

    public static void init() {
        session = new Session();
    }

    public static Session getInstance() {
        return session;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }
}
