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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Services.Pack.Crud.EvenementService;
import Models.Pack.Evenement;

import java.io.IOException;
import java.util.List;

public class AfficheEvenement {

    @FXML
    private TextField searchField;

    @FXML
    private TilePane eventTilePane;

    private EvenementService evenementService = new EvenementService();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadPacks();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchEvenement(newValue));
    }

    private void loadPacks() {
        evenementList.clear();
        evenementList.addAll(evenementService.rechercher());
        updateEventTilePane();
    }

    private void handleEdit(Evenement evenement) {
        try {
            switchScene("/Pack/ModifierEvenement.fxml", evenement);
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
                loadPacks();
            }
        });
    }

    private void searchEvenement(String motCle) {
        List<Evenement> resultatRecherche = evenementService.RechercherEvenementParMotCle(motCle.toLowerCase());
        ObservableList<Evenement> data = FXCollections.observableArrayList(resultatRecherche);
        updateEventTilePane();
    }

    private void updateEventTilePane() {
        eventTilePane.getChildren().clear();
        for (Evenement event : evenementList) {
            eventTilePane.getChildren().add(createEventCard(event));
        }
    }

    private VBox createEventCard(Evenement event) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");

        // Hover effect using CSS classes
        card.setOnMouseEntered(e -> card.getStyleClass().add("event-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("event-card-hover"));

        // Event details (mirroring colType)
        Label typeLabel = new Label( event.getTypeEvenement());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        typeLabel.setWrapText(true);
        typeLabel.setMaxWidth(200);

        // Action buttons (mirroring colActions)
        Button editButton = new Button();
        Button deleteButton = new Button();
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

        // Add tooltips
        Tooltip editTooltip = new Tooltip("Modifier l'événement");
        Tooltip deleteTooltip = new Tooltip("Supprimer l'événement");
        Tooltip.install(editButton, editTooltip);
        Tooltip.install(deleteButton, deleteTooltip);

        editButton.setOnAction(e -> handleEdit(event));
        deleteButton.setOnAction(e -> handleDelete(event));

        HBox actionButtons = new HBox(10, editButton, deleteButton);
        actionButtons.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(typeLabel, actionButtons);
        return card;
    }

    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
    }

    @FXML
    private void addEvenement(ActionEvent event) throws IOException {
        switchScene("/Pack/AjouterEvenement.fxml", event);
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

    @FXML
    private void goToAffichePack(ActionEvent event) throws IOException {
        switchScene("/Pack/AffichePack.fxml", event);
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

    // Surcharge pour gérer le cas avec un Evenement spécifique (pour l'édition)
    private void switchScene(String fxmlFile, Evenement evenement) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent layout = loader.load();
        if (fxmlFile.equals("/Pack/ModifierEvenement.fxml")) {
            ModifierEvenement controller = loader.getController();
            controller.setEvenementToEdit(evenement);
        }
        Scene newScene = new Scene(layout);
        Stage currentStage = (Stage) eventTilePane.getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
}