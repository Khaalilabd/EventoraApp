package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FXMain extends Application {
    @Override
    public void start(Stage stage)  {
<<<<<<< HEAD
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventoraAPP/Acceuil.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Utilisateur.fxml"));
>>>>>>> 9f5026f900f1fe374c19fd1dc7496dc819f174d4
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("EventoraAP");
            stage.setMaximized(true);
            stage.show();
        } catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
            r.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}