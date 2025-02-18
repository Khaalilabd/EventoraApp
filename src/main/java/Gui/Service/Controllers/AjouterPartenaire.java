package Gui.Service.Controllers;

import Models.Service.Partenaire;
import Models.Service.TypePartenaire;
import Services.Service.Crud.PartenaireService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class AjouterPartenaire {

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField adresseField;

    @FXML
    private TextField site_webField;

    @FXML
    private TextField montantPartField;

    @FXML
    private ComboBox<TypePartenaire> typefield;


    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private PartenaireService partenaireService = new PartenaireService();

    public void initialize() {
        typefield.getItems().setAll(TypePartenaire.values());
        submitButton.setOnAction(this::ajouterPartenaire);
        cancelButton.setOnAction(event -> annuler());
    }

        private void ajouterPartenaire(ActionEvent event) {
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String adresse = adresseField.getText();
        String site_web = site_webField.getText();
        String montantPart = montantPartField.getText();
        TypePartenaire type = typefield.getValue();

        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty() || site_web.isEmpty() || montantPart.isEmpty() || type == null ) {
            showAlert("Warning", "Veuillez remplir tous les champs.");
            return;
        }

        Partenaire partenaire = new Partenaire(nom, email, telephone, adresse, site_web, montantPart, type);
        partenaireService.AjouterPartenaire(partenaire);

        showAlert("Succès", "Partenaire ajouté avec succès !");
        clearFields();
        gotoAfficherPartenaire(event);

    }
       /*private void ajouterPartenaire(ActionEvent event) {
           String nom = nomField.getText();
           String email = emailField.getText();
           String telephone = telephoneField.getText();
           String adresse = adresseField.getText();
           String site_web = site_webField.getText();
           String montantPart = montantPartField.getText();
           TypePartenaire type = typefield.getValue();

           // Récupération de la date depuis le DatePicker
           LocalDate date = dateField.getValue(); // Récupérer directement la valeur du DatePicker

           // Vérification des champs
           if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty() || site_web.isEmpty() || montantPart.isEmpty() || type == null || date == null) {
               showAlert("Warning", "Veuillez remplir tous les champs.");
               return;
           }



           // Convertir LocalDate en java.sql.Date
           java.util.Date utilDate = java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
           java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

           // Création et ajout du partenaire
           Partenaire partenaire = new Partenaire(nom, email, telephone, adresse, site_web, montantPart, type, sqlDate);
           partenaireService.AjouterPartenaire(partenaire);

           // Confirmation et nettoyage des champs
           showAlert("Succès", "Partenaire ajouté avec succès !");
           clearFields();
           gotoAfficherPartenaire(event);
       }
*/


    public void gotoAfficherPartenaire(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherPartenaire.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des partenaires : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void annuler() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        montantPartField.clear();
        typefield.setValue(null);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void clearFields() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        montantPartField.clear();
        typefield.setValue(null);
    }
}
