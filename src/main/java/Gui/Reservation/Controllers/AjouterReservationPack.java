package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
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

public class AjouterReservationPack {
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

    private ReservationPackService reservationservice=new ReservationPackService();


   /* public List<Pack> getAllPacks() {
        List<Pack> packs = new ArrayList<>();
        String query = "SELECT id, nom FROM pack"; // Adaptez la requête à votre schéma
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                packs.add(new Pack(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packs;
    }
    private void loadPacks() {
    List<Pack> packs = reservationservice.getAllPacks();
    idPackfield.getItems().addAll(packs);
}
    */


    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        submitButton.setOnAction(this::ajouterReservationPack);
        cancelButton.setOnAction(event -> annuler());
        // Charger les packs dans la ComboBox
      //  loadPacks();
    }

    public void ajouterReservationPack(ActionEvent event) {
        String nom = nomfield.getText();
        String prenom = prenomfield.getText();
        String email = emailfield.getText();
        String numTel = numtelfield.getText();
        String description = descriptionfield.getText();
        LocalDate date = datefield.getValue();
        int idPack = 1;//salahaaaaaaaaaaa yaaaaaaaaa rayunnnnnnnoo
        ReservationPack reservation = new ReservationPack(idPack,nom, prenom, email, numTel, description, Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
    private void goToReservationListePack() {
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
