package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Models.Pack.Pack;
import Services.Pack.Crud.PackService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AjouterReservationPack {
    @FXML
    private ComboBox<String> idPackfield;
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

    private ReservationPackService reservationservice = new ReservationPackService();
    private PackService packService = new PackService();
    private ObservableList<String> packNames = FXCollections.observableArrayList();

    // Twilio credentials (replace with your actual values)
    private static final String ACCOUNT_SID = "ACc57c0b58eff936708b3208d34fd03469";
    private static final String AUTH_TOKEN = "b2a441186c89d703043342fcd40b372a";
    private static final String TWILIO_PHONE_NUMBER = "+12513125202"; // Your Twilio number

    @FXML
    public void initialize() {
        submitButton.setOnAction(this::ajouterReservationPack);
        cancelButton.setOnAction(event -> annuler());

        loadPackNames();

        idPackfield.setItems(packNames);

        // Date validation
        datefield.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today)); // Disable dates before today
            }
        });

        // Date validation listener (for error message)
        datefield.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isBefore(LocalDate.now())) {
                showAlert("Erreur", "Vous ne pouvez pas sélectionner une date antérieure à la date actuelle.");
                datefield.setValue(null); // Reset datepicker value
            }
        });
    }

    private void loadPackNames() {
        List<Pack> packs = packService.rechercher();
        for (Pack pack : packs) {
            packNames.add(pack.getNomPack());
        }
    }

    public void ajouterReservationPack(ActionEvent event) {
        String nom = nomfield.getText().trim();
        String prenom = prenomfield.getText().trim();
        String email = emailfield.getText().trim();
        String numTel = numtelfield.getText().trim();
        String description = descriptionfield.getText().trim();
        LocalDate date = datefield.getValue();

        String selectedPackName = idPackfield.getValue();

        // Vérification des champs vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || numTel.isEmpty() || description.isEmpty() || date == null || selectedPackName == null || selectedPackName.isEmpty()) {
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
        if (date.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date ne peut pas être dans le passé !");
            return;
        }

        // Récupération de l'ID du pack
        int idPack = packService.getIdPackByNom(selectedPackName);
        if (idPack == -1) {
            showAlert("Erreur", "Le pack sélectionné est invalide.");
            return;
        }

        // Conversion de LocalDate en Date
        Date reservationDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        try {
            // Création de l'objet ReservationPack
            ReservationPack reservation = new ReservationPack(
                    idPack,
                    1, // idMembre par défaut
                    nom,
                    prenom,
                    email,
                    numTel,
                    description,
                    reservationDate,
                    "En attente" // status par défaut
            );
            reservationservice.ajouterReservationPack(reservation);

            showAlert("Succès", "Réservation ajoutée avec succès !");
            clearFields();
            goToReservationListePack();

            // Send SMS confirmation after successful reservation
            sendSMSConfirmation(reservation);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la réservation.");
        }
    }

    private void sendSMSConfirmation(ReservationPack reservation) {
        // Initialize Twilio with your credentials
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Get the customer's phone number (format it for E.164, assuming Tunisian prefix +216)
        String customerPhoneNumber = "+216" + reservation.getNumtel(); // Ensure this is in E.164 format, e.g., "+21651863242"

        // Construct the SMS message
        String messageBody = String.format(
                "Réservation Confirmée ! ID: %d, Pack: %s, Date: %s. Répondez 'ANNULER' pour annuler.",
                reservation.getIdReservationPack(), reservation.getNom(), reservation.getDate().toString()
        );

        try {
            // Send the SMS
            Message message = Message.creator(
                    new PhoneNumber(customerPhoneNumber), // To: Customer's phone number
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // From: Your Twilio number
                    messageBody // Message body
            ).create();

            System.out.println("SMS envoyé avec succès avec SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Échec lors de l'envoi du SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du retour à la page de réservation : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomfield.clear();
        prenomfield.clear();
        emailfield.clear();
        numtelfield.clear();
        descriptionfield.clear();
        datefield.setValue(null);
        idPackfield.setValue(null); // Clear ComboBox
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
    private void goToReservationListePack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AfficheReservationPack.fxml"));
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

    public void setSelectedPack(String nomPack) {
    }
}