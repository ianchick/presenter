package app;

import app.models.Song;
import app.storage.StorageController;
import app.views.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private SongListView songListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.setId("root");

        ToolBar toolBar = new ToolBar();
        VBox listSongsBox = new VBox();
        ScrollPane slidesPane = new ScrollPane();
        ScrollPane backgroundPane = new ScrollPane();

        setChangeBackgroundView(backgroundPane);
        setSongListView(listSongsBox, slidesPane);
        setToolBar(toolBar);

        root.setTop(toolBar);
        root.setLeft(listSongsBox);
        root.setCenter(slidesPane);
        root.setBottom(backgroundPane);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        window.setScene(scene);
        window.setTitle("Slides");
        window.setMaximized(true);
        window.show();

        window.setOnCloseRequest(e -> System.exit(0));
    }

    private void setChangeBackgroundView(ScrollPane backgroundPane) {
        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);
    }

    private void setToolBar(ToolBar toolBar) {
        Button startLive = setLiveView();
        Button addSong = setCreateSongView();

        Label fontSizeLabel = new Label("Text Size");
        ObservableList<String> options = FXCollections.observableArrayList("24", "32", "40", "48", "56", "64", "72", "80", "88", "96");
        ComboBox<String> fontSizeCombo = new ComboBox<>(options);
        fontSizeCombo.setOnAction(e -> {
            LiveView.setTextSize(Integer.valueOf(fontSizeCombo.getValue()));
            if (LiveView.isLive()) {
                LiveView.setCurrentSlide(LiveView.getTextView().getText());
            }
        });

        Button searchMusixMatch = setMusixMatchButton();

        Button editSong = setEditSongButton();

        toolBar.getItems().addAll(startLive, addSong, new Separator(Orientation.VERTICAL),
                fontSizeLabel, fontSizeCombo, new Separator(Orientation.VERTICAL),
                searchMusixMatch, new Separator(Orientation.VERTICAL),
                editSong);
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

    private void setSongListView(Pane parent, ScrollPane slidesPane) {
        songListView = new SongListView();
        songListView.display(parent, slidesPane);
    }

    private Button setLiveView() {
        Button button = new Button("Start");
        button.setId("start_live_btn");
        button.setOnAction(e -> LiveView.display());
        return button;
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
}
