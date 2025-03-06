package Gui.Service.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Service {

    @FXML private Button ajouterButton;
    @FXML private Button viewButton;
    @FXML private Button ajouterButton_partenaire;
    @FXML private Button viewButton_partenaire;

    @FXML
    private void handleAddAction(ActionEvent event) {
        switchScene(event, "/Service/AjouterService.fxml");
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        switchScene(event, "/Service/AfficherService.fxml");
    }

    @FXML
    private void handleAddAction_partenaire(ActionEvent event) {
        switchScene(event, "/Service/AjouterPartenaire.fxml");
    }

    @FXML
    private void handleViewAction_partenaire(ActionEvent event) {
        switchScene(event, "/Service/AfficherPartenaire.fxml");
    }

    @FXML
    private void gotoNavUser(ActionEvent event) {
        switchScene(event, "/Utilisateurs/Utilisateur.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/Acceuil.fxml");
    }

    // Méthode switchScene suivant le style mémorisé
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Eventora");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}