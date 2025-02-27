package Gui.Service.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
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
            // Charger la nouvelle fenêtre (AjouterPartenaire.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/AjouterPartenaire.fxml"));
            AnchorPane addRecLayout = loader.load();
            Scene addRecScene = new Scene(addRecLayout);

            // Obtenir la fenêtre (Stage) actuelle
            Stage currentStage = (Stage) ajouterButton.getScene().getWindow();

            // Fermer la fenêtre actuelle (la fenêtre d'affichage des services)
            currentStage.close();

            // Ouvrir la nouvelle fenêtre
            Stage newStage = new Stage();
            newStage.setScene(addRecScene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/AfficherPartenaire.fxml"));
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
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de Reservation.fxml : " + e.getMessage());
        }
    }
    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToPartenaire(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();

        // Récupérer la fenêtre via l'élément déclencheur
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Scene scene = new Scene(partenaireLayout);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        Stage stage;

        // Vérifier si l'élément source est un MenuItem ou un Node
        if (event.getSource() instanceof MenuItem) {
            stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        } else {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        AnchorPane serviceLayout = loader.load();
        Scene scene = new Scene(serviceLayout);

        stage.setScene(scene);
        stage.show();
    }
}
