package Gui.Utilisateurs.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Acceuil {

    @FXML
    private Button experienceButton;

    @FXML
    private void goToAccueil(ActionEvent event) {
        try {
            URL fxmlURL = getClass().getResource("/Utilisateurs/Identification.fxml");

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
    @FXML
    private void goToAjouterUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
}