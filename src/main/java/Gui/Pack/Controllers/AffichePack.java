package Gui.Pack.Controllers;

import Models.Pack.Evenement;
import Models.Pack.Location;
import Models.Pack.Pack;
import Services.Pack.Crud.PackService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AffichePack {

    @FXML private TextField searchField;
    @FXML private TableView<Pack> tableView;
    @FXML private TableColumn<Pack, String> colNom;
    @FXML private TableColumn<Pack, String> colDescription;
    @FXML private TableColumn<Pack, Double> colPrix;
    @FXML private TableColumn<Pack, Location> colLocation;
    @FXML private TableColumn<Pack, Evenement> colType;
    @FXML private TableColumn<Pack, Integer> colNbrGuests;
    @FXML private TableColumn<Pack, String> colServices;
    @FXML private TableColumn<Pack, Void> colActions;

    @FXML private Label weatherLabel; // Label pour la météo actuelle
    @FXML private Label futureWeatherLabel; // Label pour les prévisions futures

    private PackService packService = new PackService();
    private ObservableList<Pack> packList = FXCollections.observableArrayList();
    private Map<String, String> forecastCache = new HashMap<>(); // Cache pour les prévisions

    // Mapping statique entre Location et une ville
    private static final Map<Location, String> LOCATION_TO_CITY = new HashMap<>();

    static {
        LOCATION_TO_CITY.put(Location.HOTEL, "Tunis");
        LOCATION_TO_CITY.put(Location.MAISON_D_HOTE, "Sousse");
        LOCATION_TO_CITY.put(Location.ESPACE_VERT, "Hammamet");
        LOCATION_TO_CITY.put(Location.SALLE_DE_FETE, "Paris");
        LOCATION_TO_CITY.put(Location.AUTRE, "Monastir");
    }

    @FXML
    public void initialize() {
        System.out.println("Initialisation de AffichePack...");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colNbrGuests.setCellValueFactory(new PropertyValueFactory<>("nbrGuests"));
        colServices.setCellValueFactory(new PropertyValueFactory<>("nomService"));

        // Actions column (Edit & Delete buttons with icons)
        colActions.setCellFactory(createActionButtons());

        // Load packs into the table
        loadPacks();

        // Dynamic search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPack(newValue);
        });

        // Row hover effect
        tableRowFactory(tableView);

        // Listener pour afficher la météo lorsqu’un pack est sélectionné
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Pack sélectionné : " + (newVal != null ? newVal.getNomPack() : "Aucun"));
            if (newVal != null) {
                fetchWeatherForPack(newVal);
                fetchFutureWeatherForPack(newVal, LocalDate.now().plusDays(7));
            } else {
                weatherLabel.setText("Météo: Aucun pack sélectionné");
                futureWeatherLabel.setText("Prévision (7 jours) : Aucun pack sélectionné");
            }
        });
    }

    private void loadPacks() {
        packList.clear();
        packList.addAll(packService.rechercher());
        tableView.setItems(packList);
    }

    private Callback<TableColumn<Pack, Void>, TableCell<Pack, Void>> createActionButtons() {
        return param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox buttons = new HBox(10, editButton, deleteButton);

            {
                // Add icons to buttons
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

                // Styling for the buttons
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                // Set actions for buttons
                editButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    handleEdit(pack);
                });

                deleteButton.setOnAction(event -> {
                    Pack pack = getTableView().getItems().get(getIndex());
                    handleDelete(pack);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        };
    }

    private void handleEdit(Pack pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/ModifierPack.fxml"));
            AnchorPane modifRecLayout = loader.load();

            ModifierPack controller = loader.getController();
            controller.setPackToEdit(pack);

            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                loadPacks(); // Refresh the list after deletion
            }
        });
    }

    private void searchPack(String motCle) {
        List<Pack> resultatRecherche = packService.RechercherPackParMotCle(motCle.toLowerCase());
        ObservableList<Pack> data = FXCollections.observableArrayList(resultatRecherche);
        tableView.setItems(data);
    }

    @FXML
    private void refreshList() {
        loadPacks();
    }

    @FXML
    private void tableRowFactory(TableView<Pack> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Pack> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
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

    @FXML
    private void addPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AjouterPack.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToEvenement(ActionEvent event)throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AfficheEvenement.fxml"));
            AnchorPane packLayout = loader.load();
            Scene scene = new Scene(packLayout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene reclamationScene = new Scene(reclamationLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(reclamationScene);
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
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }

    private void fetchWeatherForPack(Pack pack) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        System.out.println("Clé API pour météo actuelle du pack : " + (apiKey != null ? "Présente" : "Absente"));
        System.out.println("Clé API utilisée : " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            weatherLabel.setText("Météo: Clé API non configurée. Vérifiez les variables d’environnement.");
            return;
        }

        String city = LOCATION_TO_CITY.get(pack.getLocation());
        if (city == null || city.isEmpty()) {
            weatherLabel.setText("Météo: Localisation invalide ou non disponible");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
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
                        weatherLabel.setText("Météo à " + city + " : " + temperature + "°C, " + description);
                    } catch (Exception e) {
                        weatherLabel.setText("Météo: Erreur lors du chargement");
                        System.out.println("Erreur de parsing JSON : " + e.getMessage());
                        e.printStackTrace();
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> weatherLabel.setText("Météo: Erreur réseau - " + e.getMessage()));
                    System.out.println("Erreur réseau pour météo actuelle : " + e.getMessage());
                    return null;
                });
    }

    private void fetchFutureWeatherForPack(Pack pack, LocalDate futureDate) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        System.out.println("Clé API pour prévision future du pack : " + (apiKey != null ? "Présente" : "Absente"));
        System.out.println("Clé API utilisée : " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            futureWeatherLabel.setText("Prévision (7 jours) : Clé API non configurée. Vérifiez les variables d’environnement.");
            return;
        }

        String city = LOCATION_TO_CITY.get(pack.getLocation());
        if (city == null || city.isEmpty()) {
            futureWeatherLabel.setText("Prévision (7 jours) : Localisation invalide ou non disponible");
            return;
        }

        // Vérifiez si la prévision est déjà dans le cache
        String cacheKey = city + "_" + futureDate.toString();
        if (forecastCache.containsKey(cacheKey)) {
            futureWeatherLabel.setText(forecastCache.get(cacheKey));
            System.out.println("Prévision récupérée depuis le cache pour : " + cacheKey);
            return;
        }

        // Obtenez les coordonnées via Geocoding API
        HttpClient clientGeo = HttpClient.newHttpClient();
        String geoUrl = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        System.out.println("URL de géocodage : " + geoUrl);
        HttpRequest geoRequest = HttpRequest.newBuilder().uri(URI.create(geoUrl)).build();

        clientGeo.sendAsync(geoRequest, HttpResponse.BodyHandlers.ofString())
                .thenCompose(geoResponse -> {
                    try {
                        System.out.println("Réponse géocodage : " + geoResponse.body());
                        JsonObject geoJson = JsonParser.parseString(geoResponse.body()).getAsJsonArray().get(0).getAsJsonObject();
                        double lat = geoJson.get("lat").getAsDouble();
                        double lon = geoJson.get("lon").getAsDouble();
                        System.out.println("Coordonnées : lat=" + lat + ", lon=" + lon);

                        // Prévision pour une date future avec Daily Forecast 16 Days
                        HttpClient clientWeather = HttpClient.newHttpClient();
                        String weatherUrl = "https://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon +
                                "&cnt=16&units=metric&appid=" + apiKey;
                        System.out.println("URL de prévision : " + weatherUrl);
                        HttpRequest weatherRequest = HttpRequest.newBuilder().uri(URI.create(weatherUrl)).build();
                        return clientWeather.sendAsync(weatherRequest, HttpResponse.BodyHandlers.ofString());
                    } catch (Exception e) {
                        Platform.runLater(() -> futureWeatherLabel.setText("Prévision (7 jours) : Erreur de géocodage - " + e.getMessage()));
                        return CompletableFuture.completedFuture(null);
                    }
                })
                .thenAccept(weatherResponse -> {
                    if (weatherResponse != null) {
                        Platform.runLater(() -> {
                            try {
                                if (weatherResponse.statusCode() == 404) {
                                    futureWeatherLabel.setText("Prévision (7 jours) : Données non disponibles pour cette localisation");
                                    return;
                                }
                                if (weatherResponse.statusCode() == 401) {
                                    futureWeatherLabel.setText("Prévision (7 jours) : Clé API invalide ou limite atteinte");
                                    return;
                                }
                                if (weatherResponse.statusCode() != 200) {
                                    futureWeatherLabel.setText("Prévision (7 jours) : Erreur API - Code " + weatherResponse.statusCode());
                                    return;
                                }
                                String responseBody = weatherResponse.body();
                                System.out.println("Réponse API pour date future : " + responseBody);
                                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                                JsonArray dailyList = json.getAsJsonArray("list");
                                if (dailyList == null || dailyList.size() == 0) {
                                    futureWeatherLabel.setText("Prévision (7 jours) : Aucune donnée disponible");
                                    return;
                                }
                                for (int i = 0; i < dailyList.size(); i++) {
                                    JsonObject daily = dailyList.get(i).getAsJsonObject();
                                    long timestamp = daily.get("dt").getAsLong();
                                    LocalDate forecastDate = LocalDate.ofEpochDay(timestamp / 86400);
                                    if (forecastDate.equals(futureDate)) {
                                        double temperature = daily.getAsJsonObject("temp").get("day").getAsDouble();
                                        String description = daily.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                                        String forecastText = "Prévision (7 jours, " + futureDate + ") à " + city + " : " + temperature + "°C, " + description;
                                        futureWeatherLabel.setText(forecastText);
                                        forecastCache.put(cacheKey, forecastText);
                                        return;
                                    }
                                }
                                futureWeatherLabel.setText("Prévision (7 jours) : Données non disponibles pour cette date");
                            } catch (Exception e) {
                                futureWeatherLabel.setText("Prévision (7 jours) : Erreur lors du chargement");
                                System.out.println("Erreur de parsing JSON : " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    }
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> futureWeatherLabel.setText("Prévision (7 jours) : Erreur réseau - " + e.getMessage()));
                    System.out.println("Erreur réseau pour prévision future : " + e.getMessage());
                    return null;
                });
    }
}