package app.views;

import app.Mastermind;
import app.webcontrollers.ESVController;
import javafx.geometry.Insets;
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

    private String rawPassageText;

    public void display(ScrollPane parent) {
        ESVController esvController = new ESVController();
        VBox wrapper = new VBox();
        wrapper.setSpacing(8);
        TextField queryTextField = new TextField();
        queryTextField.setPromptText("Reference...");
        Button search = new Button("Search");
        search.setOnMouseClicked(event -> searchBibleReference(esvController, queryTextField));
        headersCheckbox = new CheckBox("Headers");
        footnotesCheckbox = new CheckBox("Footnotes");
        HBox bibleSearchHBox = new HBox(queryTextField, search, headersCheckbox, footnotesCheckbox);
        bibleSearchHBox.setAlignment(Pos.CENTER_LEFT);
        bibleSearchHBox.setSpacing(4);
        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setDisable(rawPassageText == null);
        TextField versesPerSlideInput = new TextField();
        versesPerSlideInput.setPromptText("Enter # of verses to show on each slide...");
        versesPerSlideInput.setPrefWidth(400);
        versesPerSlideInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                versesPerSlideInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        Button setFormatButton = new Button("Format Verses");
        setFormatButton.setOnMouseClicked(event -> {
            if (rawPassageText != null && !textArea.getText().isEmpty() && !versesPerSlideInput.getText().isEmpty()) {
                CreateSongView createSongView = new CreateSongView();
                createSongView.setFields(queryTextField.getText(), formatPassageByVerse(versesPerSlideInput));
                if (createSongView.display()) {
                    Mastermind.getInstance().getSongListView().populateSongList();
                }
            }
        });

        HBox formattingBox = new HBox(versesPerSlideInput, setFormatButton);
        formattingBox.setSpacing(4);

        wrapper.getChildren().addAll(new Label("Bible Search"), bibleSearchHBox, textArea, formattingBox);
        parent.setPadding(new Insets(4));
        parent.setId("bible_pane");
        parent.setContent(wrapper);
    }

    private void searchBibleReference(ESVController esvController, TextField queryTextField) {
        try {
            // Cache verses into rawPassageText
            rawPassageText = esvController.getText(queryTextField.getText(), headersCheckbox.isSelected(), footnotesCheckbox.isSelected());
            textArea.setText(rawPassageText);
            textArea.setDisable(rawPassageText == null);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String formatPassageByVerse(TextField versesPerSlideInput) {
        String stripped = rawPassageText.replace("\n", "").replace(" +", " ").trim();
        String[] splitVerses = stripped.split("\\[.*?]");
        StringBuilder output = new StringBuilder();
        int count = 0;
        for (String verse : splitVerses) {
            output.append(verse);
            count++;
            if (count >= Integer.valueOf(versesPerSlideInput.getText())) {
                count = 0;
                output.append("\\n\\n");
            }
        }
        return output.toString();
    }


}