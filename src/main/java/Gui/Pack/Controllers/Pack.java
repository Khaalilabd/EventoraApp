package Gui.Pack.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Utils.SessionManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Pack {

    @FXML
    private Button ajouterPackButton, viewPackButton;
    @FXML
    private ImageView imagePack;

    private Utilisateurs utilisateurConnecte; // Utilisateur connecté

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté via SessionManager
        utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateurConnecte == null) {
            showError("Erreur", "Aucun utilisateur connecté. Veuillez vous connecter.");
        } else {
            // Masquer ajouterPackButton pour MEMBRE
            if (utilisateurConnecte.getRole() == Role.MEMBRE && ajouterPackButton != null) {
                ajouterPackButton.setVisible(false);
                ajouterPackButton.setManaged(false);
            }
        }
        animateImage();
    }

    private void animateImage() {
        if (imagePack != null) {
            TranslateTransition floating = new TranslateTransition(Duration.seconds(2), imagePack);
            floating.setByY(10);
            floating.setAutoReverse(true);
            floating.setCycleCount(TranslateTransition.INDEFINITE);
            floating.play();
        }
    }

    @FXML
    private void handleAddPackAction(ActionEvent event) {
        switchScene(event, "/Pack/AjouterPack.fxml");
    }

    @FXML
    private void handleViewPacksAction(ActionEvent event) {
        switchScene(event, "/Pack/AffichePack.fxml");
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
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }

    @FXML
    private void goToParams(ActionEvent event) {
        switchScene(event, "/Utilisateurs/Parametres.fxml");
    }

    @FXML
    private void goToUser(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    @FXML
    private void deconnexion(ActionEvent event) {
        // Effacer la session
        SessionManager.getInstance().clearSession();
        // Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur", "Erreur lors du chargement de la page de connexion.");
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
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