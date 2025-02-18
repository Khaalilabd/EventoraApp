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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRec.fxml"));
        try {
            Parent root = loader.load();


            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter");
            stage.show();}
        catch (RuntimeException | IOException r){
            System.out.println(r.getMessage());
        }
    }
}
