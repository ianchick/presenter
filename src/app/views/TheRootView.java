package app.views;

import app.Mastermind;
import app.toolbars.ControlBar;
import app.toolbars.NavigationBar;
import app.views.slides.SlidesListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class TheRootView {

    private static SplitPane contentSplitPane;
    private static ScrollPane backgroundPane;
    private static SplitPane songListSplitPane;
    private static ScrollPane bibleScrollPane;
    private static VBox rightPane;
    private static BorderPane root;

    public static BorderPane init() {
        root = new BorderPane();
        root.setId("root");

        // Lyric Slides
        ScrollPane slidesView = new ScrollPane();
        Mastermind.getInstance().setSlidesListView(new SlidesListView().init(slidesView));
        slidesView.setId("slides_scroll_pane");

        // Backgrounds
        backgroundPane = new ScrollPane();
        ChangeBackgroundView changeBackgroundView = new ChangeBackgroundView();
        changeBackgroundView.display(backgroundPane);

        // Center Split Pane
        contentSplitPane = new SplitPane();
        contentSplitPane.setOrientation(Orientation.VERTICAL);
        contentSplitPane.getItems().addAll(slidesView, backgroundPane);

        // Song and Queue lists
        songListSplitPane = new SplitPane();
        songListSplitPane.setOrientation(Orientation.VERTICAL);
        songListSplitPane.setId("song_list_box");
        SongListView songListView = new SongListView().init(songListSplitPane);
        Mastermind.getInstance().setSongListView(songListView);
        SetListQueueView setListQueueView = new SetListQueueView().init(songListSplitPane);
        Mastermind.getInstance().setSetListQueueView(setListQueueView);

        // Preview Screen
        rightPane = new VBox();
        rightPane.alignmentProperty().setValue(Pos.TOP_CENTER);
        Mastermind.getInstance().setPreview(new ImageView());
        Mastermind.getInstance().getPreview().setFitWidth(250);
        Mastermind.getInstance().getPreview().imageProperty().addListener(new ChangeListener<Image>() {
            @Override
            public void changed(ObservableValue<? extends Image> observable, Image oldValue, Image newValue) {
                if (newValue == null) {
                    Mastermind.getInstance().getPreview().setFitWidth(0);
                } else {
                    Mastermind.getInstance().getPreview().setFitWidth(250);
                }
            }
        });
        rightPane.getChildren().add(Mastermind.getInstance().getPreview());

        // Bible Slides View
        bibleScrollPane = new ScrollPane();
        BibleSlidesView bibleSlidesView = new BibleSlidesView();
        bibleSlidesView.display(bibleScrollPane);

        ToolBar toolBar = ControlBar.setup();

        MenuBar menuBar = NavigationBar.setup(changeBackgroundView, backgroundPane, songListView);

        setKeyEventHandler(root);

        root.setTop(new VBox(menuBar, toolBar));
        root.setLeft(songListSplitPane);
        root.setCenter(contentSplitPane);
        root.setRight(rightPane);
        return root;
    }

    private static void setKeyEventHandler(Pane pane) {
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
                if (Mastermind.getInstance().liveViewIsShowing()) {
                    Mastermind.getInstance().getLiveView().setCurrentSlide("");
                }
            }
        });
    }

    public static void toggleBackgroundPane(boolean isVisible) {
        if (!isVisible) {
            contentSplitPane.getItems().remove(backgroundPane);
        } else {
            contentSplitPane.getItems().add(backgroundPane);
        }
    }

    public static void toggleSongsPane(boolean isVisible) {
        if (!isVisible) {
            root.getChildren().remove(songListSplitPane);
        } else {
            root.setLeft(songListSplitPane);
        }
    }

    public static void toggleBiblePane(boolean isVisible) {
        if (!isVisible) {
            rightPane.getChildren().remove(bibleScrollPane);
        } else {
            rightPane.getChildren().add(bibleScrollPane);
        }
    }
}
