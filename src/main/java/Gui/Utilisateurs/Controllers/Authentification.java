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
            afficherAlerte("Erreur", "Veuillez saisir votre nom d'utilisateur et votre mot de passe.");
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
            afficherAlerte("Erreur", "Nom d'utilisateur ou mot de passe incorrect.");
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
}
