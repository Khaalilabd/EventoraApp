package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Models.Service.Service;
import Services.Reservation.Crud.ReservationPersonaliseService;
import Services.Service.Crud.ServiceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModifierReservationPersonalise {
    @FXML
    private VBox serviceCheckboxes;
    @FXML
    private TextField nomfield;
    @FXML
    private TextField prenomfield;
    @FXML
    private TextField emailfield;
    @FXML
    private TextField numtelfield;
    @FXML
    private TextArea descriptionfield;
    @FXML
    private DatePicker datefield;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private final ReservationPersonaliseService reservationService = new ReservationPersonaliseService();
    private final ServiceService serviceService = new ServiceService();
    private ReservationPersonalise reservationToEdit;

    @FXML
    public void initialize() {
        submitButton.setOnAction(this::modifierReservationPersonalise);
        cancelButton.setOnAction(this::annuler);
        loadServicesAsCheckboxes();

        // Initialiser le ComboBox de statut
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
            "En attente",
            "Validé",
            "Refusé"
        );
        statusComboBox.setItems(statusOptions);

        // Désactiver les dates passées dans le DatePicker
        datefield.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void loadServicesAsCheckboxes() {
        List<Service> services = serviceService.RechercherService();
        for (Service service : services) {
            CheckBox checkBox = new CheckBox(service.getTitre());
            checkBox.setUserData(service.getId());
            serviceCheckboxes.getChildren().add(checkBox);
        }
    }

    public void setReservationToEdit(ReservationPersonalise reservation) {
        this.reservationToEdit = reservation;

        // Pré-remplir les champs avec les données existantes
        nomfield.setText(reservation.getNom());
        prenomfield.setText(reservation.getPrenom());
        emailfield.setText(reservation.getEmail());
        numtelfield.setText(reservation.getNumtel());
        descriptionfield.setText(reservation.getDescription());
        statusComboBox.setValue(reservation.getStatus());

        // Convertir java.util.Date ou java.sql.Date en LocalDate pour le DatePicker
        Date date = reservation.getDate();
        if (date != null) {
            LocalDate localDate;
            if (date instanceof java.sql.Date) {
                localDate = ((java.sql.Date) date).toLocalDate();
            } else {
                localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            datefield.setValue(localDate);
        } else {
            datefield.setValue(null);
        }

        // Pré-cocher les services associés à la réservation
        List<Integer> selectedServiceIds = reservation.getServiceIds();
        for (Node node : serviceCheckboxes.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                int serviceId = (Integer) checkBox.getUserData();
                checkBox.setSelected(selectedServiceIds.contains(serviceId));
            }
        }
    }

    @FXML
    private void modifierReservationPersonalise(ActionEvent event) {
        if (!validateFields()) return;

        // Récupérer les services sélectionnés
        List<Integer> selectedServiceIds = new ArrayList<>();
        for (Node node : serviceCheckboxes.getChildren()) {
            if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                selectedServiceIds.add((Integer) checkBox.getUserData());
            }
        }

        if (selectedServiceIds.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner au moins un service !");
            return;
        }

        // Mettre à jour l'objet reservationToEdit avec les nouvelles valeurs
        reservationToEdit.setNom(nomfield.getText().trim());
        reservationToEdit.setPrenom(prenomfield.getText().trim());
        reservationToEdit.setEmail(emailfield.getText().trim());
        reservationToEdit.setNumtel(numtelfield.getText().trim());
        reservationToEdit.setDescription(descriptionfield.getText().trim());
        reservationToEdit.setDate(Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationToEdit.setServiceIds(selectedServiceIds);
        reservationToEdit.setStatus(statusComboBox.getValue());

        // Appeler la méthode du service pour modifier la réservation
        reservationService.modifierReservationPersonalise(reservationToEdit);

        showAlert("Succès", "Réservation modifiée avec succès !");
        clearFields();
        goToReservationListePersonalise();
    }

    private boolean validateFields() {
        if (nomfield.getText().trim().isEmpty() || prenomfield.getText().trim().isEmpty() ||
                emailfield.getText().trim().isEmpty() || numtelfield.getText().trim().isEmpty() ||
                descriptionfield.getText().trim().isEmpty() || datefield.getValue() == null ||
                statusComboBox.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return false;
        }
        if (!emailfield.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "Adresse email invalide !");
            return false;
        }
        if (!numtelfield.getText().matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return false;
        }
        if (datefield.getValue().isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date ne peut pas être dans le passé !");
            return false;
        }
        return true;
    }

    @FXML
    public void annuler(ActionEvent event) {
        clearFields();
        goToReservationListePersonalise();
    }

    private void clearFields() {
        nomfield.clear();
        prenomfield.clear();
        emailfield.clear();
        numtelfield.clear();
        descriptionfield.clear();
        datefield.setValue(null);
        statusComboBox.setValue(null);
        serviceCheckboxes.getChildren().forEach(node -> {
            if (node instanceof CheckBox checkBox) checkBox.setSelected(false);
        });
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
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
        AnchorPane reservationLayout = loader.load();
        Scene scene = new Scene(reservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToReservationListePersonalise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AfficherReservationPersonalise.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
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