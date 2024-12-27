package central.controllers;

import central.utils.LPMUtil;
import central.utils.StageUtil;
import central.objects.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    @FXML private TextField destinationNumberPrompt, packetNumberPrompt;

    private List<String> IPAddresses, linearTraversal;
    private List<IPRoute> IPRoutes;
    private HashMap<String, String> bestMatch;
    
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

        addChangeListener(destinationNumberPrompt, packetNumberPrompt);
    }

    @FXML
    void resetClicked(ActionEvent event) {

        destinationNumberPrompt.clear();
        packetNumberPrompt.clear();
    }

    @FXML
    void startClicked(ActionEvent event) throws IOException {

        bestMatch = new HashMap<>();
        linearTraversal = new ArrayList<>();

        int destinationNumber = Integer.parseInt(destinationNumberPrompt.getText().trim());
        int hashMapCount = (int) Math.ceil(destinationNumber * 0.7);
        int trieCount = destinationNumber - hashMapCount;

        IPRoutes = LPMUtil.generateRoutingTable(destinationNumber, hashMapCount, trieCount);
        IPAddresses = LPMUtil.generateRandomIPs(Integer.parseInt(packetNumberPrompt.getText().trim()), IPRoutes);

        LinearSearchLPM linearRouter = new LinearSearchLPM(IPRoutes);
        PureTrieLPM trieRouter = new PureTrieLPM(IPRoutes);
        HybridHashTrieLPM hashTrieRouter = new HybridHashTrieLPM(IPRoutes);        

        StringBuilder linearData = LPMUtil.measureLinearPerformance(linearRouter, IPAddresses, linearTraversal);
        StringBuilder trieData = LPMUtil.measureTriePerformance(trieRouter, IPAddresses);
        StringBuilder hashTrieData = LPMUtil.measureHashTriePerformance(hashTrieRouter, IPAddresses, bestMatch);

        StageUtil newStage = new StageUtil("/resources/fxml/resultsPage.fxml");
        ResultsPageController controller = (ResultsPageController) newStage.getController();
        controller.setData(IPAddresses, IPRoutes, linearData.toString(), trieData.toString(), hashTrieData.toString(), bestMatch, linearTraversal);
        controller.initializePanes();
        controller.initializeResults();

        ((Stage)exitButton.getScene().getWindow()).close();
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