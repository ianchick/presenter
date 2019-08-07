package app;

import app.models.Song;
import app.views.SetListQueueView;
import app.views.slides.SlidesListView;
import app.views.SongListView;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * Singleton class to allow interaction between views.
 */
public class Mastermind {

    private static Mastermind mastermind;
    private SongListView songListView;
    private SlidesListView slidesListView;
    private SetListQueueView setListQueueView;
    private static Stage mainStage;

    private Song selectedSong;

    static void init(Stage window) {
        if (mastermind == null) {
            mastermind = new Mastermind();
            mainStage = window;
        }
    }

    public static Mastermind getInstance() {
        return mastermind;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }

    public SongListView getSongListView() {
        return songListView;
    }

    public void setSongListView(SongListView songListView) {
        this.songListView = songListView;
    }

    public SlidesListView getSlidesListView() {
        return slidesListView;
    }

    public void setSlidesListView(SlidesListView slidesListView) {
        this.slidesListView = slidesListView;
    }

    public SetListQueueView getSetListQueueView() {
        return setListQueueView;
    }

    public void setSetListQueueView(SetListQueueView setListQueueView) {
        this.setListQueueView = setListQueueView;
    }
}
