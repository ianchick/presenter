package app;

import app.toolbars.ControlBar;
import app.toolbars.NavigationBar;
import app.views.ChangeBackgroundView;
import app.views.LiveView;
import app.views.SongListView;
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

    private SongListView songListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws IOException {
        Configurations configurations = new Configurations();
        configurations.setup();
        configurations.setConfigValues();

        BorderPane root = setRootContent();
        setKeyEventHandler(root);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        window.setScene(scene);
        window.setTitle("Amateur Presenter");
        window.setMaximized(true);
        window.getIcons().add(new Image("icon.png"));
        window.show();
        window.setOnCloseRequest(e -> System.exit(0));
    }

    private BorderPane setRootContent() {
        BorderPane root = new BorderPane();
        root.setId("root");

        VBox listSongsBox = new VBox();
        ScrollPane slidesView = new ScrollPane();
        slidesView.setId("slides_scroll_pane");
        ScrollPane backgroundPane = new ScrollPane();

        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);

        setSongListView(listSongsBox, slidesView);

        ToolBar toolBar = ControlBar.setup(songListView);

        MenuBar menuBar = NavigationBar.setup(changeBackgroundView, backgroundPane, songListView);

        root.setTop(new VBox(menuBar, toolBar));
        root.setLeft(listSongsBox);
        root.setCenter(slidesView);
        root.setBottom(backgroundPane);
        return root;
    }

    private void setSongListView(Pane parent, ScrollPane slidesPane) {
        songListView = new SongListView();
        songListView.display(parent, slidesPane);
    }

    private void setKeyEventHandler(Pane pane) {
        pane.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.RIGHT == event.getCode()) {
                if (songListView.getSlidesListView() != null) {
                    songListView.getSlidesListView().nextSlide();
                }
            }
            if (KeyCode.LEFT == event.getCode()) {
                if (songListView.getSlidesListView() != null) {
                    songListView.getSlidesListView().previousSlide();
                }
            }
            if (KeyCode.B == event.getCode()) {
                if (LiveView.isLive()) {
                    if (LiveView.getTextView().getText().equals("")) {
                        songListView.getSlidesListView().currentSlide();
                    } else {
                        LiveView.getTextView().setText("");
                    }
                }
            }
        });
    }
}
