package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.Sponsors;
import Services.Service.Crud.ServiceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.event.ActionEvent;


public class AjouterService {

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<Location> locationfield;

    @FXML
    private ComboBox<Sponsors> sponsorfield;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField prixField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private ServiceService serviceService = new ServiceService();


    @FXML
    public void initialize() {
        locationfield.getItems().setAll(Location.values());
        sponsorfield.getItems().setAll(Sponsors.values());

        submitButton.setOnAction(this::ajouterService);
        cancelButton.setOnAction(event -> annuler());

    }

    private void ajouterService(ActionEvent event) {
        String titre = titleField.getText();
        Location location = locationfield.getValue();
        Sponsors sponsors = sponsorfield.getValue();
        String description = descriptionField.getText();
        String prix = prixField.getText();

        if (titre.isEmpty() || location == null || sponsors == null || description.isEmpty() || prix.isEmpty()) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        Service service = new Service(titre, location, sponsors, description, prix);
        serviceService.AjouterService(service);

        showAlert("Succès", "Service ajouté avec succès !");

        clearFields();

        gotoAfiicherService(event);
    }

    private void gotoAfiicherService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherService.fxml"));

            Parent root = loader.load(); // ✅ Correction : on charge bien le fichier FXML

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        locationfield.setValue(null);
        sponsorfield.setValue(null);
        descriptionField.clear();
        prixField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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


