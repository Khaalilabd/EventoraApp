package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Models.Utilisateur.Role;
import Services.Utilisateur.MembresService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterUtilisateur {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField cinField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button ajouterButton;
    @FXML private Button annulerButton;

    private final MembresService membresService = new MembresService();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.values()); // Initialisation avec toutes les valeurs de l'enum Role
    }

    @FXML
    public void ajouterMembre(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String adresse = adresseField.getText();
        String numTel = numTelField.getText();
        Role role = roleComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() || adresse.isEmpty() || numTel.isEmpty() || role == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        Utilisateurs nouveauMembre = new Utilisateurs(nom, prenom, email, cin, adresse, numTel, role);

        try {
            membresService.AjouterMem(nouveauMembre);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Membre ajouté avec succès.");
            viderChamps();

            // Navigation vers AfficherUtilisateur
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherUtilisateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace(); // Important pour le débogage
        }
    }


    @FXML
    public void cancel(ActionEvent event) {
        viderChamps();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        cinField.clear();
        adresseField.clear();
        numTelField.clear();
        roleComboBox.setValue(null);
    }
}