package Gui.Service.Controllers;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.TypeService;
import Models.Service.Partenaire;
import Services.Service.Crud.ServiceService;
import Services.Service.Crud.PartenaireService;
import Utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

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
        // Charger les valeurs des ComboBox
        locationField.getItems().setAll(Location.values());
        type_serviceField.getItems().setAll(TypeService.values());
        loadPartenaires();

        // Lier les actions des boutons
        submitButton.setOnAction(this::ajouterService);
        cancelButton.setOnAction(event -> annuler());

        // Ajouter des styles dynamiques pour indiquer les champs obligatoires au survol
        styleFields();
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
    @FXML
    private void deconnexion(ActionEvent event) {
        // Effacer la session
        SessionManager.getInstance().clearSession();
        // Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }

    @FXML
    private void goToParams(ActionEvent event) {
        switchScene(event, "/Utilisateurs/Parametres.fxml");
    }

    @FXML
    private void goToUser(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    private void styleFields() {
        // Ajouter un style dynamique pour indiquer les champs obligatoires
        titleField.setStyle(titleField.getStyle() + "; -fx-border-color: #D1D5DB;");
        descriptionField.setStyle(descriptionField.getStyle() + "; -fx-border-color: #D1D5DB;");
        prixField.setStyle(prixField.getStyle() + "; -fx-border-color: #D1D5DB;");
        idPartenaireField.setStyle(idPartenaireField.getStyle() + "; -fx-border-color: #D1D5DB;");
        locationField.setStyle(locationField.getStyle() + "; -fx-border-color: #D1D5DB;");
        type_serviceField.setStyle(type_serviceField.getStyle() + "; -fx-border-color: #D1D5DB;");

        // Mettre en surbrillance les champs en cas d'erreur
        titleField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && titleField.getText().isEmpty()) {
                titleField.setStyle(titleField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (!titleField.getText().isEmpty()) {
                titleField.setStyle(titleField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
            }
        });

        descriptionField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && descriptionField.getText().isEmpty()) {
                descriptionField.setStyle(descriptionField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (!descriptionField.getText().isEmpty()) {
                descriptionField.setStyle(descriptionField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
            }
        });

        prixField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && prixField.getText().isEmpty()) {
                prixField.setStyle(prixField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (!prixField.getText().isEmpty()) {
                prixField.setStyle(prixField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
            }
        });

        idPartenaireField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && idPartenaireField.getValue() == null) {
                idPartenaireField.setStyle(idPartenaireField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (idPartenaireField.getValue() != null) {
                idPartenaireField.setStyle(idPartenaireField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
            }
        });

        locationField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && locationField.getValue() == null) {
                locationField.setStyle(locationField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (locationField.getValue() != null) {
                locationField.setStyle(locationField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
            }
        });

        type_serviceField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && type_serviceField.getValue() == null) {
                type_serviceField.setStyle(type_serviceField.getStyle() + "; -fx-border-color: #EF4444;");
            } else if (type_serviceField.getValue() != null) {
                type_serviceField.setStyle(type_serviceField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
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

        // Validation des champs
        boolean hasError = false;

        if (idPartenaire == null) {
            idPartenaireField.setStyle(idPartenaireField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }
        if (titre.isEmpty()) {
            titleField.setStyle(titleField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }
        if (location == null) {
            locationField.setStyle(locationField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }
        if (typeService == null) {
            type_serviceField.setStyle(type_serviceField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }
        if (description.isEmpty()) {
            descriptionField.setStyle(descriptionField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }
        if (prix.isEmpty()) {
            prixField.setStyle(prixField.getStyle() + "; -fx-border-color: #EF4444;");
            hasError = true;
        }

        if (hasError) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }

        if (!titre.matches("[a-zA-Z ]+")) {
            titleField.setStyle(titleField.getStyle() + "; -fx-border-color: #EF4444;");
            showAlert("Erreur", "Le titre doit contenir uniquement des caractères alphabétiques.", Alert.AlertType.ERROR);
            return;
        }

        if (!description.matches("[a-zA-Z ,.\\-']+")) {
            descriptionField.setStyle(descriptionField.getStyle() + "; -fx-border-color: #EF4444;");
            showAlert("Erreur", "La description doit contenir uniquement des caractères alphabétiques et certains caractères spéciaux (,. - ').", Alert.AlertType.ERROR);
            return;
        }

        if (!prix.matches("\\d+(dt|DT)$")) {
            prixField.setStyle(prixField.getStyle() + "; -fx-border-color: #EF4444;");
            showAlert("Erreur", "Le prix doit être un nombre et se terminer par 'dt' ou 'DT'.", Alert.AlertType.ERROR);
            return;
        }

        // Ajouter le service
        Service service = new Service(idPartenaire, titre, location, typeService, description, prix);
        serviceService.AjouterService(service);

        // Afficher un message de succès avec une petite animation
        showAlert("Succès", "Service ajouté avec succès !", Alert.AlertType.INFORMATION);
        clearFields();

        // Petite pause avant de rediriger
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e -> switchScene(event, "/Service/AfficherService.fxml"));
        delay.play();
    }

    private void annuler() {
        clearFields();
        // Réinitialiser les styles des champs
        resetFieldStyles();
    }

    private void clearFields() {
        titleField.clear();
        locationField.setValue(null);
        type_serviceField.setValue(null);
        idPartenaireField.setValue(null);
        descriptionField.clear();
        prixField.clear();
    }

    private void resetFieldStyles() {
        titleField.setStyle(titleField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
        descriptionField.setStyle(descriptionField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
        prixField.setStyle(prixField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
        idPartenaireField.setStyle(idPartenaireField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
        locationField.setStyle(locationField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
        type_serviceField.setStyle(type_serviceField.getStyle().replace("-fx-border-color: #EF4444;", "-fx-border-color: #D1D5DB;"));
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Ajouter un style personnalisé à l'alerte
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Inter', sans-serif; -fx-font-size: 14px; -fx-background-color: #FFFFFF; -fx-border-radius: 8px;");
        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: " + (alertType == Alert.AlertType.INFORMATION ? "#3B82F6" : "#EF4444") +
                        "; -fx-text-fill: #FFFFFF; -fx-font-weight: 600; -fx-padding: 8px 16px; -fx-border-radius: 6px;"
        );

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
