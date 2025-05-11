package Gui.Pack.Controllers;

import Models.Pack.Pack;
import Models.Pack.Evenement;
import Models.Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class PackDetails {

    @FXML
    private ImageView packImage;
    @FXML
    private Label nomLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label guestsLabel;
    @FXML
    private Label servicesLabel;
    @FXML
    private Button reserveButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button exchangeRateButton;

    private Pack pack;
    private final HttpClient httpClient = HttpClient.newHttpClient();


    public void setPack(Pack pack) {
        this.pack = pack;
        populateDetails();
    }

    private void populateDetails() {
        nomLabel.setText("Nom: " + pack.getNomPack());
        descriptionLabel.setText("Description: " + pack.getDescription());
        prixLabel.setText("Prix: " + pack.getPrix() + " TND");
        locationLabel.setText("Localisation: " + pack.getLocation().getLabel());
        typeLabel.setText("Type: " + pack.getType().getTypeEvenement());
        guestsLabel.setText("Invités: " + pack.getNbrGuests());
        servicesLabel.setText("Services: " + pack.getNomServices().stream()
                .map(Service::getTitre)
                .collect(Collectors.joining(", ")));

        String imagePath = pack.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File("src/main/resources" + imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString(), 300, 300, true, true);
                    packImage.setImage(image);
                }
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image pour " + pack.getNomPack() + ": " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleReserver(ActionEvent event) throws IOException {
        // Load AjouterReservationPack.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AjouterReservationPack.fxml"));
        Parent root = loader.load();

        // Get the AjouterReservationPack controller
        Gui.Reservation.Controllers.AjouterReservationPack controller = loader.getController();

        // Set the selected pack’s name in idPackField
        controller.setSelectedPack(pack.getNomPack());

        // Show the new scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void getPriceInCurrencies(ActionEvent event) {
        double priceTND = pack.getPrix();
        try {
            String convertedPrices = convertPriceToCurrencies(priceTND);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Prix en Devises");
            alert.setHeaderText(null);
            alert.setContentText("TND " + priceTND + " = " + convertedPrices);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de convertir le prix en devises : " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private String convertPriceToCurrencies(double priceTND) throws IOException, InterruptedException {
        String primaryUrl = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/tnd.json";
        String fallbackUrl = "https://latest.currency-api.pages.dev/v1/currencies/tnd.json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(primaryUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseExchangeRates(response.body(), priceTND);
            } else {
                System.out.println("Échec de la requête principale, passage au fallback : " + response.statusCode());
                return tryFallbackExchangeRates(fallbackUrl, priceTND);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la requête principale, passage au fallback : " + e.getMessage());
            return tryFallbackExchangeRates(fallbackUrl, priceTND);
        }
    }

    private String tryFallbackExchangeRates(String fallbackUrl, double priceTND) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fallbackUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseExchangeRates(response.body(), priceTND);
            } else {
                System.out.println("Échec de la requête fallback : " + response.body());
                return "Erreur lors de la récupération des taux de change.";
            }
        } catch (Exception e) {
            System.out.println("Exception lors de la récupération des taux de change via fallback : " + e.getMessage());
            e.printStackTrace();
            return "Erreur lors de la récupération des taux de change : " + e.getMessage();
        }
    }

    private String parseExchangeRates(String responseBody, double priceTND) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject rates = jsonResponse.getJSONObject("tnd");
            double usdRate = rates.getDouble("usd");
            double eurRate = rates.getDouble("eur");

            double priceUSD = priceTND * usdRate;
            double priceEUR = priceTND * eurRate;

            return String.format("USD %.2f, EUR %.2f", priceUSD, priceEUR);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'analyse des taux de change : " + e.getMessage());
            e.printStackTrace();
            return "Erreur lors de l'analyse des taux de change : " + e.getMessage();
        }
    }

    @FXML
    private void goToAffichePack(ActionEvent event) throws IOException {
        switchScene("/Pack/AffichePack.fxml", event);
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
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

    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent layout = loader.load();
        Scene newScene = new Scene(layout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
}