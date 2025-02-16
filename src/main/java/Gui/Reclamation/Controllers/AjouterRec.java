package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import Services.Reclamation.Crud.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AjouterRec {

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<TypeReclamation> typeField; // ComboBox pour le type

    @FXML
    private TextArea descField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Remplir la ComboBox avec les valeurs de l'Enum TypeReclamation
        typeField.getItems().setAll(TypeReclamation.values());

        submitButton.setOnAction(event -> ajouterReclamation());
        cancelButton.setOnAction(event -> annuler());
    }

    private void ajouterReclamation() {
        String titre = titleField.getText();
        String description = descField.getText();
        TypeReclamation type = typeField.getValue();

        if (titre.isEmpty() || description.isEmpty() || type == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Reclamation reclamation = new Reclamation(1, titre, description, type); // Remplace "1" par l'ID utilisateur réel
        reclamationService.AjouterRec(reclamation);
        showAlert("Succès", "Réclamation ajoutée avec succès !");
        clearFields();
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        descField.clear();
        typeField.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
