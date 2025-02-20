package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModifierUtilisateur {

    @FXML private TextField idField; // Champ pour l'ID du membre à modifier
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField cinField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelField;
    @FXML private ComboBox<Role> roleComboBox;
    @FXML private Button modifierButton;
    @FXML private Button annulerButton;

    private final MembresService membresService = new MembresService();
    private Utilisateurs membreAModifier; // Variable pour stocker le membre à modifier

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.ADMIN, Role.AGENT, Role.MEMBRE);
    }

    @FXML
    public void rechercherMembre() {
        try {
            int id = Integer.parseInt(idField.getText());
            membreAModifier = membresService.rechercherMem(id);

            if (membreAModifier != null) {
                // Remplir les champs avec les informations du membre
                nomField.setText(membreAModifier.getNom());
                prenomField.setText(membreAModifier.getPrenom());
                emailField.setText(membreAModifier.getEmail());
                cinField.setText(membreAModifier.getCin());
                adresseField.setText(membreAModifier.getAdresse());
                numTelField.setText(membreAModifier.getNumTel());
                roleComboBox.setValue(membreAModifier.getRole());
            } else {
                afficherAlerte("Erreur", "Aucun membre trouvé avec cet ID.");
                viderChamps();
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "ID invalide. Veuillez entrer un nombre entier.");
        }
    }


    @FXML
    public void modifierMembre() {
        if (membreAModifier == null) {
            afficherAlerte("Erreur", "Veuillez d'abord rechercher un membre.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String adresse = adresseField.getText();
        String numTel = numTelField.getText();
        Role role = roleComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() || adresse.isEmpty() || numTel.isEmpty() || role == null) {
            afficherAlerte("Erreur", "Tous les champs sont obligatoires.");
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
        afficherAlerte("Succès", "Membre modifié avec succès.");
        viderChamps();
        membreAModifier = null; // Réinitialiser le membre à modifier
    }

    @FXML
    public void cancel() {
        viderChamps();
        membreAModifier = null; // Réinitialiser le membre à modifier
        System.out.println("Opération annulée.");
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        idField.clear(); // Effacer aussi le champ ID
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        cinField.clear();
        adresseField.clear();
        numTelField.clear();
        roleComboBox.setValue(null);
    }
}