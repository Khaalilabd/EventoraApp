package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Services.Reclamation.Crud.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AjouterRec {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Initialisation des actions des boutons
        submitButton.setOnAction(event -> handleSubmit());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleSubmit() {
        String title = titleField.getText();
        String description = descField.getText();

        // Vérification que les champs ne sont pas vides
        if (title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Créer un objet Reclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(title);
        reclamation.setDescription(description);
        reclamation.setIdUser(2); // idUser par défaut (à remplacer par l'ID de l'utilisateur connecté)

        // Ajouter la réclamation via le service
        reclamationService.AjouterRec(reclamation);

        // Afficher une alerte de succès
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réclamation ajoutée avec succès !");

        // Réinitialisation des champs après soumission
        titleField.clear();
        descField.clear();
    }

    private void handleCancel() {
        // Réinitialisation des champs en cas d'annulation
        titleField.clear();
        descField.clear();
        System.out.println("Opération annulée.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de texte d'en-tête
        alert.setContentText(message);
        alert.showAndWait();
    }
}