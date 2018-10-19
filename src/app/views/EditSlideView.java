package app.views;

import app.models.Slide;
import app.models.Song;
import app.storage.StorageController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class EditSlideView {

    private Song song;
    private boolean saved;
    private SlidesListView context;

    public EditSlideView(Song song, SlidesListView context) {
        this.context = context;
        this.song = song;
    }

    public boolean display(Slide slide) {
        saved = false;
        Stage window = new Stage();
        window.setTitle("Edit Slide");
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(10));
        TextArea content = new TextArea();
        content.setText(slide.getContent());
        Button submit = new Button("Save");
        submit.setOnAction(e -> {
            slide.setContent(content.getText());
            song.setLyricsFromSlides();
            saved = StorageController.saveFile(StorageController.SONGS_PATH, song.getTitle(), song.getLyrics());
            window.close();
            File file = StorageController.getFile(StorageController.SONGS_PATH, song.getTitle());
            song.setSlides(StorageController.getSlidesFromFile(file));
            context.display(song, context.getParent());
        });
        vbox.getChildren().addAll(content, submit);
        window.setScene(new Scene(vbox));
        window.show();
        return saved;
    }
}
