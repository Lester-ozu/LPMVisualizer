package central.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import central.objects.IPRoute;

import central.objects.IPRoute;
import central.utils.StageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ResultsPageController implements Initializable {

    @FXML private Label backButton, exitButton, memoryHashTrie, memoryLinearSearch, memoryPureTrie, minimizeButton, timeHashTrie, timeLinearSearch, timePureTrie;
    @FXML private FlowPane bestMatchFlowPane, ipFlowPane, routesFlowPane;

    private List<String> IPAddresses;
    private List<IPRoute> IPRoutes;
    private HashMap<String, String> bestMatch;
    private String linearData, trieData, hashTrieData;

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
        // backButton.setOnMouseClicked(event -> {

        //     ((Stage)backButton.getScene().getWindow()).close();

        //     try {
        //         new StageUtil("/resources/fxml/.fxml");
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // });
    }

    public void setData(List<String> IPAddresses, List<IPRoute> IPRoutes, String linearData, String trieData, String hashTrieData, HashMap<String, String> bestMatch) {

        this.IPAddresses = IPAddresses;
        this.IPRoutes = IPRoutes;
        this.bestMatch = bestMatch;
        this.linearData = linearData;
        this.trieData = trieData;
        this.hashTrieData = hashTrieData;
    }

    public void initializePanes() {

        ipFlowPane.setPrefSize(226, 20 * IPAddresses.size());
        routesFlowPane.setPrefSize(226, 19 * IPRoutes.size());
        bestMatchFlowPane.setPrefSize(226, 19.3 * bestMatch.size());

        ipFlowPane.getChildren().clear();
        routesFlowPane.getChildren().clear();
        bestMatchFlowPane.getChildren().clear();

        for(String ip : IPAddresses) {

            Label ipLabel = new Label(ip);
            ipLabel.setPrefSize(222, 18);
            ipLabel.setFont(new Font("Montserrat Light", 14));
            ipLabel.setAlignment(Pos.CENTER);
            ipLabel.setStyle("-fx-border-width: 0.5; -fx-border-color: black;");
            ipFlowPane.getChildren().add(ipLabel);
        }

        for(IPRoute route : IPRoutes) {
            
            Label routeLabel = new Label(route.getPrefix() + " || " + route.getDestination());
            routeLabel.setPrefSize(220, 18);
            routeLabel.setFont(new Font("Montserrat Light", 13));
            routeLabel.setAlignment(Pos.CENTER);
            routeLabel.setStyle("-fx-border-width: 0.5; -fx-border-color: black;");
            routesFlowPane.getChildren().add(routeLabel);
        }

        for(Map.Entry<String, String> entry : bestMatch.entrySet()) {

            Label bestMatchLabel = new Label(entry.getKey() + " -> " + entry.getValue());
            bestMatchLabel.setPrefSize(220, 18);
            bestMatchLabel.setFont(new Font("Montserrat Light", 13));
            bestMatchLabel.setAlignment(Pos.CENTER);
            bestMatchLabel.setStyle("-fx-border-width: 0.5; -fx-border-color: black;");
            bestMatchFlowPane.getChildren().add(bestMatchLabel);
        }
    }

    public void initializeResults() {

        String [] linearArray = linearData.split("\n");
        String [] trieArray = trieData.split("\n");
        String [] hashTrieArray = hashTrieData.split("\n");

        timeLinearSearch.setText("Time Taken: " + String.format("%.2f", Double.parseDouble(linearArray[0].trim())) + " ms");
        memoryLinearSearch.setText("Memory Usage: " + String.format("%.2f", Double.parseDouble(linearArray[1].trim())) + " kb");

        timePureTrie.setText("Time Taken: " + String.format("%.2f",  Double.parseDouble(trieArray[0].trim())) + " ms");
        memoryPureTrie.setText("Memory Usage: " + String.format("%.2f", Double.parseDouble(trieArray[1].trim())) + " kb");

        timeHashTrie.setText("Time Taken: " + String.format("%.2f", Double.parseDouble(hashTrieArray[0].trim())) + " ms");
        memoryHashTrie.setText("Memory Usage: " + String.format("%.2f", Double.parseDouble(hashTrieArray[1].trim())) + " kb");
    }
}
