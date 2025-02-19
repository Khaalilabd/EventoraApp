package Gui.Reservation.Controllers;

import Models.Reservation.Type;
import javafx.scene.Node;
import Models.Reservation.Reservation;
import Services.Reservation.Crud.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierReservation {
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

    private Reservation reservationToEdit;
    private ReservationService reservationservice=new ReservationService();

    @FXML
    public void initialize() {
         // Ajoute tous les types de réclamation à la liste du ComboBox

        submitButton.setOnAction(this::modifierReservation);
        cancelButton.setOnAction(event -> annuler());
    }
    // Méthode pour pré-remplir les champs avec les données de la réclamation à modifier
    public void setReservationToEdit(Reservation reservation) {
        this.reservationToEdit = reservation;

        // Pré-remplir les champs avec les données de la réclamation existante
        nomfield.setText(reservation.getNom());
        prenomfield.setText(reservation.getPrenom());
        emailfield.setText(reservation.getEmail());
        numtelfield.setText(reservation.getNumTel());
        descriptionfield.setText(reservation.getDescription());
        datefield.setValue(null);

    }
    private void modifierReservation(ActionEvent event) {
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

        // Mise à jour de la réclamation avec les nouvelles valeurs
        reservationToEdit.setNom(nom);
        reservationToEdit.setPrenom(prenom);
        reservationToEdit.setEmail(email);
        reservationToEdit.setNumTel(numTel);
        reservationToEdit.setDescription(description);
        reservationToEdit.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Appel à la méthode ModifierRec du service
        reservationservice.modifier(reservationToEdit);

        showAlert("Succès", "reservation modifiée avec succès !");
        clearFields();
        goToAfficherReservation(event);
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
    private void goToAfficherReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheReservation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des reservations : " + e.getMessage());
            e.printStackTrace();
        }
    }


}
