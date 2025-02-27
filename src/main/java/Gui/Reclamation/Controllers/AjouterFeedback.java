package Gui.Reclamation.Controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import Services.Reclamation.Crud.FeedBackService;
import Services.Utilisateur.Crud.MembresService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import Models.Utilisateur.Utilisateurs;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AjouterFeedback {

    @FXML
    private ComboBox<String> userField;
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
    private final MembresService utilisateurService = new MembresService();
    private ObservableList<String> userNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadUserNames();
        userField.setItems(userNames);

        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            int ratingValue = i + 1;
            stars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> updateRating(ratingValue));
        }
        submitButton.setOnAction(event -> handleSubmit());
    }

    private void loadUserNames() {
        List<Utilisateurs> utilisateurs = utilisateurService.RechercherMem();
        for (Utilisateurs utilisateur : utilisateurs) {
            userNames.add(utilisateur.getNom() + " " + utilisateur.getPrenom());
        }
    }

    private void updateRating(int value) {
        rating = value;
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setStyle(i < rating ? "-fx-text-fill: gold;" : "-fx-text-fill: gray;");
        }
    }

    @FXML
    private void handleSubmit() {
        String description = descField.getText();
        Recommend recommend = recommendCheck.isSelected() ? Recommend.Oui : Recommend.Non;
        String selectedUserName = userField.getValue();

        if (description.trim().isEmpty() || selectedUserName == null) {
            showAlert("Error", "Please provide feedback and select a user.");
            return;
        }

        int userId = utilisateurService.getIdByNomPrenom(selectedUserName);
        if (userId == -1) {
            showAlert("Erreur", "Utilisateur non trouvé.");
            return;
        }

        Feedback newFeedBack = new Feedback();
        newFeedBack.setVote(rating);
        newFeedBack.setDescription(description);
        newFeedBack.setIdUser(userId);
        newFeedBack.setRecommend(recommend);
        feedBackService.AjouterFeedBack(newFeedBack);

        showAlert("Success", "Thank you for your feedback!");

        goToAfficherFeedback();
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
    private void goToAfficherFeedback() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AfficheFeedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene scene = new Scene(feedbackLayout);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the feedback display page.");
        }
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
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
}