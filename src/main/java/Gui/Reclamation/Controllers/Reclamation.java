package Gui.Reclamation.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Reclamation {

    @FXML
    private VBox mainLayout;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button sendFeedbackButton;

    @FXML
    private TextArea feedbackField;

    @FXML
    private Label feedbackLabel;

    // Méthode pour l'action du bouton "Ajouter"
    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
            // Charger l'interface AjouterRec.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRec.fxml"));
            VBox addRecLayout = loader.load();

            // Créer une nouvelle scène et l'ouvrir dans une nouvelle fenêtre
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) addButton.getScene().getWindow(); // Récupérer la fenêtre actuelle
            currentStage.setScene(addRecScene); // Changer la scène
            currentStage.show(); // Afficher la nouvelle scène

        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'erreur si le fichier FXML n'est pas trouvé
        }
    }

    // Méthode pour l'action du bouton "Modifier"
    @FXML
    private void handleEditAction(ActionEvent event) {
        // Logique pour modifier une réclamation
        System.out.println("Modifier une réclamation");
        editButton.setStyle("-fx-background-color: #E6957E;"); // Optionnel : changement de couleur
    }

    // Méthode pour l'action du bouton "Supprimer"
    @FXML
    private void handleDeleteAction(ActionEvent event) {
        // Logique pour supprimer une réclamation
        System.out.println("Supprimer une réclamation");
        deleteButton.setStyle("-fx-background-color: #E6957E;"); // Optionnel : changement de couleur
    }

    // Méthode pour l'action du bouton "Envoyer Feedback"
    @FXML
    private void handleSendFeedbackAction(ActionEvent event) {
        String feedback = feedbackField.getText();
        if (feedback != null && !feedback.trim().isEmpty()) {
            // Logique pour envoyer le feedback (par exemple, sauvegarde ou envoi au serveur)
            System.out.println("Feedback envoyé : " + feedback);
            feedbackField.clear(); // Efface le champ de texte après l'envoi
            feedbackLabel.setText("Feedback envoyé avec succès !");
            feedbackLabel.setStyle("-fx-text-fill: green;"); // Change la couleur du texte en vert
        } else {
            feedbackLabel.setText("Veuillez entrer un feedback.");
            feedbackLabel.setStyle("-fx-text-fill: red;"); // Change la couleur du texte en rouge
        }
    }
}
