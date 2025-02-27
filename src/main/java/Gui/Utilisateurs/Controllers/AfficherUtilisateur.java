package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherUtilisateur {

    @FXML private TableView<Utilisateurs> tableView;
    @FXML private TableColumn<Utilisateurs, Integer> colId;
    @FXML private TableColumn<Utilisateurs, String> colNom;
    @FXML private TableColumn<Utilisateurs, String> colPrenom;
    @FXML private TableColumn<Utilisateurs, String> colEmail;
    @FXML private TableColumn<Utilisateurs, String> colCin;
    @FXML private TableColumn<Utilisateurs, String> colAdresse;
    @FXML private TableColumn<Utilisateurs, String> colNumTel;
    @FXML private TableColumn<Utilisateurs, String> colRole;
    @FXML private Button supprimerButton;
    @FXML private Button modifierButton;

    private final MembresService membresService = new MembresService();
    private ObservableList<Utilisateurs> utilisateurs;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        chargerUtilisateurs();

        supprimerButton.setDisable(true);
        modifierButton.setDisable(true);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            supprimerButton.setDisable(newVal == null);
            modifierButton.setDisable(newVal == null);
        });
    }

    private void chargerUtilisateurs() {
        List<Utilisateurs> listeUtilisateurs = membresService.RechercherMem();
        utilisateurs = FXCollections.observableArrayList(listeUtilisateurs);
        tableView.setItems(utilisateurs);
    }

    @FXML
    private void supprimerUtilisateur() {
        Utilisateurs utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
            alert.setContentText(utilisateurSelectionne.getNom() + " " + utilisateurSelectionne.getPrenom());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    membresService.SupprimerMem(utilisateurSelectionne);
                    chargerUtilisateurs();
                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Suppression réussie");
                    confirmation.setContentText("L'utilisateur a été supprimé avec succès.");
                    confirmation.showAndWait();
                }
            });
        }
    }

    @FXML
    private void modifierUtilisateur() {
        Utilisateurs utilisateurSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (utilisateurSelectionne != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/ModifierUtilisateur.fxml"));
                Parent root = loader.load();
                ModifierUtilisateur controller = loader.getController();
                controller.setUtilisateur(utilisateurSelectionne);
                Stage stage = new Stage();
                stage.setTitle("Modifier Utilisateur");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.setOnHidden(event -> chargerUtilisateurs());
                stage.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Erreur lors du chargement de la fenêtre de modification.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);  
            stage.show();

        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }
    // Afficher une alerte en cas d'erreur
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
