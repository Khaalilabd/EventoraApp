package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.TypeService;
import Models.Service.Partenaire; // Si vous avez une classe Partenaire pour l'ID partenaire
import Services.Service.Crud.PartenaireService;
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
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class ModifierService {
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<Location> locationfield;
    @FXML
    private ComboBox<TypeService> type_servicefield;
    @FXML
    private ComboBox<Partenaire> idPartenaireField; // ComboBox pour les partenaires
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField prixField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    private Service serviceToEdit;
    private final ServiceService serviceService = new ServiceService();
    @FXML
    public void initialize() {
        locationfield.getItems().setAll(Location.values()); // Charger les valeurs pour le Location
        type_servicefield.getItems().setAll(TypeService.values()); // Charger les valeurs pour le TypeService
        loadPartenaires();
        submitButton.setOnAction(this::modifierService);
        cancelButton.setOnAction(event -> annuler(event));
    }
    private void loadPartenaires() {
        PartenaireService partenaireService = new PartenaireService();
        List<Partenaire> partenaires = partenaireService.RechercherPartenaire();
        idPartenaireField.getItems().setAll(partenaires);
        idPartenaireField.setCellFactory(param -> new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom_partenaire());
                }
            }
        });
        idPartenaireField.setButtonCell(new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom_partenaire());  // Afficher le nom_partenaire
                }
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

        // Validation : Vérification que tous les champs sont remplis
        if (titre.isEmpty() || location == null || typeService == null || description.isEmpty() || prix.isEmpty() || idPartenaire == -1) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation : Le prix doit être un nombre et se terminer par 'dt' ou 'DT'
        if (!prix.matches("\\d+(dt|DT)$")) {
            showAlert("Erreur", "Le prix doit être un nombre et se terminer par 'dt' ou 'DT'.");
            return;
        }

        // Mise à jour des données du service
        serviceToEdit.setId_partenaire(idPartenaire);
        serviceToEdit.setTitre(titre);
        serviceToEdit.setLocation(location);
        serviceToEdit.setTypeService(typeService);
        serviceToEdit.setDescription(description);
        serviceToEdit.setPrix(prix);

        // Appel au service pour effectuer la modification
        serviceService.ModifierService(serviceToEdit);

        // Message de confirmation
        showAlert("Succès", "Le service a été modifié avec succès !");
        clearFields();
        goToAfficherService(event);
    }

    private void annuler(ActionEvent event) {
        clearFields();
            goToAfficherService(event);
    }
    private void clearFields() {
        titleField.clear();
        locationfield.setValue(null);
        type_servicefield.setValue(null);
        descriptionField.clear();
        prixField.clear();
        idPartenaireField.setValue(null); // Vider le ComboBox ID partenaire
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        // Charger la nouvelle scène (Service.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene serviceScene = new Scene(reclamationLayout);

        // Obtenir la fenêtre (Stage) actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        currentStage.close();

        // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
        Stage newStage = new Stage();
        newStage.setScene(serviceScene);
        newStage.show();
    }

    private void goToAfficherService(ActionEvent event) {
        try {
            // Charger la scène d'affichage des services (AfficherService.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/AfficherService.fxml"));
            Parent root = loader.load();
            Scene afficherServiceScene = new Scene(root);

            // Obtenir la fenêtre (Stage) actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Ouvrir la nouvelle fenêtre (AfficherService.fxml)
            Stage newStage = new Stage();
            newStage.setScene(afficherServiceScene);
            newStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des services : " + e.getMessage());
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

}
