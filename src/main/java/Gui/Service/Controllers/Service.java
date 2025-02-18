package Gui.Service.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
public class Service {
    @FXML
    private Button ajouterButton;

    @FXML
    private Button viewButton;

    // Action pour ajouter une réclamation
    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();
            currentStage.setScene(addRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action pour afficher les réclamations
    @FXML
    private void handleViewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheService.fxml"));
            AnchorPane viewRecLayout = loader.load();
            Scene viewRecScene = new Scene(viewRecLayout);
            Stage currentStage = (Stage) viewButton.getScene().getWindow();
            currentStage.setScene(viewRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
