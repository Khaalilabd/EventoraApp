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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import Services.Pack.Crud.PackService;
import Models.Pack.Pack;
import Models.Pack.Evenement;
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
    private TableColumn<Pack, String> colServices;
    @FXML
    private TableColumn<Pack, String> colActions;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortByCombo;
    @FXML
    private ToggleButton sortOrderToggle;

    private PackService packService = new PackService();
    private ObservableList<Pack> packList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colLocation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLocation().getLabel()));
        colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().getTypeEvenement()));
        colNbrGuests.setCellValueFactory(new PropertyValueFactory<>("nbrGuests"));
        colServices.setCellValueFactory(cellData -> {
            List<Service> services = cellData.getValue().getNomServices();
            String servicesString = services.stream()
                    .map(Service::getTitre)
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(servicesString);
        });
        colActions.setCellFactory(createActionButtons());

        // Initialize sorting options
        sortByCombo.getItems().addAll("Nom Pack", "Prix", "Invités");
        sortByCombo.setValue("Nom Pack"); // Default sort by nomPack
        sortOrderToggle.setSelected(true); // Default to ascending

        // Load packs into the table
        loadPacks();

        // Listeners for dynamic search and sorting
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchPack(newValue));
        sortByCombo.valueProperty().addListener((obs, old, newVal) -> applySort());
        sortOrderToggle.selectedProperty().addListener((obs, old, newVal) -> {
            sortOrderToggle.setText(newVal ? "⬆ Ascendant" : "⬇ Descendant");
            applySort();
        });

        // Row hover effect
        tableRowFactory(tableView);
    }

    private void loadPacks() {
        packList.clear();
        packList.addAll(packService.rechercher());
        applySort();
    }

    private void searchPack(String motCle) {
        List<Pack> resultatRecherche = packService.RechercherPackParMotCle(motCle.toLowerCase());
        packList.clear();
        packList.addAll(resultatRecherche);
        applySort();
    }

    private void applySort() {
        String sortBy = sortByCombo.getValue();
        boolean ascending = sortOrderToggle.isSelected();
        Comparator<Pack> comparator = null;
        switch (sortBy) {
            case "Nom Pack":
                comparator = Comparator.comparing(Pack::getNomPack);
                break;
            case "Prix":
                comparator = Comparator.comparingDouble(Pack::getPrix);
                break;
            case "Invités":
                comparator = Comparator.comparingInt(Pack::getNbrGuests);
                break;
            default:
                comparator = Comparator.comparing(Pack::getNomPack);
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        packList.sort(comparator);
        tableView.setItems(packList);
    }

    private Callback<TableColumn<Pack, String>, TableCell<Pack, String>> createActionButtons() {
        return param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox buttons = new HBox(10, editButton, deleteButton);

            {
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
            switchScene("/Pack/ModifierPack.fxml", pack);
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
                loadPacks();
            }
        });
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        sortByCombo.setValue("Nom Pack");
        sortOrderToggle.setSelected(true);
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
        switchScene("/Pack/Packs.fxml", event);
    }

    @FXML
    private void addPack(ActionEvent event) throws IOException {
        switchScene("/Pack/AjouterPack.fxml", event);
    }

    @FXML
    private void goToEvenement(ActionEvent event) throws IOException {
        switchScene("/Pack/AfficheEvenement.fxml", event);
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Reclamation.fxml", event);
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        switchScene("/Reservation/Reservation.fxml", event);
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        switchScene("/Service/Service.fxml", event);
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Feedback.fxml", event);
    }

    // Méthode générique pour changer de scène avec un événement
    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent layout = loader.load();
        Scene newScene = new Scene(layout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }

    // Surcharge pour gérer le cas avec un Pack spécifique (pour l'édition)
    private void switchScene(String fxmlFile, Pack pack) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent layout = loader.load();
        if (fxmlFile.equals("/Pack/ModifierPack.fxml")) {
            ModifierPack controller = loader.getController();
            controller.setPackToEdit(pack);
        }
        Scene newScene = new Scene(layout);
        Stage currentStage = (Stage) tableView.getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
}