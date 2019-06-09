package app.views;

import app.webcontrollers.ESVController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class BibleSlidesView {

    public void display(ScrollPane parent) {
        ESVController esvController = new ESVController();
        try {
            TextArea print = new TextArea(esvController.getText());
            parent.setContent(print);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
