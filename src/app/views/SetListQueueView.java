package app.views;

import app.Mastermind;
import app.Session;
import app.models.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SetListQueueView {

    private ListView<Song> listView;
    private ObservableList<Song> queue;

    public ObservableList<Song> getQueue() {
        return queue;
    }

    public SetListQueueView init(Pane parent) {
        listView = new ListView<>();
        listView.setId("song_list_listview");
        VBox.setVgrow(listView, Priority.ALWAYS);
        parent.getChildren().addAll(listView);
        queue = FXCollections.observableArrayList();
        listView.setItems(queue);
        setListViewClickListener();
        return this;
    }

    private void setListViewClickListener() {
        listView.setOnMouseClicked(event -> {
            Song song = listView.getSelectionModel().getSelectedItem();
            Session.getInstance().setSelectedSong(song);
            Mastermind.getInstance().getSongListView().setSelectedSong(song);
            if (event.getClickCount() == 2) {
                removeSong(song);
            }
        });
    }

    public void addSong(Song song) {
        queue.add(song);
    }

    public void removeSong(Song song) {
        queue.remove(song);
    }
}
