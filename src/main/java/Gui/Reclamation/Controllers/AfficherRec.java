package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Services.Reclamation.Crud.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class AfficherRec {

    @FXML
    private TableView<Reclamation> tableView;

    @FXML
    private TableColumn<Reclamation, Integer> colId;

    @FXML
    private TableColumn<Reclamation, String> colTitre;

    @FXML
    private TableColumn<Reclamation, String> colDescription;

    @FXML
    private TableColumn<Reclamation, String> colType;

    @FXML
    private TableColumn<Reclamation, Void> colActions;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    public void initialize() {
        // Appliquer un style CSS
        tableView.getStyleClass().add("styled-table");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Ajouter la colonne avec les boutons Modifier et Supprimer
        addActionsColumn();

        // Charger les réclamations
        loadReclamations();
    }

    private void loadReclamations() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.RechercherRec());
        tableView.setItems(reclamations);
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");

            {
                btnModifier.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    modifierReclamation(reclamation);
                });

                btnSupprimer.setOnAction(event -> {
                    Reclamation reclamation = getTableView().getItems().get(getIndex());
                    supprimerReclamation(reclamation);
                });

                // Appliquer des styles
                btnModifier.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                btnSupprimer.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, btnModifier, btnSupprimer);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void modifierReclamation(Reclamation reclamation) {
        System.out.println("Modification de la réclamation : " + reclamation);
        // Implémentez ici la logique d'édition
    }

    private void supprimerReclamation(Reclamation reclamation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette réclamation ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                reclamationService.SupprimerRec(reclamation);
                loadReclamations();
            }
        });
    }

    @FXML
    private void goToHome(ActionEvent event) {
        System.out.println("Redirection vers l'accueil...");
    }

    @FXML
    private void goToReclamations(ActionEvent event) {
        System.out.println("Redirection vers les réclamations...");
    }

    @FXML
    private void logout(ActionEvent event) {
        System.out.println("Déconnexion...");
    }
}
