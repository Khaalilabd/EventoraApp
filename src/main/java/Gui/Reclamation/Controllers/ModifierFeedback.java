package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.FeedBackService;
import Services.Utilisateur.Crud.MembresService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import Models.Utilisateur.Utilisateurs;
import Utils.SessionManager;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ModifierFeedback {

    @FXML private Label feedbackIdLabel;
    @FXML private ComboBox<String> userField;
    @FXML private JFXTextArea descField;
    @FXML private JFXCheckBox recommendCheck;
    @FXML private JFXButton submitButton;
    @FXML private JFXButton star1, star2, star3, star4, star5;

    private int rating = 0;
    private final FeedBackService feedBackService = new FeedBackService();
    private final MembresService utilisateurService = new MembresService();
    private ObservableList<String> userNames = FXCollections.observableArrayList();
    private Feedback feedback;

    @FXML
    public void initialize() {
        loadUserNames();
        userField.setItems(userNames);

        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            int ratingValue = i + 1;
            stars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> updateRating(ratingValue));
        }
    }

    private void loadUserNames() {
        List<Utilisateurs> utilisateurs = utilisateurService.RechercherMem();
        for (Utilisateurs utilisateur : utilisateurs) {
            userNames.add(utilisateur.getNom() + " " + utilisateur.getPrenom());
        }
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
        feedbackIdLabel.setText("Feedback ID: " + feedback.getId());
        // Pré-remplir le nom de l'utilisateur (non modifiable)
        String userName = utilisateurService.getUserNameById(feedback.getIdUser());
        userField.setValue(userName);
        userField.setDisable(true); // Désactiver la modification de l'utilisateur

        // Pré-remplir les étoiles
        rating = feedback.getVote();
        updateRating(rating);

        // Pré-remplir la description
        descField.setText(feedback.getDescription());

        // Pré-remplir la recommandation
        recommendCheck.setSelected(feedback.getRecommend() == Recommend.Oui);
    }

    private void updateRating(int value) {
        rating = value;
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setStyle(i < rating ? "-fx-text-fill: gold;" : "-fx-text-fill: gray;");
        }
    }

    @FXML
    private void saveFeedback(ActionEvent event) {
        try {
            String description = descField.getText();
            Recommend recommend = recommendCheck.isSelected() ? Recommend.Oui : Recommend.Non;

            if (description.trim().isEmpty()) {
                showAlert("Error", "Please provide feedback description.");
                return;
            }

            // Mettre à jour l'objet Feedback
            feedback.setVote(rating);
            feedback.setDescription(description);
            feedback.setRecommend(recommend);

            // Sauvegarder dans la base de données
            feedBackService.ModifierFeedBack(feedback);

            // Afficher une confirmation
            showAlert("Success", "Feedback modified successfully!");

            // Retourner à la liste des feedbacks
            goToafficheFeedb(event);
        } catch (Exception e) {
            showAlert("Error", "An error occurred while modifying the feedback: " + e.getMessage());
        }
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Feedback.fxml", event);
    }

    @FXML
    private void goToAccueil(ActionEvent event) throws IOException {
        switchScene("/EventoraAPP/EventoraAPP.fxml", event);
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        switchScene("/Reservation/Reservation.fxml", event);
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        switchScene("/Service/Service.fxml", event);
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
    }
    @FXML
    private void goToafficheFeedb(ActionEvent event) throws IOException {
        switchScene("/Reclamation/AfficheFeedback.fxml", event);
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Reclamation.fxml", event);
    }

    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent layout = loader.load();
            Scene newScene = new Scene(layout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            showAlert("Error", "Error while changing scene: " + e.getMessage());
            throw e;
        }
    }

    private void showAlert(String title, String message) {
        JFXAlert<?> alert = new JFXAlert<>();
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(message));
        alert.setContent(layout);
        alert.show();
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
            System.out.println("Erreur lors du chargement de la page de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}