package Gui.Reservation.Controllers;

import Models.Reservation.Reservation;
import Services.Reservation.Crud.ReservationService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjouterReservation {
    @FXML
    private ComboBox idoffrefield;
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

    private ReservationService reservationservice=new ReservationService();

    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        submitButton.setOnAction(this::ajouterReservation);
        cancelButton.setOnAction(event -> annuler());
    }

    public void ajouterReservation(ActionEvent event) {
        String nom = nomfield.getText();
        String prenom = prenomfield.getText();
        String email = emailfield.getText();
        String numTel = numtelfield.getText();
        String description = descriptionfield.getText();
        LocalDate date = datefield.getValue();
        int idoffre = 1;//salahaaaaaaaaaaa yaaaaaaaaa rayunnnnnnnoo
        Reservation reservation = new Reservation(idoffre,nom, prenom, email, numTel, description, Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationservice.ajouter(reservation);
        showAlert("Succès", "Reservation ajouté avec succès !");
        clearFields();

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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
