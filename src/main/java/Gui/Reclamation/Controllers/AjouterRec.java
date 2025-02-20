package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import Models.Reclamation.Statut;  // Assurez-vous d'importer l'énum Statut
import Services.Reclamation.Crud.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterRec {

    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<TypeReclamation> typeField; // ComboBox pour le type
    @FXML
    private TextArea descField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private final ReclamationService reclamationService = new ReclamationService();

    private int userId = 1;

    @FXML
    public void initialize() {
        typeField.getItems().setAll(TypeReclamation.values());
        submitButton.setOnAction(this::ajouterReclamation);
        cancelButton.setOnAction(event -> annuler());
    }

    private void ajouterReclamation(ActionEvent event) {
        String titre = titleField.getText();
        String description = descField.getText();
        TypeReclamation type = typeField.getValue();
        if (titre.isEmpty() || description.isEmpty() || type == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        Reclamation reclamation = new Reclamation(userId, titre, description, type);
        reclamation.setStatut(Statut.EN_ATTENTE);
        reclamationService.AjouterRec(reclamation);
        showAlert("Succès", "Réclamation ajoutée avec succès !");
        clearFields();
        goToAfficherRec(event);
    }

    private void goToAfficherRec(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AfficheRec.fxml"));
            Parent root = loader.load(); //
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        descField.clear();
        typeField.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
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
    private void goToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene feedbackScene = new Scene(feedbackLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(feedbackScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Services/Service.fxml"));
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
