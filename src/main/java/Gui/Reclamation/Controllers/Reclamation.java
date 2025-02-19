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

public class Reclamation {

    @FXML
    private Button ajouterButton;
    @FXML
    private Button viewButton;

    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRec.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();
            currentStage.setScene(addRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheRec.fxml"));
            AnchorPane viewRecLayout = loader.load();
            Scene viewRecScene = new Scene(viewRecLayout);
            Stage currentStage = (Stage) viewButton.getScene().getWindow();
            currentStage.setScene(viewRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        // Charger l'interface Reclamation.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene feedbackScene = new Scene(feedbackLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(feedbackScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
