package app.toolbars;

import app.MusixMatchControl;
import app.models.Song;
import app.storage.StorageController;
import app.views.CreateSongView;
import app.views.LiveView;
import app.views.SlidesListView;
import app.views.SongListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;

import java.io.File;

public class NavigationBar {

    private Button liveViewButton;
    private SongListView songListView;

    public ToolBar setup(SongListView songListView) {
        ToolBar toolBar = new ToolBar();
        this.songListView = songListView;

        Button startLive = setLiveView();
        Button addSong = setCreateSongView();

        Label fontSizeLabel = new Label("Text Size");
        ObservableList<String> options = FXCollections.observableArrayList("24", "32", "40", "48", "56", "64", "72", "80", "88", "96", "104");
        ComboBox<String> fontSizeCombo = new ComboBox<>(options);
        fontSizeCombo.setOnAction(e -> {
            LiveView.setTextSize(Integer.valueOf(fontSizeCombo.getValue()));
            if (LiveView.isLive()) {
                LiveView.setFontSize(LiveView.getTextView().getText());
            }
        });

        Button searchMusixMatch = setMusixMatchButton();

        Button editSong = setEditSongButton();

        toolBar.getItems().addAll(startLive, addSong, new Separator(Orientation.VERTICAL),
                fontSizeLabel, fontSizeCombo, new Separator(Orientation.VERTICAL),
                searchMusixMatch, new Separator(Orientation.VERTICAL),
                editSong);

        return toolBar;
    }

    private Button setLiveView() {
        if (liveViewButton == null) {
            liveViewButton = new Button("Start");
            liveViewButton.setId("start_live_btn");
        }
        liveViewButton.setOnAction(e -> {
            if (!LiveView.isLive()) {
                LiveView.display();
                liveViewButton.setText("Stop");
            }
            else {
                LiveView.getWindow().close();
                LiveView.setLive(false);
                liveViewButton.setText("Start");
            }
        });
        return liveViewButton;
    }

    private Button setCreateSongView() {
        Button button = new Button("Add Song");
        button.setOnAction(e -> {
            CreateSongView createSongView = new CreateSongView();
            if (createSongView.display()) {
                songListView.populateSongList();
            }
        });
        return button;
    }

    private Button setMusixMatchButton() {
        MusixMatchControl controller = new MusixMatchControl();
        controller.auth();
        Button button = new Button("Search For Lyrics");
        button.setOnAction(e -> {
            if (controller.displaySearch()) {
                songListView.populateSongList();
            }
        });
        return button;
    }

    private Button setEditSongButton() {
        Button button = new Button("Edit Song");
        button.setOnAction(e -> {
            Song song = songListView.getSelectedSong();
            if (song != null) {
                CreateSongView createSongView = new CreateSongView();
                createSongView.setFields(song.getTitle(), song.getLyrics());
                createSongView.display();
                File file = StorageController.getFile(StorageController.SONGS_PATH, song.getTitle());
                song.setSlides(StorageController.getSlidesFromFile(file));
                SlidesListView slidesListView = songListView.getSlidesListView();
                if (slidesListView != null) {
                    slidesListView.display(song, slidesListView.getParent());
                }
            }
        });
        return button;
    }
}
