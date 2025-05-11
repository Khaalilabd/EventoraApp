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

    /**
     * Anime le bouton "Rechercher".
     */
    private void animateButton() {
        TranslateTransition floating = new TranslateTransition(Duration.seconds(2), rechercherButton);
        floating.setByY(10);
        floating.setAutoReverse(true);
        floating.setCycleCount(TranslateTransition.INDEFINITE);
        floating.play();
    }

    /**
     * Charge un utilisateur pour modification.
     * @param utilisateur L'utilisateur à modifier.
     */
    public void setUtilisateur(Utilisateurs utilisateur) {
        this.membreAModifier = utilisateur;
        if (membreAModifier != null) {
            remplirChamps();
        }
    }

    /**
     * Recherche un membre par ID.
     * @param event L'événement déclenché par l'utilisateur.
     */
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
        } catch (Exception e) {
            showError("Erreur", "Une erreur s'est produite lors de la recherche : " + e.getMessage());
        }
    }

    /**
     * Modifie les informations d'un membre.
     * @param event L'événement déclenché par l'utilisateur.
     */
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

        if (!validateFields(nom, prenom, email, cin, adresse, numTel, role)) {
            return;
        }

        membreAModifier.setNom(nom);
        membreAModifier.setPrenom(prenom);
        membreAModifier.setEmail(email);
        membreAModifier.setCin(cin);
        membreAModifier.setAdresse(adresse);
        membreAModifier.setNumTel(numTel);
        membreAModifier.setRole(role);

        try {
            membresService.ModifierMem(membreAModifier);
            showSuccess("Succès", "Membre modifié avec succès.");
            switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
        } catch (Exception e) {
            showError("Erreur", "Une erreur s'est produite lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Annule la modification et retourne à la liste des utilisateurs.
     * @param event L'événement déclenché par l'utilisateur.
     */
    @FXML
    private void cancel(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    // Méthodes de navigation

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

    /**
     * Change la scène actuelle.
     * @param event L'événement déclenché par l'utilisateur.
     * @param fxmlPath Le chemin du fichier FXML.
     */
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

    private boolean validateFields(String nom, String prenom, String email, String cin, String adresse, String numTel, Role role) {
        StringBuilder errors = new StringBuilder();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() ||
                adresse.isEmpty() || numTel.isEmpty() || role == null) {
            errors.append("Tous les champs sont obligatoires.\n");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.append("Format d'email invalide.\n");
        }

        if (!numTel.matches("\\d+")) {
            errors.append("Le numéro de téléphone doit contenir uniquement des chiffres.\n");
        }

        if (!cin.matches("\\d{8}")) {
            errors.append("Le CIN doit contenir exactement 8 chiffres.\n");
        }

        if (!nom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+") || !prenom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+")) {
            errors.append("Le nom et le prénom doivent contenir uniquement des lettres, espaces ou tirets.\n");
        }

        if (errors.length() > 0) {
            showError("Erreur", errors.toString());
            return false;
        }

        return true;
    }

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
