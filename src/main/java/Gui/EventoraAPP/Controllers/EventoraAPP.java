package Gui.EventoraAPP.Controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private VBox chatContainer;

    @FXML
    private Button chatButton;

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
        changeScene(event, "/Reservation/Reservation.fxml", "Eventora - Réservations");
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        changeScene(event, "/Service/Service.fxml", "Eventora - Services");
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        changeScene(event, "/Pack/Packs.fxml", "Eventora - Packs");
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        changeScene(event, "/Reclamation/Feedback.fxml", "Eventora - Feedback");
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        changeScene(event, "/Reclamation/Reclamation.fxml", "Eventora - Réclamations");
    }

    @FXML
    private void goToParametres(ActionEvent event) {
        System.out.println("Paramètres cliqué");
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        System.out.println("Déconnexion cliquée");
        changeScene(event, "/Utilisateurs/Authentification.fxml", "Eventora - Authentification");
    }

    @FXML
    private void goToChat(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatAi/ChatAi.fxml"));
        Parent chatLayout = loader.load();
        Scene feedbackScene = new Scene(chatLayout);
        Stage newStage = new Stage();
        newStage.setScene(feedbackScene);
        newStage.setTitle("Eventora - Chatbot");
        Platform.runLater(() -> {
            newStage.setMaximized(true);
            newStage.show();
        });
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
    private void changeScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);

        Platform.runLater(() -> {
            stage.setMaximized(true);
            stage.show();
        });
    }

    public void toggleChat() {
        if (chatContainer.getChildren().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatAi/ChatAi.fxml"));
                Parent chatBox = loader.load();
                chatContainer.getChildren().add(chatBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!chatContainer.isVisible()) {
            chatContainer.setTranslateX(350);
            chatContainer.setVisible(true);
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), chatContainer);
            slideIn.setToX(0);
            slideIn.play();
        } else {
            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), chatContainer);
            slideOut.setToX(350);
            slideOut.setOnFinished(event -> chatContainer.setVisible(false));
            slideOut.play();
        }
    }
}