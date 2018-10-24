package app.toolbars;

import app.models.Song;
import app.storage.StorageController;
import app.views.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ControlBar {

    private static Button liveViewButton;
    private static SongListView songListView;
    private static Button editSongButton;
    private static Button deleteSongButton;

    public static ToolBar setup(SongListView songList) {
        ToolBar toolBar = new ToolBar();
        songListView = songList;

        Button startLive = setLiveView();
        Button addSong = setCreateSongView();

        Label fontSizeLabel = new Label("Text Size");
        ComboBox<String> fontSizeCombo = setFontSizeComboBox();
        Button searchWebButton = setWebSearchButton();

        setEditSongButton();
        setDeleteSongButton();

        toolBar.getItems().addAll(startLive, addSong, new Separator(Orientation.VERTICAL),
                fontSizeLabel, fontSizeCombo, new Separator(Orientation.VERTICAL),
                searchWebButton, new Separator(Orientation.VERTICAL),
                editSongButton, deleteSongButton);

        return toolBar;
    }

    private static ComboBox<String> setFontSizeComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("24", "32", "40", "48", "56", "64", "72", "80", "88", "96", "104");
        ComboBox<String> fontSizeCombo = new ComboBox<>(options);
        fontSizeCombo.getSelectionModel().select(5);
        fontSizeCombo.setOnAction(e -> {
            LiveView.setTextSize(Integer.valueOf(fontSizeCombo.getValue()));
            if (LiveView.isLive()) {
                LiveView.setFontSize(LiveView.getTextView().getText());
            }
        });
        return fontSizeCombo;
    }

    private static Button setLiveView() {
        if (liveViewButton == null) {
            liveViewButton = new Button();
            Image buttonImage = new Image("play_button.png");
            ImageView imageView = new ImageView(buttonImage);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.preserveRatioProperty().setValue(false);
            liveViewButton.setGraphic(imageView);
        }
        liveViewButton.setOnAction(e -> {
            if (!LiveView.isLive()) {
                LiveView.display();
                LiveView.setLive(true);
                setImageButtonImage("stop_button.png");
            } else {
                LiveView.getWindow().close();
                LiveView.setLive(false);
                setImageButtonImage("play_button.png");
            }
        });
        return liveViewButton;
    }

    public static void setImageButtonImage(String fileName) {
        ((ImageView) liveViewButton.getGraphic()).setImage(new Image(fileName));
    }

    private static Button setCreateSongView() {
        Button button = new Button("Add Song");
        button.setOnAction(e -> {
            CreateSongView createSongView = new CreateSongView();
            if (createSongView.display()) {
                songListView.populateSongList();
            }
        });
        return button;
    }

    private static Button setWebSearchButton() {
        WebSearchView webSearchView = new WebSearchView();
        Button button = new Button("Search For Lyrics");
        button.setOnAction(e -> {
            if (webSearchView.displaySearch()) {
                songListView.populateSongList();
            }
        });
        return button;
    }

    private static Button setEditSongButton() {
        editSongButton = new Button("Edit Song");
        editSongButton.setDisable(true);
        editSongButton.setOnAction(e -> {
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
        return editSongButton;
    }

    private static Button setDeleteSongButton() {
        deleteSongButton = new Button("Delete Song");
        deleteSongButton.setDisable(true);
        deleteSongButton.setOnAction(e -> {
            Song song = songListView.getSelectedSong();
            if (song != null) {
                StorageController.deleteFile(StorageController.convertTitleToFileName(song.getTitle()));
                editSongButton.setDisable(true);
                deleteSongButton.setDisable(true);
                NavigationBar.refresh();
            }
        });
        return deleteSongButton;
    }

    public static Button getEditSongButton() {
        return editSongButton;
    }

    public static Button getDeleteSongButton() {
        return deleteSongButton;
    }
}
