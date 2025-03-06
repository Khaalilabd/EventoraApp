package Gui.Pack.Controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Services.Pack.Crud.EvenementService;
import Models.Pack.Evenement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AfficheEvenement {

    @FXML
    private TextField searchField;

    @FXML
    private TilePane eventTilePane;

    private EvenementService evenementService = new EvenementService();
    private ObservableList<Evenement> evenementList = FXCollections.observableArrayList();

    // Retrieve API key from environment variable
    private static final String DEEPAI_API_KEY = System.getenv("DEEPAI_API_KEY");
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String CACHE_DIR = "src/main/resources/cache";

    // Track the selected event and card
    private Evenement selectedEvent;
    private VBox selectedCard;

    @FXML
    public void initialize() {
        // Check if API key is set
        if (DEEPAI_API_KEY == null || DEEPAI_API_KEY.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Configuration");
            alert.setHeaderText(null);
            alert.setContentText("L'API key de DeepAI n'est pas définie. Veuillez configurer la variable d'environnement DEEPAI_API_KEY.");
            alert.showAndWait();
            return;
        }

        // Create cache directory if it doesn't exist
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        loadPacks();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchEvenement(newValue));
    }

    private void loadPacks() {
        evenementList.clear();
        evenementList.addAll(evenementService.rechercher());
        updateEventTilePane();
    }

    private void handleEdit(Evenement evenement) {
        switchScene(null, "/Pack/ModifierEvenement.fxml", evenement); // Pas d'ActionEvent ici, ajusté ci-dessous
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
        if (motCle == null || motCle.trim().isEmpty()) {
            loadPacks();
            return;
        }

        List<Evenement> resultatRecherche = evenementService.RechercherEvenementParMotCle(motCle.toLowerCase());
        evenementList.clear();
        evenementList.addAll(resultatRecherche);
        updateEventTilePane();
    }

    private void updateEventTilePane() {
        eventTilePane.getChildren().clear();
        selectedEvent = null; // Reset selected event on refresh
        selectedCard = null;
        for (Evenement event : evenementList) {
            eventTilePane.getChildren().add(createEventCard(event));
        }
    }

    private VBox createEventCard(Evenement event) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");

        // Handle selection on click
        card.setOnMouseClicked(e -> {
            if (selectedCard != null) {
                selectedCard.getStyleClass().remove("event-card-selected");
            }
            selectedEvent = event;
            selectedCard = card;
            card.getStyleClass().add("event-card-selected");
        });

        card.setOnMouseEntered(e -> card.getStyleClass().add("event-card-hover"));
        card.setOnMouseExited(e -> card.getStyleClass().remove("event-card-hover"));

        ImageView imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        String eventNameKey = event.getTypeEvenement().toLowerCase().replace(" ", "_");
        String cachePath = CACHE_DIR + "/" + eventNameKey + ".png";

        try {
            File cachedImage = new File(cachePath);
            if (cachedImage.exists()) {
                imageView.setImage(new Image(cachedImage.toURI().toString()));
            } else {
                imageView.setImage(new Image("https://via.placeholder.com/300x150.png?text=Image+Not+Available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImage(new Image("https://via.placeholder.com/300x150.png?text=Image+Not+Available"));
        }

        Label typeLabel = new Label(event.getTypeEvenement());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        typeLabel.setWrapText(true);
        typeLabel.setMaxWidth(200);

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

        Tooltip editTooltip = new Tooltip("Modifier l'événement");
        Tooltip deleteTooltip = new Tooltip("Supprimer l'événement");
        Tooltip.install(editButton, editTooltip);
        Tooltip.install(deleteButton, deleteTooltip);

        editButton.setOnAction(e -> handleEdit(event));
        deleteButton.setOnAction(e -> handleDelete(event));

        HBox actionButtons = new HBox(10, editButton, deleteButton);
        actionButtons.setStyle("-fx-alignment: center;");

        card.getChildren().addAll(imageView, typeLabel, actionButtons);
        card.setUserData(imageView);

        return card;
    }

    @FXML
    private void generateImageForSelectedEvent(ActionEvent event) {
        if (selectedEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun Événement Sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un événement avant de générer une image.");
            alert.showAndWait();
            return;
        }

        String eventNameKey = selectedEvent.getTypeEvenement().toLowerCase().replace(" ", "_");
        String cachePath = CACHE_DIR + "/" + eventNameKey + ".png";

        try {
            File cachedImage = new File(cachePath);
            if (cachedImage.exists()) {
                ImageView imageView = (ImageView) selectedCard.getUserData();
                imageView.setImage(new Image(cachedImage.toURI().toString()));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Image Déjà Générée");
                alert.setHeaderText(null);
                alert.setContentText("L'image pour cet événement a déjà été générée.");
                alert.showAndWait();
            } else {
                String imageUrl = fetchGeneratedImage(selectedEvent.getTypeEvenement());
                if (imageUrl != null) {
                    downloadAndSaveImage(imageUrl, cachePath);
                    ImageView imageView = (ImageView) selectedCard.getUserData();
                    imageView.setImage(new Image(cachedImage.toURI().toString()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Image Générée");
                    alert.setHeaderText(null);
                    alert.setContentText("L'image pour l'événement " + selectedEvent.getTypeEvenement() + " a été générée et enregistrée.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de Génération");
                    alert.setHeaderText(null);
                    alert.setContentText("Impossible de générer l'image. Vérifiez votre connexion ou vos crédits API.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Génération");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la génération de l'image : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private String fetchGeneratedImage(String eventName) throws IOException, InterruptedException {
        String prompt = "A beautiful " + eventName.toLowerCase() + " scene, vibrant colors, detailed, high quality";
        String requestBody = String.format("{\"text\": \"%s\"}", prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.deepai.org/api/text2img"))
                .header("api-key", DEEPAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            String imageUrl = jsonResponse.get("output_url").getAsString();
            return imageUrl;
        } else {
            System.err.println("API request failed with status: " + response.statusCode());
            System.err.println("Response: " + response.body());
            return null;
        }
    }

    private void downloadAndSaveImage(String imageUrl, String cachePath) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() == 200) {
            byte[] imageBytes = response.body();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            File outputFile = new File(cachePath);
            ImageIO.write(bufferedImage, "png", outputFile);
        } else {
            System.err.println("Failed to download image with status: " + response.statusCode());
        }
    }

    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml", null);
    }

    @FXML
    private void addEvenement(ActionEvent event) {
        switchScene(event, "/Pack/AjouterEvenement.fxml", null);
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
    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml", null);
    }

    @FXML
    private void goToAffichePack(ActionEvent event) {
        switchScene(event, "/Pack/AffichePack.fxml", null);
    }


    // Méthode unifiée switchScene avec le style mémorisé
    private void switchScene(ActionEvent event, String fxmlFile, Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane layout = loader.load();
            if (fxmlFile.equals("/Pack/ModifierEvenement.fxml") && evenement != null) {
                ModifierEvenement controller = loader.getController();
                controller.setEvenementToEdit(evenement);
            }

            Scene scene = new Scene(layout);
            Stage stage;
            if (event != null) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else {
                // Si pas d'ActionEvent, utiliser eventTilePane comme fallback
                stage = (Stage) eventTilePane.getScene().getWindow();
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