package app.views;

import app.Configurations;
import app.Mastermind;
import app.storage.StorageController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;

public class ChangeBackgroundView {

    private FlowPane flow;
    private static StackPane activeBackground;

    public void display(ScrollPane parentScrollPane) {
        flow = new FlowPane();
        flow.setId("background_flow");
        Tooltip.install(flow, new Tooltip("Select slide to set background image.\nSelect same slide to remove background"));
        parentScrollPane.setId("background_scroll_pane");
        parentScrollPane.setFitToHeight(true);
        parentScrollPane.setFitToWidth(true);
        parentScrollPane.setContent(flow);
        setBackgroundSlides();
    }

    private void setBackgroundSlides() {
        ArrayList<File> backgroundFiles = StorageController.getFilesFromDir(Configurations.getBackgroundsPath());
        for (File file : backgroundFiles) {
            StackPane pane = new StackPane();
            ImageView imageView = new ImageView();
            pane.setId("background_preview_pane");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            pane.getChildren().add(imageView);
            imageView.setFitWidth(300);
            imageView.setFitHeight(150);
            imageView.preserveRatioProperty().setValue(false);
            pane.setOnMouseClicked(e -> {
                if (Mastermind.getInstance().liveViewIsShowing()) {
                    if (activeBackground != pane) {
                        Mastermind.getInstance().getLiveView().setBackground(image);
                        if (activeBackground != null) {
                            activeBackground.setBorder(null);
                        }
                        activeBackground = pane;
                        activeBackground.setBorder(new Border(new BorderStroke(Color.valueOf("#49E20E"), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(4))));
                    } else {
                        activeBackground.setBorder(null);
                        activeBackground = null;
                        Mastermind.getInstance().getLiveView().setBackground(null);
                    }
                }
            });
            flow.getChildren().add(pane);
        }
    }

    public static void unsetActiveBackground() {
        if (activeBackground != null) {
            activeBackground.setBorder(null);
        }
        activeBackground = null;
    }
}
