package app.views.slides;

import app.Mastermind;
import app.models.Slide;
import app.models.Song;
import app.views.dialogs.EditSlideView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class SlidesListView {

    private static StackPane activeSlide;
    private static int activeSlideIndex;
    private FlowPane slidesFlowPane;
    private ScrollPane parentScrollPane;
    private Song song;

    public static void unsetActiveSlideBorders() {
        if (activeSlide != null) {
            activeSlide.setBorder(null);
        }
        activeSlide = null;
    }

    public SlidesListView init(ScrollPane parent) {
        this.parentScrollPane = parent;
        display(null);
        return this;
    }

    public void display(Song song) {
        this.song = song;
        if (slidesFlowPane == null) {
            slidesFlowPane = new FlowPane();
        }
        slidesFlowPane.setId("slides_flow");
        if (song != null) {
            setSlides(song);
        }
        parentScrollPane.setFitToHeight(true);
        parentScrollPane.setFitToWidth(true);
        parentScrollPane.setContent(slidesFlowPane);

        parentScrollPane.viewportBoundsProperty().addListener((observableValue, oldBounds, newBounds) -> {
            slidesFlowPane.setPrefWidth(newBounds.getWidth());
            slidesFlowPane.setPrefHeight(newBounds.getHeight());
        });
    }

    public void clear() {
        slidesFlowPane.getChildren().clear();
    }

    private void setSlides(Song song) {
        slidesFlowPane.getChildren().clear();
        for (Slide slide : song.getSlides()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SlideView.fxml"));
            try {
                slidesFlowPane.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((SlideViewController)loader.getController()).setSlideContent(slide.getContent());
        }
        activeSlideIndex = -1;
        activeSlide = null;
        setSlideClickEvent();
    }

    private void setSlideClickEvent() {
        slidesFlowPane.getChildren().forEach(item -> item.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                Text text = (Text) item.lookup("#slide_text");
                EditSlideView editSlideView = new EditSlideView(song);
                editSlideView.display(song.getSlideFromText(text.getText()));
            } else {
                if (Mastermind.getInstance().liveViewIsShowing()) {
                    Text text = (Text) item.lookup("#slide_text");
                    Mastermind.getInstance().getLiveView().setCurrentSlide(text.getText());
                    setActiveSlide((StackPane) item);
                    activeSlideIndex = slidesFlowPane.getChildren().indexOf(item);
                }
            }
        }));
    }

    private void setActiveSlide(StackPane item) {
        if (activeSlide != null) {
            activeSlide.setBorder(null);
        }
        activeSlide = item;
        activeSlide.setBorder(new Border(new BorderStroke(Color.valueOf("#49E20E"), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(4))));
    }

    public void previousSlide() {
        // No slide is selected, go to index 0 to select first slide.
        if (activeSlideIndex == -1) {
            nextSlide();
        }
        if (Mastermind.getInstance().liveViewIsShowing() && activeSlideIndex > 0) {
            activeSlideIndex = activeSlideIndex - 1;
            String lyric = song.getSlides().get(activeSlideIndex).getContent();
            setActiveSlide((StackPane) slidesFlowPane.getChildren().get(activeSlideIndex));
            Mastermind.getInstance().getLiveView().setCurrentSlide(lyric);
        }
    }

    public void nextSlide() {
        if (Mastermind.getInstance().liveViewIsShowing() && activeSlideIndex < slidesFlowPane.getChildren().size() - 1) {
            activeSlideIndex = activeSlideIndex + 1;
            String lyric = song.getSlides().get(activeSlideIndex).getContent();
            setActiveSlide((StackPane) slidesFlowPane.getChildren().get(activeSlideIndex));
            Mastermind.getInstance().getLiveView().setCurrentSlide(lyric);
        }
    }

    public void currentSlide() {
        if (Mastermind.getInstance().liveViewIsShowing()) {
            String lyric = song.getSlides().get(activeSlideIndex).getContent();
            setActiveSlide((StackPane) slidesFlowPane.getChildren().get(activeSlideIndex));
            Mastermind.getInstance().getLiveView().setCurrentSlide(lyric);
        }
    }
}
