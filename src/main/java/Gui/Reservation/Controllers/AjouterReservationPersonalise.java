package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Models.Service.Service;
import Services.Reservation.Crud.ReservationPersonaliseService;
import Services.Service.Crud.ServiceService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterReservationPersonalise implements Initializable {
    @FXML
    private VBox serviceCheckboxes;
    @FXML
    private TextField nomfield, prenomfield, emailfield, numtelfield;
    @FXML
    private TextArea descriptionfield;
    @FXML
    private DatePicker datefield;
    @FXML
    private Button submitButton, cancelButton;
    @FXML
    private ComboBox<String> idServicefield;

    private final ReservationPersonaliseService reservationService = new ReservationPersonaliseService();
    private final ServiceService serviceService = new ServiceService();
    private static final String ACCOUNT_SID = "ACc57c0b58eff936708b3208d34fd03469";
    private static final String AUTH_TOKEN = "b2a441186c89d703043342fcd40b372a";
    private static final String TWILIO_PHONE_NUMBER = "+12513125202";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submitButton.setOnAction(this::ajouterReservationPersonalise);
        cancelButton.setOnAction(event -> clearFields());
        loadServicesAsCheckboxes();

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

    @FXML
    public void ajouterReservationPersonalise(ActionEvent event) {
        if (!validateFields()) return;

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

        Date date = Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        ReservationPersonalise reservation = new ReservationPersonalise(
                1, // idMembre par défaut
                nomfield.getText().trim(),
                prenomfield.getText().trim(),
                emailfield.getText().trim(),
                numtelfield.getText().trim(),
                descriptionfield.getText().trim(),
                date,
                "En attente", // status par défaut
                selectedServiceIds
        );

        reservationService.ajouterReservationPersonalise(reservation);
        sendSMSConfirmation(reservation);
        showAlert("Succès", "Réservation ajoutée avec succès !");
        clearFields();
        goToReservationListePersonalise();
    }

    private boolean validateFields() {
        if (nomfield.getText().trim().isEmpty() || prenomfield.getText().trim().isEmpty() ||
                emailfield.getText().trim().isEmpty() || numtelfield.getText().trim().isEmpty() ||
                descriptionfield.getText().trim().isEmpty() || datefield.getValue() == null) {
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
        return true;
    }

    private void sendSMSConfirmation(ReservationPersonalise reservation) {
        if (ACCOUNT_SID == null || AUTH_TOKEN == null) {
            showAlert("Erreur", "Les identifiants Twilio ne sont pas configurés correctement.");
            System.err.println("ACCOUNT_SID ou AUTH_TOKEN est null.");
            return;
        }

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            String customerPhoneNumber = "+216" + reservation.getNumtel();
            String messageBody = "Réservation confirmée ! Répondez 'ANNULER' pour annuler.";

            System.out.println("Envoi SMS vers : " + customerPhoneNumber);
            Message message = Message.creator(
                    new PhoneNumber(customerPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    messageBody
            ).create();
            System.out.println("SMS envoyé avec succès : " + message.getSid());
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("Erreur Twilio : Code " + e.getCode() + " - " + e.getMessage());
            showAlert("Erreur Twilio", "Échec de l'envoi du SMS : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de l'envoi du SMS : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomfield.clear();
        prenomfield.clear();
        emailfield.clear();
        numtelfield.clear();
        descriptionfield.clear();
        datefield.setValue(null);
        serviceCheckboxes.getChildren().forEach(node -> {
            if (node instanceof CheckBox checkBox) checkBox.setSelected(false);
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void goToReservationListePersonalise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AfficherReservationPersonalise.fxml"));
            AnchorPane layout = loader.load();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(layout));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la navigation : " + e.getMessage());
        }
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
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void annuler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AfficherReservationPersonalise.fxml"));
            AnchorPane layout = loader.load();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(layout));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la navigation : " + e.getMessage());
        }
    }
}
