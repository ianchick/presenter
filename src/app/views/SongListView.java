package app.views;

import app.models.Song;
import app.storage.StorageController;
import app.toolbars.ControlBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public class SongListView {

    private Song selectedSong;
    private SlidesListView slidesListView;
    private TextField searchBar;

    private ListView<Song> listView;

    public void display(Pane parent, ScrollPane slidesPane) {
        listView = new ListView<>();
        VBox.setVgrow(listView, Priority.ALWAYS);
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        listView.setId("song_list_listview");
        populateSongList();
        listView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (!listView.getSelectionModel().isEmpty()) {
                selectedSong = listView.getSelectionModel().getSelectedItem();
                setSlidesListView(slidesPane);
                ControlBar.getEditSongButton().setDisable(false);
            }
        });
        parent.getChildren().addAll(searchBar, listView);
    }

    public void populateSongList() {
        ObservableList<Song> rawData= FXCollections.observableArrayList(getSongs());
        FilteredList<Song> filteredList= new FilteredList<>(rawData, data -> true);
        listView.setItems(filteredList);
        searchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredList.setPredicate(data -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseSearch = newValue.toLowerCase();
                return (data.getTitle().toLowerCase()).contains(lowerCaseSearch);
            });
        }));
    }

    private ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        ArrayList<File> files = StorageController.getFilesFromDir(StorageController.SONGS_PATH);
        for (File file : files) {
            Song song = new Song(StorageController.convertFileNameToTitle(file.getName()));
            song.setSlides(StorageController.getSlidesFromFile(file));
            song.setLyricsFromSlides();
            songs.add(song);
        }
        return songs;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public SlidesListView getSlidesListView() {
        return slidesListView;
    }

    private void setSlidesListView(ScrollPane slidesPane) {
        if (slidesListView == null) {
            slidesListView = new SlidesListView();
        }
        slidesListView.display(selectedSong, slidesPane);
    }
}