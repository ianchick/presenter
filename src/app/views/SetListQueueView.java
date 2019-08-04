package app.views;

import app.Mastermind;
import app.models.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SetListQueueView {

    private ListView<Song> listView;
    private ObservableList<Song> queue;

    public ObservableList<Song> getQueue() {
        return queue;
    }

    public SetListQueueView init(SplitPane parent) {
        VBox content = new VBox();
        listView = new ListView<>();
        listView.setTooltip(new Tooltip("Double click item to remove from set list"));
        listView.setId("song_list_listview");
        VBox.setVgrow(listView, Priority.ALWAYS);
        Label label = new Label("Set List:");
        label.setId("list_label");
        content.getChildren().addAll(label, listView);
        parent.getItems().add(content);
        queue = FXCollections.observableArrayList();
        listView.setItems(queue);
        setListViewClickListener();
        return this;
    }

    private void setListViewClickListener() {
        listView.setOnMouseClicked(event -> {
            Mastermind.getInstance().getSongListView().getSearchBar().clear();
            Song song = listView.getSelectionModel().getSelectedItem();
            Mastermind.getInstance().setSelectedSong(song);
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
