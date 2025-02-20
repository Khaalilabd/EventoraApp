package Gui.Service.Controllers;
import Models.Service.Location;
import Models.Service.Service;
import Models.Service.TypeService;
import Models.Service.Partenaire;
import Services.Service.Crud.ServiceService;
import Services.Service.Crud.PartenaireService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;
public class AjouterService {
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<Location> locationField;
    @FXML
    private ComboBox<TypeService> type_serviceField;
    @FXML
    private ComboBox<Partenaire> idPartenaireField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField prixField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
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
                } else
                {
                    setText(item.getNom_partenaire());  // Affiche le nom du partenaire
                }
            }
        });
    }
    private void ajouterService(ActionEvent event) {
        Partenaire partenaire = idPartenaireField.getValue(); // Récupération du partenaire sélectionné
        Integer idPartenaire = partenaire != null ? partenaire.getId_partenaire() : null;
        String titre = titleField.getText();
        Location location = locationField.getValue();
        TypeService typeService = type_serviceField.getValue();
        String description = descriptionField.getText();
        String prix = prixField.getText();

        // Vérification que tous les champs sont remplis
        if (idPartenaire == null || titre.isEmpty() || location == null || typeService == null || description.isEmpty() || prix.isEmpty()) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation : Le titre doit contenir uniquement des caractères alphabétiques
        if (!titre.matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le titre doit contenir uniquement des caractères alphabétiques.");
            return;
        }

        // Validation : La description doit contenir uniquement des caractères alphabétiques (ou espaces)
        if (!description.matches("[a-zA-Z ,.\\-']+")) { // Inclut des caractères spéciaux comme la virgule, le point et l'apostrophe
            showAlert("Erreur", "La description doit contenir uniquement des caractères alphabétiques.");
            return;
        }

        // Validation : Le prix doit être un nombre et se terminer par 'dt' ou 'DT'
        if (!prix.matches("\\d+(dt|DT)$")) {
            showAlert("Erreur", "Le prix doit être un nombre et se terminer par 'dt' ou 'DT'.");
            return;
        }

        // Si toutes les validations passent, ajouter le service
        Service service = new Service(idPartenaire, titre, location, typeService, description, prix);
        serviceService.AjouterService(service);
        showAlert("Succès", "Service ajouté avec succès !");
        clearFields();
        gotoAfficherService(event);
    }

    private void gotoAfficherService(ActionEvent event) {
        try {
            // Charger la nouvelle scène (AfficherService.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherService.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Obtenir la fenêtre (Stage) actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des services : " + e.getMessage());
            e.printStackTrace();
        }
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        // Charger la nouvelle scène (Service.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        // Obtenir la fenêtre (Stage) actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        currentStage.close();

        // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }
    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void annuler() {
        clearFields();  // Réinitialise les champs
    }

}
