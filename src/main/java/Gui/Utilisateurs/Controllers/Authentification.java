package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Authentification {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final MembresService membresService = new MembresService();

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
            // Authentification réussie, redirection vers la page principale
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/Accueil.fxml")); // Chemin vers la page d'accueil
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            afficherAlerte("Erreur", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }


    private boolean verifierMotDePasse(String motDePasseSaisi, String motDePasseStocke) throws SQLException {
        // Implémentation de la logique de vérification du mot de passe (comparaison sécurisée)
        // Utiliser une bibliothèque de hachage de mot de passe (BCrypt, Argon2, etc.) pour comparer les mots de passe hachés

        if (motDePasseStocke == null) {
            return false; // Gérer le cas où le mot de passe stocké est null (utilisateur non trouvé ou pas de mot de passe défini)
        }

        return motDePasseSaisi.equals(motDePasseStocke); // Exemple : comparaison directe (à améliorer avec du hachage)
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}