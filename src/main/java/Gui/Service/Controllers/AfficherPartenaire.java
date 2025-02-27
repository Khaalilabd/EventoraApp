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

    @FXML private Label weatherLabel; // Label pour la météo actuelle
    @FXML private Label futureWeatherLabel; // Label pour les prévisions futures

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

        // Listener pour afficher la météo lorsqu’un partenaire est sélectionné
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Partenaire sélectionné : " + (newVal != null ? newVal.getNom_partenaire() : "Aucun"));
            if (newVal != null) {
                fetchWeatherForPartner(newVal);
                fetchFutureWeatherForPartner(newVal, LocalDate.now().plusDays(5)); // Limité à 5 jours avec /forecast
            } else {
                weatherLabel.setText("Météo: Aucun partenaire sélectionné");
                futureWeatherLabel.setText("Prévision (5 jours) : Aucun partenaire sélectionné");
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
            weatherLabel.setText("Météo: Clé API non configurée. Vérifiez les variables d’environnement.");
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            weatherLabel.setText("Météo: Adresse invalide ou non disponible");
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

    private void fetchFutureWeatherForPartner(Partenaire partenaire, LocalDate futureDate) {
        String apiKey = System.getenv("OPENWEATHERMAP_API_KEY");
        System.out.println("Clé API pour prévision future du partenaire : " + (apiKey != null ? "Présente" : "Absente"));
        System.out.println("Clé API utilisée : " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            futureWeatherLabel.setText("Prévision (5 jours) : Clé API non configurée. Vérifiez les variables d’environnement.");
            return;
        }

        String city = extractCityFromAddress(partenaire.getAdresse_partenaire());
        if (city == null || city.isEmpty()) {
            futureWeatherLabel.setText("Prévision (5 jours) : Adresse invalide ou non disponible");
            return;
        }

        // Vérifiez si la prévision est déjà dans le cache
        String cacheKey = city + "_" + futureDate.toString();
        if (forecastCache.containsKey(cacheKey)) {
            futureWeatherLabel.setText(forecastCache.get(cacheKey));
            System.out.println("Prévision récupérée depuis le cache pour : " + cacheKey);
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
                                futureWeatherLabel.setText("Prévision (5 jours) : Données non disponibles pour cette localisation");
                                return;
                            }
                            if (weatherResponse.statusCode() == 401) {
                                futureWeatherLabel.setText("Prévision (5 jours) : Clé API invalide ou limite atteinte");
                                return;
                            }
                            if (weatherResponse.statusCode() != 200) {
                                futureWeatherLabel.setText("Prévision (5 jours) : Erreur API - Code " + weatherResponse.statusCode());
                                return;
                            }
                            String responseBody = weatherResponse.body();
                            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                            JsonArray hourlyList = json.getAsJsonArray("list");
                            if (hourlyList == null || hourlyList.size() == 0) {
                                futureWeatherLabel.setText("Prévision (5 jours) : Aucune donnée disponible");
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
                                    String forecastText = "Prévision (5 jours, " + futureDate + ") à " + city + " : " + temperature + "°C, " + description;
                                    futureWeatherLabel.setText(forecastText);
                                    forecastCache.put(cacheKey, forecastText);
                                    return;
                                }
                            }
                            futureWeatherLabel.setText("Prévision (5 jours) : Données non disponibles pour cette date");
                        } catch (Exception e) {
                            futureWeatherLabel.setText("Prévision (5 jours) : Erreur lors du chargement");
                            System.out.println("Erreur de parsing JSON : " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> futureWeatherLabel.setText("Prévision (5 jours) : Erreur réseau - " + e.getMessage()));
                    System.out.println("Erreur réseau pour prévision future : " + e.getMessage());
                    return null;
                });
    }

    private String extractCityFromAddress(String address) {
        if (address == null || address.isEmpty()) return null;
        String[] parts = address.trim().split("[,\\-]");
        for (String part : parts) {
            part = part.trim();
            if (part.matches("[A-Za-z\\s]+")) { // Vérifie si c’est une ville (lettres et espaces)
                return part;
            }
        }
        return null; // Retourne null si aucune ville n’est trouvée
    }
}