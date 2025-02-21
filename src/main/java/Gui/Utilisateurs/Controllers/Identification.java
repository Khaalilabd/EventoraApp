package Gui.Utilisateurs.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Identification {

    public void goToAuthentification(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToInscription(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAcceuil(ActionEvent event) { // <-- Correction ici
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/Acceuil.fxml")); // Charge Acceuil.fxml
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToIdentification(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateur/Identification.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAccueil(ActionEvent event) {
        try {
            // Get the FXML file URL.  More robust way to handle paths.
            URL fxmlURL = getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml");

            if (fxmlURL == null) {
                System.err.println("Error: Could not find AjouterUtilisateur.fxml");
                return; // Important: Exit the method if the file isn't found.
            }

            FXMLLoader loader = new FXMLLoader(fxmlURL); // Use FXMLLoader directly
            Parent root = loader.load();

            // Get the Stage from the button's scene.  More reliable.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 1022, 687); // Set your desired dimensions
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage()); // More informative error message
            e.printStackTrace(); // Keep this for debugging
        }
    }
}

