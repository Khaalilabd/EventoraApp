package Gui.Service.Controllers;

import Models.Service.Partenaire;
import Services.Service.Crud.PartenaireService;
import javafx.application.Platform;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AfficherPartenaire {

    @FXML private TableView<Partenaire> tableView;
    @FXML private TableColumn<Partenaire, Integer> idColumn;
    @FXML private TableColumn<Partenaire, String> nomColumn;
    @FXML private TableColumn<Partenaire, String> emailColumn;
    @FXML private TableColumn<Partenaire, String> adresseColumn;
    @FXML private TableColumn<Partenaire, String> site_webColumn;
    @FXML private TableColumn<Partenaire, String> typeColumn;
    @FXML private TableColumn<Partenaire, Void> colActions;
    @FXML private TextField searchField;

    @FXML private HBox currentWeatherBox;
    @FXML private ImageView currentWeatherIcon;
    @FXML private Label currentWeatherLabel;
    @FXML private HBox forecastWeatherBox;
    @FXML private ImageView forecastWeatherIcon;
    @FXML private Label forecastWeatherLabel;

    @FXML private Label locationLabel;
    @FXML private WebView mapView;

    private final PartenaireService partenaireService;
    private ObservableList<Partenaire> partenairesList = FXCollections.observableArrayList();
    private Map<String, String> forecastCache = new HashMap<>();

    public AfficherPartenaire() {
        this.partenaireService = new PartenaireService();
    }

    public void initialize() {
        configureColumns();
        addActionsColumn();
        loadPartenaires();

        FilteredList<Partenaire> filteredData = new FilteredList<>(partenairesList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(partenaire -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return partenaire.getNom_partenaire().toLowerCase().contains(lowerCaseFilter) ||
                        partenaire.getEmail_partenaire().toLowerCase().contains(lowerCaseFilter) ||
                        partenaire.getAdresse_partenaire().toLowerCase().contains(lowerCaseFilter);
            });
        });
        tableView.setItems(filteredData);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fetchWeatherForPartner(newVal);
                fetchFutureWeatherForPartner(newVal, LocalDate.now().plusDays(5));
                fetchLocationForPartner(newVal);
            } else {
                clearWeatherAndMap();
            }
        });

        tableRowFactory();
    }

    private void configureColumns() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_partenaire()).asObject());
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_partenaire()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail_partenaire()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse_partenaire()));
        site_webColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSite_web()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType_partenaire().getLabel()));
    }

    private void loadPartenaires() {
        List<Partenaire> partenaires = partenaireService.RechercherPartenaire();
        partenairesList.setAll(partenaires);
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
            protected void updateItem(Void item, boolean empty) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/ModifierPartenaire.fxml"));
            AnchorPane layout = loader.load();
            ModifierPartenaire controller = loader.getController();
            controller.setPartenaireToEdit(partenaire);

            Scene scene = new Scene(layout);
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : /Service/ModifierPartenaire.fxml");
            e.printStackTrace();
        }
    }

    private void handleDelete(Partenaire partenaire) {
        partenaireService.SupprimerPartenaire(partenaire);
        loadPartenaires();
    }

    @FXML
    private void addPartenaire(ActionEvent event) {
        switchScene(event, "/Service/AjouterPartenaire.fxml");
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
    private void goToPartenaire(ActionEvent event) {
        switchScene(event, "/Service/Partenaire.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
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

    // Méthode switchScene suivant le style mémorisé
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearWeatherAndMap() {
        if (currentWeatherLabel != null) currentWeatherLabel.setText("Météo: Aucun partenaire sélectionné");
        if (currentWeatherIcon != null) currentWeatherIcon.setImage(null);
        if (forecastWeatherLabel != null) forecastWeatherLabel.setText("Prévision (5 jours) : Aucun partenaire sélectionné");
        if (forecastWeatherIcon != null) forecastWeatherIcon.setImage(null);
        if (locationLabel != null) locationLabel.setText("Coordonnées: Aucun partenaire sélectionné");
        if (mapView != null) mapView.getEngine().loadContent("");
    }

    private void fetchWeatherForPartner(Partenaire partenaire) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            currentWeatherLabel.setText("Météo: Clé API non configurée");
            currentWeatherIcon.setImage(null);
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            currentWeatherLabel.setText("Météo: Adresse invalide");
            currentWeatherIcon.setImage(null);
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> Platform.runLater(() -> {
                    try {
                        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                        double temperature = json.getAsJsonObject("main").get("temp").getAsDouble();
                        String description = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                        String iconCode = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

                        currentWeatherLabel.setText("Météo à " + city + " : " + temperature + "°C, " + description);
                        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";
                        currentWeatherIcon.setImage(new Image(iconUrl));
                    } catch (Exception e) {
                        currentWeatherLabel.setText("Météo: Erreur lors du chargement");
                        currentWeatherIcon.setImage(null);
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        currentWeatherLabel.setText("Météo: Erreur réseau - " + e.getMessage());
                        currentWeatherIcon.setImage(null);
                    });
                    return null;
                });
    }

    private void fetchFutureWeatherForPartner(Partenaire partenaire, LocalDate futureDate) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            forecastWeatherLabel.setText("Prévision (5 jours) : Clé API non configurée");
            forecastWeatherIcon.setImage(null);
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            forecastWeatherLabel.setText("Prévision (5 jours) : Adresse invalide");
            forecastWeatherIcon.setImage(null);
            return;
        }

        String cacheKey = city + "_" + futureDate.toString();
        if (forecastCache.containsKey(cacheKey)) {
            forecastWeatherLabel.setText(forecastCache.get(cacheKey));
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        String weatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + apiKey;
        HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).build();

        client.sendAsync(weatherRequest, HttpResponse.BodyHandlers.ofString())
                .thenAccept(weatherResponse -> Platform.runLater(() -> {
                    try {
                        JsonObject json = JsonParser.parseString(weatherResponse.body()).getAsJsonObject();
                        JsonArray hourlyList = json.getAsJsonArray("list");

                        for (int i = 0; i < hourlyList.size(); i++) {
                            JsonObject forecast = hourlyList.get(i).getAsJsonObject();
                            long timestamp = forecast.get("dt").getAsLong();
                            LocalDate forecastDate = LocalDate.ofEpochDay(timestamp / 86400);
                            if (forecastDate.equals(futureDate)) {
                                double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();
                                String description = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                                String iconCode = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

                                String forecastText = "Prévision (5 jours, " + futureDate + ") à " + city + " : " + temperature + "°C, " + description;
                                forecastWeatherLabel.setText(forecastText);
                                forecastCache.put(cacheKey, forecastText);

                                String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";
                                forecastWeatherIcon.setImage(new Image(iconUrl));
                                return;
                            }
                        }
                        forecastWeatherLabel.setText("Prévision (5 jours) : Données non disponibles");
                        forecastWeatherIcon.setImage(null);
                    } catch (Exception e) {
                        forecastWeatherLabel.setText("Prévision (5 jours) : Erreur lors du chargement");
                        forecastWeatherIcon.setImage(null);
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        forecastWeatherLabel.setText("Prévision (5 jours) : Erreur réseau - " + e.getMessage());
                        forecastWeatherIcon.setImage(null);
                    });
                    return null;
                });
    }

    private void fetchLocationForPartner(Partenaire partenaire) {
        String address = partenaire.getAdresse_partenaire();
        if (address == null || address.isEmpty()) {
            locationLabel.setText("Coordonnées: Adresse invalide");
            mapView.getEngine().loadContent("");
            return;
        }

        String encodedAddress = address.replace(" ", "+");
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "EventoraApp/1.0 (contact: your-email@example.com)")
                .build();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> Platform.runLater(() -> {
                    try {
                        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
                        if (jsonArray.size() == 0) {
                            locationLabel.setText("Coordonnées: Adresse non trouvée");
                            mapView.getEngine().loadContent("");
                            return;
                        }

                        JsonObject json = jsonArray.get(0).getAsJsonObject();
                        double lat = json.get("lat").getAsDouble();
                        double lon = json.get("lon").getAsDouble();
                        String displayName = json.has("display_name") ? json.get("display_name").getAsString() : address;

                        locationLabel.setText("Coordonnées: Latitude = " + lat + ", Longitude = " + lon);

                        String htmlContent = "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />" +
                                "<script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\" defer></script>" +
                                "<style>html, body, #map {margin: 0; padding: 0; width: 100%; height: 100%;}</style>" +
                                "</head>" +
                                "<body>" +
                                "<div id=\"map\"></div>" +
                                "<script>" +
                                "document.addEventListener('DOMContentLoaded', function() {" +
                                "    var map = L.map('map').setView([" + lat + ", " + lon + "], 13);" +
                                "    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
                                "        attribution: '© OpenStreetMap contributors'" +
                                "    }).addTo(map);" +
                                "    L.marker([" + lat + ", " + lon + "]).addTo(map)" +
                                "        .bindPopup('" + displayName + "')" +
                                "        .openPopup();" +
                                "});" +
                                "</script>" +
                                "</body>" +
                                "</html>";

                        mapView.getEngine().loadContent(htmlContent);
                    } catch (Exception e) {
                        locationLabel.setText("Coordonnées: Erreur lors du chargement");
                        mapView.getEngine().loadContent("");
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        locationLabel.setText("Coordonnées: Erreur réseau - " + e.getMessage());
                        mapView.getEngine().loadContent("");
                    });
                    return null;
                });
    }

    private String extractCityFromAddress(String address) {
        if (address == null || address.isEmpty()) return null;
        String[] parts = address.trim().split("[,\\-]");
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i].trim();
            if (part.matches("[A-Za-z\\s]+")) {
                return part;
            }
        }
        return null;
    }
}