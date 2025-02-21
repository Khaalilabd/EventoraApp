package Gui.Utilisateurs.Controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class Acceuil {

    @FXML
    private Button experienceButton;

    @FXML
    private ImageView logo; // Ajout de l'ImageView pour l'animation

    @FXML
    public void initialize() {
        // Ajout de l'animation de fondu pour l'ImageView
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), logo);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    @FXML
    private void goToAuth(ActionEvent event) {
        try {
            URL fxmlURL = getClass().getResource("/Utilisateurs/Authentification.fxml");

            if (fxmlURL == null) {
                System.err.println("Error: Could not find AjouterUtilisateur.fxml");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlURL);
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
