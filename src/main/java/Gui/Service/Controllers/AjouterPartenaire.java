package Gui.Service.Controllers;

import Models.Service.Partenaire;
import Models.Service.TypePartenaire;
import Services.Service.Crud.PartenaireService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterPartenaire {

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private TextField adresseField;
    @FXML private TextField site_webField;
    @FXML private ComboBox<TypePartenaire> typefield;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private final PartenaireService partenaireService = new PartenaireService();

    public void initialize() {
        typefield.getItems().setAll(TypePartenaire.values());
        submitButton.setOnAction(this::ajouterPartenaire);
        cancelButton.setOnAction(event -> annuler());
    }

    private void ajouterPartenaire(ActionEvent event) {
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String adresse = adresseField.getText();
        String site_web = site_webField.getText();
        TypePartenaire type = typefield.getValue();

        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty() || site_web.isEmpty() || type == null) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nom.matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le nom doit contenir uniquement des caractères alphabétiques.");
            return;
        }

        if (!email.matches("^[\\w.-]+@(gmail\\.com|esprit\\.tn)$")) {
            showAlert("Erreur", "L'email doit se terminer par '@gmail.com' ou '@esprit.tn'.");
            return;
        }

        if (!telephone.matches("\\d+")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir uniquement des chiffres.");
            return;
        }

        Partenaire partenaire = new Partenaire(nom, email, telephone, adresse, site_web, type);
        partenaireService.AjouterPartenaire(partenaire);
        showAlert("Succès", "Partenaire ajouté avec succès !");
        clearFields();
        switchScene(event, "/Service/AfficherPartenaire.fxml");
    }

    private void annuler() {
        clearFields();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") || title.equals("Warning") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        typefield.setValue(null);
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
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