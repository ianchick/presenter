package app;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class MusixMatchController {

    private MusixMatch musixMatch;

    public void auth() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("secrets.json")) {
            JSONObject obj = (JSONObject) parser.parse(reader);
            String apiKey = (String) obj.get("token");
            musixMatch = new MusixMatch(apiKey);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String fuzzySearchLyrics(String trackTitle, String artist) throws MusixMatchException {
        Track track = musixMatch.getMatchingTrack(trackTitle, artist);
        TrackData data = track.getTrack();
        int trackId = data.getTrackId();
        Lyrics lyrics = musixMatch.getLyrics(trackId);
        return lyrics.getLyricsBody();
    }
}
