package Gui.Reservation.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de " + fxmlPath + " : " + e.getMessage());
        }
    }

    @FXML
    private void handleAddActionPack(ActionEvent event) {
        loadScene(event, "/Reservation/AjouterReservationPack.fxml");
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        loadScene(event, "/Reservation/AfficheReservationPack.fxml");
    }

    @FXML
    private void handleViewActionService(ActionEvent event) {
        loadScene(event, "/Reservation/AfficherReservationPersonalise.fxml");
    }

    @FXML
    private void handleAddActionService(ActionEvent event) {
        loadScene(event, "/Reservation/AjouterReservationPersonalise.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        loadScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        loadScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        loadScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de /Service/Service.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void goToPack(ActionEvent event) {
        loadScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void gotoNavUser(ActionEvent event) {
        loadScene(event, "/Utilisateurs/Utilisateur.fxml");
    }
}
