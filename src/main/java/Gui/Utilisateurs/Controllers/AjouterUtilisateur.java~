package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Models.Utilisateur.Role;
import Services.Utilisateur.Crud.MembresService;
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
    @FXML private Button retourButton;
    @FXML private PasswordField motDePasseField;

    private final MembresService membresService = new MembresService();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.values());
    }

    @FXML
    public void ajouterMembre(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String numTel = numTelField.getText();
        String adresse = adresseField.getText();
        String motDePasse = motDePasseField.getText();
        Role role = roleComboBox.getValue();

        if (cin.length() > 8) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le CIN ne peut pas dépasser 8 caractères.");
            return;
        }
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() || numTel.isEmpty() || adresse.isEmpty() || motDePasse.isEmpty() || role == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        Utilisateurs nouveauMembre = new Utilisateurs(nom, prenom, email, cin, numTel, adresse, role , motDePasse);

        try {
            System.out.println("Nom: " + nom);
            System.out.println("Prénom: " + prenom);
            System.out.println("Email: " + email);
            System.out.println("CIN: " + cin);
            System.out.println("NumTel: " + numTel);
            System.out.println("Adresse: " + adresse);
            System.out.println("MotDePasse: " + motDePasse);
            System.out.println("Role: " + role);

            membresService.AjouterMem(nouveauMembre);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Membre ajouté avec succès.");
            viderChamps();
            goToAuth(event);
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void cancel(ActionEvent event) {
        viderChamps();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void goToAcceuil() {
        try {
            java.net.URL fileUrl = getClass().getResource("/Utilisateurs/Identification.fxml");
            if (fileUrl == null) {
                System.err.println("Erreur : Impossible de trouver Acceuil.fxml dans /");
                return;
            }
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToUSer(ActionEvent event) {
        try {
            java.net.URL fileUrl = getClass().getResource("/Utilisateurs/Utilisateur.fxml");
            if (fileUrl == null) {
                System.err.println("Erreur : Impossible de trouver Utilisateur.fxml dans /");
                return;
            }
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        motDePasseField.clear();
    }
    public void goToAuth(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
