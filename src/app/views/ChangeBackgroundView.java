package app.views;

import app.storage.StorageController;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.ArrayList;

public class ChangeBackgroundView {

    private FlowPane flow;
    private ScrollPane parent;
    private StackPane activeBackground;

    public void display(ScrollPane parentScrollPane) {
        flow = new FlowPane();
        flow.setPadding(new Insets(10));
        parent = parentScrollPane;
        parent.setId("background_scroll_pane");
        parent.setFitToHeight(true);
        parent.setFitToWidth(true);
        parent.setContent(flow);

        setBackgroundSlides();
    }

    public void setBackgroundSlides() {
        ArrayList<File> backgroundFiles = StorageController.getFilesFromDir(StorageController.BACKGROUNDS_PATH);
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
                if (LiveView.isLive()) {
                    if (activeBackground != pane) {
                        LiveView.setBackground(image);
                        activeBackground = pane;
                    } else {
                        activeBackground = null;
                        LiveView.setBackground(null);
                    }
                }
            });
            flow.getChildren().add(pane);
        }
    }
}
