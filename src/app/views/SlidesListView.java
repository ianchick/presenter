package app.views;

import app.models.Slide;
import app.models.Song;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SlidesListView {

    private FlowPane flow;
    private static StackPane activeSlide;
    private ScrollPane parent;
    private Song song;

    public void display(Song song, ScrollPane parentScrollPane) {
        this.song = song;
        this.parent = parentScrollPane;
        flow = new FlowPane();
        flow.setId("slides_flow");
        if (song != null) {
            setSlides(song);
        }
        parent.setFitToHeight(true);
        parent.setFitToWidth(true);
        parent.setContent(flow);

        parent.viewportBoundsProperty().addListener((observableValue, oldBounds, newBounds) -> {
            flow.setPrefWidth(newBounds.getWidth());
            flow.setPrefHeight(newBounds.getHeight());
        });
    }

    private void setSlides(Song song) {
        flow.getChildren().clear();
        for (Slide slide : song.getSlides()) {
            StackPane pane = new StackPane();
            pane.setId("slide_preview_pane");
            Text text = new Text();
            text.setId("slide_preview_text");
            text.setText(slide.getContent());
            pane.getChildren().add(text);
            flow.getChildren().add(pane);
        }
        setSlideClickEvent();
    }

    private void setSlideClickEvent() {
        flow.getChildren().forEach(item -> item.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                Text text = (Text) ((StackPane) item).getChildren().get(0);
                EditSlideView editSlideView = new EditSlideView(song, this);
                editSlideView.display(song.getSlideFromText(text.getText()));
            } else {
                if (LiveView.isLive()) {
                    Text text = (Text) ((StackPane) item).getChildren().get(0);
                    LiveView.setCurrentSlide(text.getText());
                    if (activeSlide != null) {
                        activeSlide.setBorder(null);
                    }
                    activeSlide = (StackPane) item;
                    activeSlide.setBorder(new Border(new BorderStroke(Color.valueOf("#49E20E"), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(4))));
                }
            }
        }));
    }

    public static void unsetActiveSlide() {
        if (activeSlide != null) {
            activeSlide.setBorder(null);
        }
        activeSlide = null;
    }

    public ScrollPane getParent() {
        return parent;
    }
}
