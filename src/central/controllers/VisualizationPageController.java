package central.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class VisualizationPageController implements Initializable{

    @FXML private Pane contentPane;
    @FXML private Label exitButton, titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        exitButton.setOnMouseEntered(event ->{exitButton.setStyle("-fx-text-fill: white;");});
        exitButton.setOnMouseExited(event ->{exitButton.setStyle("");});
        exitButton.setOnMouseClicked(event -> {((Stage)exitButton.getScene().getWindow()).close();});
    }

    public void setContentPane(Pane pane) {

        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(pane);
    }

    public void setTitle(String text) {

        this.titleLabel.setText(text);
    }
}
