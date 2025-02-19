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
    private JFXTextArea descField;
    @FXML
    private JFXCheckBox recommendCheck;
    @FXML
    private JFXButton star1, star2, star3, star4, star5;
    @FXML
    private JFXButton submitButton;
    @FXML
    private Button cancelButton;
    private final FeedBackService feedbackService = new FeedBackService();
    private int selectedStars = 0;

    @FXML
    public void initialize() {
        star1.setOnAction(e -> selectStars(1));
        star2.setOnAction(e -> selectStars(2));
        star3.setOnAction(e -> selectStars(3));
        star4.setOnAction(e -> selectStars(4));
        star5.setOnAction(e -> selectStars(5));
        submitButton.setOnAction(this::handleSubmit);
        cancelButton.setOnAction(this::handleCancel);
    }

    private void selectStars(int stars) {
        selectedStars = stars;
        resetStarButtons();
        for (int i = 0; i < stars; i++) {
            JFXButton starButton = getStarButton(i);
            starButton.setStyle("-fx-font-size: 24px; -fx-text-fill: gold;");
        }
    }

    private void resetStarButtons() {
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
        Recommend recommendation = recommendCheck.isSelected() ? Recommend.Oui : Recommend.Non;
        if (description.isEmpty() || vote == 0) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        Feedback feedback = new Feedback(description, vote, recommendation);
        feedbackService.ModifierFeedBack(feedback);
        showAlert("Succès", "Feedback envoyé avec succès !");
        clearFields();
        navigateToFeedbackPage(event);
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheFeedback.fxml"));
            AnchorPane feedbackPage = loader.load();
            Scene scene = new Scene(feedbackPage);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(scene); // Changer la scène
            stage.show();

        } catch (IOException e) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheFeedback.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            Stage stage = (Stage) currentScene.getWindow();
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
