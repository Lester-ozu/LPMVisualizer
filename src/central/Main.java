package central;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Scanner;

public class Main extends Application{
    public static void main(String[] args) throws Exception {
        
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/mainScene.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("LPM Visualizer");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }

        catch (Exception e) {

            e.printStackTrace();
        }
    }
}
