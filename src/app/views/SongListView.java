package app.views;

import app.models.Song;
import app.storage.StorageController;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public class SongListView {

    private ArrayList<Song> songList;
    private Song selectedSong;
    private SlidesListView slidesListView;

    private ListView<Song> listView;

    public void display(Pane parent, ScrollPane slidesPane) {
        listView = new ListView<>();
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setId("song_list_listview");
        populateSongList();
        listView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (!listView.getSelectionModel().isEmpty()) {
                selectedSong = listView.getSelectionModel().getSelectedItem();
                setSlidesListView(slidesPane);
            }
        });
        parent.getChildren().add(listView);
    }

    public void populateSongList() {
        songList = getSongs();
        listView.setItems(FXCollections.observableArrayList(songList));
    }

    private void setSlidesListView(ScrollPane slidesPane) {
        slidesListView = new SlidesListView();
        slidesListView.display(selectedSong, slidesPane);
    }

    private ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        ArrayList<File> files = StorageController.getFilesFromDir(StorageController.SONGS_PATH);
        if (files != null) {
            for (File file : files) {
                Song song = new Song(StorageController.convertFileNameToTitle(file.getName()));
                song.setSlides(StorageController.getSlidesFromFile(file));
                song.setLyricsFromSlides();
                songs.add(song);
            }
        }
        return songs;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public SlidesListView getSlidesListView() {
        return slidesListView;
    }
}