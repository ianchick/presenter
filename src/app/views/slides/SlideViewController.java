package app.views.slides;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SlideViewController  {
    @FXML
    public Text slide_preview_text;
    @FXML
    public StackPane slide_preview_pane;

    public Text getSlideContent() {
        return slide_preview_text;
    }

    public void setSlideContent(String slideContent) {
        this.slide_preview_text.setText(slideContent);
    }
}
