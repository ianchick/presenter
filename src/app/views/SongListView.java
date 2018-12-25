package app.views;

import app.Configurations;
import app.Mastermind;
import app.Session;
import app.models.Song;
import app.storage.StorageController;
import app.toolbars.ControlBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

public class SongListView {

    private Song selectedSong;
    private TextField searchBar;
    private ListView<Song> listView;
    private ObservableList<Song> songList;

    public SongListView init(SplitPane parent) {
        VBox content = new VBox();
        listView = new ListView<>();
        listView.setId("song_list_listview");
        VBox.setVgrow(listView, Priority.ALWAYS);
        initSearchBar();
        songList = FXCollections.observableArrayList();
        populateSongList();
        setSongListClickListener();
        Label label = new Label("Songs:");
        label.setId("list_label");
        content.getChildren().addAll(label, searchBar, listView);
        parent.getItems().add(content);
        return this;
    }

    private void setSongListClickListener() {
        listView.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (!listView.getSelectionModel().isEmpty()) {
                selectedSong = listView.getSelectionModel().getSelectedItem();
                Session.getInstance().setSelectedSong(selectedSong);
                SlidesListView slidesListView = Mastermind.getInstance().getSlidesListView();
                slidesListView.display(selectedSong);
                ControlBar.getEditSongButton().setDisable(false);
                ControlBar.getDeleteSongButton().setDisable(false);
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Song song = Session.getInstance().getSelectedSong();
                SetListQueueView setList = Mastermind.getInstance().getSetListQueueView();
                if (!setList.getQueue().contains(song)) {
                    setList.addSong(song);
                }
            }
        });
    }

    public void setSelectedSong(Song song) {
        listView.getSelectionModel().select(song);
    }

    private void initSearchBar() {
        searchBar = new TextField();
        searchBar.setPromptText("Search...");
    }

    public void populateSongList() {
        songList.addAll(getSongsFromFiles());
        FilteredList<Song> filteredList = new FilteredList<>(songList, data -> true);
        searchBar.textProperty().addListener(((observable, oldValue, newValue) -> filteredList.setPredicate(data -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseSearch = newValue.toLowerCase();
            return (data.getTitle().toLowerCase()).contains(lowerCaseSearch);
        })));
        SortedList<Song> sortedList = new SortedList<>(filteredList);
        sortedList.setComparator((s1, s2) -> s1.getTitle().compareToIgnoreCase(s2.getTitle()));
        listView.setItems(sortedList);
    }

    private ArrayList<Song> getSongsFromFiles() {
        ArrayList<Song> songs = new ArrayList<>();
        ArrayList<File> files = StorageController.getFilesFromDir(Configurations.getSongsPath());
        for (File file : files) {
            Song song = new Song(StorageController.convertFileNameToTitle(file.getName()));
            song.setSlides(StorageController.getSlidesFromFile(file));
            song.setLyricsFromSlides();
            ArrayList<String> songTitles = new ArrayList<>();
            for (Song s : songList) {
                songTitles.add(s.getTitle());
            }
            if (!songTitles.contains(song.getTitle())) {
                songList.add(song);
            }
        }
        return songs;
    }

    public void removeSong(Song song) {
        songList.remove(song);
    }
}