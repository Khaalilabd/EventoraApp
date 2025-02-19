package Gui.Reservation.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Reservation {
    @FXML
    private Button ajouterButton;

    @FXML
    private Button viewButton;

    // Action pour ajouter une reservation
    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();
            currentStage.setScene(addRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action pour afficher les reservations
    @FXML
    private void handleViewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheReservation.fxml"));
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
    private void goToReservation(ActionEvent event) throws IOException {
        // Charger l'interface Reservation.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
