package app.views.slides;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SlideViewController  {

    public Text slide_text;
    public StackPane slide_pane;

    public SlideViewController() {
    }

    public Text getSlideContent() {
        return slide_text;
    }

    public void setSlideContent(String slideContent) {
        this.slide_text.setText(slideContent);
        this.slide_text.wrappingWidthProperty().bind(slide_pane.widthProperty());
    }
}