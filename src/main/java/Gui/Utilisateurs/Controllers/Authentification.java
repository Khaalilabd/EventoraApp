package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
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
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

public class Authentification {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final MembresService membresService = new MembresService();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private void seConnecter(ActionEvent event) throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir votre nom d'utilisateur et votre mot de passe.");
            return;
        }

        Utilisateurs utilisateur = membresService.rechercherMemParNom(username);
        if (utilisateur != null && verifierMotDePasse(password, utilisateur.getMotDePasse())) {
            Parent root = FXMLLoader.load(getClass().getResource("/EventoraAPP/EventoraAPP.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } else {
            afficherAlerte("Erreur", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        String email = usernameField.getText().trim(); // Use usernameField for email
        if (email.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer un email valide.");
            return;
        }

        String normalizedEmail = email.toLowerCase();
        Utilisateurs utilisateur = membresService.rechercherMemParEmail(normalizedEmail);
        if (utilisateur == null) {
            afficherAlerte("Erreur", "Email non trouvé.");
            return;
        }

        String token = membresService.generatePasswordResetToken(normalizedEmail);
        if (token == null) {
            afficherAlerte("Erreur", "Échec de la génération du token. Veuillez réessayer.");
            return;
        }

        String apiToken = System.getenv("POSTMARK_API_TOKEN");
        if (apiToken == null || apiToken.trim().isEmpty()) {
            afficherAlerte("Erreur", "POSTMARK_API_TOKEN environment variable is not set or empty.");
            return;
        }

        String fromEmail = "khalil.abdelmoumen@esprit.tn";
        String subject = "Password Reset for Eventora";
        String htmlContent = "<h1>Password Reset for Eventora</h1>" +
                "<p>Hello,</p>" +
                "<p>We received a request to reset your password for Eventora. Copy this token and use it to reset your password:</p>" +
                "<p><strong>Token:</strong> " + token + "</p>" +
                "<p>This token expires in 1 hour. If you did not request a password reset, please ignore this email.</p>" +
                "<p>Best regards,<br>The Eventora Team</p>";
        String textContent = "Hello,\n\n" +
                "We received a request to reset your password for Eventora. Copy this token to reset your password:\n" +
                token + "\n\n" +
                "This token expires in 1 hour. If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\nThe Eventora Team";

        String json = String.format(
                "{\"From\":\"%s\",\"To\":\"%s\",\"Subject\":\"%s\",\"TextBody\":\"%s\",\"HtmlBody\":\"%s\"}",
                escapeJsonString(fromEmail), escapeJsonString(normalizedEmail), escapeJsonString(subject),
                escapeJsonString(textContent), escapeJsonString(htmlContent)
        );

        System.out.println("Sending email to: " + normalizedEmail);
        System.out.println("JSON Payload: " + json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.postmarkapp.com/email"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("X-Postmark-Server-Token", apiToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Postmark Response Status: " + response.statusCode());
            System.out.println("Postmark Response Body: " + response.body());
            if (response.statusCode() == 200) {
                afficherAlerte("Succès", "Un email avec un token de réinitialisation a été envoyé. Vérifiez votre boîte de réception.");
                redirectToResetPassword(event, normalizedEmail); // Redirect with email
            } else {
                afficherAlerte("Erreur", "Échec de l'envoi de l'email : " + response.body());
            }
        } catch (Exception e) {
            afficherAlerte("Erreur", "Échec de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // New method to redirect to ResetPassword with pre-filled email
    private void redirectToResetPassword(ActionEvent event, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/ResetPassword.fxml"));
            Parent root = loader.load();
            ResetPassword controller = loader.getController();
            controller.setEmail(email); // Pre-fill the email
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            afficherAlerte("Erreur", "Échec du chargement de la page de réinitialisation : " + e.getMessage());
        }
    }

    private boolean verifierMotDePasse(String motDePasseSaisi, String motDePasseStocke) throws SQLException {
        return motDePasseStocke != null && motDePasseSaisi.equals(motDePasseStocke);
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToinscription(ActionEvent event) {
        changerScene(event, "/Utilisateurs/AjouterUtilisateur.fxml");
    }

    @FXML
    public void goBack(ActionEvent event) {
        changerScene(event, "/EventoraAPP/Acceuil.fxml");
    }

    private void changerScene(ActionEvent event, String cheminFXML) {
        try {
            URL fxmlURL = getClass().getResource(cheminFXML);
            if (fxmlURL == null) {
                System.err.println("Error: Could not find " + cheminFXML);
                return;
            }
            Parent root = FXMLLoader.load(fxmlURL);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String escapeJsonString(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}