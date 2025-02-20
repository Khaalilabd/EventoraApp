package Gui.Service.Controllers;

import Services.Service.Crud.ServiceService;
import Models.Service.Location;
import Models.Service.TypeService;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class Statistique {

    @FXML
    private AreaChart<String, Number> locationAreaChart;

    @FXML
    private AreaChart<String, Number> typeServiceAreaChart;

    @FXML
    private CategoryAxis locationXAxis;

    @FXML
    private NumberAxis locationYAxis;

    @FXML
    private CategoryAxis typeServiceXAxis;

    @FXML
    private NumberAxis typeServiceYAxis;

    private final ServiceService serviceService = new ServiceService();

    // Initialisation des graphiques
    public void initialize() {
        // Récupérer les statistiques par Location et les ajouter au graphique
        Map<Location, Integer> locationStats = serviceService.getLocationStats();
        populateAreaChart(locationAreaChart, locationStats, "Services par Location");

        // Récupérer les statistiques par TypeService et les ajouter au graphique
        Map<TypeService, Integer> typeServiceStats = serviceService.getTypeServiceStats();
        populateAreaChart(typeServiceAreaChart, typeServiceStats, "Services par Type");
    }

    // Méthode générique pour remplir un AreaChart avec des données
    private <T> void populateAreaChart(AreaChart<String, Number> chart, Map<T, Integer> data, String seriesName) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (Map.Entry<T, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        chart.getData().add(series);
    }
}
