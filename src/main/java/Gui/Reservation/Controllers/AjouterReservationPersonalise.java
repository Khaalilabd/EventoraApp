package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Models.Reservation.ReservationPersonalise;
import Services.Reservation.Crud.ReservationPackService;
import Services.Reservation.Crud.ReservationPersonaliseService;
import javafx.event.ActionEvent;
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

public class AjouterReservationPersonalise {
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

    private ReservationPersonaliseService reservationservice=new ReservationPersonaliseService();


   /* private void loadServices() {
        List<Service> services = serviceCrud.getAllServices();
        idServicefield.getItems().addAll(services);
    }
    public List<Service> getAllService() {
        List<Service> Service = new ArrayList<>();
        String query = "SELECT id, nom FROM Service"; // Adaptez la requête à votre schéma
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                packs.add(new Service(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packs;
    }
*/
    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        submitButton.setOnAction(this::ajouterReservationPersonalise);
        cancelButton.setOnAction(event -> annuler());
        // Charger les services dans la ComboBox
      //  loadServices();
    }

    public void ajouterReservationPersonalise(ActionEvent event) {
        String nom = nomfield.getText();
        String prenom = prenomfield.getText();
        String email = emailfield.getText();
        String numTel = numtelfield.getText();
        String description = descriptionfield.getText();
        LocalDate date = datefield.getValue();
        ReservationPersonalise reservation = new ReservationPersonalise(nom, prenom, email, numTel, description, Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationservice.ajouterReservationPersonalise(reservation);
        showAlert("Succès", "ReservationPersonalise ajouté avec succès !");
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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
        AnchorPane ReservationLayout = loader.load();
        Scene scene = new Scene(ReservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToReservationListePersonalise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservationPersonalise.fxml"));
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
