package Gui.Service.Controllers;

import Services.Service.Crud.ServiceService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Models.Service.Service;
import java.io.IOException;
import java.util.List;

public class AfficherService {

    @FXML
    private TableView<Service> tableView;

    @FXML
    private TableColumn<Service, Integer> idColumn;

    @FXML
    private TableColumn<Service, String> titleColumn;

    @FXML
    private TableColumn<Service, String> locationColumn;

    @FXML
    private TableColumn<Service, String> sponsorColumn;

    @FXML
    private TableColumn<Service, String> descriptionColumn;

    @FXML
    private TableColumn<Service, String> prixColumn;

    @FXML
    private TableColumn<Service, String> colActions;

    @FXML
    private TextField searchField;  // The search field for filtering services

    private final ServiceService serviceService;

    private ObservableList<Service> servicesList = FXCollections.observableArrayList();

    public AfficherService() {
        this.serviceService = new ServiceService();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation().getLabel()));
        sponsorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSponsors().getLabel()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        prixColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrix()));
        addActionsColumn();
        loadServices();

        // Create a FilteredList to filter services based on the search input
        FilteredList<Service> filteredData = new FilteredList<>(servicesList, p -> true);

        // Add a listener to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all services if the search field is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return service.getTitre().toLowerCase().contains(lowerCaseFilter) ||
                        service.getLocation().getLabel().toLowerCase().contains(lowerCaseFilter) ||
                        service.getSponsors().getLabel().toLowerCase().contains(lowerCaseFilter) ||
                        service.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Ensure that the TableView displays the filtered data
        tableView.setItems(filteredData);
    }

    private void loadServices() {
        List<Service> services = serviceService.RechercherService();
        servicesList.setAll(services); // Update the list of services
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox hBox = new HBox(10);

            {
                // Loading icons for buttons
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));

                // Icons for buttons
                ImageView editImageView = new ImageView(editIcon);
                ImageView deleteImageView = new ImageView(deleteIcon);

                editImageView.setFitHeight(20);
                editImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteImageView.setFitWidth(20);

                // Add icons to buttons
                editButton.setGraphic(editImageView);
                deleteButton.setGraphic(deleteImageView);

                // Button styles
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                hBox.getChildren().addAll(editButton, deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                    deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    setGraphic(hBox);
                }
            }
        });
    }

    private void handleEdit(Service service) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierService.fxml"));
            AnchorPane modifRecLayout = loader.load();

            ModifierService controller = loader.getController();
            controller.setServiceToEdit(service);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Service service) {
        System.out.println("Supprimer le service avec ID : " + service.getId());
        serviceService.SupprimerService(service);
        loadServices();  // Reload the list of services after deletion
    }

    @FXML
    private void addService() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter un service");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadServices(); // Reload the services list
    }

    @FXML
    private void tableRowFactory(TableView<Service> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Service> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
