package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Models.Service.Service;
import Services.Reservation.Crud.ReservationPersonaliseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Services.Service.Crud.ServiceService;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ModifierReservationPersonalise {
    @FXML
    private ComboBox idServicefield;
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
    private ReservationPersonalise reservationToEdit;

    @FXML
    public void initialize() {
        // Ajoute tous les types de réclamation à la liste du ComboBox

        submitButton.setOnAction(this::modifierReservationPersonalise);
        cancelButton.setOnAction(event -> annuler());
        loadServices();

    }
    private void loadServices() {
        List<Service> services = serviceService.RechercherService();
        idServicefield.getItems().addAll(services);
    }
    // Méthode pour pré-remplir les champs avec les données de la réclamation à modifier
    public void setReservationToEdit(ReservationPersonalise reservation) {
        this.reservationToEdit = reservation;

        // Pré-remplir les champs avec les données de la réclamation existante
        nomfield.setText(reservation.getNom());
        prenomfield.setText(reservation.getPrenom());
        emailfield.setText(reservation.getEmail());
        numtelfield.setText(reservation.getNumtel());
        descriptionfield.setText(reservation.getDescription());
        datefield.setValue(LocalDate.ofInstant(Instant.ofEpochMilli(reservation.getDate().getTime()), ZoneId.systemDefault()));

    }
    private void modifierReservationPersonalise(ActionEvent event) {
        String nom = nomfield.getText();
        String prenom = prenomfield.getText();
        String email = emailfield.getText();
        String numTel = numtelfield.getText();
        String description = descriptionfield.getText();
        LocalDate date = datefield.getValue();

        if (nom.isEmpty() || email.isEmpty() || prenom.isEmpty() ) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Mise à jour de la reservation avec les nouvelles valeurs
        reservationToEdit.setNom(nom);
        reservationToEdit.setPrenom(prenom);
        reservationToEdit.setEmail(email);
        reservationToEdit.setNumtel(numTel);
        reservationToEdit.setDescription(description);
        reservationToEdit.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Appel à la méthode ModifierRec du service
        reservationservice.modifierReservationPersonalise(reservationToEdit);

        showAlert("Succès", "reservation modifiée avec succès !");
        clearFields();
        goToReservationListePersonalise();
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
            AnchorPane ReservationLayout = loader.load();
            Scene scene = new Scene(ReservationLayout);
            Stage stage = (Stage) submitButton.getScene().getWindow(); // Récupère la fenêtre actuelle
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
