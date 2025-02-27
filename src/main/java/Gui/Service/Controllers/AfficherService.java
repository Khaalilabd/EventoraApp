package Gui.Service.Controllers;

import Services.Service.Crud.ServiceService;
import Services.Service.Crud.PartenaireService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import Models.Service.Service;
import Models.Service.Partenaire;

import java.io.IOException;
import java.util.List;

public class AfficherService {

    @FXML
    private TableView<Service> tableView;

    @FXML
    private TableColumn<Service, Integer> idColumn;

    @FXML
    private TableColumn<Service, String> id_partenaireColumn;

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
    private TextField searchField;

    private final ServiceService serviceService;
    private final PartenaireService partenaireService;

    private ObservableList<Service> servicesList = FXCollections.observableArrayList();

    public AfficherService() {
        this.serviceService = new ServiceService();
        this.partenaireService = new PartenaireService();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        id_partenaireColumn.setCellValueFactory(cellData -> {
            int idPartenaire = cellData.getValue().getId_partenaire();
            Partenaire partenaire = getPartenaireById(idPartenaire);
            return new SimpleStringProperty(partenaire != null ? partenaire.getNom_partenaire() : "Inconnu");
        });

        // Mise à jour des colonnes avec surlignement
        setHighlightedColumn(titleColumn, Service::getTitre);
        setHighlightedColumn(locationColumn, service -> service.getLocation().getLabel());
        setHighlightedColumn(sponsorColumn, service -> service.getTypeService().getLabel());
        setHighlightedColumn(descriptionColumn, Service::getDescription);
        setHighlightedColumn(prixColumn, Service::getPrix);

        addActionsColumn();
        loadServices();

        FilteredList<Service> filteredData = new FilteredList<>(servicesList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return service.getTitre().toLowerCase().contains(lowerCaseFilter)
                        || service.getLocation().getLabel().toLowerCase().contains(lowerCaseFilter)
                        || service.getTypeService().getLabel().toLowerCase().contains(lowerCaseFilter)
                        || service.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
            tableView.refresh();
        });
        tableView.setItems(filteredData);
    }

    private void loadServices() {
        List<Service> services = serviceService.RechercherService();
        servicesList.setAll(services);
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox hBox = new HBox(10);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/ModifierService.fxml"));
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
        loadServices();
    }

    private Partenaire getPartenaireById(int idPartenaire) {
        for (Partenaire partenaire : partenaireService.RechercherPartenaire()) {
            if (partenaire.getId_partenaire() == idPartenaire) {
                return partenaire;
            }
        }
        return null;
    }

    private void setHighlightedColumn(TableColumn<Service, String> column, javafx.util.Callback<Service, String> mapper) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    String filter = searchField.getText();
                    TextFlow textFlow = createHighlightedText(item, filter);
                    setGraphic(textFlow);
                }
            }
        });
        column.setCellValueFactory(cellData -> new SimpleStringProperty(mapper.call(cellData.getValue())));
    }

    private TextFlow createHighlightedText(String text, String filter) {
        if (filter == null || filter.isEmpty()) {
            return new TextFlow(new Text(text));
        }

        String lowerText = text.toLowerCase();
        String lowerFilter = filter.toLowerCase();

        int index = lowerText.indexOf(lowerFilter);
        if (index == -1) {
            return new TextFlow(new Text(text));
        }

        Text before = new Text(text.substring(0, index));
        Text highlight = new Text(text.substring(index, index + filter.length()));
        Text after = new Text(text.substring(index + filter.length()));

        highlight.setStyle("-fx-fill: green;");
        return new TextFlow(before, highlight, after);
    }


    @FXML
    private void goToStatistiques(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Statistique.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Services");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addService(ActionEvent event) throws IOException {
        try {
            // Charger le fichier FXML pour la fenêtre "AjouterService"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/AjouterService.fxml"));
            Scene scene = new Scene(loader.load());

            // Récupérer la fenêtre (Stage) actuelle à partir de l'événement
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Fermer la fenêtre actuelle
            currentStage.close();

            // Créer une nouvelle fenêtre pour "AjouterService"
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Ajouter un service");
            newStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la fenêtre d'ajout de service : " + e.getMessage());
            e.printStackTrace();
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
    private void refreshList() {
        loadServices(); // Recharge la liste des services
    }
    @FXML
    private void goToPartenaire(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();
        Scene scene = new Scene(partenaireLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
