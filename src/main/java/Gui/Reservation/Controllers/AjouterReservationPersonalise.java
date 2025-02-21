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
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AjouterReservationPersonalise {
    @FXML
    private ComboBox<Service> idServicefield;
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
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private ReservationPersonaliseService reservationservice = new ReservationPersonaliseService();
    private ServiceService serviceService = new ServiceService();

    @FXML
    public void initialize() {
        submitButton.setOnAction(this::ajouterReservationPersonalise);
        cancelButton.setOnAction(event -> annuler());

        loadServices();
    }

    private void loadServices() {
        List<Service> services = serviceService.RechercherService();
        idServicefield.getItems().addAll(services);
    }

    @FXML
    public void ajouterReservationPersonalise(ActionEvent event) {
        String nom = nomfield.getText().trim();
        String prenom = prenomfield.getText().trim();
        String email = emailfield.getText().trim();
        String numTel = numtelfield.getText().trim();
        String description = descriptionfield.getText().trim();
        LocalDate localDate = datefield.getValue(); // Récupérer la date

        Service selectedService = idServicefield.getValue();

        // Vérification des champs vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numTel.isEmpty() || description.isEmpty() || localDate == null || selectedService == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérification du format de l'email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Erreur", "Veuillez entrer une adresse email valide !");
            return;
        }

        // Vérification du numéro de téléphone (exactement 8 chiffres)
        if (!numTel.matches("^\\d{8}$")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
            return;
        }

        // Vérification que la date n'est pas dans le passé
        if (localDate.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date ne peut pas être dans le passé !");
            return;
        }

        // Conversion de LocalDate en Date
        Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        try {
            ReservationPersonalise reservation = new ReservationPersonalise(nom, prenom, email, numTel, description, date, selectedService.getId()); // Passer idService
            reservationservice.ajouterReservationPersonalise(reservation);

            showAlert("Succès", "Réservation ajoutée avec succès !");
            clearFields();
            goToReservationListePersonalise();

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la réservation.");
        }
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        nomfield.clear();
        prenomfield.clear();
        emailfield.clear();
        numtelfield.clear();
        descriptionfield.clear();
        datefield.setValue(null);
        idServicefield.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
        AnchorPane ReservationLayout = loader.load();
        Scene scene = new Scene(ReservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToReservationListePersonalise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AfficherReservationPersonalise.fxml"));
            AnchorPane ReservationLayout = loader.load();
            Scene scene = new Scene(ReservationLayout);
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