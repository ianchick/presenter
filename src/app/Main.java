package app;

import app.toolbars.NavigationBar;
import app.views.ChangeBackgroundView;
import app.views.SongListView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private SongListView songListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {
        BorderPane root = new BorderPane();
        root.setId("root");

        VBox listSongsBox = new VBox();
        ScrollPane slidesView = new ScrollPane();
        slidesView.setId("slides_scroll_pane");
        ScrollPane slidesPane = slidesView;
        ScrollPane backgroundPane = new ScrollPane();

        setChangeBackgroundView(backgroundPane);
        setSongListView(listSongsBox, slidesPane);

        ToolBar toolBar = NavigationBar.setup(songListView);

        root.setTop(toolBar);
        root.setLeft(listSongsBox);
        root.setCenter(slidesPane);
        root.setBottom(backgroundPane);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        window.setScene(scene);
        window.setTitle("Amateur Presenter");
        window.setMaximized(true);
        window.getIcons().add(new Image("file:resources/icon.png"));
        window.show();
        window.setOnCloseRequest(e -> System.exit(0));
    }

    private void setChangeBackgroundView(ScrollPane backgroundPane) {
        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);
    }

    private void setSongListView(Pane parent, ScrollPane slidesPane) {
        songListView = new SongListView();
        songListView.display(parent, slidesPane);
    }
}
