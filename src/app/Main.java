package app;

import app.toolbars.ControlBar;
import app.toolbars.NavigationBar;
import app.views.ChangeBackgroundView;
import app.views.LiveView;
import app.views.SongListView;
import app.views.TheRootView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws IOException {
        Configurations configurations = new Configurations();
        configurations.setup();
        init(window);
        window.show();
    }

    private void init(Stage window) {
        BorderPane root = new TheRootView().init();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        window.setScene(scene);
        window.setTitle("Amateur Presenter");
        window.setMaximized(true);
        window.getIcons().add(new Image("icon.png"));
        window.setOnCloseRequest(e -> System.exit(0));
    }
}