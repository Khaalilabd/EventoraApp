package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ModifierUtilisateur {

    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField cinField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button modifierButton;
    @FXML private Button annulerButton;
    @FXML private Button rechercherButton;

    private final MembresService membresService = new MembresService();
    private Utilisateurs membreAModifier;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.ADMIN, Role.AGENT, Role.MEMBRE);
        animateButton();
    }

    // Animation du bouton "Rechercher" (inspirée de Reclamation)
    private void animateButton() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), rechercherButton);
        floating.setByY(10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }

    // Charger un utilisateur depuis AfficherUtilisateur
    public void setUtilisateur(Utilisateurs utilisateur) {
        this.membreAModifier = utilisateur;
        if (membreAModifier != null) {
            remplirChamps();
        }
    }

    @FXML
    private void rechercherMembre(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());
            membreAModifier = membresService.rechercherMem(id);

            if (membreAModifier != null) {
                remplirChamps();
            } else {
                showError("Erreur de recherche", "Aucun membre trouvé avec cet ID.");
                viderChamps();
            }
        } catch (NumberFormatException e) {
            showError("Erreur d'entrée", "ID invalide. Veuillez entrer un nombre entier.");
        }
    }

    @FXML
    private void modifierMembre(ActionEvent event) {
        if (membreAModifier == null) {
            showError("Erreur", "Veuillez d'abord sélectionner ou rechercher un membre.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String adresse = adresseField.getText();
        String numTel = numTelField.getText();
        Role role = roleComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() ||
                adresse.isEmpty() || numTel.isEmpty() || role == null) {
            showError("Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("Erreur", "Format d'email invalide.");
            return;
        }

        if (!numTel.matches("\\d+")) {
            showError("Erreur", "Le numéro de téléphone doit contenir uniquement des chiffres.");
            return;
        }

        if (!cin.matches("\\d{8}")) {
            showError("Erreur", "Le CIN doit contenir exactement 8 chiffres.");
            return;
        }

        if (!nom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+") || !prenom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+")) {
            showError("Erreur", "Le nom et le prénom doivent contenir uniquement des lettres, espaces ou tirets.");
            return;
        }

        membreAModifier.setNom(nom);
        membreAModifier.setPrenom(prenom);
        membreAModifier.setEmail(email);
        membreAModifier.setCin(cin);
        membreAModifier.setAdresse(adresse);
        membreAModifier.setNumTel(numTel);
        membreAModifier.setRole(role);

        membresService.ModifierMem(membreAModifier);
        showSuccess("Succès", "Membre modifié avec succès.");
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    @FXML
    private void cancel(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    // Méthodes de navigation inspirées de Reclamation et du FXML fourni
    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
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
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    // Méthode de navigation générique
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

    // Méthodes utilitaires
    private void remplirChamps() {
        idField.setText(String.valueOf(membreAModifier.getId()));
        nomField.setText(membreAModifier.getNom());
        prenomField.setText(membreAModifier.getPrenom());
        emailField.setText(membreAModifier.getEmail());
        cinField.setText(membreAModifier.getCin());
        adresseField.setText(membreAModifier.getAdresse());
        numTelField.setText(membreAModifier.getNumTel());
        roleComboBox.setValue(membreAModifier.getRole());
    }

    private void viderChamps() {
        idField.clear();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        cinField.clear();
        adresseField.clear();
        numTelField.clear();
        roleComboBox.setValue(null);
    }

    // Gestion des erreurs et succès
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}