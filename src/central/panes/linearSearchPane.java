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
    private double boxWidth = 95, boxHeight = 30, hGap = 15;
    private Map<String, Rectangle> recTable = new HashMap<>();

    public linearSearchPane (List<String> data, String target) {

        this.data = data;
        this.target = target;

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);

        this.widthProperty().addListener((obs, oldVal, newVal) -> displayList());
        this.heightProperty().addListener((obs, oldVal, newVal) -> displayList());
    }

    public void displayList() {

        recTable.clear();
        this.getChildren().clear();
        if (data != null && !data.isEmpty()) {

            double totalWidth = data.size() * boxWidth + (data.size() - 1) * hGap;
            double startX = (getWidth() - totalWidth) / 2;
            double startY = (getHeight() - boxHeight) / 2;
            
            for(int i = 1 ; i < data.size() ; i++) {

                String [] parts = data.get(i).split("=");

                displayItem(parts[1], i, startX + i * (boxWidth + hGap), startY);
            }
        }
    }

    private void displayItem(String item, int index, double x, double y) {

        Rectangle rect = new Rectangle(x, y, boxWidth, boxHeight);
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.BLACK);

        Text text;

        if(item.length() <= 14) {
            
            text = new Text(x + boxWidth / 7, y + boxHeight / 2 + 5, item);
        }

        else {

            text = new Text(x + boxWidth / 22, y + boxHeight / 2 + 5, item);
        }

        text.setStyle("-fx-font-size: 12px;");

        recTable.put(item, rect);
        this.getChildren().addAll(rect, text);
    }

    public void visualizeTraversal(double speed) {

        if (data == null || data.isEmpty()) return;

        Timeline timeline = new Timeline();
        for (int i = 1; i < data.size(); i++) {

            String [] parts = data.get(i).split("=");

            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed), e -> highlightItem(parts))
            );

            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed + speed), e -> resetItemColor(parts[1]))
            );

            if(data.get(i).equals(target)) {

                timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(i * speed), e -> highlightItem(parts))
                );

                timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(i * speed + speed), e -> resetItemColor(parts[1]))
                );
            }
        }

        timeline.play();
    }

    private void highlightItem(String [] item) {

        Rectangle rect = recTable.get(item[1]);

        if(item[0].equals("1")) {

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
