package Gui.Pack.Controllers;

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
import Services.Pack.Crud.PackService;
import Models.Pack.Pack;

public class AffichePack {

    @FXML
    private TableView<Pack> tableView;
    @FXML
    private TableColumn<Pack, Integer> colId;
    @FXML
    private TableColumn<Pack, String> colNom;
    @FXML
    private TableColumn<Pack, String> colDescription;
    @FXML
    private TableColumn<Pack, Double> colPrix;
    @FXML
    private TableColumn<Pack, String> colLocation;
    @FXML
    private TableColumn<Pack, String> colType;
    @FXML
    private TableColumn<Pack, Integer> colNbrGuests;
    @FXML
    private TableColumn<Pack, String> colServices;
    @FXML
    private TableColumn<Pack, String> colActions;
    @FXML
    private TextField searchField;

    private PackService packService = new PackService();
    private ObservableList<Pack> packList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colNbrGuests.setCellValueFactory(new PropertyValueFactory<>("nbrGuests"));
        colServices.setCellValueFactory(new PropertyValueFactory<>("nomService"));
        colActions.setCellFactory(createActionButtons());

        addActionsColumn();
        loadPacks();

        // Recherche dynamique
      searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPack(newValue);
       });
    }


    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
        colActions.setCellFactory(new Callback<TableColumn<Pack, String>, TableCell<Pack, String>>() {
            @Override
            public TableCell<Pack, String> call(TableColumn<Pack, String> param) {
                return new TableCell<Pack, String>() {
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
        packList.clear();
        packList.addAll(packService.rechercher());
        tableView.setItems(packList);
    }

    private Callback<TableColumn<Pack, String>, TableCell<Pack, String>> createActionButtons() {
        return param -> new TableCell<>() {
            final Button editButton = new Button("Modifier");
            final Button deleteButton = new Button("Supprimer");
            final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    //showEditDialog(pack);
                });

                deleteButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    //confirmAndDelete(pack);
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

    private void handleEdit(Pack pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/ModifierPack.fxml"));
            AnchorPane modifRecLayout = loader.load();

            ModifierPack controller = loader.getController();
            controller.setPackToEdit(pack);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Pack pack) {
        System.out.println("Supprimer le pack avec ID : " + pack.getId());
        packService.supprimer(pack);
        loadPacks();
    }

    // Recherche dynamique dès que l'utilisateur tape
   private void searchPack(String motCle) {
        List<Pack> resultatRecherche = packService.RechercherPackParMotCle(motCle.toLowerCase());

        ObservableList<Pack> data = FXCollections.observableArrayList(resultatRecherche);
        tableView.setItems(data);
    }



    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void tableRowFactory(TableView<Pack> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Pack> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
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

    @FXML
    private void addPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AjouterPack.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToPackType(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AfficheTypePack.fxml"));

            if (loader.getLocation() == null) {
                System.out.println("FXML file not found! Ensure the path is correct.");
                return;
            }

            AnchorPane packLayout = loader.load();
            Scene scene = new Scene(packLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
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
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Services/Service.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }

}
