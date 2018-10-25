package app;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Configurations {

    private static String CONFIG_PATH = "config.yaml";
    private static String SONGS_PATH;
    private static String BACKGROUNDS_PATH;
    private static String DEFAULT_FONT;
    private static int DEFAULT_FONT_SIZE;

    public void setup() {
        File config = new File(CONFIG_PATH);
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            generateDefaultConfig();
        }
    }

    private void generateDefaultConfig() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Map<String, String> data = new LinkedHashMap<>();
        data.put("songs_dir", "songs");
        data.put("bg_dir", "backgrounds");
        data.put("default_font", "Arial");
        data.put("default_font_size", "64");
        Yaml yaml = new Yaml(options);
        FileWriter writer = null;
        try {
            writer = new FileWriter(CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        yaml.dump(data, writer);
    }

    public Map<String, String> readConfig() throws IOException {
        Map<String, String> config;
        Yaml yaml = new Yaml();
        try (FileInputStream in = new FileInputStream(CONFIG_PATH)) {
            config = yaml.load(in);
        }
        return config;
    }

    public void setConfigValues() throws IOException {
        Map<String, String> config = readConfig();
        setSongsPath(config.get("songs_dir"));
        setBackgroundsPath(config.get("bg_dir"));
        setDefaultFont(config.get("default_font"));
        setDefaultFontSize(Integer.valueOf(config.get("default_font_size")));
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

}
