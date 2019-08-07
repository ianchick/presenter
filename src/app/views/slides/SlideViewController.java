package app.views.slides;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SlideViewController  {

    public Text slide_text;
    public StackPane slide_pane;
    public Text transition_text;
    public ImageView transition_background;
    public ImageView background;

    public SlideViewController() {
    }

    public void setSlideContent(String slideContent) {
        this.slide_text.setText(slideContent);
        this.slide_text.wrappingWidthProperty().bind(slide_pane.widthProperty());
    }
}