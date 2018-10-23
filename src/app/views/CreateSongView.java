package app.views;

import app.storage.StorageController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateSongView {

    private TextField titleField;
    private TextArea lyricsField;
    private boolean songSaved;

    public boolean display() {
        songSaved = false;
        Stage window = new Stage();
        window.setTitle("Add Song");
        window.initModality(Modality.APPLICATION_MODAL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label titleLabel = new Label("Song Title");
        if (titleField == null) {
            titleField = new TextField();
        }
        Label lyricsLabel = new Label("Lyrics");
        if (lyricsField == null) {
            lyricsField = new TextArea();
        }
        Button submit = new Button("Save");
        submit.setOnAction(e -> {
            if (validations()) {
                songSaved = StorageController.saveFile(StorageController.SONGS_PATH, titleField.getText(), lyricsField.getText());
                window.close();
            }
        });

        content.getChildren().addAll(titleLabel, titleField, lyricsLabel, lyricsField, submit);
        Scene scene = new Scene(content);
        window.setScene(scene);
        window.showAndWait();
        return songSaved;
    }

    private boolean validations() {
        boolean isValid = true;
        if (titleField.getText().isEmpty()) {
            titleField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            isValid = false;
        }
        if (lyricsField.getText().isEmpty()) {
            lyricsField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            isValid = false;
        }
        return isValid;
    }

    public void setFields(String title, String lyrics) {
        if (titleField == null) {
            titleField = new TextField();
        }
        if (lyricsField == null) {
            lyricsField = new TextArea();
        }
        titleField.setText(title);
        lyricsField.setText(lyrics);
    }
}
