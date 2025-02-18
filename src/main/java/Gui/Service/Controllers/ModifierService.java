package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.Sponsors;
import Models.Service.TypePartenaire;
import Services.Service.Crud.ServiceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Models.Service.Service;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import for JavaFX


import java.io.IOException;

public class ModifierService {

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

    private Service serviceToEdit; // Ajout de cette variable pour contenir la réclamation à modifier
    private final ServiceService serviceService = new ServiceService();

    @FXML
    public void initialize() {
        locationfield.getItems().setAll(Location.values()); // Ajoute tous les types de réclamation à la liste du ComboBox
        sponsorfield.getItems().setAll(Sponsors.values()); // Ajoute tous les types de réclamation à la liste du ComboBox

        submitButton.setOnAction(this::modifierService);
        cancelButton.setOnAction(event -> annuler());
    }

    // Méthode pour pré-remplir les champs avec les données de la réclamation à modifier
    public void setServiceToEdit(Service service) {
        this.serviceToEdit = service;

        // Vérification : affichage des valeurs reçues dans la console pour debug
        System.out.println("Titre: " + service.getTitre());
        System.out.println("location: " + service.getLocation());
        System.out.println("Sponsor: " + service.getSponsors());
        System.out.println("Description: " + service.getDescription());
        System.out.println("Prix: " + service.getPrix());


        // Pré-remplir les champs avec les données de la réclamation existante
        titleField.setText(service.getTitre());
        locationfield.setValue(service.getLocation()); // Sélectionner le type de réclamation
        sponsorfield.setValue(service.getSponsors()); // Sélectionner le type de réclamation
        descriptionField.setText(service.getDescription());
        prixField.setText(service.getPrix()); // Sélectionner le type de réclamation
    }

    private void modifierService(ActionEvent event) {
        String titre = titleField.getText();
        Location location = locationfield.getValue();
        Sponsors sponsors = sponsorfield.getValue();

        String description = descriptionField.getText();
        String prix = prixField.getText();



        // Mise à jour de la réclamation avec les nouvelles valeurs
        serviceToEdit.setTitre(titre);
        serviceToEdit.setLocation(location);
        serviceToEdit.setSponsors(sponsors);

        serviceToEdit.setDescription(description);
        serviceToEdit.setPrix(prix);

        // Appel à la méthode ModifierRec du service
        serviceService.ModifierService(serviceToEdit);

        showAlert("Succès", "Réclamation modifiée avec succès !");
        clearFields();
        goToAfficherService(event);
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

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());

        alert.setTitle(title);
        alert.setHeaderText(null);  // On peut personnaliser ou laisser vide
        alert.setContentText(content);

        alert.showAndWait();
    }


    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void goToAfficherService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheService.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des services : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
