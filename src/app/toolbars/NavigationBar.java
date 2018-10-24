package app.toolbars;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class NavigationBar {

    private static MenuBar menuBar;
    private static Menu fileMenu;

    public static MenuBar setup() {
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        MenuItem refresh = new MenuItem("Refresh");
        MenuItem preferences = new MenuItem("Preferences");
        fileMenu.getItems().addAll(refresh, preferences);
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }
}
