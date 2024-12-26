package central.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import central.utils.StageUtil;
import central.objects.*;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuPageController implements Initializable {

    @FXML private Label backButton, exitButton, minimizeButton;
    @FXML private JFXButton hashTrieButton, linearSearchButton, pureTrieButton;

    private MainPageController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        minimizeButton.setOnMouseEntered(event ->{minimizeButton.setStyle("-fx-text-fill: white;");});
        minimizeButton.setOnMouseExited(event ->{minimizeButton.setStyle("");});
        minimizeButton.setOnMouseClicked(event -> {((Stage)minimizeButton.getScene().getWindow()).setIconified(true);});
        
        exitButton.setOnMouseEntered(event ->{exitButton.setStyle("-fx-text-fill: white;");});
        exitButton.setOnMouseExited(event ->{exitButton.setStyle("");});
        exitButton.setOnMouseClicked(event -> {((Stage)minimizeButton.getScene().getWindow()).close();});

        backButton.setOnMouseEntered(event ->{backButton.setStyle("-fx-text-fill: white;");});
        backButton.setOnMouseExited(event ->{backButton.setStyle("");});
        backButton.setOnMouseClicked(event -> {

            ((Stage)backButton.getScene().getWindow()).close();

            try {
                new StageUtil("/resources/fxml/mainPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void hashTrieClicked(ActionEvent event) {

        
    }

    @FXML
    void linearSearchClicked(ActionEvent event) {

       
    }

    @FXML
    void pureTrieClicked(ActionEvent event) {

        
    }

    public void setParentController(MainPageController parentController) {this.parentController = parentController;}
}