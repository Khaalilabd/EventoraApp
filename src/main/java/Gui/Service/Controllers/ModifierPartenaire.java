package Gui.Service.Controllers;

import Models.Service.*;
import Models.Service.Partenaire;
import Models.Service.Service;
import Services.Service.Crud.PartenaireService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.awt.*;
import java.io.IOException;

public class ModifierPartenaire {

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

    private Partenaire partenaireToEdit;
    private final PartenaireService partenaireService = new PartenaireService();

    @FXML
    public void initialize() {
        typefield.getItems().setAll(TypePartenaire.values());
        submitButton.setOnAction(this::modifierPartenaire);
        cancelButton.setOnAction(event -> annuler());
    }

    public void setPartenaireToEdit(Partenaire partenaire) {
        this.partenaireToEdit = partenaire;

        // Vérification : affichage des valeurs reçues dans la console pour debug
        System.out.println("Nom: " + partenaire.getNom_sponsors());
        System.out.println("Email: " + partenaire.getEmail_sponsors());
        System.out.println("Telephone: " + partenaire.getTelephone_sponsors());
        System.out.println("Adresse: " + partenaire.getAdresse_sponsors());
        System.out.println("Site web: " + partenaire.getSite_web());
        System.out.println("Montant: " + partenaire.getMontant_sponsors());
        System.out.println("Type: " + partenaire.getType_sponsors().getLabel());



        // Pré-remplir les champs avec les données de la réclamation existante
        nomField.setText(partenaire.getNom_sponsors());
        emailField.setText(partenaire.getEmail_sponsors()); // Sélectionner le type de réclamation
        telephoneField.setText(partenaire.getTelephone_sponsors());
        adresseField.setText(partenaire.getAdresse_sponsors());
        site_webField.setText(partenaire.getSite_web());
        montantPartField.setText(partenaire.getMontant_sponsors());
        typefield.setValue(partenaire.getType_sponsors()); // Sélectionner le type de réclamation
    }

    private void modifierPartenaire(ActionEvent event) {
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String adresse = adresseField.getText();
        String site_web = site_webField.getText();
        String montantPart = montantPartField.getText();
        TypePartenaire type = typefield.getValue();




        // Mise à jour de la réclamation avec les nouvelles valeurs
        partenaireToEdit.setNom_sponsors(nom);
        partenaireToEdit.setEmail_sponsors(email);
        partenaireToEdit.setTelephone_sponsors(telephone);
        partenaireToEdit.setAdresse_sponsors(adresse);
        partenaireToEdit.setSite_web(site_web);
        partenaireToEdit.setMontant_sponsors(montantPart);
        partenaireToEdit.setType_sponsors(type);

        // Appel à la méthode ModifierRec du service
        partenaireService.ModifierPartenaire(partenaireToEdit);

        showAlert("Succès", "Le service a bis modifiée avec succès !");
        clearFields();
        goToAfficherPartenaire(event);
    }


    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        nomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        site_webField.clear();
        montantPartField.clear();
        typefield.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Partenaire.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void goToAfficherPartenaire(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPartenaire.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des partenaires : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
