package app;

import app.views.TheRootView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws IOException {
        Configurations.setup();
        Mastermind.init(window);
        init(window);
        window.show();
    }

    private void init(Stage window) {
        BorderPane root = TheRootView.init();
        Scene scene = new Scene(root);
        String songlistStyles = getClass().getResource("styles/songs.css").toExternalForm();
        String slidesStyles = getClass().getResource("styles/slides.css").toExternalForm();
        String biblesearchStyles = getClass().getResource("styles/bible.css").toExternalForm();
        String backgroundStyles = getClass().getResource("styles/backgrounds.css").toExternalForm();
        String genericStyles = getClass().getResource("styles/style.css").toExternalForm();

        scene.getStylesheets().addAll(songlistStyles, slidesStyles, biblesearchStyles, backgroundStyles, genericStyles);
        window.setScene(scene);
        window.setTitle("Amateur Presenter");
        window.setMaximized(true);
        window.getIcons().add(new Image("icon.png"));
        window.setOnCloseRequest(e -> System.exit(0));
    }
}