package central.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import central.objects.IPRoute;
import central.objects.TrieNode;
import central.panes.LinearSearchPane;
import central.utils.StageUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ResultsPageController implements Initializable {

    @FXML private Label backButton, exitButton, memoryHashTrie, memoryLinearSearch, 
                        memoryPureTrie, minimizeButton, timeHashTrie, timeLinearSearch, 
                        timePureTrie, ipBarClear, routeBarClear, bestMatchBarClear,
                        linearSearchLabel, trieLabel, hashTrieLabel;
    @FXML private FlowPane bestMatchFlowPane, ipFlowPane, routesFlowPane;
    @FXML private TextField ipSearchBar, routeSearchBar, bestMatchSearchBar;

    private List<String> IPAddresses, linearTraversal, trieTraversal;
    private List<IPRoute> IPRoutes;
    private HashMap<String, String> bestMatch;
    private String linearData, trieData, hashTrieData;
    private TrieNode trieNodeRoot;

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

        ipBarClear.setOnMouseClicked(event -> {ipSearchBar.clear();});
        ipBarClear.setOnMouseEntered(event ->{ipBarClear.setStyle("-fx-text-fill: red;");});
        ipBarClear.setOnMouseExited(event ->{ipBarClear.setStyle("");});
        
        routeBarClear.setOnMouseClicked(event -> {routeSearchBar.clear();});
        routeBarClear.setOnMouseEntered(event ->{routeBarClear.setStyle("-fx-text-fill: red;");});
        routeBarClear.setOnMouseExited(event ->{routeBarClear.setStyle("");});

        bestMatchBarClear.setOnMouseClicked(event -> {bestMatchSearchBar.clear();});
        bestMatchBarClear.setOnMouseEntered(event ->{bestMatchBarClear.setStyle("-fx-text-fill: red;");});
        bestMatchBarClear.setOnMouseExited(event ->{bestMatchBarClear.setStyle("");});

        linearSearchLabel.setOnMouseEntered(event ->{linearSearchLabel.setStyle("-fx-text-fill: white");});
        linearSearchLabel.setOnMouseExited(event ->{linearSearchLabel.setStyle("");});
        linearSearchLabel.setOnMouseClicked(event -> {
            try {
                visualizeLinearSearch();
            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    public void setData(List<String> IPAddresses, List<IPRoute> IPRoutes, String linearData, String trieData, String hashTrieData, HashMap<String, String> bestMatch, List<String> linearTraversal,  List<String> trieTraversal, TrieNode trieNodeRoot) {

        this.IPAddresses = IPAddresses;
        this.IPRoutes = IPRoutes;
        this.bestMatch = bestMatch;
        this.linearData = linearData;
        this.trieData = trieData;
        this.hashTrieData = hashTrieData;
        this.linearTraversal = linearTraversal;
        this.trieTraversal = trieTraversal;
        this.trieNodeRoot = trieNodeRoot;
    }

    public void initializePanes() {

        ipFlowPane.setPrefSize(226, 20 * IPAddresses.size());
        routesFlowPane.setPrefSize(226, 20 * IPRoutes.size());
        bestMatchFlowPane.setPrefSize(226, 19.3 * bestMatch.size());

        ipFlowPane.getChildren().clear();
        routesFlowPane.getChildren().clear();
        bestMatchFlowPane.getChildren().clear();

        ipSearchBar.textProperty().addListener((observable, oldValue, newValue) -> filterFlowPane(ipFlowPane, IPAddresses, null, newValue));
        routeSearchBar.textProperty().addListener((observable, oldValue, newValue) -> filterFlowPane(routesFlowPane, IPRoutes, null, newValue));
        bestMatchSearchBar.textProperty().addListener((observable, oldValue, newValue) -> filterFlowPane(bestMatchFlowPane, null, bestMatch, newValue));

        populateFlowPane(ipFlowPane, IPAddresses, null);
        populateFlowPane(routesFlowPane, IPRoutes, null);
        populateFlowPane(bestMatchFlowPane, null, bestMatch);
    }

    private void filterFlowPane(FlowPane flowPane, List<?> items, HashMap<?, ?> bestMatch,  String query) {

        flowPane.getChildren().clear();

        if(items != null && !items.isEmpty() && items.get(0) instanceof String) {

            for (String item : (List<String>) items) {
                if (item.contains(query)) {
                    Label label = createLabel(item);
                    flowPane.getChildren().add(label);
                }
            }
        }

        else if (items != null && !items.isEmpty() && items.get(0) instanceof IPRoute) {

            for (IPRoute route : (List<IPRoute>) items) {
                String content = route.getPrefix() + " || " + route.getDestination();
                if (content.contains(query)) {
                    Label label = createLabel(content);
                    flowPane.getChildren().add(label);
                }
            }
        }

        else if (bestMatch != null && !bestMatch.isEmpty()){

            for (Map.Entry<?, ?> entry : bestMatch.entrySet()) {
                String content = entry.getKey() + " -> " + entry.getValue();
                if (content.contains(query)) {
                    Label label = createLabel(content);
                    flowPane.getChildren().add(label);
                }
            }
        }
    }

    private void populateFlowPane(FlowPane flowPane, List<?> items, HashMap<?, ?> bestMatch) {
        filterFlowPane(flowPane, items, bestMatch, "");
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

    private Label createLabel(String text) {

        Label label = new Label(text);
        label.setPrefSize(222, 18);
        label.setFont(new Font("Montserrat Light", 13));
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-border-width: 0.5; -fx-border-color: black;");
        return label;
    }

    private void visualizeLinearSearch() throws IOException {

        LinearSearchPane linearPane = new LinearSearchPane(linearTraversal, linearTraversal.getLast());

        StageUtil modalStage = new StageUtil("/resources/fxml/visualizationPage.fxml", ((Stage)ipBarClear.getScene().getWindow()));
        VisualizationPageController controller = (VisualizationPageController) modalStage.getController();
        controller.setTitle("Linear Search Visualization", linearTraversal.get(0));
        controller.setContentPane(linearPane, linearPane);

        controller.getContentPane().setPrefSize(115 * linearTraversal.size(), 300);

        linearPane.displayList();
        linearPane.visualizeTraversal(0.5);
    }
}
