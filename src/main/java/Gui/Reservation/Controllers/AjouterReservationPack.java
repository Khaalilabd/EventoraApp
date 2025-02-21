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
        String nom = nomfield.getText();
        String prenom = prenomfield.getText();
        String email = emailfield.getText();
        String numTel = numtelfield.getText();
        String description = descriptionfield.getText();
        LocalDate date = datefield.getValue();

        String selectedPackName = idPackfield.getValue();
        if (selectedPackName == null || selectedPackName.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un pack.");
            return;
        }

        int idPack = packService.getIdPackByNom(selectedPackName);
        if (idPack == -1) {
            showAlert("Erreur", "ID du pack non trouvé.");
            return;
        }

        ReservationPack reservation = new ReservationPack(idPack, nom, prenom, email, numTel, description, Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationservice.ajouterReservationPack(reservation);
        showAlert("Succès", "ReservationPack ajouté avec succès !");
        clearFields();
        goToReservationListePack();
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
}