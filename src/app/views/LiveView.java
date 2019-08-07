package app.views;

import app.Configurations;
import app.toolbars.ControlBar;
import app.views.slides.SlideViewController;
import app.views.slides.SlidesListView;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LiveView {

    private Stage window;
    private int fontSize = Configurations.getDefaultFontSize();
    private String fontName = Configurations.getDefaultFont();
    private int textAnimSpeed = 2000;

    private SlideViewController controller;

    private ImageView background;
    private ImageView transitionBackground;

    /**
     * Opens Live View window, sets title, handles close events, sets window size and key listener, and loads SlideView.fxml
     */
    public LiveView() {
        window = new Stage();
        window.setTitle("Live");
        window.setAlwaysOnTop(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/app/views/slides/SlideView.fxml"));
        try {
            window.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();

        window.widthProperty().addListener((obs, oldVal, newVal) -> {
            controller.slide_text.setWrappingWidth(window.getWidth() - controller.slide_pane.getInsets().getLeft());
        });

        window.getScene().getStylesheets().add(getClass().getResource("/app/styles/slides.css").toExternalForm());
        handleCloseEvents();
        setDisplaySizeOnScreen();
    }

    private void handleCloseEvents() {
        window.setOnHidden(e -> {
            e.consume();
            closeLiveView();
        });
        window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                closeLiveView();
            }
        });
    }

    private void closeLiveView() {
        controller.slide_text.setText("");
        controller.transition_text.setText("");
        SlidesListView.unsetActiveSlideBorders();
        ChangeBackgroundView.unsetActiveBackground();
        ControlBar.setImageButtonImage("play_button.png");
        window.close();
    }

    /**
     * Checks if there are external displays, full screen. Else, windowed mode.
     */
    private void setDisplaySizeOnScreen() {
        if (Screen.getScreens().size() > 1) {
            window.setFullScreen(true);
            window.setFullScreenExitHint("");
            Rectangle2D bounds = Screen.getScreens().get(1).getVisualBounds();
            window.setX(bounds.getMinX());
            window.setY(bounds.getMinY());
        } else {
            window.setHeight(700);
            window.setWidth(800);
        }
    }

    public void setFont(String font, int size) {
        fontSize = size;
        fontName = font;
        controller.slide_text.setFont(new Font(font, size));
    }

    public void setFont(int size) {
        setFont(fontName, size);
    }

    public void setFont(String font) {
        setFont(font, fontSize);
    }

    public void setCurrentSlide(String text) {
        controller.transition_text.setText(controller.slide_text.getText());
        controller.setFont(fontName, fontSize);
        controller.slide_text.setText(text);
        animateLyrics(controller.transition_text, controller.slide_text);
    }

    public Text getTextView() {
        return controller.slide_text;
    }

    public void setBackground(Image image) {
        if (background == null) {
            background = new ImageView();
            transitionBackground = new ImageView();
        }
        if (image == null) {
            transitionBackground.setImage(background.getImage());
            background.setImage(null);
        } else {
            controller.slide_pane.widthProperty().addListener((obs, oldVal, newVal) -> background.setFitWidth(newVal.doubleValue()));
            controller.slide_pane.heightProperty().addListener((obs, oldVal, newVal) -> background.setFitHeight(newVal.doubleValue()));
            background.setFitWidth(controller.slide_pane.getWidth());
            background.setFitHeight(controller.slide_pane.getHeight());
            background.preserveRatioProperty().setValue(false);
            controller.slide_pane.widthProperty().addListener((obs, oldVal, newVal) -> transitionBackground.setFitWidth(newVal.doubleValue()));
            controller.slide_pane.heightProperty().addListener((obs, oldVal, newVal) -> transitionBackground.setFitHeight(newVal.doubleValue()));
            transitionBackground.setFitWidth(controller.slide_pane.getWidth());
            transitionBackground.setFitHeight(controller.slide_pane.getHeight());
            transitionBackground.preserveRatioProperty().setValue(false);

            transitionBackground.setImage(background.getImage());
            background.setImage(image);
        }
        animateBackground(transitionBackground, background);
    }

    private void animateLyrics(Text oldView, Text newView) {
        FadeTransition oldft = new FadeTransition(Duration.millis(textAnimSpeed * .4), oldView);
        oldft.setFromValue(1);
        oldft.setToValue(0);
        oldft.play();
        FadeTransition newft = new FadeTransition(Duration.millis(textAnimSpeed), newView);
        newft.setFromValue(0);
        newft.setToValue(1);
        newft.play();
    }

    private void animateBackground(ImageView oldView, ImageView newView) {
        FadeTransition oldft = new FadeTransition(Duration.millis(1500), oldView);
        oldft.setFromValue(1);
        oldft.setToValue(0);
        oldft.play();
        FadeTransition newft = new FadeTransition(Duration.millis(2000), newView);
        newft.setFromValue(0);
        newft.setToValue(1);
        newft.play();
    }

    public Stage getWindow() {
        return window;
    }

    public void setTextAnimSpeed(int textAnimSpeed) {
        this.textAnimSpeed = textAnimSpeed;
    }
}
