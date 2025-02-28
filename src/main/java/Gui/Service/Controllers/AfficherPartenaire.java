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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

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
import java.util.concurrent.CompletableFuture;

public class AfficherPartenaire {

    @FXML private TableView<Partenaire> tableView;
    @FXML private TableColumn<Partenaire, Integer> idColumn;
    @FXML private TableColumn<Partenaire, String> nomColumn;
    @FXML private TableColumn<Partenaire, String> emailColumn;
    @FXML private TableColumn<Partenaire, String> adresseColumn;
    @FXML private TableColumn<Partenaire, String> site_webColumn;
    @FXML private TableColumn<Partenaire, String> typeColumn;
    @FXML private TableColumn<Partenaire, Void> colActions;
    @FXML private TextField searchField; // Le champ de texte pour la recherche

    @FXML private HBox currentWeatherBox; // Conteneur pour la météo actuelle
    @FXML private ImageView currentWeatherIcon; // Icône pour la météo actuelle
    @FXML private Label currentWeatherLabel; // Label pour la météo actuelle
    @FXML private HBox forecastWeatherBox; // Conteneur pour la prévision
    @FXML private ImageView forecastWeatherIcon; // Icône pour la prévision
    @FXML private Label forecastWeatherLabel; // Label pour la prévision

    @FXML private Label locationLabel; // Label pour les coordonnées GPS
    @FXML private WebView mapView; // WebView pour afficher la carte OpenStreetMap

