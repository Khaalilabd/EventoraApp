package Gui.Reclamation.Controllers;

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

public class Feedback {
    @FXML
    private Button ajouterButton;
    @FXML
    private Button viewButton;
    @FXML
    private ImageView imageFeedback;

    @FXML
    public void initialize() {
        animateImage();
    }

    // Méthode générique pour charger une nouvelle scène
    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlFile);
            e.printStackTrace();
        }
    }

    // Animation de l'image
    private void animateImage() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), imageFeedback);
        floating.setByY(10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }

    // Méthode pour fermer la scène actuelle et charger une nouvelle scène
    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        loadScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        loadScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        loadSceneInNewStage(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        loadScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        loadScene(event, "/Reclamation/AjouterFeedback.fxml");
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        loadScene(event, "/Reclamation/AfficheFeedback.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        loadScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        loadScene(event, "/Reclamation/Feedback.fxml");
    }

    // Ouvre une nouvelle scène dans une nouvelle fenêtre
    private void loadSceneInNewStage(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlFile);
            e.printStackTrace();
        }
    }

    // Afficher une alerte en cas d'erreur
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
