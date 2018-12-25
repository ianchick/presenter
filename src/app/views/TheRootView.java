package app.views;

import app.Mastermind;
import app.toolbars.ControlBar;
import app.toolbars.NavigationBar;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TheRootView {

    public BorderPane init() {
        BorderPane root = new BorderPane();
        root.setId("root");

        // Lyric Slides
        ScrollPane slidesView = new ScrollPane();
        Mastermind.getInstance().setSlidesListView(new SlidesListView().init(slidesView));
        slidesView.setId("slides_scroll_pane");

        // Backgrounds
        ScrollPane backgroundPane = new ScrollPane();
        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);

        // Center Split Pane
        SplitPane contentSplitPane = new SplitPane();
        contentSplitPane.setOrientation(Orientation.VERTICAL);
        contentSplitPane.getItems().addAll(slidesView, backgroundPane);

        // Song and Queue lists
        SplitPane songListSplitPane = new SplitPane();
        songListSplitPane.setOrientation(Orientation.VERTICAL);
        songListSplitPane.setId("song_list_box");
        SongListView songListView = new SongListView().init(songListSplitPane);
        Mastermind.getInstance().setSongListView(songListView);
        SetListQueueView setListQueueView = new SetListQueueView().init(songListSplitPane);
        Mastermind.getInstance().setSetListQueueView(setListQueueView);

        ToolBar toolBar = ControlBar.setup();

        MenuBar menuBar = NavigationBar.setup(changeBackgroundView, backgroundPane, songListView);

        setKeyEventHandler(root);

        root.setTop(new VBox(menuBar, toolBar));
        root.setLeft(songListSplitPane);
        root.setCenter(contentSplitPane);
        return root;
    }

    private void setKeyEventHandler(Pane pane) {
        SlidesListView slidesListView = Mastermind.getInstance().getSlidesListView();
        pane.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.RIGHT == event.getCode()) {
                if (slidesListView != null) {
                    slidesListView.nextSlide();
                }
            }
            if (KeyCode.LEFT == event.getCode()) {
                if (slidesListView != null) {
                    slidesListView.previousSlide();
                }
            }
            if (KeyCode.B == event.getCode()) {
                if (LiveView.isLive()) {
                    if (LiveView.getTextView().getText().equals("")) {
                        slidesListView.currentSlide();
                    } else {
                        LiveView.getTextView().setText("");
                    }
                }
            }
        });
    }
}
