package Gui.Reclamation.Controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private void animateImage() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), imageFeedback);
        floating.setByY(10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }
    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        loadScene(event, "/Reclamation/Feedback.fxml");
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de Reservation.fxml : " + e.getMessage());
        }
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }
    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
}
