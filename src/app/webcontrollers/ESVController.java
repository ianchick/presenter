package app.webcontrollers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ESVController {

    public String getText(String search, boolean withHeaders, boolean withFootnotes) throws IOException, ParseException {
        String baseURL = "https://api.esv.org/v3/passage/text/?";
        StringBuilder searchQuery = new StringBuilder("q=" + search);
        if (!withHeaders) {
            searchQuery.append("&include-headings=false");
        }
        if (!withFootnotes) {
            searchQuery.append("&include-footnotes=false");
        }
        URL url = new URL(baseURL + searchQuery.toString().replace(" ", "").trim());
        URLConnection uc = url.openConnection();

        JSONParser tokenParser = new JSONParser();
        JSONObject tokenJSON = (JSONObject) tokenParser.parse(new FileReader("secrets.json"));
        uc.addRequestProperty("Authorization", "Token " + tokenJSON.get("esv_token"));

        JSONParser jsonParser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) jsonParser.parse(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
        } catch (ParseException e) {
            e.printStackTrace();
            return "Reference could not be found";
        }
        return ((JSONArray) response.get("passages")).get(0).toString();
    }

    public String getText(String search) throws IOException, ParseException {
        return getText(search, false, false);
    }
}
