package app.models;

import java.util.ArrayList;

public class Song {

    private ArrayList<Slide> slides;
    private String title;
    private String lyrics;

    public Song(String title) {
        this.title = title;
    }

    public ArrayList<Slide> getSlides() {
        if (slides == null) {
            return new ArrayList<>();
        }
        return slides;
    }

    public void setSlides(ArrayList<Slide> slides) {
        this.slides = slides;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyricsFromSlides() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Slide slide : slides) {
            String lyric = slide.getContent();
            stringBuilder.append(lyric);
            stringBuilder.append("\n\n");
        }
        lyrics = stringBuilder.toString();
    }

    public Slide getSlideFromText(String text) {
        for (Slide slide: slides) {
            if (slide.getContent().equals(text)) {
                return slide;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
