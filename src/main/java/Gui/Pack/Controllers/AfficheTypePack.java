package Gui.Pack.Controllers;

import Models.Pack.Pack;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import Services.Pack.Crud.TypePackService;
import Models.Pack.TypePack;

public class AfficheTypePack {

    @FXML
    private TableView<TypePack> tableView;
    @FXML
    private TableColumn<TypePack, Integer> colId;
    @FXML
    private TableColumn<TypePack, String> colType;

    @FXML
    private TableColumn<TypePack, String> colActions;
    @FXML
    private TextField searchField;

    private TypePackService typePackService = new TypePackService();
    private ObservableList<TypePack> typePackList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colActions.setCellFactory(createActionButtons());

        addActionsColumn();
        loadPacks();

        // Recherche dynamique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTypePack(newValue);
        });
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
        colActions.setCellFactory(new Callback<TableColumn<TypePack, String>, TableCell<TypePack, String>>() {
            @Override
            public TableCell<TypePack, String> call(TableColumn<TypePack, String> param) {
                return new TableCell<TypePack, String>() {
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
                };
            }
        });
    }

    private void loadPacks() {
        typePackList.clear();
        typePackList.addAll(typePackService.rechercher());
        tableView.setItems(typePackList);
    }

    private Callback<TableColumn<TypePack, String>, TableCell<TypePack, String>> createActionButtons() {
        return param -> new TableCell<>() {
            final Button editButton = new Button("Modifier");
            final Button deleteButton = new Button("Supprimer");
            final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    TypePack typePack = getTableView().getItems().get(getIndex());
                    // Handle editing logic
                });

                deleteButton.setOnAction(event -> {
                    TypePack typePack = getTableView().getItems().get(getIndex());
                    handleDelete(typePack); // Handle deletion
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        };
    }

    private void handleEdit(TypePack typePack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierTypePack.fxml"));
            AnchorPane modifRecLayout = loader.load();

            ModifierTypePack controller = loader.getController();
            controller.setTypePackToEdit(typePack);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(TypePack typePack) {
        System.out.println("Supprimer le type avec ID : " + typePack.getId());
        typePackService.supprimer(typePack);
        loadPacks();
    }

    // Recherche dynamique dès que l'utilisateur tape
    private void searchTypePack(String motCle) {
        List<TypePack> resultatRecherche = typePackService.RechercherTypeParMotCle(motCle.toLowerCase());

        ObservableList<TypePack> data = FXCollections.observableArrayList(resultatRecherche);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void tableRowFactory(TableView<TypePack> tableView) { // Corrected TableView type
        tableView.setRowFactory(tv -> {
            TableRow<TypePack> row = new TableRow<>();
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
    private void addTypePack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterTypePack.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }
    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }

}
