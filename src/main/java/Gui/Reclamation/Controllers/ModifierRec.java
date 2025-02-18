package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import Services.Reclamation.Crud.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierRec {

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

    private Reclamation reclamationToEdit; // Ajout de cette variable pour contenir la réclamation à modifier
    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        typeField.getItems().setAll(TypeReclamation.values()); // Ajoute tous les types de réclamation à la liste du ComboBox

        submitButton.setOnAction(this::modifierReclamation);
        cancelButton.setOnAction(event -> annuler());
    }

    // Méthode pour pré-remplir les champs avec les données de la réclamation à modifier
    public void setReclamationToEdit(Reclamation reclamation) {
        this.reclamationToEdit = reclamation;

        // Vérification : affichage des valeurs reçues dans la console pour debug
        System.out.println("Titre: " + reclamation.getTitre());
        System.out.println("Description: " + reclamation.getDescription());
        System.out.println("Type: " + reclamation.getType());

        // Pré-remplir les champs avec les données de la réclamation existante
        titleField.setText(reclamation.getTitre());
        descField.setText(reclamation.getDescription());
        typeField.setValue(reclamation.getType()); // Sélectionner le type de réclamation
    }

    private void modifierReclamation(ActionEvent event) {
        String titre = titleField.getText();
        String description = descField.getText();
        TypeReclamation type = typeField.getValue();

        if (titre.isEmpty() || description.isEmpty() || type == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Mise à jour de la réclamation avec les nouvelles valeurs
        reclamationToEdit.setTitre(titre);
        reclamationToEdit.setDescription(description);
        reclamationToEdit.setType(type);

        // Appel à la méthode ModifierRec du service
        reclamationService.ModifierRec(reclamationToEdit);

        showAlert("Succès", "Réclamation modifiée avec succès !");
        clearFields();
        goToAfficherRec(event);
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

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());

        alert.setTitle(title);
        alert.setHeaderText(null);  // On peut personnaliser ou laisser vide
        alert.setContentText(content);

        alert.showAndWait();
    }


    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void goToAfficherRec(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheRec.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void goToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene feedbackScene = new Scene(feedbackLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(feedbackScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
