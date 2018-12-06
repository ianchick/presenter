package app;

import app.views.SetListQueueView;
import app.views.SlidesListView;
import app.views.SongListView;

/**
 * Singleton class to allow interaction between views.
 */
public class Mastermind {

    private static Mastermind mastermind;
    private SongListView songListView;
    private SlidesListView slidesListView;
    private SetListQueueView setListQueueView;

    public static void init() {
        mastermind = new Mastermind();
    }

    public static Mastermind getInstance() {
        return mastermind;
    }

    public SongListView getSongListView() {
        return songListView;
    }

    public void setSongListView(SongListView songListView) {
        this.songListView = songListView;
    }

    public SlidesListView getSlidesListView() {
        return slidesListView;
    }

    public void setSlidesListView(SlidesListView slidesListView) {
        this.slidesListView = slidesListView;
    }

    public SetListQueueView getSetListQueueView() {
        return setListQueueView;
    }

    public void setSetListQueueView(SetListQueueView setListQueueView) {
        this.setListQueueView = setListQueueView;
    }
}
