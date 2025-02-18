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

public class Partenaire {

    @FXML
    private Button ajouterButton;

    @FXML
    private Button viewButton;

    @FXML
    private void handleAddAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPartenaire.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();
            currentStage.setScene(addRecScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleViewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPartenaire.fxml"));
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
    private void goToPartenaire(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();
        Scene scene = new Scene(partenaireLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
