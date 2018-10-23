package app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScrapingController {

    private static final String BASE_URL = "https://www.azlyrics.com/lyrics/";

    public Document getSongDocument(String artist, String songTitle) {
        String artistUrl = artist.toLowerCase().replace(" ", "");
        String songUrl = songTitle.toLowerCase().replace(" ", "");
        String url = BASE_URL + artistUrl + "/" + songUrl + ".html";
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDocumentContent(Document document) {
        String content = "";
        if (document != null) {
            Element element = document.body();
            Elements elements = element.select("body > div.container.main-page > div > div.col-xs-12.col-lg-8.text-center > div:nth-child(8)");
            content = elements.first().wholeText();
        }
        return content;
    }

    public String getLyrics(String artist, String songTitle) {
        return getDocumentContent(getSongDocument(artist, songTitle));
    }
}