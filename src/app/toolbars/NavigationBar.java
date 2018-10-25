package app.toolbars;

import app.Configurations;
import app.views.ChangeBackgroundView;
import app.views.SongListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NavigationBar {

    private static MenuBar menuBar;
    private static Menu fileMenu;

    private static ChangeBackgroundView backgroundView;
    private static ScrollPane backgroundPane;
    private static SongListView songListView;

    public static MenuBar setup(ChangeBackgroundView bgView, ScrollPane bgPane, SongListView listView) {
        backgroundPane = bgPane;
        backgroundView = bgView;
        songListView = listView;

        menuBar = new MenuBar();
        fileMenu = new Menu("File");
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
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    public static void refresh() {
        backgroundView.display(backgroundPane);
        songListView.populateSongList();
    }
}
