package Gui.Service.Controllers;

import Services.Service.Crud.PartenaireService;
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
import Models.Service.Partenaire;

import java.io.IOException;
import java.util.List;

public class AfficherPartenaire {

    @FXML
    private TableView<Partenaire> tableView;

    @FXML
    private TableColumn<Partenaire, Integer> idColumn;

    @FXML
    private TableColumn<Partenaire, String> nomColumn;

    @FXML
    private TableColumn<Partenaire, String> emailColumn;

    @FXML
    private TableColumn<Partenaire, String> adresseColumn;

    @FXML
    private TableColumn<Partenaire, String> site_webColumn;

    @FXML
    private TableColumn<Partenaire, String> montantColumn;

    @FXML
    private TableColumn<Partenaire, String> typeColumn;

    @FXML
    private TableColumn<Partenaire, String> colActions;

    private final PartenaireService partenaireService;

    public AfficherPartenaire() {
        this.partenaireService = new PartenaireService();
    }

    public void initialize() {
        configureColumns();
        addActionsColumn();
        loadPartenaires();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_sponsors()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_sponsors()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail_sponsors()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse_sponsors()));
        site_webColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSite_web()));
        montantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMontant_sponsors()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_sponsors().getLabel()));
    }

    private void loadPartenaires() {
        List<Partenaire> partenaires = partenaireService.RechercherPartenaire();
        tableView.getItems().setAll(partenaires);
    }

    private void addActionsColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            final Button editButton = createActionButton("/Images/modif.png");
            final Button deleteButton = createActionButton("/Images/supp.png");
            final HBox hBox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private Button createActionButton(String iconPath) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        button.setGraphic(imageView);
        button.getStyleClass().add("table-button");
        return button;
    }

    private void handleEdit(Partenaire partenaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPartenaire.fxml"));
            AnchorPane modifLayout = loader.load();

            ModifierPartenaire controller = loader.getController();
            controller.setPartenaireToEdit(partenaire);

            tableView.getScene().setRoot(modifLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Partenaire partenaire) {
        partenaireService.SupprimerPartenaire(partenaire);
        loadPartenaires();
    }

    @FXML
    private void addPartenaire() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPartenaire.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter un partenaire");
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadPartenaires();
    }

    @FXML
    private void tableRowFactory() {
        tableView.setRowFactory(tv -> {
            TableRow<Partenaire> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
    }

    @FXML
    private void goToPartenaire(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();
        Scene scene = new Scene(partenaireLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
