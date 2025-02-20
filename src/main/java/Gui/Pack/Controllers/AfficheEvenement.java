package Gui.Pack.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Services.Pack.Crud.EvenementService;
import Models.Pack.Evenement;

import java.io.IOException;
import java.util.List;

public class AfficheEvenement {

    @FXML
    private TableView<Evenement> tableView;
    //@FXML
    //private TableColumn<Evenement, Integer> colId;
    @FXML
    private TableColumn<Evenement, String> colType;
    @FXML
    private TableColumn<Evenement, String> colActions;
    @FXML
    private TextField searchField;

    private EvenementService evenementService = new EvenementService();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeEvenement"));
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox hBox = new HBox(10);

            {
                // Ajouter les icônes et les boutons pour chaque action
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));
                ImageView editImageView = new ImageView(editIcon);
                ImageView deleteImageView = new ImageView(deleteIcon);
                editImageView.setFitHeight(20);
                editImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteImageView.setFitWidth(20);
                editButton.setGraphic(editImageView);
                deleteButton.setGraphic(deleteImageView);
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                // Ajouter les boutons dans le HBox
                hBox.getChildren().addAll(editButton, deleteButton);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Ajouter les actions des boutons pour chaque ligne
                    editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                    deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    setGraphic(hBox);
                }
            }
        });

        loadPacks();

        // Recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchEvenement(newValue);
        });
    }

    private void loadPacks() {
        evenementList.clear();
        evenementList.addAll(evenementService.rechercher());
        tableView.setItems(evenementList);
    }

    private void handleEdit(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
            AnchorPane modifRecLayout = loader.load();

            ModifierEvenement controller = loader.getController();
            controller.setEvenementToEdit(evenement);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Evenement evenement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression d'un événement");
        alert.setContentText("Voulez-vous vraiment supprimer le type d'événement : " + evenement.getTypeEvenement() + " ?");

        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                evenementService.supprimer(evenement);
                loadPacks(); // Rafraîchir la liste après suppression
            }
        });
    }

    // Recherche dynamique dès que l'utilisateur tape
    private void searchEvenement(String motCle) {
        List<Evenement> resultatRecherche = evenementService.RechercherEvenementParMotCle(motCle.toLowerCase());

        ObservableList<Evenement> data = FXCollections.observableArrayList(resultatRecherche);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void tableRowFactory(TableView<Evenement> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Evenement> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addEvenement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
