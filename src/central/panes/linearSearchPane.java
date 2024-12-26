package central.panes;

import java.util.HashMap;
import java.util.Map;

import central.objects.LinearSearchLPM;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class linearSearchPane extends Pane{
    
    private LinearSearchLPM lpm;
    private double radius = 15, vGap = 40;
    private Map<String, Circle> nodeMap = new HashMap<>();

    public linearSearchPane (LinearSearchLPM lpm) {

        this.lpm = lpm;

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);

        // this.widthProperty().addListener((observable, oldValue, newValue) -> displayRoutes());
        // this.heightProperty().addListener((observable, oldValue, newValue) -> displayRoutes());
    }

    
}
