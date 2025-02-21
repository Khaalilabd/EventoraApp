package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    // Nouvelle méthode pour définir l'utilisateur à modifier depuis AfficherUtilisateur
    public void setUtilisateur(Utilisateurs utilisateur) {
        this.membreAModifier = utilisateur;
        if (membreAModifier != null) {
            remplirChamps();
        }
    }

    @FXML
    public void rechercherMembre() {
        try {
            int id = Integer.parseInt(idField.getText());
            membreAModifier = membresService.rechercherMem(id);

            if (membreAModifier != null) {
                remplirChamps();
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
            afficherAlerte("Erreur", "Veuillez d'abord sélectionner ou rechercher un membre.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String adresse = adresseField.getText();
        String numTel = numTelField.getText();
        Role role = roleComboBox.getValue();

        // Vérification que tous les champs sont remplis
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() ||
                adresse.isEmpty() || numTel.isEmpty() || role == null) {
            afficherAlerte("Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        // Contrainte : Vérifier le format de l'email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            afficherAlerte("Erreur", "Format d'email invalide.");
            return;
        }

        // Contrainte : Le numéro de téléphone doit contenir uniquement des chiffres
        if (!numTel.matches("\\d+")) {
            afficherAlerte("Erreur", "Le numéro de téléphone doit contenir uniquement des chiffres.");
            return;
        }

        // Contrainte : Le CIN doit contenir exactement 8 chiffres
        if (!cin.matches("\\d{8}")) {
            afficherAlerte("Erreur", "Le CIN doit contenir exactement 8 chiffres.");
            return;
        }

        // Contrainte optionnelle : Le nom et le prénom ne doivent contenir que des lettres, espaces ou tirets
        if (!nom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+") || !prenom.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\s-]+")) {
            afficherAlerte("Erreur", "Le nom et le prénom doivent contenir uniquement des lettres, espaces ou tirets.");
            return;
        }

        // Mise à jour de l'objet membre avec les nouvelles valeurs
        membreAModifier.setNom(nom);
        membreAModifier.setPrenom(prenom);
        membreAModifier.setEmail(email);
        membreAModifier.setCin(cin);
        membreAModifier.setAdresse(adresse);
        membreAModifier.setNumTel(numTel);
        membreAModifier.setRole(role);

        membresService.ModifierMem(membreAModifier);
        afficherAlerte("Succès", "Membre modifié avec succès.");
        fermerFenetre(); // Fermer la fenêtre après modification réussie
    }

    @FXML
    public void cancel() {
        fermerFenetre(); // Fermer la fenêtre au lieu de juste vider les champs
    }

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

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void fermerFenetre() {
        Stage stage = (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
}
