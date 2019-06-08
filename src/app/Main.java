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
        Session.init();
        Mastermind.init();
        init(window);
        window.show();
    }

    private void init(Stage window) {
        BorderPane root = TheRootView.init();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        window.setScene(scene);
        window.setTitle("Amateur Presenter");
        window.setMaximized(true);
        window.getIcons().add(new Image("icon.png"));
        window.setOnCloseRequest(e -> System.exit(0));
    }
}