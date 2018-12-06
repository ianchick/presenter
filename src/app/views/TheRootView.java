package app.views;

import app.toolbars.ControlBar;
import app.toolbars.NavigationBar;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TheRootView {

    SongListView songListView;

    public BorderPane init() {
        BorderPane root = new BorderPane();
        root.setId("root");

        setKeyEventHandler(root);

        VBox listSongsBox = new VBox();
        ScrollPane slidesView = new ScrollPane();
        slidesView.setId("slides_scroll_pane");
        ScrollPane backgroundPane = new ScrollPane();

        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);

        songListView = new SongListView().init(listSongsBox, slidesView);

        ToolBar toolBar = ControlBar.setup(songListView);

        MenuBar menuBar = NavigationBar.setup(changeBackgroundView, backgroundPane, songListView);

        root.setTop(new VBox(menuBar, toolBar));
        root.setLeft(listSongsBox);
        root.setCenter(slidesView);
        root.setBottom(backgroundPane);
        return root;
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
