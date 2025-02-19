package Gui.Reservation.Controllers;

import Models.Reservation.Offre;
import Models.Reservation.Type;
import Services.Reservation.Crud.OffreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjouterOffre {

    @FXML
    private ComboBox typeoffrefield;
    @FXML
    private DatePicker datefield;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private OffreService offreservice=new OffreService();

    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        typeoffrefield.getItems().setAll(Type.values());
        submitButton.setOnAction(this::ajouterOffre);
        cancelButton.setOnAction(event -> annuler());
    }
    public void ajouterOffre(ActionEvent event) {
        LocalDate date = datefield.getValue();
        Type typeoffre = (Type) typeoffrefield.getValue();//salahaaaaaaaaaaa yaaaaaaaaa rayunnnnnnnoo
        Offre offre = new Offre(typeoffre,Date.from(datefield.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        offreservice.ajouter(offre);
        showAlert("Succès", "Offre ajouté avec succès !");
        clearFields();

    }
    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        datefield.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