    private final PartenaireService partenaireService;
    private ObservableList<Partenaire> partenairesList = FXCollections.observableArrayList();
    private Map<String, String> forecastCache = new HashMap<>(); // Cache pour les prévisions

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
                    return true; // Si rien n'est écrit, afficher tous les partenaires
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return partenaire.getNom_partenaire().toLowerCase().contains(lowerCaseFilter) ||
                        partenaire.getEmail_partenaire().toLowerCase().contains(lowerCaseFilter) ||
                        partenaire.getAdresse_partenaire().toLowerCase().contains(lowerCaseFilter);
            });
        });
        tableView.setItems(filteredData);

        // Listener pour afficher la météo et la localisation lorsqu’un partenaire est sélectionné
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Partenaire sélectionné : " + (newVal != null ? newVal.getNom_partenaire() : "Aucun"));
            if (newVal != null) {
                fetchWeatherForPartner(newVal);
                fetchFutureWeatherForPartner(newVal, LocalDate.now().plusDays(5)); // Limité à 5 jours avec /forecast
                fetchLocationForPartner(newVal); // Récupérer et afficher la localisation
            } else {
                if (currentWeatherLabel != null) {
                    currentWeatherLabel.setText("Météo: Aucun partenaire sélectionné");
                }
                if (currentWeatherIcon != null) {
                    currentWeatherIcon.setImage(null);
                }
                if (forecastWeatherLabel != null) {
                    forecastWeatherLabel.setText("Prévision (5 jours) : Aucun partenaire sélectionné");
                }
                if (forecastWeatherIcon != null) {
                    forecastWeatherIcon.setImage(null);
                }
                if (locationLabel != null) {
                    locationLabel.setText("Coordonnées: Aucun partenaire sélectionné");
                }
                if (mapView != null) {
                    mapView.getEngine().loadContent(""); // Effacer la carte
                }
            }
        });

        // Ajout de l'effet de survol sur les lignes du tableau
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
            // Charger la fenêtre de modification du partenaire
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/ModifierPartenaire.fxml"));
            AnchorPane modifLayout = loader.load();

            // Obtenir le controller de la fenêtre de modification
            ModifierPartenaire controller = loader.getController();
            controller.setPartenaireToEdit(partenaire);

            // Obtenir le stage actuel (fenêtre actuelle) et le fermer
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close(); // Fermer la fenêtre actuelle

            // Créer une nouvelle scène avec la fenêtre de modification et l'afficher
            Stage newStage = new Stage();
            newStage.setScene(new Scene(modifLayout));
            newStage.show();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/AjouterPartenaire.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();
        Scene scene = new Scene(partenaireLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
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

    private void fetchWeatherForPartner(Partenaire partenaire) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        System.out.println("Clé API pour météo actuelle du partenaire : " + (apiKey != null ? "Présente" : "Absente"));
        System.out.println("Clé API utilisée : " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            if (currentWeatherLabel != null) {
                currentWeatherLabel.setText("Météo: Clé API non configurée. Vérifiez les variables d’environnement.");
            }
            if (currentWeatherIcon != null) {
                currentWeatherIcon.setImage(null);
            }
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            if (currentWeatherLabel != null) {
                currentWeatherLabel.setText("Météo: Adresse invalide ou non disponible");
            }
            if (currentWeatherIcon != null) {
                currentWeatherIcon.setImage(null);
            }
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        System.out.println("URL météo actuelle : " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> Platform.runLater(() -> {
                    System.out.println("Réponse API pour météo actuelle : " + response);
                    try {
                        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                        double temperature = json.getAsJsonObject("main").get("temp").getAsDouble();
                        String description = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                        String iconCode = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

                        // Afficher le texte de la météo
                        if (currentWeatherLabel != null) {
                            currentWeatherLabel.setText("Météo à " + city + " : " + temperature + "°C, " + description);
                        }

                        // Charger l’icône météo
                        if (currentWeatherIcon != null) {
                            String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";
                            Image icon = new Image(iconUrl);
                            currentWeatherIcon.setImage(icon);
                        }
                    } catch (Exception e) {
                        if (currentWeatherLabel != null) {
                            currentWeatherLabel.setText("Météo: Erreur lors du chargement");
                        }
                        if (currentWeatherIcon != null) {
                            currentWeatherIcon.setImage(null);
                        }
                        System.out.println("Erreur de parsing JSON : " + e.getMessage());
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        if (currentWeatherLabel != null) {
                            currentWeatherLabel.setText("Météo: Erreur réseau - " + e.getMessage());
                        }
                        if (currentWeatherIcon != null) {
                            currentWeatherIcon.setImage(null);
                        }
                    });
                    System.out.println("Erreur réseau pour météo actuelle : " + e.getMessage());
                    return null;
                });
    }

    private void fetchFutureWeatherForPartner(Partenaire partenaire, LocalDate futureDate) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        System.out.println("Clé API pour prévision future du partenaire : " + (apiKey != null ? "Présente" : "Absente"));
        System.out.println("Clé API utilisée : " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            if (forecastWeatherLabel != null) {
                forecastWeatherLabel.setText("Prévision (5 jours) : Clé API non configurée. Vérifiez les variables d’environnement.");
            }
            if (forecastWeatherIcon != null) {
                forecastWeatherIcon.setImage(null);
            }
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            if (forecastWeatherLabel != null) {
                forecastWeatherLabel.setText("Prévision (5 jours) : Adresse invalide ou non disponible");
            }
            if (forecastWeatherIcon != null) {
                forecastWeatherIcon.setImage(null);
            }
            return;
        }

        // Vérifiez si la prévision est déjà dans le cache
        String cacheKey = city + "_" + futureDate.toString();
        if (forecastCache.containsKey(cacheKey)) {
            if (forecastWeatherLabel != null) {
                forecastWeatherLabel.setText(forecastCache.get(cacheKey));
            }
            // Pas d’icône dans le cache, on peut recharger l’icône si nécessaire
            return;
        }

        // Utilisation de l'API gratuite /data/2.5/forecast (5 jours, toutes les 3 heures)
        HttpClient client = HttpClient.newHttpClient();
        String weatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=" + apiKey;
        System.out.println("URL de prévision : " + weatherUrl);
        HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).build();

        client.sendAsync(weatherRequest, HttpResponse.BodyHandlers.ofString())
                .thenAccept(weatherResponse -> {
                    Platform.runLater(() -> {
                        try {
                            System.out.println("Code de réponse : " + weatherResponse.statusCode());
                            System.out.println("Réponse API pour date future : " + weatherResponse.body());
                            if (weatherResponse.statusCode() == 404) {
                                if (forecastWeatherLabel != null) {
                                    forecastWeatherLabel.setText("Prévision (5 jours) : Données non disponibles pour cette localisation");
                                }
                                if (forecastWeatherIcon != null) {
                                    forecastWeatherIcon.setImage(null);
                                }
                                return;
                            }
                            if (weatherResponse.statusCode() == 401) {
                                if (forecastWeatherLabel != null) {
                                    forecastWeatherLabel.setText("Prévision (5 jours) : Clé API invalide ou limite atteinte");
                                }
                                if (forecastWeatherIcon != null) {
                                    forecastWeatherIcon.setImage(null);
                                }
                                return;
                            }
                            if (weatherResponse.statusCode() != 200) {
                                if (forecastWeatherLabel != null) {
                                    forecastWeatherLabel.setText("Prévision (5 jours) : Erreur API - Code " + weatherResponse.statusCode());
                                }
                                if (forecastWeatherIcon != null) {
                                    forecastWeatherIcon.setImage(null);
                                }
                                return;
                            }
                            String responseBody = weatherResponse.body();
                            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                            JsonArray hourlyList = json.getAsJsonArray("list");
                            if (hourlyList == null || hourlyList.size() == 0) {
                                if (forecastWeatherLabel != null) {
                                    forecastWeatherLabel.setText("Prévision (5 jours) : Aucune donnée disponible");
                                }
                                if (forecastWeatherIcon != null) {
                                    forecastWeatherIcon.setImage(null);
                                }
                                return;
                            }

                            // Trouver les données pour la date future
                            for (int i = 0; i < hourlyList.size(); i++) {
                                JsonObject forecast = hourlyList.get(i).getAsJsonObject();
                                long timestamp = forecast.get("dt").getAsLong();
                                LocalDate forecastDate = LocalDate.ofEpochDay(timestamp / 86400);
                                if (forecastDate.equals(futureDate)) {
                                    double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();
                                    String description = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                                    String iconCode = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

                                    String forecastText = "Prévision (5 jours, " + futureDate + ") à " + city + " : " + temperature + "°C, " + description;
                                    if (forecastWeatherLabel != null) {
                                        forecastWeatherLabel.setText(forecastText);
                                    }
                                    forecastCache.put(cacheKey, forecastText);

                                    // Charger l’icône météo
                                    if (forecastWeatherIcon != null) {
                                        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + ".png";
                                        Image icon = new Image(iconUrl);
                                        forecastWeatherIcon.setImage(icon);
                                    }
                                    return;
                                }
                            }
                            if (forecastWeatherLabel != null) {
                                forecastWeatherLabel.setText("Prévision (5 jours) : Données non disponibles pour cette date");
                            }
                            if (forecastWeatherIcon != null) {
                                forecastWeatherIcon.setImage(null);
                            }
                        } catch (Exception e) {
                            if (forecastWeatherLabel != null) {
                                forecastWeatherLabel.setText("Prévision (5 jours) : Erreur lors du chargement");
                            }
                            if (forecastWeatherIcon != null) {
                                forecastWeatherIcon.setImage(null);
                            }
                            System.out.println("Erreur de parsing JSON : " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        if (forecastWeatherLabel != null) {
                            forecastWeatherLabel.setText("Prévision (5 jours) : Erreur réseau - " + e.getMessage());
                        }
                        if (forecastWeatherIcon != null) {
                            forecastWeatherIcon.setImage(null);
                        }
                    });
                    System.out.println("Erreur réseau pour prévision future : " + e.getMessage());
                    return null;
                });
    }

    private void fetchLocationForPartner(Partenaire partenaire) {
        String address = partenaire.getAdresse_partenaire();
        if (address == null || address.isEmpty()) {
            System.out.println("Adresse nulle ou vide : " + address);
            locationLabel.setText("Coordonnées: Adresse invalide ou non disponible");
            mapView.getEngine().loadContent("");
            return;
        }
        System.out.println("Log 1 - Adresse complète utilisée pour le géocodage : " + address);

        // Remplacer les espaces par des "+" pour l’URL
        String encodedAddress = address.replace(" ", "+");
        System.out.println("Log 2 - Adresse encodée pour l’URL : " + encodedAddress);

        HttpClient client = HttpClient.newHttpClient();
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";
        System.out.println("Log 3 - URL de géocodage (Nominatim) : " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "EventoraApp/1.0 (contact: your-email@example.com)") // Remplace par ton e-mail
                .build();

        // Ajouter un délai pour respecter la politique de Nominatim (1 requête par seconde)
        try {
            System.out.println("Log 4 - Ajout d’un délai de 1 seconde avant la requête");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Erreur lors du délai : " + e.getMessage());
            e.printStackTrace();
        }

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> Platform.runLater(() -> {
                    System.out.println("Log 5 - Réponse API pour géocodage (Nominatim) : " + response);
                    try {
                        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
                        if (jsonArray == null || jsonArray.size() == 0) {
                            System.out.println("Log 6 - Aucune donnée de géocodage trouvée pour l’adresse : " + address);
                            locationLabel.setText("Coordonnées: Adresse non trouvée (" + address + ")");
                            mapView.getEngine().loadContent("");
                            return;
                        }

                        JsonObject json = jsonArray.get(0).getAsJsonObject();
                        double lat = json.get("lat").getAsDouble();
                        double lon = json.get("lon").getAsDouble();
                        String displayName = json.has("display_name") ? json.get("display_name").getAsString() : address;

                        System.out.println("Log 7 - Coordonnées extraites : lat=" + lat + ", lon=" + lon + ", display_name=" + displayName);

                        // Afficher les coordonnées dans le label
                        locationLabel.setText("Coordonnées: Latitude = " + lat + ", Longitude = " + lon);

                        // Utiliser Leaflet pour afficher la carte
                        String htmlContent = "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />" +
                                "<script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\" defer></script>" +
                                "<style>" +
                                "html, body, #map {" +
                                "    margin: 0;" +
                                "    padding: 0;" +
                                "    width: 100%;" +
                                "    height: 100%;" +
                                "}" +
                                "</style>" +
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

                        System.out.println("Log 8 - Chargement du contenu HTML dans le WebView");
                        mapView.getEngine().loadContent(htmlContent);
                    } catch (Exception e) {
                        System.out.println("Log 9 - Erreur lors du traitement de la réponse API : " + e.getMessage());
                        locationLabel.setText("Coordonnées: Erreur lors du chargement");
                        mapView.getEngine().loadContent("");
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        System.out.println("Log 10 - Erreur réseau lors du géocodage : " + e.getMessage());
                        locationLabel.setText("Coordonnées: Erreur réseau - " + e.getMessage());
                        mapView.getEngine().loadContent("");
                    });
                    return null;
                });
    }

    private String extractCityFromAddress(String address) {
        if (address == null || address.isEmpty()) return null;
        String[] parts = address.trim().split("[,\\-]");
        // Parcourir les parties de la fin vers le début pour privilégier la ville principale
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i].trim();
            if (part.matches("[A-Za-z\\s]+")) { // Vérifie si c’est une ville (lettres et espaces)
                return part;
            }
        }
        return null; // Retourne null si aucune ville n’est trouvée
    }
}