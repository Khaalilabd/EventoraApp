package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMain extends Application {
    @Override
<<<<<<< HEAD
    public void start(Stage stage)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
=======
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));

>>>>>>> 045bb7bc8abc11f947fe3eaa6f96081c22d0f87e
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Eventora - Accueil");
            stage.setMaximized(true);
            stage.show();
        } catch (RuntimeException | IOException r) {
            System.out.println(r.getMessage());
            r.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}