package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.MembresService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AfficherUtilisateur {

    @FXML
    private TableView<Utilisateurs> tableView;
    @FXML
    private TableColumn<Utilisateurs, Integer> colId;
    @FXML
    private TableColumn<Utilisateurs, String> colNom;
    @FXML
    private TableColumn<Utilisateurs, String> colPrenom;
    @FXML
    private TableColumn<Utilisateurs, String> colEmail;
    @FXML
    private TableColumn<Utilisateurs, String> colCin;
    @FXML
    private TableColumn<Utilisateurs, String> colAdresse;
    @FXML
    private TableColumn<Utilisateurs, String> colNumTel;
    @FXML
    private TableColumn<Utilisateurs, String> colRole;
    @FXML
    private Button supprimerButton; // Bouton pour supprimer

    private final MembresService membresService = new MembresService();
    private ObservableList<Utilisateurs> utilisateurs;

    @FXML
    public void initialize() {
        // Initialisation des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Chargement des données
        chargerUtilisateurs();

        // Gestion du bouton supprimer (désactivé au début)
        supprimerButton.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            supprimerButton.setDisable(newVal == null); // Activer si une ligne est sélectionnée
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
                    chargerUtilisateurs(); // Mettre à jour le tableau
                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setTitle("Suppression réussie");
                    confirmation.setContentText("L'utilisateur a été supprimé avec succès.");
                    confirmation.showAndWait();

                }
            });


        }
    }
}