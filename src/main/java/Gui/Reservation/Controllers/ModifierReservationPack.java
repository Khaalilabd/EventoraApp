package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierReservationPack {
    @FXML
    private ComboBox idPackfield;
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

    private ReservationPack reservationToEdit;
    private ReservationPackService reservationservice=new ReservationPackService();

    @FXML
    public void initialize() {
        // Ajoute tous les types de réclamation à la liste du ComboBox

        submitButton.setOnAction(this::modifierReservationPack);
        cancelButton.setOnAction(event -> annuler());
    }
    // Méthode pour pré-remplir les champs avec les données de la réclamation à modifier
    public void setReservationToEdit(ReservationPack reservation) {
        this.reservationToEdit = reservation;

        // Pré-remplir les champs avec les données de la réclamation existante
        nomfield.setText(reservation.getNom());
        prenomfield.setText(reservation.getPrenom());
        emailfield.setText(reservation.getEmail());
        numtelfield.setText(reservation.getNumtel());
        descriptionfield.setText(reservation.getDescription());
        datefield.setValue(LocalDate.ofInstant(Instant.ofEpochMilli(reservation.getDate().getTime()), ZoneId.systemDefault()));

    }
    private void modifierReservationPack(ActionEvent event) {
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
        reservationservice.modifierReservationPack(reservationToEdit);

        showAlert("Succès", "reservation modifiée avec succès !");
        clearFields();
        goToReservationPack();

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
        AnchorPane reservationLayout = loader.load();
        Scene scene = new Scene(reservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToReservationPack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheReservationPack.fxml"));
            AnchorPane ReservationLayout = loader.load();
            Scene scene = new Scene(ReservationLayout);
            Stage stage = (Stage) submitButton.getScene().getWindow(); // Récupère la fenêtre actuelle
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
