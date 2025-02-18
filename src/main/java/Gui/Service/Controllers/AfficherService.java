package Gui.Service.Controllers;

import Services.Service.Crud.ServiceService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

    private final ServiceService serviceService;

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
    }

    private void loadServices() {
        List<Service> services = serviceService.RechercherService();
        tableView.getItems().setAll(services);
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox hBox = new HBox(10);

            {
                // Chargement des icônes dans les boutons
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));

                // Icônes pour les boutons
                ImageView editImageView = new ImageView(editIcon);
                ImageView deleteImageView = new ImageView(deleteIcon);

                editImageView.setFitHeight(20);
                editImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteImageView.setFitWidth(20);

                // Ajout des icônes dans les boutons
                editButton.setGraphic(editImageView);
                deleteButton.setGraphic(deleteImageView);

                // Style des boutons
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierRec.fxml"));
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
        loadServices();  // Recharger la liste des services après suppression
    }

    @FXML
    private void addService() throws IOException {
        // Charger l'interface AjouterService.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterService.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter un service");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadServices(); // Recharger les services
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
        // Charger l'interface Reclamation.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
