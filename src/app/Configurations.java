package app;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configurations {

    private static String CONFIG_PATH = "config.yml";
    private static String SONGS_PATH;
    private static String BACKGROUNDS_PATH;
    private static String DEFAULT_FONT;
    private static int DEFAULT_FONT_SIZE;
    private static ArrayList<String> FONT_LIST;

    public static void setup() throws IOException {
        File config = new File(CONFIG_PATH);
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            generateDefaultConfig();
        }
        setConfigValues();
    }

    private static void generateDefaultConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Map<String, String> data = new LinkedHashMap<>();
        data.put("songs_dir", "songs");
        data.put("bg_dir", "backgrounds");
        data.put("default_font", "Arial");
        data.put("default_font_size", "64");
        data.put("font_list", "Arial, Courier New, Helvetica, Times New Roman");
        Yaml yaml = new Yaml(options);
        FileWriter writer = null;
        try {
            writer = new FileWriter(CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        yaml.dump(data, writer);
    }

    private static Map<String, String> readConfig() throws IOException {
        Map<String, String> config;
        Yaml yaml = new Yaml();
        try (FileInputStream in = new FileInputStream(CONFIG_PATH)) {
            config = yaml.load(in);
        }
        return config;
    }

    public static void setConfigValues() throws IOException {
        Map<String, String> config = readConfig();
        setSongsPath(config.get("songs_dir"));
        setBackgroundsPath(config.get("bg_dir"));
        setDefaultFont(config.get("default_font"));
        setDefaultFontSize(Integer.valueOf(config.get("default_font_size")));
        setFontList(new ArrayList<>(Arrays.asList(config.get("font_list").split(", "))));
    }

    public static String getSongsPath() {
        return SONGS_PATH;
    }

    public static void setSongsPath(String songsPath) {
        SONGS_PATH = songsPath;
    }

    public static String getBackgroundsPath() {
        return BACKGROUNDS_PATH;
    }

    public static void setBackgroundsPath(String backgroundsPath) {
        BACKGROUNDS_PATH = backgroundsPath;
    }

    public static int getDefaultFontSize() {
        return DEFAULT_FONT_SIZE;
    }

    public static void setDefaultFontSize(int defaultFontSize) {
        DEFAULT_FONT_SIZE = defaultFontSize;
    }

    public static String getDefaultFont() {
        return DEFAULT_FONT;
    }

    public static void setDefaultFont(String defaultFont) {
        DEFAULT_FONT = defaultFont;
    }

    public static String getConfigPath() {
        return CONFIG_PATH;
    }

    public static ArrayList<String> getFontList() {
        return FONT_LIST;
    }

    public static void setFontList(ArrayList<String> fontList) {
        FONT_LIST = fontList;
    }
}
