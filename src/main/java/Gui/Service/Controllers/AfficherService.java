package Gui.Service.Controllers;

import Services.Service.Crud.ServiceService;
import Services.Service.Crud.PartenaireService;
import Utils.SessionManager;
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

    @FXML private TableView<Service> tableView;
    @FXML private TableColumn<Service, Integer> idColumn;
    @FXML private TableColumn<Service, String> id_partenaireColumn; // Vérifiez cet ID
    @FXML private TableColumn<Service, String> titleColumn;
    @FXML private TableColumn<Service, String> locationColumn;
    @FXML private TableColumn<Service, String> sponsorColumn;
    @FXML private TableColumn<Service, String> descriptionColumn;
    @FXML private TableColumn<Service, String> prixColumn;
    @FXML private TableColumn<Service, String> colActions;
    @FXML private TextField searchField;

    private final ServiceService serviceService;
    private final PartenaireService partenaireService;
    private ObservableList<Service> servicesList = FXCollections.observableArrayList();

    public AfficherService() {
        this.serviceService = new ServiceService();
        this.partenaireService = new PartenaireService();
    }

    @FXML
    public void initialize() {
        // Vérification des injections
        if (idColumn == null || id_partenaireColumn == null || titleColumn == null || locationColumn == null ||
                sponsorColumn == null || descriptionColumn == null || prixColumn == null || colActions == null ||
                tableView == null || searchField == null) {
            System.err.println("Erreur : Une ou plusieurs colonnes ou composants ne sont pas injectés correctement.");
            return;
        }

        // Configuration des colonnes
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setStyle("-fx-alignment: CENTER;");

        id_partenaireColumn.setCellValueFactory(cellData -> {
            int idPartenaire = cellData.getValue().getId_partenaire();
            Partenaire partenaire = getPartenaireById(idPartenaire);
            return new SimpleStringProperty(partenaire != null ? partenaire.getNom_partenaire() : "Inconnu");
        });
        id_partenaireColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        setHighlightedColumn(titleColumn, Service::getTitre, "-fx-alignment: CENTER-LEFT;");
        setHighlightedColumn(locationColumn, service -> service.getLocation().getLabel(), "-fx-alignment: CENTER-LEFT;");
        setHighlightedColumn(sponsorColumn, service -> service.getTypeService().getLabel(), "-fx-alignment: CENTER-LEFT;");
        setHighlightedColumn(descriptionColumn, Service::getDescription, "-fx-alignment: CENTER-LEFT; -fx-padding: 5px;");
        setHighlightedColumn(prixColumn, Service::getPrix, "-fx-alignment: CENTER-RIGHT;");

        // Ajout de la colonne des actions avec icônes
        addActionsColumn();
        loadServices();

        // Configuration de la recherche
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

        // Placeholder pour tableau vide
        tableView.setPlaceholder(new Label("Aucun service trouvé."));

        // Ajustement de la hauteur des lignes
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Service item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle("-fx-pref-height: 35px;"); // Hauteur compacte des lignes
                }
            }
        });
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
                // Icônes agrandies
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));
                if (editIcon == null || deleteIcon == null) {
                    System.err.println("Erreur : Icônes non trouvées dans /Images/");
                } else {
                    ImageView editImageView = new ImageView(editIcon);
                    ImageView deleteImageView = new ImageView(deleteIcon);
                    editImageView.setFitHeight(20);
                    editImageView.setFitWidth(20);
                    deleteImageView.setFitHeight(20);
                    deleteImageView.setFitWidth(20);
                    editButton.setGraphic(editImageView);
                    deleteButton.setGraphic(deleteImageView);
                }
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
            AnchorPane layout = loader.load();
            ModifierService controller = loader.getController();
            controller.setServiceToEdit(service);

            Scene scene = new Scene(layout);
            Stage stage = new Stage(); // Nouvelle fenêtre
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : /Service/ModifierService.fxml");
            e.printStackTrace();
        }
    }

    private void handleDelete(Service service) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer la suppression");
        confirm.setHeaderText("Voulez-vous vraiment supprimer ce service ?");
        confirm.setContentText("Cette action est irréversible.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                serviceService.SupprimerService(service);
                loadServices();
            }
        });
    }

    private Partenaire getPartenaireById(int idPartenaire) {
        for (Partenaire partenaire : partenaireService.RechercherPartenaire()) {
            if (partenaire.getId_partenaire() == idPartenaire) {
                return partenaire;
            }
        }
        return null;
    }

    private void setHighlightedColumn(TableColumn<Service, String> column, javafx.util.Callback<Service, String> mapper, String style) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setText(item);
                    setStyle(style); // Applique le style personnalisé (alignement, padding)
                    String filter = searchField.getText();
                    if (filter != null && !filter.isEmpty()) {
                        TextFlow textFlow = createHighlightedText(item, filter);
                        setGraphic(textFlow);
                        setText(null); // Remplace le texte brut par le TextFlow
                    }
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

        highlight.setStyle("-fx-fill: #3B82F6; -fx-font-weight: bold;");
        return new TextFlow(before, highlight, after);
    }
    @FXML
    private void deconnexion(ActionEvent event) {
        // Effacer la session
        SessionManager.getInstance().clearSession();
        // Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }

    @FXML
    private void goToParams(ActionEvent event) {
        switchScene(event, "/Utilisateurs/Parametres.fxml");
    }

    @FXML
    private void goToUser(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    @FXML
    private void goToStatistiques(ActionEvent event) {
        openNewWindow(event, "/Service/Statistique.fxml");
    }

    @FXML
    private void addService(ActionEvent event) {
        switchScene(event, "/Service/AjouterService.fxml"); // Conserver le comportement existant pour ajouter
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void refreshList() {
        loadServices();
    }

    @FXML
    private void goToPartenaire(ActionEvent event) {
        switchScene(event, "/Service/Partenaire.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void openNewWindow(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage newStage = new Stage(); // Nouvelle fenêtre
            newStage.setScene(scene);
            newStage.setTitle("Statistiques"); // Titre facultatif
            newStage.setMaximized(false); // Ou ajustez selon vos besoins
            newStage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}