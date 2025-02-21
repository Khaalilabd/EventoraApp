package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import Models.Reclamation.Statut;
import Models.Utilisateur.Utilisateurs;
import Services.Reclamation.Crud.ReclamationService;
import Services.Utilisateur.Crud.MembresService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;

public class AjouterRec {

    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<TypeReclamation> typeField;
    @FXML
    private ComboBox<String> userField;
    @FXML
    private TextArea descField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private final ReclamationService reclamationService = new ReclamationService();
    private final MembresService utilisateurService = new MembresService();
    private ObservableList<String> userNames = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeField.getItems().setAll(TypeReclamation.values());
        submitButton.setOnAction(this::ajouterReclamation);
        cancelButton.setOnAction(event -> annuler());

        loadUserNames();
        userField.setItems(userNames);
    }

    private void loadUserNames() {
        List<Utilisateurs> utilisateurs = utilisateurService.RechercherMem();
        for (Utilisateurs utilisateur : utilisateurs) {
            userNames.add(utilisateur.getNom() + " " + utilisateur.getPrenom());
        }
    }

    private void ajouterReclamation(ActionEvent event) {
        String titre = titleField.getText();
        String description = descField.getText();
        TypeReclamation type = typeField.getValue();
        String selectedUserName = userField.getValue();

        if (titre.isEmpty() || description.isEmpty() || type == null || selectedUserName == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }
        int userId = utilisateurService.getIdByNomPrenom(selectedUserName);
        if (userId == -1) {
            showAlert("Erreur", "Utilisateur non trouvé.");
            return;
        }

        Reclamation reclamation = new Reclamation(userId, titre, description, type);
        reclamation.setStatut(Statut.EN_ATTENTE);
        reclamationService.AjouterRec(reclamation);
        showAlert("Succès", "Réclamation ajoutée avec succès !");
        clearFields();
        goToAfficherRec(event);
    }

    private void goToAfficherRec(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AfficheRec.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des réclamations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void annuler() {
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        descField.clear();
        typeField.setValue(null);
        userField.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene feedbackScene = new Scene(feedbackLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(feedbackScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
            AnchorPane reservationLayout = loader.load();
            Scene scene = new Scene(reservationLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de Reservation.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}