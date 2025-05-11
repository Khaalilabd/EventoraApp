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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Acceuil {

    @FXML
    private Button experienceButton;

    @FXML
    private ImageView logo;

    @FXML
    public void initialize() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), logo);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    @FXML
    private void goToAuth(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Authentification.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Eventora - Authentification");
            stage.setMaximized(true);
            stage.show();
        } catch (RuntimeException | IOException r) {
            System.out.println(r.getMessage());
            r.printStackTrace();
        }
    }
}