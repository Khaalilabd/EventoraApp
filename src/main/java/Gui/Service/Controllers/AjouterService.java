package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.TypeService;
import Models.Service.Partenaire;
import Services.Service.Crud.ServiceService;
import Services.Service.Crud.PartenaireService;
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

public class AjouterService {

    @FXML private TextField titleField;
    @FXML private ComboBox<Location> locationField;
    @FXML private ComboBox<TypeService> type_serviceField;
    @FXML private ComboBox<Partenaire> idPartenaireField;
    @FXML private TextArea descriptionField;
    @FXML private TextField prixField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private final ServiceService serviceService = new ServiceService();
    private final PartenaireService partenaireService = new PartenaireService();

    @FXML
    public void initialize() {
        locationField.getItems().setAll(Location.values());
        type_serviceField.getItems().setAll(TypeService.values());
        loadPartenaires();
        submitButton.setOnAction(this::ajouterService);
        cancelButton.setOnAction(event -> annuler());
    }

    private void loadPartenaires() {
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

    private void ajouterService(ActionEvent event) {
        Partenaire partenaire = idPartenaireField.getValue();
        Integer idPartenaire = partenaire != null ? partenaire.getId_partenaire() : null;
        String titre = titleField.getText();
        Location location = locationField.getValue();
        TypeService typeService = type_serviceField.getValue();
        String description = descriptionField.getText();
        String prix = prixField.getText();

        if (idPartenaire == null || titre.isEmpty() || location == null || typeService == null || description.isEmpty() || prix.isEmpty()) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        if (!titre.matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le titre doit contenir uniquement des caractères alphabétiques.");
            return;
        }

        if (!description.matches("[a-zA-Z ,.\\-']+")) {
            showAlert("Erreur", "La description doit contenir uniquement des caractères alphabétiques et certains caractères spéciaux (,. - ').");
            return;
        }

        if (!prix.matches("\\d+(dt|DT)$")) {
            showAlert("Erreur", "Le prix doit être un nombre et se terminer par 'dt' ou 'DT'.");
            return;
        }

        Service service = new Service(idPartenaire, titre, location, typeService, description, prix);
        serviceService.AjouterService(service);
        showAlert("Succès", "Service ajouté avec succès !");
        clearFields();
        switchScene(event, "/Service/AfficherService.fxml");
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        locationField.setValue(null);
        type_serviceField.setValue(null);
        idPartenaireField.setValue(null);
        descriptionField.clear();
        prixField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") || title.equals("Warning") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
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