package Gui.Reclamation.Controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Reclamation {

    @FXML
    private Button ajouterButton, viewButton;
    @FXML
    private ImageView imageReclamation;

    @FXML
    public void initialize() {
        animateImage();
    }

    private void animateImage() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), imageReclamation);
        floating.setByY(10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        switchScene(event, "/AjouterRec.fxml");
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        switchScene(event, "/AfficheRec.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Feedback.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
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