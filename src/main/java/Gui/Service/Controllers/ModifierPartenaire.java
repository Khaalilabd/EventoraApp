package Gui.Service.Controllers;
import Models.Service.*;
import Models.Service.Partenaire;
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
        System.out.println("Nom: " + partenaire.getNom_partenaire());
        System.out.println("Email: " + partenaire.getEmail_partenaire());
        System.out.println("Telephone: " + partenaire.getTelephone_partenaire());
        System.out.println("Adresse: " + partenaire.getAdresse_partenaire());
        System.out.println("Site web: " + partenaire.getSite_web());
        System.out.println("Montant: " + partenaire.getMontant_partenaire());
        System.out.println("Type: " + partenaire.getType_partenaire().getLabel());
        nomField.setText(partenaire.getNom_partenaire());
        emailField.setText(partenaire.getEmail_partenaire());
        telephoneField.setText(partenaire.getTelephone_partenaire());
        adresseField.setText(partenaire.getAdresse_partenaire());
        site_webField.setText(partenaire.getSite_web());
        montantPartField.setText(partenaire.getMontant_partenaire());
        typefield.setValue(partenaire.getType_partenaire());
    }
    private void modifierPartenaire(ActionEvent event) {
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String adresse = adresseField.getText();
        String site_web = site_webField.getText();
        String montantPart = montantPartField.getText();
        TypePartenaire type = typefield.getValue();
        partenaireToEdit.setNom_partenaire(nom);
        partenaireToEdit.setEmail_partenaire(email);
        partenaireToEdit.setTelephone_partenaire(telephone);
        partenaireToEdit.setAdresse_partenaire(adresse);
        partenaireToEdit.setSite_web(site_web);
        partenaireToEdit.setMontant_partenaire(montantPart);
        partenaireToEdit.setType_partenaire(type);
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
        // Charger la nouvelle scène (Partenaire.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();
        Scene partenaireScene = new Scene(partenaireLayout);

        // Obtenir la fenêtre (Stage) actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Fermer la fenêtre actuelle
        currentStage.close();

        // Créer une nouvelle fenêtre et lui attribuer la nouvelle scène
        Stage newStage = new Stage();
        newStage.setScene(partenaireScene);
        newStage.show();
    }

    @FXML
    private void goToAfficherPartenaire(ActionEvent event) {
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

}
