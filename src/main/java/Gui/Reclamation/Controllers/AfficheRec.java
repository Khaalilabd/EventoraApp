package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.ReclamationService;
import Models.Reclamation.Reclamation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.control.TableCell;

import java.util.List;

public class AfficheRec {

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
    private TableColumn<Reclamation, String> colActions;

    private ReclamationService reclamationService;

    public AfficheRec() {
        this.reclamationService = new ReclamationService();
    }

    @FXML
    public void initialize() {
        // Définir les sources des données pour chaque colonne
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getLabel()));

        // Ajouter la colonne avec les boutons Modifier et Supprimer
        addActionsColumn();

        // Charger les réclamations
        loadReclamations();
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.RechercherRec();
        tableView.getItems().setAll(reclamations);
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(new Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>>() {
            @Override
            public TableCell<Reclamation, String> call(TableColumn<Reclamation, String> param) {
                return new TableCell<Reclamation, String>() {
                    final Button editButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");
                    final HBox hBox = new HBox(10);

                    {
                        hBox.getChildren().addAll(editButton, deleteButton);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                            deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));

                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void handleEdit(Reclamation reclamation) {
        System.out.println("Modifier la réclamation avec ID : " + reclamation.getId());
    }

    private void handleDelete(Reclamation reclamation) {
        System.out.println("Supprimer la réclamation avec ID : " + reclamation.getId());
        reclamationService.SupprimerRec(reclamation);
        loadReclamations();
    }

    @FXML
    private void goToHome() {
        System.out.println("Navigation vers la page d'accueil");
    }

    @FXML
    private void goToReclamations() {
        System.out.println("Navigation vers les réclamations");
    }

    @FXML
    private void logout() {
        System.out.println("Déconnexion");
    }

    @FXML
    private void addReclamation() {
        System.out.println("Ajout d'une réclamation");
    }
}


