package Gui.Utilisateurs.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class Utilisateur {

    @FXML private Button reclamationButton;
    @FXML private Button informationsButton;
    @FXML private Button reservationButton;
    @FXML private Button parametresButton;

    @FXML
    public void initialize() {
        // Initialisation si nécessaire
    }

    @FXML
    public void goToAcceuil(ActionEvent event) {
        navigateTo("/Utilisateurs/Identification.fxml", "Accueil", event);
    }

    @FXML
    public void goToReservation(ActionEvent event) {
        navigateTo("/Reservation/Reservation.fxml", "Réservation", event);
    }

    @FXML
    public void goToService(ActionEvent event) {
        navigateTo("/Service/Services.fxml", "Services", event);
    }

    @FXML
    public void goToPack(ActionEvent event) {
        navigateTo("/Pack/Packs.fxml", "Packs", event);
    }

    @FXML
    public void goToFeedback(ActionEvent event) {
        navigateTo("/Reclamation/Feedback.fxml", "Feedback", event);
    }

    @FXML
    public void goToReclamation(ActionEvent event) {
        navigateTo("/Reclamation/Reclamation.fxml", "Réclamation", event);
    }

    @FXML
    public void goToParametres(ActionEvent event) {
        navigateTo("/Utilisateurs/Parametres.fxml", "Paramètres", event);
    }

    @FXML
    public void goToAuth(ActionEvent event) {
        navigateTo("/Utilisateurs/Authentification.fxml", "Authentification", event);
    }

    @FXML
    public void goToInformations(ActionEvent event) {
        navigateTo("/Utilisateurs/Informations.fxml", "Informations", event);
    }

    private void navigateTo(String fxmlPath, String pageName, ActionEvent event) {
        try {
            java.net.URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                System.err.println("Erreur : Impossible de trouver " + pageName + ".fxml dans " + fxmlPath);
                showAlert("Erreur", "Impossible de charger la page " + pageName + ".");
                return;
            }
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page " + pageName + " : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}