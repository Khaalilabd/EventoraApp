package Gui.Service.Controllers;
import Services.Service.Crud.ServiceService;
import Models.Service.Location;
import Models.Service.TypeService;
import Services.Service.Crud.ServiceService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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
}
