package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

public class AjouterUtilisateur {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField cinField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelField;
    @FXML private Button ajouterButton;
    @FXML private Button annulerButton;
    @FXML private Button retourButton;
    @FXML private PasswordField motDePasseField;
    @FXML private TextField imageField; // Nouveau champ
    @FXML private Button chooseImageButton; // Nouveau bouton

    private final MembresService membresService = new MembresService();

    @FXML
    public void initialize() {
        // Add input validation listeners for real-time feedback
        addInputValidation();
    }

    private void addInputValidation() {
        // CIN should not exceed 8 characters
        cinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 8) {
                cinField.setText(oldValue);
                afficherAlerte(Alert.AlertType.WARNING, "Attention", "Le CIN ne peut pas dépasser 8 caractères.");
            }
        });

        // Phone number should be numeric and reasonable length (e.g., 8-15 digits)
        numTelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 15) {
                numTelField.setText(oldValue);
                afficherAlerte(Alert.AlertType.WARNING, "Attention", "Le numéro de téléphone doit contenir uniquement des chiffres (max 15).");
            }
        });
    }

    @FXML
    public void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Définir le dossier de destination
                Path destinationDir = Paths.get("src/main/resources/Images/users/");
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                }

                // Copier l'image dans le dossier
                String fileName = selectedFile.getName();
                Path destinationPath = destinationDir.resolve(fileName);
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le champ imageField avec le chemin relatif
                imageField.setText("/Images/users/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'upload de l'image : " + e.getMessage());
            }
        }
    }

    @FXML
    public void ajouterMembre(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String cin = cinField.getText().trim();
        String numTel = numTelField.getText().trim();
        String adresse = adresseField.getText().trim();
        String motDePasse = motDePasseField.getText();
        String image = imageField.getText().trim();

        // Enhanced validation
        if (!validateInputs(nom, prenom, email, cin, numTel, adresse, motDePasse, image)) {
            return;
        }

        Utilisateurs nouveauMembre = new Utilisateurs(nom, prenom, cin, email, adresse, numTel, motDePasse, image);

        // Disable buttons during processing to prevent multiple submissions
        toggleButtons(true);

        try {
            // Log the data for debugging
            System.out.println("Ajout d'un nouveau membre :");
            System.out.println("Nom: " + nom);
            System.out.println("Prénom: " + prenom);
            System.out.println("Email: " + email);
            System.out.println("CIN: " + cin);
            System.out.println("NumTel: " + numTel);
            System.out.println("Adresse: " + adresse);
            System.out.println("MotDePasse: " + motDePasse);
            System.out.println("Image: " + image);

            // Add the member
            membresService.AjouterMem(nouveauMembre);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Membre ajouté avec succès.");

            // Clear fields and navigate to authentication
            viderChamps();
            goToAuth(event);
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace();
        } finally {
            toggleButtons(false);
        }
    }

    private boolean validateInputs(String nom, String prenom, String email, String cin, String numTel, String adresse, String motDePasse, String image) {
        // Check for empty fields
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() || numTel.isEmpty() || adresse.isEmpty() || motDePasse.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires (sauf l'image).");
            return false;
        }

        // Email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide.");
            return false;
        }

        // Phone number minimum length
        if (numTel.length() < 8) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit contenir au moins 8 chiffres.");
            return false;
        }

        // Password minimum length
        if (motDePasse.length() < 6) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
            return false;
        }

        // Image validation (optional field, but if provided, check format)
        if (!image.isEmpty() && !image.matches(".*\\.(png|jpg|jpeg)$")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'image doit être au format PNG, JPG ou JPEG.");
            return false;
        }

        return true;
    }

    private void toggleButtons(boolean disable) {
        Platform.runLater(() -> {
            ajouterButton.setDisable(disable);
            annulerButton.setDisable(disable);
            retourButton.setDisable(disable);
        });
    }

    @FXML
    public void goToAcceuil() {
        navigateTo("/Utilisateurs/Identification.fxml", "Accueil");
    }

    @FXML
    public void goToAuth(ActionEvent event) {
        navigateTo("/Utilisateurs/Authentification.fxml", "Authentification");
    }

    @FXML
    public void goToUSer(ActionEvent event) {
        navigateTo("/Utilisateurs/Utilisateur.fxml", "Utilisateur");
    }

    private void navigateTo(String fxmlPath, String pageName) {
        try {
            java.net.URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                System.err.println("Erreur : Impossible de trouver " + pageName + ".fxml dans " + fxmlPath);
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page " + pageName + ".");
                return;
            }
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true); // Rend la fenêtre maximisée
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page " + pageName + " : " + e.getMessage());
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
        motDePasseField.clear();
        imageField.clear();
    }
}
