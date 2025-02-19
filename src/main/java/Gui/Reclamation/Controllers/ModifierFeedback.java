package Gui.Reclamation.Controllers;

import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import Services.Reclamation.Crud.FeedBackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.jfoenix.controls.*;

import java.io.IOException;

public class ModifierFeedback {

    @FXML
    private JFXTextArea descField; // Champ de texte pour la description

    @FXML
    private JFXCheckBox recommendCheck; // Case à cocher pour la recommandation

    @FXML
    private JFXButton star1, star2, star3, star4, star5; // Boutons d'étoiles

    @FXML
    private JFXButton submitButton; // Bouton de soumission

    @FXML
    private Button cancelButton; // Bouton d'annulation

    private final FeedBackService feedbackService = new FeedBackService(); // Service de gestion des feedbacks
    private int selectedStars = 0; // Variable pour stocker les étoiles sélectionnées

    @FXML
    public void initialize() {
        // Configuration des boutons d'étoiles
        star1.setOnAction(e -> selectStars(1));
        star2.setOnAction(e -> selectStars(2));
        star3.setOnAction(e -> selectStars(3));
        star4.setOnAction(e -> selectStars(4));
        star5.setOnAction(e -> selectStars(5));

        // Action des boutons
        submitButton.setOnAction(this::handleSubmit);
        cancelButton.setOnAction(this::handleCancel);
    }

    private void selectStars(int stars) {
        selectedStars = stars;
        // Mettre à jour l'apparence des étoiles (par exemple, changer leur couleur)
        resetStarButtons();
        // Modifier la couleur des étoiles sélectionnées
        for (int i = 0; i < stars; i++) {
            JFXButton starButton = getStarButton(i);
            starButton.setStyle("-fx-font-size: 24px; -fx-text-fill: gold;");
        }
    }

    private void resetStarButtons() {
        // Réinitialiser la couleur de toutes les étoiles
        star1.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
        star2.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
        star3.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
        star4.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
        star5.setStyle("-fx-font-size: 24px; -fx-text-fill: gray;");
    }

    private JFXButton getStarButton(int index) {
        switch (index) {
            case 0: return star1;
            case 1: return star2;
            case 2: return star3;
            case 3: return star4;
            case 4: return star5;
            default: return null;
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String description = descField.getText();
        int vote = selectedStars;

        // Récupérer l'état de la case à cocher (si elle est cochée, recommend = Oui, sinon = Non)
        Recommend recommendation = recommendCheck.isSelected() ? Recommend.Oui : Recommend.Non;

        if (description.isEmpty() || vote == 0) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Créer un nouvel objet Feedback avec les données
        Feedback feedback = new Feedback(description, vote, recommendation);

        // Appeler le service pour enregistrer le feedback
        feedbackService.ModifierFeedBack(feedback);

        showAlert("Succès", "Feedback envoyé avec succès !");
        clearFields();
        navigateToFeedbackPage(event);
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            // Charger la scène AfficheFeedback
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheFeedback.fxml"));
            AnchorPane feedbackPage = loader.load();  // Charger le fichier FXML

            // Créer une nouvelle scène
            Scene scene = new Scene(feedbackPage);

            // Obtenir la fenêtre actuelle (Stage)
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(scene); // Changer la scène
            stage.show(); // Afficher la nouvelle scène

        } catch (IOException e) {
            // Gérer les erreurs de chargement
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Une erreur est survenue lors du changement de page.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearFields() {
        descField.clear();
        recommendCheck.setSelected(false);
        selectedStars = 0;

        // Réinitialiser l'apparence des étoiles
        resetStarButtons();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void navigateToFeedbackPage(ActionEvent event) {
        try {
            // Charger la scène de la page "AfficheFeedback"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheFeedback.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene currentScene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();

            // Changer de scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de navigation vers la page des feedbacks : " + e.getMessage());
        }
    }


    public void setFeedback(Feedback feedback) {
        // Logique pour modifier les informations du feedback (si nécessaire)
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
