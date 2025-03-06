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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
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
    @FXML private TextField textFieldPassword; // Champ pour afficher le mot de passe en clair
    @FXML private TextField imageField;
    @FXML private Button chooseImageButton;
    @FXML private Button generatePasswordButton; // Bouton pour générer un mot de passe
    @FXML private Button togglePasswordButton; // Bouton pour révéler temporairement le mot de passe
    @FXML private StackPane passwordContainer; // Conteneur du champ de mot de passe

    private final MembresService membresService = new MembresService();
    private static final String IMAGE_DIR = System.getProperty("user.image.dir", "src/main/resources/Images/users/");
    private static final String API_KEY = "jdhXyNvADDDqUg9coiHcfQ==DyCXNx8yocrDHQSA"; // Clé API pour générer le mot de passe

    @FXML
    public void initialize() {
        addInputValidation();
        // Synchroniser les deux champs de mot de passe
        textFieldPassword.textProperty().bindBidirectional(motDePasseField.textProperty());
        // Par défaut, masquer le champ texte et afficher le champ mot de passe
        textFieldPassword.setVisible(false);
        motDePasseField.setVisible(true);

        // Gestion de la révélation temporaire du mot de passe
        togglePasswordButton.setOnMousePressed(this::showPassword);
        togglePasswordButton.setOnMouseReleased(this::hidePassword);

        // Débogage des événements sur le conteneur
        passwordContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> System.out.println("Mouse pressed on password container"));
        passwordContainer.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> System.out.println("Mouse released on password container"));
    }

    // Révéler le mot de passe lorsqu'on appuie sur l'icône œil
    @FXML
    public void showPassword(MouseEvent event) {
        System.out.println("showPassword called");
        motDePasseField.setVisible(false);
        textFieldPassword.setVisible(true);
    }

    // Masquer le mot de passe lorsqu'on relâche l'icône œil
    @FXML
    public void hidePassword(MouseEvent event) {
        System.out.println("hidePassword called");
        motDePasseField.setVisible(true);
        textFieldPassword.setVisible(false);
    }

    @FXML
    public void generatePassword() {
        try {
            String generatedPassword = fetchPasswordFromApi();
            motDePasseField.setText(generatedPassword);
            System.out.println("Generated password: " + generatedPassword);
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de générer un mot de passe via l'API : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String fetchPasswordFromApi() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.api-ninjas.com/v1/passwordgenerator?length=12"))
                .header("X-Api-Key", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            return jsonResponse.split("\"random_password\": \"")[1].split("\"")[0];
        } else {
            throw new IOException("Erreur API : " + response.statusCode());
        }
    }

    private void addInputValidation() {
        cinField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 8) {
                cinField.setText(oldValue);
                afficherAlerte(Alert.AlertType.WARNING, "Attention", "Le CIN doit contenir 8 chiffres maximum.");
            }
        });

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
                Path destinationDir = Paths.get(IMAGE_DIR);
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                }
                String fileName = selectedFile.getName();
                Path destinationPath = destinationDir.resolve(fileName);
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                imageField.setText("/Images/users/" + fileName);
            } catch (IOException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Échec de la copie de l'image : " + e.getMessage());
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
        String motDePasse = motDePasseField.getText().trim(); // Utiliser directement le champ mot de passe
        String image = imageField.getText().trim();

        if (!validateInputs(nom, prenom, email, cin, numTel, adresse, motDePasse, image)) {
            return;
        }

        Utilisateurs nouveauMembre = new Utilisateurs(nom, prenom, cin, email, adresse, numTel, motDePasse, image);

        toggleButtons(true);
        try {
            System.out.println("Ajout d'un nouveau membre :");
            System.out.println("Nom: " + nom);
            System.out.println("Prénom: " + prenom);
            System.out.println("Email: " + email);
            System.out.println("CIN: " + cin);
            System.out.println("NumTel: " + numTel);
            System.out.println("Adresse: " + adresse);
            System.out.println("MotDePasse: " + motDePasse);
            System.out.println("Image: " + image);
            System.out.println("Token: " + nouveauMembre.getToken());

            membresService.AjouterMem(nouveauMembre);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Membre ajouté avec succès. Un email d'activation a été envoyé à " + email + ".");
            viderChamps();
            goToAuth(event);
        } catch (RuntimeException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du membre : " + e.getMessage());
        } finally {
            toggleButtons(false);
        }
    }

    private boolean validateInputs(String nom, String prenom, String email, String cin, String numTel, String adresse, String motDePasse, String image) {
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || cin.isEmpty() || numTel.isEmpty() || adresse.isEmpty() || motDePasse.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs sont obligatoires (sauf l'image).");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide.");
            return false;
        }

        if (!cin.matches("\\d{8}")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le CIN doit contenir exactement 8 chiffres.");
            return false;
        }

        if (!numTel.matches("[259]\\d{7}")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit commencer par 2, 5 ou 9 et contenir 8 chiffres.");
            return false;
        }

        if (motDePasse.length() < 6) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
            return false;
        }

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
    public void goToAcceuil(ActionEvent event) {
        navigateTo("/Utilisateurs/Identification.fxml", "Accueil", event);
    }

    @FXML
    public void goToAuth(ActionEvent event) {
        navigateTo("/Utilisateurs/Authentification.fxml", "Authentification", event);
    }

    @FXML
    public void goToUSer(ActionEvent event) {
        navigateTo("/Utilisateurs/Utilisateur.fxml", "Utilisateur", event);
    }

    private void navigateTo(String fxmlPath, String pageName, ActionEvent event) {
        try {
            java.net.URL fileUrl = getClass().getResource(fxmlPath);
            if (fileUrl == null) {
                System.err.println("Fichier FXML introuvable : " + fxmlPath);
                throw new IOException("Fichier FXML introuvable : " + fxmlPath);
            }
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de " + pageName + " : " + e.getMessage());
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(titre);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void viderChamps() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        cinField.clear();
        adresseField.clear();
        numTelField.clear();
        motDePasseField.clear();
        textFieldPassword.clear();
        imageField.clear();
    }

    // Classe interne pour générer un mot de passe localement (option alternative)
    public static class PasswordUtils {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        private static final int PASSWORD_LENGTH = 12;

        public static String generatePassword() {
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                password.append(CHARACTERS.charAt(randomIndex));
            }
            return password.toString();
        }
    }
}