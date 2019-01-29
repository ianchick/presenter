package app.toolbars;

import app.Configurations;
import app.Mastermind;
import app.Session;
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
import java.util.Optional;

public class ControlBar {

    private static Button liveViewButton;
    private static SongListView songListView;
    private static Button editSongButton;
    private static Button deleteSongButton;

    public static ToolBar setup() {
        ToolBar toolBar = new ToolBar();
        songListView = Mastermind.getInstance().getSongListView();

        Button startLive = setLiveView();
        Button addSong = setCreateSongView();

        Label fontSizeLabel = new Label("Text Size:");
        ComboBox<String> fontSizeCombo = setFontSizeComboBox();
        ComboBox<String> fontCombo = setFontComboBox();
        Button searchWebButton = setWebSearchButton();

        Label animSpeedLabel = new Label("Animation Speed:");
        ComboBox<Integer> animSpeedCombo = setAnimSpeedComboBox();

        setEditSongButton();
        setDeleteSongButton();

        toolBar.getItems().addAll(startLive, addSong, new Separator(Orientation.VERTICAL),
                fontSizeLabel, fontSizeCombo, fontCombo, new Separator(Orientation.VERTICAL),
                searchWebButton, new Separator(Orientation.VERTICAL),
                editSongButton, deleteSongButton, new Separator(Orientation.VERTICAL),
                animSpeedLabel, animSpeedCombo);

        return toolBar;
    }

    private static ComboBox<Integer> setAnimSpeedComboBox() {
        ObservableList<Integer> options = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ComboBox<Integer> animSpeedCombo = new ComboBox<>(options);
        animSpeedCombo.getSelectionModel().select(4);
        animSpeedCombo.setOnAction(e -> {
            int speedMilli = animSpeedCombo.getValue() * 400;
            LiveView.setTextAnimSpeed(speedMilli);
        });
        return animSpeedCombo;
    }

    private static ComboBox<String> setFontSizeComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("24", "32", "40", "48", "56", "64", "72", "80", "88", "96", "104");
        ComboBox<String> fontSizeCombo = new ComboBox<>(options);
        fontSizeCombo.getSelectionModel().select(String.valueOf(Configurations.getDefaultFontSize()));
        fontSizeCombo.setOnAction(e -> {
            LiveView.setTextSize(Integer.valueOf(fontSizeCombo.getValue()));
            if (LiveView.isLive()) {
                LiveView.setFontSize(LiveView.getTextView().getText());
            }
        });
        return fontSizeCombo;
    }

    private static ComboBox<String> setFontComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("Arial", "Courier New", "Helvetica", "Times New Roman");
        ComboBox<String> fontCombo = new ComboBox<>(options);
        fontCombo.getSelectionModel().select(Configurations.getDefaultFont());
        fontCombo.setOnAction(e -> {
            LiveView.setFont(fontCombo.getValue());
            if (LiveView.isLive()) {
                LiveView.setFontSize(LiveView.getTextView().getText());
            }
        });
        return fontCombo;
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
        editSongButton.setTooltip(new Tooltip("Edit the currently selected song. Slides are split by empty lines.\nRight click on slides to edit individually. Edits will not display real time.\nAfter saving edits, reselect slide to display changes"));
        editSongButton.setDisable(true);
        editSongButton.setOnAction(e -> {
            Song song = Session.getInstance().getSelectedSong();
            if (song != null) {
                CreateSongView createSongView = new CreateSongView();
                createSongView.setFields(song.getTitle(), song.getLyrics());
                createSongView.display();
                File file = StorageController.getFile(Configurations.getSongsPath(), song.getTitle());
                song.setSlides(StorageController.getSlidesFromFile(file));
                song.setLyricsFromSlides();
                SlidesListView slidesListView = Mastermind.getInstance().getSlidesListView();
                if (slidesListView != null) {
                    slidesListView.display(song);
                }
                NavigationBar.refresh();
            }
        });
        return editSongButton;
    }

    private static void setDeleteSongButton() {
        deleteSongButton = new Button("Delete Song");
        deleteSongButton.setDisable(true);
        deleteSongButton.setOnAction(e -> {
            if (confirmDelete()) {
                Song song = Session.getInstance().getSelectedSong();
                if (song != null) {
                    StorageController.deleteFile(StorageController.convertTitleToFileName(song.getTitle()));
                    Mastermind.getInstance().getSongListView().removeSong(song);
                    Mastermind.getInstance().getSetListQueueView().removeSong(song);
                    editSongButton.setDisable(true);
                    deleteSongButton.setDisable(true);
                    NavigationBar.refresh();
                    Mastermind.getInstance().getSlidesListView().clear();
                }
            }
        });
    }

    public static Button getEditSongButton() {
        return editSongButton;
    }

    public static Button getDeleteSongButton() {
        return deleteSongButton;
    }

    private static boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Song");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Are you sure you want to delete this song?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
