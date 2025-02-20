package Gui.Service.Controllers;

import Models.Service.Partenaire;
import Models.Service.TypePartenaire;
import Services.Service.Crud.PartenaireService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.event.ActionEvent;
public class AjouterPartenaire {
    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField site_webField;

    @FXML
    private ComboBox<TypePartenaire> typefield;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    private PartenaireService partenaireService = new PartenaireService();
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

        // Vérifier que tous les champs sont remplis
        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty() || site_web.isEmpty() || type == null) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation : le nom ne doit contenir que des caractères alphabétiques
        if (!nom.matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le nom doit contenir uniquement des caractères alphabétiques.");
            return;
        }

        // Validation : l'email doit se terminer par "@gmail.com" ou "@esprit.tn"
        if (!email.matches("^[\\w.-]+@(gmail\\.com|esprit\\.tn)$")) {
            showAlert("Erreur", "L'email doit se terminer par '@gmail.com' ou '@esprit.tn'.");
            return;
        }

        // Validation : le téléphone doit contenir uniquement des chiffres
        if (!telephone.matches("\\d+")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir uniquement des chiffres.");
            return;
        }

        // Validation : le montant doit contenir uniquement des chiffres et se terminer par "dt" ou "DT"


        // Si toutes les validations passent, créer et ajouter le partenaire
        Partenaire partenaire = new Partenaire(nom, email, telephone, adresse, site_web, type);
        partenaireService.AjouterPartenaire(partenaire);
        showAlert("Succès", "Partenaire ajouté avec succès !");
        clearFields();
        gotoAfficherPartenaire(event);
    }

    public void gotoAfficherPartenaire(ActionEvent event) {
        try {
            // Charger la nouvelle scène (AfficherPartenaire.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPartenaire.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Obtenir la fenêtre (Stage) actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des partenaires : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void annuler() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        typefield.setValue(null);
    }
    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void clearFields() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        typefield.setValue(null);
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        // Charger la nouvelle scène (Service.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        // Obtenir la fenêtre (Stage) actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        currentStage.close();

        // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
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
    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
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
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
