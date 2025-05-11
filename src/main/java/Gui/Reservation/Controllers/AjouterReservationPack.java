package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.sql.Date;
import java.time.LocalDate;

public class AjouterReservationPack {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ComboBox<String> idoffrefield;

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

    private final ReservationPackService reservationPackService = new ReservationPackService();

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> handleSubmit());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleSubmit() {
        try {
            int idPack = 1;
            String nom = nomfield.getText();
            String prenom = prenomfield.getText();
            String email = emailfield.getText();
            String numTel = numtelfield.getText();
            String description = descriptionfield.getText();
            LocalDate localDate = datefield.getValue();
            Date sqlDate = (localDate != null) ? Date.valueOf(localDate) : null;

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numTel.isEmpty() || sqlDate == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            ReservationPack reservationPack = new ReservationPack(idPack, nom, prenom, email, numTel, description, sqlDate);
            reservationPackService.ajouterReservationPack(reservationPack);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès.");
            handleCancel(); // Réinitialiser les champs après l'ajout

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void handleCancel() {
        idoffrefield.setValue(null);
        nomfield.clear();
        prenomfield.clear();
        emailfield.clear();
        numtelfield.clear();
        descriptionfield.clear();
        datefield.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
