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
import java.net.URL;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStyleClass().add("alert-style"); // Applique le style CSS
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez saisir votre nom d'utilisateur et votre mot de passe.");
            alert.showAndWait();
            return;
        }
        Utilisateurs utilisateur = membresService.rechercherMemParNom(username);
        if (utilisateur != null && verifierMotDePasse(password, utilisateur.getMotDePasse())) {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/AfficherUtilisateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getStyleClass().add("alert-style");
            alert.setTitle("Erreur");
            alert.setContentText("Nom d'utilisateur ou mot de passe incorrect.");
            alert.showAndWait();
        }
    }


    private boolean verifierMotDePasse(String motDePasseSaisi, String motDePasseStocke) throws SQLException {
        if (motDePasseStocke == null) {
            return false;
        }
        return motDePasseSaisi.equals(motDePasseStocke);
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToinscription(ActionEvent event) { // Méthode publique
        try {
            URL fxmlURL = getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml");

            if (fxmlURL == null) {
                System.err.println("Error: Could not find AjouterUtilisateur.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlURL); // Use FXMLLoader directly
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687); // Set your desired dimensions
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent event) { // Méthode publique
        try {
            URL fxmlURL = getClass().getResource("/Utilisateurs/Acceuil.fxml");

            if (fxmlURL == null) {
                System.err.println("Error: Could not find Accueil.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlURL); // Use FXMLLoader directly
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687); // Set your desired dimensions
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
