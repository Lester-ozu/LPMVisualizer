package central.controllers;

import central.utils.LPMUtil;
import central.objects.IPRoute;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainPageController implements Initializable{

    @FXML private Label exitButton, minimizeButton, warningLabel;
    @FXML private JFXButton resetButton, startButton;
    @FXML private TextField destinationNumberPrompt, hashMapCountPrompt, trieCountPrompt;

    private List<String> IPAddresses;
    private List<IPRoute> IPRoutes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        warningLabel.setVisible(false);
        startButton.setDisable(true);
        
        minimizeButton.setOnMouseEntered(event ->{minimizeButton.setStyle("-fx-text-fill: white;");});
        minimizeButton.setOnMouseExited(event ->{minimizeButton.setStyle("");});
        minimizeButton.setOnMouseClicked(event -> {((Stage)minimizeButton.getScene().getWindow()).setIconified(true);});
        
        exitButton.setOnMouseEntered(event ->{exitButton.setStyle("-fx-text-fill: white;");});
        exitButton.setOnMouseExited(event ->{exitButton.setStyle("");});
        exitButton.setOnMouseClicked(event -> {((Stage)minimizeButton.getScene().getWindow()).close();});

        addChangeListener(destinationNumberPrompt, hashMapCountPrompt, trieCountPrompt);
    }

    @FXML
    void resetClicked(ActionEvent event) {

        destinationNumberPrompt.clear();
        hashMapCountPrompt.clear();
        trieCountPrompt.clear();
    }

    @FXML
    void startClicked(ActionEvent event) {

        int destinationNumber = Integer.parseInt(destinationNumberPrompt.getText().trim());
        int hashMapCount = Integer.parseInt(hashMapCountPrompt.getText().trim());
        int trieCount = Integer.parseInt(trieCountPrompt.getText().trim());

        IPRoutes = LPMUtil.generateRoutingTable(destinationNumber, hashMapCount, trieCount);
        IPAddresses = LPMUtil.generateRandomIPs(destinationNumber, IPRoutes);
    }

    private void addChangeListener(TextField... textFields) {

        for(TextField textField : textFields) {

            textField.textProperty().addListener((observable, oldValue, newValue) -> {

                if(allFieldsValid(textFields)) {

                    startButton.setDisable(false);
                    warningLabel.setVisible(false);
                }

                else {

                    startButton.setDisable(true);
                    warningLabel.setVisible(containsInvalidInput(textFields));
                }
            });
        }
    }

    private boolean allFieldsValid(TextField... textFields) {

        for(TextField textField : textFields) {

            String text = textField.getText().trim();
            if(text.isEmpty() || !text.matches("\\d+")) {

                return false;
            }
        }

        return true;
    }

    private boolean containsInvalidInput(TextField... textFields) {
        for (TextField textField : textFields) {
            String text = textField.getText().trim();
            if (!text.isEmpty() && !text.matches("\\d+")) {
                return true;
            }
        }
        return false;
    }

    public List<IPRoute> getIPRoutes() {return IPRoutes;}
    public List<String> getIPAddresses() {return IPAddresses;}
}