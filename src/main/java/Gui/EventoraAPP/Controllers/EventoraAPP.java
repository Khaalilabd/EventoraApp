package Gui.EventoraAPP.Controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class EventoraAPP {

    @FXML
    private Button btnAccueil, btnUtilisateur, btnReservation, btnService, btnPack, btnFeedback, btnReclamation, btnParametres, btnDeconnexion;

    @FXML
    private VBox card1, card2, card3;

    @FXML
    private void goToAccueil(ActionEvent event) {
        System.out.println("Accueil cliqué");
    }

    @FXML
    private void goToUtilisateur(ActionEvent event) {
        System.out.println("Utilisateur cliqué");
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        changeScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        changeScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        changeScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        changeScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        changeScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToParametres(ActionEvent event) {
        System.out.println("Paramètres cliqué");
    }

    @FXML
    private void logout(ActionEvent event) {
        System.out.println("Déconnexion cliquée");
        Stage stage = (Stage) btnDeconnexion.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        animateCard(card1);
        animateCard(card2);
        animateCard(card3);
    }

    private void animateCard(VBox card) {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), card);
        floating.setByY(-10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }

    // 🔹 Méthode générique pour changer de scène et occuper tout l'écran
    private void changeScene(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.setMaximized(true); // 🔥 Permet à la fenêtre d'occuper tout l'écran sans mode plein écran
        stage.show();
    }
    @FXML
    private void goToChat(ActionEvent event) throws IOException {
        // Charger la nouvelle scène
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chatbot/Chat.fxml"));
        Parent chatLayout = loader.load();
        Scene feedbackScene = new Scene(chatLayout);

        // Obtenir la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        // Ouvrir la nouvelle scène dans une nouvelle fenêtre
        Stage newStage = new Stage();
        newStage.setScene(feedbackScene);
        newStage.show();
    }
}
