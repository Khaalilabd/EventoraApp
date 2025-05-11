package Gui.Pack.Controllers;

import Models.Pack.Evenement;
import Services.Pack.Crud.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierEvenement {

    @FXML
    private TextField NomTypeField;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private Evenement evenementToEdit;
    private final EvenementService EvenementService = new EvenementService();

    @FXML
    public void initialize() {
        // Désactivation du bouton tant qu'un événement n'est pas défini
        submitButton.setDisable(true);
    }

    public void setEvenementToEdit(Evenement evenement) {
        this.evenementToEdit = evenement;

        // Remplissage des champs avec les données existantes
        NomTypeField.setText(evenement.getTypeEvenement());

        // Activation du bouton une fois que les données sont chargées
        submitButton.setDisable(false);
    }

    @FXML
    private void modifierEvenement(ActionEvent event) {
        try {
            String type = NomTypeField.getText();
            if (type.isEmpty()) {
                showAlert("Erreur", "Les champs doivent être remplis.");
                return;
            }
            if (!type.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
                showAlert("Erreur", "Le type ne doit contenir que des lettres et des espaces.");
                return;
            }
            this.evenementToEdit.setTypeEvenement(type);
            EvenementService.modifier(this.evenementToEdit);
            showAlert("Succès", "Type modifié avec succès !");
            switchScene(event, "/Pack/AfficheEvenement.fxml");

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la modification du type.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        switchScene(event, "/Pack/AfficheEvenement.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml"); // Corrigé le chemin de "/Packs.fxml" à "/Pack/Packs.fxml"
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    // Méthode switchScene suivant le style mémorisé
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}