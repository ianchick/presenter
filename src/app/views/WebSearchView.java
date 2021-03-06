package app.views;

import app.views.dialogs.CreateSongView;
import app.webcontrollers.WebScrapingController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WebSearchView {

    private boolean songSaved;

    public boolean displaySearch() {
        songSaved = false;
        Stage window = new Stage();
        window.setTitle("Web Search");
        window.initModality(Modality.APPLICATION_MODAL);
        VBox content = new VBox(10);
        Text errorText = new Text();
        errorText.setFill(Color.RED);
        errorText.setManaged(false);
        Label titleLabel = new Label("Track Title");
        TextField title = new TextField();
        Label artistLabel = new Label("Artist Name");
        TextField artist = new TextField();
        Button submit = new Button("Search");
        submit.setOnAction(e -> {
            if (validations(title, artist)) {
                WebScrapingController controller = new WebScrapingController();
                String lyrics = controller.getLyrics(artist.getText(), title.getText());
                if (lyrics != null) {
                    CreateSongView createSongView = new CreateSongView();
                    createSongView.setFields(title.getText(), lyrics);
                    songSaved = createSongView.display();
                    window.close();
                } else {
                    errorText.setManaged(true);
                    errorText.setText("Song and lyrics not found");
                }
            }
        });
        content.setPadding(new Insets(10));
        content.getChildren().addAll(errorText, titleLabel, title, artistLabel, artist, submit);
        window.setScene(new Scene(content, 300, 220));
        window.showAndWait();
        return songSaved;
    }

    private boolean validations(TextField title, TextField artist) {
        boolean isValid = true;
        if (title.getText().isEmpty()) {
            title.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            isValid = false;
        }
        if (artist.getText().isEmpty()) {
            artist.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
            isValid = false;
        }
        return isValid;
    }
}
