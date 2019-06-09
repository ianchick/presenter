package app.views;

import app.Mastermind;
import app.webcontrollers.ESVController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class BibleSlidesView {

    private TextArea textArea;
    private CheckBox headersCheckbox;
    private CheckBox footnotesCheckbox;

    public void display(ScrollPane parent) {
        ESVController esvController = new ESVController();

        VBox wrapper = new VBox();

        TextField queryTextField = new TextField();
        queryTextField.setPromptText("Reference...");
        Button search = new Button("Search");
        search.setOnMouseClicked(event -> {
            try {
                textArea.setText(esvController.getText(queryTextField.getText(), headersCheckbox.isSelected(), footnotesCheckbox.isSelected()));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        });
        headersCheckbox = new CheckBox("Headers");
        footnotesCheckbox = new CheckBox("Footnotes");

        HBox bibleSearchHBox = new HBox(queryTextField, search, headersCheckbox, footnotesCheckbox);
        bibleSearchHBox.setAlignment(Pos.CENTER_LEFT);
        bibleSearchHBox.setSpacing(4);
        textArea = new TextArea();
        textArea.setWrapText(true);

        TextField versesPerSlideInput = new TextField();
        versesPerSlideInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                versesPerSlideInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        Button setFormatButton = new Button("Format Verses");
        setFormatButton.setOnMouseClicked(event -> {
            String stripped = textArea.getText().replace("\n", "").replace(" +", " ").trim();
            String[] splitVerses = stripped.split("\\[.*?]");
            StringBuilder output = new StringBuilder();
            int count = 0;
            for (String verse : splitVerses) {
                if (count < Integer.valueOf(versesPerSlideInput.getText())) {
                    output.append(verse);
                    count++;
                } else {
                    count = 0;
                    output.append("\\n\\n");
                }
            }
            textArea.setText(output.toString());
        });

        HBox formattingBox = new HBox(versesPerSlideInput, setFormatButton);

        Button createSlidesButton = new Button("Create Slides");
        createSlidesButton.setOnMouseClicked(event -> {
            CreateSongView createSongView = new CreateSongView();
            createSongView.setFields(queryTextField.getText(), textArea.getText());
            if (createSongView.display()) {
                Mastermind.getInstance().getSongListView().populateSongList();
            }
        });

        wrapper.getChildren().addAll(bibleSearchHBox, textArea, formattingBox, createSlidesButton);
        parent.setContent(wrapper);
    }
}
