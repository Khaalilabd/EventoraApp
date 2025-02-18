package Gui.Reclamation.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;  // Import AnchorPane au lieu de VBox
import javafx.stage.Stage;

public class Reclamation {

    @FXML
    private Button ajouterButton;


    @FXML
    private void handleAddAction(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRec.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();
            currentStage.setScene(addRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
