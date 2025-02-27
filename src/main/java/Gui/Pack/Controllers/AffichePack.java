package Gui.Pack.Controllers;

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
import java.util.stream.Collectors;
import Services.Pack.Crud.PackService;
import Models.Pack.Pack;
import Models.Service.Service;

public class AffichePack {

    @FXML
    private TableView<Pack> tableView;
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
    private TableColumn<Pack, String> colServices; // Updated to display List<Service>
    @FXML
    private TableColumn<Pack, String> colActions;
    @FXML
    private TextField searchField;

    private PackService packService = new PackService();
    private ObservableList<Pack> packList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set cell value factories
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colLocation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation().getLabel()));
        colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().getTypeEvenement()));
        colNbrGuests.setCellValueFactory(new PropertyValueFactory<>("nbrGuests"));

        // Custom cell value factory for colServices to display List<Service> as a comma-separated string
        colServices.setCellValueFactory(cellData -> {
            List<Service> services = cellData.getValue().getNomServices();
            String servicesString = services.stream()
                    .map(Service::getTitre)
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(servicesString);
        });

        // Actions column (Edit & Delete buttons with icons)
        colActions.setCellFactory(createActionButtons());

        // Load packs into the table
        loadPacks();

        // Dynamic search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPack(newValue);
        });

        // Row hover effect
        tableRowFactory(tableView);
    }

    private void loadPacks() {
        packList.clear();
        packList.addAll(packService.rechercher());
        tableView.setItems(packList);
    }

    private Callback<TableColumn<Pack, String>, TableCell<Pack, String>> createActionButtons() {
        return param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox buttons = new HBox(10, editButton, deleteButton);

            {
                // Add icons to buttons
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

                // Styling for the buttons
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                // Set actions for buttons
                editButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    handleEdit(pack);
                });

                deleteButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    handleDelete(pack);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression d'un pack");
        alert.setContentText("Voulez-vous vraiment supprimer le pack : " + pack.getNomPack() + " ?");

        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                packService.supprimer(pack);
                loadPacks(); // Refresh the list after deletion
            }
        });
    }

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
    private void goToEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AfficheEvenement.fxml"));
            AnchorPane packLayout = loader.load();
            Scene scene = new Scene(packLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene reclamationScene = new Scene(reclamationLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(reclamationScene);
        currentStage.show();
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
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }
}