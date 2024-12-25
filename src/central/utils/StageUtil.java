package central.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageUtil {

    double xOffset, yOffset;

    public StageUtil(String path) throws IOException {

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        
        scene.setOnMousePressed(event -> {

            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.show();
    }
}
