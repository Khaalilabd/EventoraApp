package Gui.Utilisateurs.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class Acceuil {

    @FXML
    private Button experienceButton;

    @FXML
    private void goToAjouterUtilisateur() {
        try {
            // Vérifier si le fichier est trouvé
            java.net.URL fileUrl = getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml");
            if (fileUrl == null) {
                System.err.println("Erreur : Impossible de trouver AjouterUtilisateur.fxml dans /Gui/Utilisateurs/");
                return;
            }

            // Charger le fichier FXML
            Parent root = FXMLLoader.load(fileUrl);
            Stage stage = (Stage) experienceButton.getScene().getWindow();
            Scene scene = new Scene(root, 1022, 687);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}