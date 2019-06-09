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

public class ESVController {

    public String getText() throws IOException, ParseException {
        String baseURL = "https://api.esv.org/v3/passage/text/?q=John+3:16-18&include-footnotes=false&include-headings=false";
        URL url = new URL(baseURL);
        URLConnection uc = url.openConnection();

        JSONParser tokenParser = new JSONParser();
        JSONObject tokenJSON = (JSONObject)tokenParser.parse(new FileReader("secrets.json"));
        uc.addRequestProperty("Authorization", "Token " + tokenJSON.get("esv_token"));

        JSONParser jsonParser = new JSONParser();
        JSONObject response = (JSONObject)jsonParser.parse(new InputStreamReader(uc.getInputStream(), "UTF-8"));
        return ((JSONArray)response.get("passages")).get(0).toString();
    }
}
