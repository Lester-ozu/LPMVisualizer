package central.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import central.panes.linearSearchPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class VisualizationPageController implements Initializable{

    @FXML private Pane contentPane;
    @FXML private Label exitButton, titleLabel, ipFindLabel;
    @FXML private ImageView retryButton;

    private linearSearchPane linearPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        retryButton.setOnMouseClicked(event -> {linearPane.visualizeTraversal(0.5);});
        
        exitButton.setOnMouseEntered(event ->{exitButton.setStyle("-fx-text-fill: white;");});
        exitButton.setOnMouseExited(event ->{exitButton.setStyle("");});
        exitButton.setOnMouseClicked(event -> {((Stage)exitButton.getScene().getWindow()).close();});
    }

    public void setContentPane(Pane pane, linearSearchPane linearPane) {

        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(pane);

        pane.prefWidthProperty().bind(contentPane.widthProperty());
        pane.prefHeightProperty().bind(contentPane.heightProperty());

        this.linearPane = linearPane;
    }

    public void setTitle(String text, String ipFind) {

        this.titleLabel.setText(text);
        this.ipFindLabel.setText("Finding the best match of " + ipFind);
    }

    public Pane getContentPane() {

        return contentPane;
    }
}
