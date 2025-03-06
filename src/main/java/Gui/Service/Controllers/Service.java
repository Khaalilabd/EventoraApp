package Gui.Service.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Service {
    @FXML
    private Button ajouterButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button ajouterButton_partenaire;
    @FXML
    private Button viewButton_partenaire;

    @FXML
    private void handleAddAction(ActionEvent event) {
        loadScene(event, "/Service/AjouterService.fxml");
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        loadScene(event, "/Service/AfficherService.fxml");
    }

    @FXML
    private void handleAddAction_partenaire(ActionEvent event) {
        loadScene(event, "/Service/AjouterPartenaire.fxml");
    }

    @FXML
    private void handleViewAction_partenaire(ActionEvent event) {
        loadScene(event, "/Service/AfficherPartenaire.fxml");
    }

    @FXML
    private void gotoNavUser(ActionEvent event) {
        loadScene(event, "/Utilisateurs/Utilisateur.fxml");
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
        loadScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        loadScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        loadScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        loadScene(event, "/EventoraAPP/Acceuil.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Eventora");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible de charger la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
