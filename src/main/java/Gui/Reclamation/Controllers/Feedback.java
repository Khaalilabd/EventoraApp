package Gui.Reclamation.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Feedback {

    @FXML
    private Button ajouterButton;  // Correspond à "fx:id=ajouterButton" dans Feedback.fxml

    @FXML
    private Button viewButton;  // Correspond à "fx:id=viewButton" dans Feedback.fxml

    // Méthode pour charger une scène en fonction du fichier FXML donné
    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Action pour ajouter un feedback
    @FXML
    private void handleAddAction(ActionEvent event) {
        loadScene(event, "/AjouterFeedback.fxml");
    }


    // Action pour afficher les feedbacks
    @FXML
    private void handleViewAction(ActionEvent event) {
        loadScene(event, "/AfficheFeedback.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        loadScene(event, "/Reclamation.fxml");
    }
    @FXML
    private void goToFeedback(ActionEvent event) {
        loadScene(event, "/Feedback.fxml");
    }

}
