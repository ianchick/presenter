package app.views;

import app.webcontrollers.ESVController;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;

public class BibleSlidesView {

    private TextArea textArea;
    private CheckBox headersCheckbox;
    private CheckBox footnotesCheckbox;

    public void display(ScrollPane parent) {
        ESVController esvController = new ESVController();

        VBox wrapper = new VBox();

        TextField queryTextField = new TextField();
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
        textArea = new TextArea();
        textArea.setWrapText(true);
        wrapper.getChildren().addAll(bibleSearchHBox, textArea);
        parent.setContent(wrapper);

    }
}
