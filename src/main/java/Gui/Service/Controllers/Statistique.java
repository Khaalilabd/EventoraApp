package Gui.Service.Controllers;
import Services.Service.Crud.ServiceService;
import Models.Service.Location;
import Models.Service.TypeService;
import Services.Service.Crud.ServiceService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class Statistique {

    @FXML
    private BarChart<String, Number> locationBarChart;
    @FXML
    private BarChart<String, Number> typeServiceBarChart;
    @FXML
    private CategoryAxis locationXAxis;
    @FXML
    private CategoryAxis typeServiceXAxis;
    @FXML
    private NumberAxis locationYAxis;
    @FXML
    private NumberAxis typeServiceYAxis;

    private ServiceService serviceservice = new ServiceService();

    // Initialisation des graphiques
    public void initialize() {
        // Récupérer les statistiques par Location depuis la base de données
        Map<Location, Integer> locationStats = serviceservice.getLocationStats();

        XYChart.Series<String, Number> locationSeries = new XYChart.Series<>();
        locationSeries.setName("Services par Location");
        for (Map.Entry<Location, Integer> entry : locationStats.entrySet()) {
            locationSeries.getData().add(new XYChart.Data<>(entry.getKey().getLabel(), entry.getValue()));
        }
        locationBarChart.getData().add(locationSeries);

        // Récupérer les statistiques par TypeService depuis la base de données
        Map<TypeService, Integer> typeServiceStats = serviceservice.getTypeServiceStats();

        XYChart.Series<String, Number> typeServiceSeries = new XYChart.Series<>();
        typeServiceSeries.setName("Services par Type");
        for (Map.Entry<TypeService, Integer> entry : typeServiceStats.entrySet()) {
            typeServiceSeries.getData().add(new XYChart.Data<>(entry.getKey().getLabel(), entry.getValue()));
        }
        typeServiceBarChart.getData().add(typeServiceSeries);
    }
    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Feedback.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
