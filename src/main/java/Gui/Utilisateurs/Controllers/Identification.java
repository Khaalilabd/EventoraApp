package Gui.Utilisateurs.Controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;  // Assurez-vous d'importer ImageView
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Identification {


    @FXML
    private FadeTransition fadeTransition;
    @FXML
    private ImageView backgroundImage;



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

    public void goToAcceuil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Utilisateurs/Acceuil.fxml"));
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
    private void onFadeComplete() {
        // Cette méthode sera appelée après la fin de la transition de fondu
        System.out.println("Transition de fondu terminée");
        // Par exemple, on pourrait passer à une autre scène ici
    }



    public void initialize() {
        // Crée l'animation de fondu
        fadeTransition = new FadeTransition(Duration.seconds(2), backgroundImage);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        fadeTransition.setOnFinished(event -> onFadeComplete());
        // Démarre l'animation
        fadeTransition.play();
    }
}
