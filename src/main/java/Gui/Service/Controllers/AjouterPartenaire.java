package Gui.Service.Controllers;

import Models.Service.Partenaire;
import Models.Service.TypePartenaire;
import Services.Service.Crud.PartenaireService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.event.ActionEvent;
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
    public void gotoAfficherPartenaire(ActionEvent event) {
        try {
            // Charger la nouvelle scène (AfficherPartenaire.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPartenaire.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Obtenir la fenêtre (Stage) actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
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
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        // Charger la nouvelle scène (Service.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        // Obtenir la fenêtre (Stage) actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        currentStage.close();

        // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }
}
