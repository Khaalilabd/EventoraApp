package Gui.Reclamation.Controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import Services.Reclamation.Crud.FeedBackService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterFeedback {

    @FXML
    private JFXTextArea descField;

    @FXML
    private JFXCheckBox recommendCheck;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton star1, star2, star3, star4, star5;

    private int rating = 0;
    private final FeedBackService feedBackService = new FeedBackService();

    @FXML
    private void initialize() {
        // Initialisation du système de notation par étoiles
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            int ratingValue = i + 1;
            stars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> updateRating(ratingValue));
        }

        submitButton.setOnAction(event -> handleSubmit());
    }

    // Mise à jour de la note (étoiles dorées ou grises)
    private void updateRating(int value) {
        rating = value;
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setStyle(i < rating ? "-fx-text-fill: gold;" : "-fx-text-fill: gray;");
        }
    }

    // Gestion de la soumission du feedback
    @FXML
    private void handleSubmit() {
        String description = descField.getText();

        // Vérifier la case recommend
        Recommend recommend = recommendCheck.isSelected() ? Recommend.YES : Recommend.NO;

        if (description.trim().isEmpty()) {
            showAlert("Error", "Please provide feedback.");
            return;
        }
        Feedback newFeedBack = new Feedback();
        newFeedBack.setVote(rating);
        newFeedBack.setDescription(description);
        newFeedBack.setIdUser(1); // Exemple, à remplacer par l'ID de l'utilisateur actuel
        newFeedBack.setRecommend(recommend); // Enregistrer directement l'énumération

        // Ajouter le feedback dans la base de données
        feedBackService.AjouterFeedBack(newFeedBack);

        // Afficher les informations de feedback pour débogage (facultatif)
        System.out.println("Rating: " + rating);
        System.out.println("Feedback: " + description);
        System.out.println("Recommend: " + recommend);

        showAlert("Success", "Thank you for your feedback!");

        goToAfficherFeedback();
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        JFXAlert<?> alert = new JFXAlert<>();
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(message));
        alert.setContent(layout);
        alert.show();
    }

    // Méthode pour rediriger vers AfficherFeedback
    @FXML
    private void goToAfficherFeedback() {
        try {
            // Charger l'interface AfficherFeedback.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheFeedback.fxml")); // Vérifie que c'est le bon chemin
            AnchorPane feedbackLayout = loader.load();
            Scene scene = new Scene(feedbackLayout);
            Stage stage = (Stage) submitButton.getScene().getWindow(); // On récupère la scène de l'utilisateur
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the feedback display page.");
        }
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        // Charger l'interface Reclamation.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
