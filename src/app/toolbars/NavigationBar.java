package app.toolbars;

import app.Configurations;
import app.views.ChangeBackgroundView;
import app.views.SongListView;
import app.views.TheRootView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NavigationBar {

    private static Menu fileMenu;
    private static Menu viewMenu;

    private static ChangeBackgroundView backgroundView;
    private static ScrollPane backgroundPane;
    private static SongListView songListView;

    public static MenuBar setup(ChangeBackgroundView bgView, ScrollPane bgPane, SongListView listView) {
        backgroundPane = bgPane;
        backgroundView = bgView;
        songListView = listView;

        MenuBar menuBar = new MenuBar();
        fileMenu = new Menu("File");
        viewMenu = new Menu("View");
        setViewMenu();
        setFileMenu();
        menuBar.getMenus().addAll(fileMenu, viewMenu);
        return menuBar;
    }

    private static void setViewMenu() {
        CheckMenuItem toggleBackgrounds = new CheckMenuItem("Toggle Backgrounds");
        toggleBackgrounds.setSelected(true);
        toggleBackgrounds.setOnAction(e -> TheRootView.toggleBackgroundPane(toggleBackgrounds.isSelected()));

        CheckMenuItem toggleSongs = new CheckMenuItem("Toggle Songs");
        toggleSongs.setSelected(true);
        toggleSongs.setOnAction(e -> TheRootView.toggleSongsPane(toggleSongs.isSelected()));

        CheckMenuItem toggleBible = new CheckMenuItem("Toggle Bible Search");
        toggleBible.setSelected(true);
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
