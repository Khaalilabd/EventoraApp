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
import javafx.scene.layout.AnchorPane; // Ajouté pour le style
import javafx.stage.Stage;
import Services.Pack.Crud.PackService;
import Models.Pack.Pack;
import Models.Pack.Evenement;
import Models.Service.Service;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AffichePack {

    @FXML
    private TilePane packTilePane;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Evenement> EventFilter;
    @FXML
    private ComboBox<String> sortByCombo;
    @FXML
    private ToggleButton sortOrderToggle;

    private PackService packService = new PackService();
    private ObservableList<Pack> packList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        sortByCombo.getItems().addAll("Nom Pack", "Prix", "Invités");
        sortByCombo.setValue("Nom Pack");
        sortOrderToggle.setSelected(true);
        sortOrderToggle.setText("⬆ Ascendant");

        List<Evenement> evenements = packService.getAllEvenements();
        EventFilter.getItems().addAll(evenements);
        EventFilter.setConverter(new javafx.util.StringConverter<Evenement>() {
            @Override
            public String toString(Evenement evenement) {
                return evenement != null ? evenement.getTypeEvenement() : "";
            }

            @Override
            public Evenement fromString(String string) {
                return null;
            }
        });

        loadPacks();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchPack(newValue));
        EventFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterByEvent(newVal));
        sortByCombo.valueProperty().addListener((obs, old, newVal) -> applySort());
        sortOrderToggle.selectedProperty().addListener((obs, old, newVal) -> {
            sortOrderToggle.setText(newVal ? "⬆ Ascendant" : "⬇ Descendant");
            applySort();
        });
    }

    private void loadPacks() {
        packList.clear();
        packList.addAll(packService.rechercher());
        updatePackTilePane();
    }

    private void searchPack(String motCle) {
        Evenement selectedEvent = EventFilter.getValue();
        String typeEvenement = (selectedEvent != null) ? selectedEvent.getTypeEvenement() : null;
        List<Pack> resultatRecherche = packService.RechercherPackParEvenement(motCle.toLowerCase(), typeEvenement);
        packList.clear();
        packList.addAll(resultatRecherche);
        applySort();
    }

    private void filterByEvent(Evenement event) {
        String motCle = searchField.getText().toLowerCase();
        String typeEvenement = (event != null) ? event.getTypeEvenement() : null;
        List<Pack> resultatRecherche = packService.RechercherPackParEvenement(motCle, typeEvenement);
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
        updatePackTilePane();
    }

    private void updatePackTilePane() {
        packTilePane.getChildren().clear();
        for (Pack pack : packList) {
            packTilePane.getChildren().add(createPackCard(pack));
        }
    }

    private VBox createPackCard(Pack pack) {
        VBox card = new VBox(10);
        card.getStyleClass().add("pack-card");

        card.setOnMouseEntered(event -> card.getStyleClass().add("pack-card-hover"));
        card.setOnMouseExited(event -> card.getStyleClass().remove("pack-card-hover"));
        card.setOnMouseClicked(event -> switchScene(null, "/Pack/PackDetails.fxml", pack));

        ImageView imageView = new ImageView();
        String imagePath = pack.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File("src/main/resources" + imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString(), 150, 150, true, true);
                    imageView.setImage(image);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image pour " + pack.getNomPack() + ": " + e.getMessage());
            }
        }
        imageView.setStyle("-fx-alignment: center;");

        VBox detailsBox = new VBox(5);
        Label nomLabel = new Label("Nom: " + pack.getNomPack());
        nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        Label descriptionLabel = new Label("Description: " + pack.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(200);
        Label prixLabel = new Label("Prix: " + pack.getPrix() + " TND");
        Label locationLabel = new Label("Localisation: " + pack.getLocation().getLabel());
        Label typeLabel = new Label("Type: " + pack.getType().getTypeEvenement());
        Label guestsLabel = new Label("Invités: " + pack.getNbrGuests());
        Label servicesLabel = new Label("Services: " + pack.getNomServices().stream().map(Service::getTitre).collect(Collectors.joining(", ")));
        servicesLabel.setWrapText(true);
        servicesLabel.setMaxWidth(200);

        detailsBox.getChildren().addAll(nomLabel, descriptionLabel, prixLabel, locationLabel, typeLabel, guestsLabel, servicesLabel);

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

        Tooltip editTooltip = new Tooltip("Modifier le pack");
        Tooltip deleteTooltip = new Tooltip("Supprimer le pack");
        Tooltip.install(editButton, editTooltip);
        Tooltip.install(deleteButton, deleteTooltip);

        editButton.setOnAction(event -> handleEdit(pack));
        deleteButton.setOnAction(event -> handleDelete(pack));

        HBox actionButtons = new HBox(10, editButton, deleteButton);
        actionButtons.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(imageView, detailsBox, actionButtons);
        return card;
    }

    private void handleEdit(Pack pack) {
        switchScene(null, "/Pack/ModifierPack.fxml", pack);
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
        EventFilter.setValue(null);
        sortByCombo.setValue("Nom Pack");
        sortOrderToggle.setSelected(true);
        loadPacks();
    }

    @FXML
    private void addPack(ActionEvent event) {
        switchScene(event, "/Pack/AjouterPack.fxml", null);
    }

    @FXML
    private void goToEvenement(ActionEvent event) {
        switchScene(event, "/Pack/AfficheEvenement.fxml", null);
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml", null);
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml", null);
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml", null);
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml", null);
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml", null);
    }

    // Méthode switchScene modifiée selon le style mémorisé, avec support pour Pack
    private void switchScene(ActionEvent event, String fxmlFile, Pack pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane layout = loader.load();

            // Gestion spécifique pour PackDetails.fxml et ModifierPack.fxml
            if (fxmlFile.equals("/Pack/PackDetails.fxml") && pack != null) {
                PackDetails controller = loader.getController();
                controller.setPack(pack);
            } else if (fxmlFile.equals("/Pack/ModifierPack.fxml") && pack != null) {
                ModifierPack controller = loader.getController();
                controller.setPackToEdit(pack);
            }

            Scene scene = new Scene(layout);
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                stage = (Stage) packTilePane.getScene().getWindow(); // Fallback si pas d'événement
            }
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlFile);
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