package app;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScrapingController {

    private static final String BASE_URL = "https://www.azlyrics.com/lyrics/";

    private Document getSongDocument(String artist, String songTitle) {
        Document doc;
        String artistUrl = artist.toLowerCase().replace(" ", "");
        String songUrl = songTitle.toLowerCase().replace(" ", "");
        String url = BASE_URL + artistUrl + "/" + songUrl + ".html";
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    private String getDocumentContent(Document document) {
        String content = null;
        if (document != null) {
            Element element = document.body();
            Elements elements = element.select("body > div.container.main-page > div > div.col-xs-12.col-lg-8.text-center > div:nth-child(8)");
            content = elements.first().wholeText().trim();
        }
        return content;
    }

    public String getLyrics(String artist, String songTitle) {
        return getDocumentContent(getSongDocument(artist, songTitle));
    }
}