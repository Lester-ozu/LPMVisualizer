package central;

import central.utils.StageUtil;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application{
    
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage arg0) throws Exception {
        
        new StageUtil("/resources/fxml/mainPage.fxml");
    }
}
