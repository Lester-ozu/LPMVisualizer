package central.panes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class linearSearchPane extends Pane{
    
    private List<String> data;
    private String target;
    private double boxWidth = 50, boxHeight = 30, hGap = 10;
    private Map<String, Rectangle> recTable = new HashMap<>();

    public linearSearchPane (List<String> data, String target) {

        this.data = data;
        this.target = target;

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);
    }

    public void displayList() {

        recTable.clear();
        if (data != null && !data.isEmpty()) {

            double totalWidth = data.size() * boxWidth + (data.size() - 1) * hGap;
            double startX = (getWidth() - totalWidth) / 2;
            double startY = (getHeight() / 2 - boxHeight) / 2;
            for(int i = 0 ; i < data.size() ; i++) {

                displayItem(data.get(i), i, startX + i * (boxWidth + hGap), startY);
            }
        }
    }

    private void displayItem(String item, int index, double x, double y) {

        Rectangle rect = new Rectangle(x, y, boxWidth, boxHeight);
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.BLACK);

        Text text = new Text(x + boxWidth / 4, y + boxHeight / 2 + 5, item);
        text.setStyle("-fx-font-size: 12px;");

        recTable.put(item, rect);
        this.getChildren().addAll(rect, text);
    }

    public void visualizeTraversal(double speed) {

        if (data == null || data.isEmpty()) return;

        Timeline timeline = new Timeline();
        for (int i = 0; i < data.size(); i++) {

            int index = i;

            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed), e -> highlightItem(data.get(index), target))
            );

            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed + speed), e -> resetItemColor(data.get(index)))
            );

            if(data.get(i).equals(target)) {

                timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed), e -> highlightItem(data.get(index), target))
                );

                timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(i * speed + speed), e -> resetItemColor(data.get(index)))
                );
            }
        }

        timeline.play();
    }

    private void highlightItem(String item, String target) {

        Rectangle rect = recTable.get(item);

        if(item.equals(target)) {

            rect.setFill(Color.GREEN);
        }

        else if(rect != null) {

            rect.setFill(Color.RED);
        }

        else {

            System.out.println("Cannot find rectangle for String: " + item);
        }
    }

    private void resetItemColor(String item) {

        Rectangle rect = recTable.get(item);

        if (rect != null) {

            rect.setFill(Color.LIGHTBLUE);
        } 
        
        else {

            System.out.println("Cannot find rectangle for String: " + item);
        }
    }
}
