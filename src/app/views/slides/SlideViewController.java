package app.views.slides;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SlideViewController  {

    public Text slide_text;
    public StackPane slide_pane;
    public Text transition_text;

    public SlideViewController() {
    }

    public Text getSlideContent() {
        return slide_text;
    }

    public void setSlideContent(String slideContent) {
        this.slide_text.setText(slideContent);
        this.slide_text.wrappingWidthProperty().bind(slide_pane.widthProperty());
    }

    public void setFont(String font, int size) {
        this.transition_text.setFont(new Font(font, size));
        this.slide_text.setFont(new Font(font, size));
    }
}