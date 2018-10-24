package app.storage;

import app.models.Slide;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class StorageController {

    public static final String SONGS_PATH = "songs";
    public static final String BACKGROUNDS_PATH = "backgrounds";

    public static boolean saveFile(String path, String title, String lyrics) {
        String filename = convertTitleToFileName(title);
        String content = convertLyricsToFileFormat(lyrics);
        boolean saved = false;
        checkDirectory(new File(path));
        File file = new File(path, filename);
        try {
            saved = file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(content);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return saved;
    }

    public static File getFile(String path, String title) {
        File file = new File(path, convertTitleToFileName(title));
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    public static ArrayList<Slide> getSlidesFromFile(File file) {
        ArrayList<Slide> slides = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String fileText;
            while ((fileText = br.readLine()) != null) {
                String t = convertFileFormatToLyrics(fileText);
                for (String lyric : t.split("\n\n")) {
                    slides.add(new Slide(lyric));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return slides;
    }

    public static ArrayList<File> getFilesFromDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] list = dir.listFiles();
        if (list != null) {
            return new ArrayList<>(Arrays.asList(list));
        }
        return new ArrayList<>();
    }

    public static boolean deleteFile(String title) {
        String filename = convertTitleToFileName(title);
        File file = new File(SONGS_PATH, filename);
        return file.delete();
    }

    public static String convertTitleToFileName(String title) {
        return title.replace(" ", "_");
    }

    public static String convertFileNameToTitle(String filename) {
        return filename.replace("_", " ");
    }

    private static String convertLyricsToFileFormat(String text) {
        return text.replace("\n", "\\n");
    }

    private static String convertFileFormatToLyrics(String text) {
        return text.replace("\\n", "\n");
    }

    private static void checkDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
