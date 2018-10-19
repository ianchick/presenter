package app.views;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LiveView {

    private static Text textView;
    private static boolean isLive;
    private static Stage window;
    private static int textSize = 64;

    private static ImageView background;
    private static StackPane contentPane;

    public static void display() {
        setLive(true);
        setup();
        window.show();
    }

    private static void setup() {
        window = new Stage();
        window.setTitle("Live");
        window.setAlwaysOnTop(true);
        // Stay focused for keyboard input when Live
        window.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (isLive()) {
                if (!newValue) {
                    if (!window.isFocused()) {
                        window.requestFocus();
                    }
                }
            }
        });
        handleCloseEvents();
        setDisplaySizeOnScreen();
        contentPane = new StackPane();
        contentPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        setBackground(null);
        textView = new Text();
        textView.setFill(Color.WHITE);
        textView.setTextAlignment(TextAlignment.CENTER);
        textView.setFont(new Font("Arial", textSize));
        contentPane.getChildren().add(background);
        contentPane.getChildren().add(textView);
        window.setScene(new Scene(contentPane));
    }

    public static boolean isLive() {
        return isLive;
    }

    private static void setLive(boolean live) {
        isLive = live;
    }

    private static void handleCloseEvents() {
        window.setOnCloseRequest(e -> {
            e.consume();
            closeLiveView();
        });
        window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                closeLiveView();
            }
        });
    }

    private static void closeLiveView() {
        setLive(false);
        SlidesListView.unsetActiveSlide();
        window.close();
    }

    /**
     * Checks if there are external displays, full screen. Else, windowed mode.
     */
    private static void setDisplaySizeOnScreen() {
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

    public static void setCurrentSlide(String text) {
        textView.setFont(new Font("Arial", textSize));
        textView.setText(text);
    }

    public static void setTextSize(int size) {
        textSize = size;
    }

    public static Text getTextView() {
        return textView;
    }

    public static void setBackground(Image image) {
        if (background == null) {
            background = new ImageView();
        }
        if (image == null) {
            background.setImage(null);
            background.setVisible(false);
        } else {
            background.setVisible(true);
            contentPane.widthProperty().addListener((obs, oldVal, newVal) -> background.setFitWidth(newVal.doubleValue()));
            contentPane.heightProperty().addListener((obs, oldVal, newVal) -> background.setFitHeight(newVal.doubleValue()));
            background.setFitWidth(contentPane.getWidth());
            background.setFitHeight(contentPane.getHeight());
            background.preserveRatioProperty().setValue(false);
            background.setImage(image);
        }
    }
}
