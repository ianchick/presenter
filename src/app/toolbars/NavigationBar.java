package app.toolbars;

import app.Configurations;
import app.Mastermind;
import app.models.Song;
import app.storage.StorageController;
import app.views.ChangeBackgroundView;
import app.views.SongListView;
import app.views.TheRootView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NavigationBar {

    private static Menu fileMenu;
    private static Menu viewMenu;
    private static Menu slidesMenu;

    private static ChangeBackgroundView backgroundView;
    private static ScrollPane backgroundPane;
    private static SongListView songListView;

    public static MenuBar setup(ChangeBackgroundView bgView, ScrollPane bgPane, SongListView listView) {
        backgroundPane = bgPane;
        backgroundView = bgView;
        songListView = listView;

        MenuBar menuBar = new MenuBar();
        fileMenu = new Menu("File");
        slidesMenu = new Menu("Slides");
        viewMenu = new Menu("View");
        setViewMenu();
        setFileMenu();
        setSlidesMenu();
        menuBar.getMenus().addAll(fileMenu, viewMenu, slidesMenu);
        return menuBar;
    }

    private static void setSlidesMenu() {
        MenuItem importPowerpoint = new MenuItem("Import Powerpoint");
        importPowerpoint.setOnAction(event -> {
            importPowerpoint();
        });

        MenuItem exportPowerpoint = new MenuItem("Export Set List");
        exportPowerpoint.setOnAction(event -> {
            XMLSlideShow ppt = new XMLSlideShow();
            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
            defaultMaster.getBackground().setFillColor(Color.BLACK);
            XSLFSlideLayout titleLayout = defaultMaster.getLayout(SlideLayout.TITLE);
            titleLayout.removeShape(titleLayout.getPlaceholder(1));

            ArrayList<Song> setList = new ArrayList<>(Mastermind.getInstance().getSetListQueueView().getQueue());
            if (!setList.isEmpty()) {
                for (Song song : setList) {
                    // Blank slide before each song
                    ppt.createSlide(titleLayout).getPlaceholder(0).clearText();

                    for (app.models.Slide songSlide : song.getSlides()) {
                        XSLFSlide slide = ppt.createSlide(titleLayout);
                        slide.getPlaceholder(0).clearText();
                        XSLFTextRun run = slide.getPlaceholder(0).addNewTextParagraph().addNewTextRun();
                        run.setFontSize(36d);
                        run.setFontFamily("Arial");
                        run.setFontColor(Color.WHITE);
                        run.setText(songSlide.getContent());
                    }
                }
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Powerpoint (.pptx)", "*.pptx"));
                fileChooser.setTitle("Export Set List");
                File powerpoint = fileChooser.showSaveDialog(Mastermind.getInstance().getMainStage());
                if (powerpoint != null) {
                    try {
                        FileOutputStream out = new FileOutputStream(powerpoint.getPath());
                        ppt.write(out);
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        slidesMenu.getItems().addAll(importPowerpoint, exportPowerpoint);
    }

    private static void importPowerpoint() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Powerpoint");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Powerpoint files", "*.pptx", "*.ppt");
        fileChooser.getExtensionFilters().add(extFilter);
        File pptFile = fileChooser.showOpenDialog(Mastermind.getInstance().getMainStage());
        SlideShow slideShow = null;
        try {
            slideShow = SlideShowFactory.create(pptFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SlideShowExtractor extractor = new SlideShowExtractor(slideShow);
        ArrayList<app.models.Slide> songSlides = new ArrayList<>();
        for (int i = 0; i < slideShow.getSlides().size(); i++) {
            String slideText = extractor.getText((Slide)slideShow.getSlides().get(i)).trim();
            songSlides.add(new app.models.Slide(slideText));
        }
        Song pptSong = new Song(FilenameUtils.getBaseName(pptFile.getName()));
        if (StorageController.getFile(Configurations.getSongsPath(), pptSong.getTitle()) != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Import Powerpoint");
            alert.setHeaderText(null);
            alert.setContentText("A song with that name already exists.");
            alert.showAndWait();
        } else {
            pptSong.setSlides(songSlides);
            pptSong.setLyricsFromSlides();
            boolean songSaved = StorageController.saveFile(Configurations.getSongsPath(), pptSong.getTitle(), pptSong.getLyrics());
            if (songSaved) {
                refresh();
            }
        }
    }

    private static void setViewMenu() {
        CheckMenuItem toggleBackgrounds = new CheckMenuItem("Toggle Backgrounds");
        toggleBackgrounds.setSelected(true);
        toggleBackgrounds.setOnAction(e -> TheRootView.toggleBackgroundPane(toggleBackgrounds.isSelected()));

        CheckMenuItem toggleSongs = new CheckMenuItem("Toggle Songs");
        toggleSongs.setSelected(true);
        toggleSongs.setOnAction(e -> TheRootView.toggleSongsPane(toggleSongs.isSelected()));

        // TODO: In Development Tag
        CheckMenuItem toggleBible = new CheckMenuItem("Toggle Bible Search (In Developement)");
        toggleBible.setSelected(false);
        toggleBible.setOnAction(e -> TheRootView.toggleBiblePane(toggleBible.isSelected()));

        viewMenu.getItems().addAll(toggleBackgrounds, toggleSongs, toggleBible);
    }

    private static void setFileMenu() {
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction(e -> refresh());
        MenuItem openSongsDir = new MenuItem("Songs Directory");
        openSongsDir.setOnAction(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dir;
            try {
                dir = new File(Configurations.getSongsPath());
                desktop.open(dir);
            } catch (IllegalArgumentException | IOException e1) {
                System.out.println("File Not Found");
            }
        });
        MenuItem openBgDir = new MenuItem("Backgrounds Directory");
        openBgDir.setOnAction(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dir;
            try {
                dir = new File(Configurations.getBackgroundsPath());
                desktop.open(dir);
            } catch (IllegalArgumentException | IOException e1) {
                System.out.println("File Not Found");
            }
        });
        MenuItem preferences = new MenuItem("Preferences");
        preferences.setOnAction(e -> {
            Desktop desktop = Desktop.getDesktop();
            File dir;
            try {
                dir = new File(Configurations.getConfigPath());
                desktop.open(dir);
            } catch (IllegalArgumentException | IOException e1) {
                System.out.println("File Not Found");
            }
        });
        fileMenu.getItems().addAll(refresh, openSongsDir, openBgDir, preferences);
    }

    public static void refresh() {
        try {
            Configurations.setConfigValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView.display(backgroundPane);
        songListView.populateSongList();
    }
}