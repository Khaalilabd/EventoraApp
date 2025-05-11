package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import Utils.SessionManager; // Importer SessionManager
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

public class Authentification {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final MembresService membresService = new MembresService();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    private void seConnecter(ActionEvent event) throws IOException, SQLException {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Erreur", "Veuillez saisir votre email et votre mot de passe.");
            return;
        }

        // Utiliser rechercherMemParEmail au lieu de rechercherMemParNom
        Utilisateurs utilisateur = membresService.rechercherMemParEmail(email);
        if (utilisateur != null && verifierMotDePasse(password, utilisateur.getMotDePasse())) {
            // Stocker l'utilisateur connecté dans SessionManager
            SessionManager.getInstance().setUtilisateurConnecte(utilisateur);
            switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
        } else {
            showError("Erreur", "Email ou mot de passe incorrect.");
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showError("Erreur", "Veuillez entrer un email valide.");
            return;
        }

        String normalizedEmail = email.toLowerCase();
        Utilisateurs utilisateur = membresService.rechercherMemParEmail(normalizedEmail); // Utiliser rechercherMemParEmail
        if (utilisateur == null) {
            showError("Erreur", "Email non trouvé.");
            return;
        }

        String token = membresService.generatePasswordResetToken(normalizedEmail);
        if (token == null) {
            showError("Erreur", "Échec de la génération du token. Veuillez réessayer.");
            return;
        }

        String apiToken = System.getenv("POSTMARK_API_TOKEN");
        if (apiToken == null || apiToken.trim().isEmpty()) {
            showError("Erreur", "POSTMARK_API_TOKEN environment variable is not set or empty.");
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
                showSuccess("Succès", "Un email avec un token de réinitialisation a été envoyé. Vérifiez votre boîte de réception.");
                redirectToResetPassword(event, normalizedEmail);
            } else {
                showError("Erreur", "Échec de l'envoi de l'email : " + response.body());
            }
        } catch (Exception e) {
            showError("Erreur", "Échec de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void redirectToResetPassword(ActionEvent event, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/ResetPassword.fxml"));
            AnchorPane layout = loader.load();
            ResetPassword controller = loader.getController();
            controller.setEmail(email);
            switchScene(event, layout);
        } catch (IOException e) {
            showError("Erreur", "Échec du chargement de la page de réinitialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean verifierMotDePasse(String motDePasseSaisi, String motDePasseStocke) throws SQLException {
        return motDePasseStocke != null && motDePasseSaisi.equals(motDePasseStocke);
    }

    @FXML
    public void goToinscription(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AjouterUtilisateur.fxml");
    }

    @FXML
    public void goBack(ActionEvent event) {
        switchScene(event, "/EventoraAPP/Acceuil.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            scene.getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void switchScene(ActionEvent event, AnchorPane layout) {
        try {
            Scene scene = new Scene(layout);
            scene.getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible d'afficher la page.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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