package app;

import app.views.CreateSongView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class MusixMatchControl {

    private MusixMatch musixMatch;
    private boolean songSaved;

    public void auth() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("secrets.json")) {
            JSONObject obj = (JSONObject) parser.parse(reader);
            String apiKey = (String) obj.get("token");
            musixMatch = new MusixMatch(apiKey);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String fuzzySearchLyrics(String trackTitle, String artist) throws MusixMatchException {
        Track track = musixMatch.getMatchingTrack(trackTitle, artist);
        TrackData data = track.getTrack();
        int trackId = data.getTrackId();
        Lyrics lyrics = musixMatch.getLyrics(trackId);
        return lyrics.getLyricsBody();
    }

    public boolean displaySearch() {
        songSaved = false;
        Stage window = new Stage();
        window.setTitle("MusixMatch Search");
        window.initModality(Modality.APPLICATION_MODAL);
        VBox content = new VBox(10);
        Label titleLabel = new Label("Track Title");
        TextField title = new TextField();
        Label artistLabel = new Label("Artist Name");
        TextField artist = new TextField();
        Button submit = new Button("Search");
        submit.setOnAction(e -> {
            if (validations(title, artist)) {
                try {
                    String lyrics = fuzzySearchLyrics(title.getText(), artist.getText());
                    CreateSongView createSongView = new CreateSongView();
                    createSongView.setFields(title.getText(), lyrics);
                    songSaved = createSongView.display();
                } catch (MusixMatchException e1) {
                    e1.printStackTrace();
                }
                window.close();
            }
        });
        content.setPadding(new Insets(10));
        content.getChildren().addAll(titleLabel, title, artistLabel, artist, submit);
        window.setScene(new Scene(content));
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
