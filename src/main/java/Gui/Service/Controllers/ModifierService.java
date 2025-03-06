package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.TypeService;
import Models.Service.Partenaire;
import Models.Service.Service;
import Services.Service.Crud.PartenaireService;
import Services.Service.Crud.ServiceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ModifierService {

    @FXML private TextField titleField;
    @FXML private ComboBox<Location> locationfield;
    @FXML private ComboBox<TypeService> type_servicefield;
    @FXML private ComboBox<Partenaire> idPartenaireField;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private Service serviceToEdit;
    private final ServiceService serviceService = new ServiceService();

    @FXML
    public void initialize() {
        locationfield.getItems().setAll(Location.values());
        type_servicefield.getItems().setAll(TypeService.values());
        loadPartenaires();
        submitButton.setOnAction(this::modifierService);
        cancelButton.setOnAction(this::annuler);
    }

    private void loadPartenaires() {
        PartenaireService partenaireService = new PartenaireService();
        List<Partenaire> partenaires = partenaireService.RechercherPartenaire();
        idPartenaireField.getItems().setAll(partenaires);
        idPartenaireField.setCellFactory(param -> new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom_partenaire());
            }
        });
        idPartenaireField.setButtonCell(new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom_partenaire());
            }
        });
    }

    public void setServiceToEdit(Service service) {
        this.serviceToEdit = service;
        Partenaire partenaire = findPartenaireById(service.getId_partenaire());
        idPartenaireField.setValue(partenaire);
        titleField.setText(service.getTitre());
        locationfield.setValue(service.getLocation());
        type_servicefield.setValue(service.getTypeService());
        descriptionField.setText(service.getDescription());
        prixField.setText(service.getPrix());
    }

    private Partenaire findPartenaireById(int id) {
        for (Partenaire partenaire : idPartenaireField.getItems()) {
            if (partenaire.getId_partenaire() == id) {
                return partenaire;
            }
        }
        return null;
    }

    private void modifierService(ActionEvent event) {
        String titre = titleField.getText();
        Location location = locationfield.getValue();
        TypeService typeService = type_servicefield.getValue();
        String description = descriptionField.getText();
        String prix = prixField.getText();
        Partenaire partenaire = idPartenaireField.getValue();
        int idPartenaire = partenaire != null ? partenaire.getId_partenaire() : -1;

        if (titre.isEmpty() || location == null || typeService == null || description.isEmpty() || prix.isEmpty() || idPartenaire == -1) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        if (!prix.matches("\\d+(dt|DT)$")) {
            showAlert("Erreur", "Le prix doit être un nombre et se terminer par 'dt' ou 'DT'.");
            return;
        }

        serviceToEdit.setId_partenaire(idPartenaire);
        serviceToEdit.setTitre(titre);
        serviceToEdit.setLocation(location);
        serviceToEdit.setTypeService(typeService);
        serviceToEdit.setDescription(description);
        serviceToEdit.setPrix(prix);

        serviceService.ModifierService(serviceToEdit);
        showAlert("Succès", "Le service a été modifié avec succès !");
        clearFields();
        switchScene(event, "/Service/AfficherService.fxml");
    }

    private void annuler(ActionEvent event) {
        clearFields();
        switchScene(event, "/Service/AfficherService.fxml");
    }

    private void clearFields() {
        titleField.clear();
        locationfield.setValue(null);
        type_servicefield.setValue(null);
        descriptionField.clear();
        prixField.clear();
        idPartenaireField.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") || title.equals("Warning") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    // Méthode switchScene suivant le style mémorisé
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}