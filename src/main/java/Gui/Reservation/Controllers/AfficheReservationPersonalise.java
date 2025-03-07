package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Models.Service.Service;
import Services.Reservation.Crud.ReservationPersonaliseService;
import Services.Service.Crud.ServiceService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AfficheReservationPersonalise {

    @FXML
    private TextField searchField;

    @FXML
    private GridPane reservationGrid;

    @FXML
    private Label totalLabel;

    private final ReservationPersonaliseService reservationService;
    private final ServiceService serviceService;

    public AfficheReservationPersonalise() {
        this.reservationService = new ReservationPersonaliseService();
        this.serviceService = new ServiceService();
    }

    @FXML
    public void initialize() {
        loadReservations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterReservations(newValue));
    }

    private void loadReservations() {
        try {
            reservationGrid.getChildren().clear();
            List<ReservationPersonalise> reservations = reservationService.rechercherReservationPersonalise();
            populateGrid(reservations);
            updateStatistics(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Impossible de charger les réservations", e.getMessage());
        }
    }

    private void populateGrid(List<ReservationPersonalise> reservations) {
        reservationGrid.getChildren().clear();
        reservationGrid.getColumnConstraints().clear();
        int column = 0;
        int row = 0;
        for (ReservationPersonalise reservation : reservations) {
            VBox bubble = createBubble(reservation);
            reservationGrid.add(bubble, column, row);
            column++;
            if (column >= 3) { // Limite à 3 bulles par ligne
                column = 0;
                row++;
            }
        }
        for (int i = 0; i < 3; i++) { // Définit 3 colonnes
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(33.33); // Chaque colonne prend 1/3 de la largeur
            reservationGrid.getColumnConstraints().add(colConst);
        }
    }

    private void updateStatistics(List<ReservationPersonalise> reservations) {
        long total = reservations.size();
        totalLabel.setText("Total: " + total);
    }

    private VBox createBubble(ReservationPersonalise reservation) {
        VBox bubble = new VBox(10);
        bubble.getStyleClass().add("bubble");
        bubble.setAlignment(Pos.CENTER);
        bubble.setPrefWidth(200);
        bubble.setPrefHeight(300);

        // Ajouter tous les détails de la réservation dans la bulle
        Label idLabel = new Label("ID: " + reservation.getIdReservationPersonalise());
        idLabel.getStyleClass().add("bubble-label");
        Label nomLabel = new Label("Nom: " + reservation.getNom());
        nomLabel.getStyleClass().add("bubble-label");
        Label prenomLabel = new Label("Prénom: " + reservation.getPrenom());
        prenomLabel.getStyleClass().add("bubble-label");
        Label emailLabel = new Label("Email: " + reservation.getEmail());
        emailLabel.getStyleClass().add("bubble-label");
        emailLabel.setWrapText(true);
        Label numTelLabel = new Label("Num. Téléphone: " + reservation.getNumtel());
        numTelLabel.getStyleClass().add("bubble-label");
        Label descLabel = new Label("Description: " + reservation.getDescription());
        descLabel.getStyleClass().add("bubble-label");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(50);
        Label dateLabel = new Label("Date: " + reservation.getDate().toString());
        dateLabel.getStyleClass().add("bubble-label");

        // Ajouter les services associés
        List<Integer> serviceIds = reservation.getServiceIds();
        String serviceTitles = "Aucun service";
        if (serviceIds != null && !serviceIds.isEmpty()) {
            List<Service> allServices = serviceService.RechercherService();
            serviceTitles = serviceIds.stream()
                    .map(id -> allServices.stream()
                            .filter(s -> s.getId() == id)
                            .findFirst()
                            .map(Service::getTitre)
                            .orElse("Service inconnu (" + id + ")"))
                    .collect(Collectors.joining(", "));
        }
        Label servicesLabel = new Label("Services: " + serviceTitles);
        servicesLabel.getStyleClass().add("bubble-label");
        servicesLabel.setWrapText(true);

        // Ajouter les boutons d'action directement dans la bulle
        HBox actionButtons = new HBox(5);
        actionButtons.setAlignment(Pos.CENTER);
        Button editButton = createIconButton("/Images/modif.png", () -> handleEdit(reservation));
        Button deleteButton = createIconButton("/Images/supp.png", () -> handleDelete(reservation));
        Button exportButton = createIconButton("/Images/pdf.png", () -> handleExport(reservation));
        actionButtons.getChildren().addAll(editButton, deleteButton, exportButton);

        // Ajouter tous les éléments dans la bulle
        bubble.getChildren().addAll(idLabel, nomLabel, prenomLabel, emailLabel, numTelLabel, descLabel, dateLabel, servicesLabel, actionButtons);

        // Ajouter un événement de clic pour ouvrir une fenêtre avec les détails complets
        bubble.setOnMouseClicked(event -> showDetailsWindow(reservation));

        // Ajouter une animation de flottement
        TranslateTransition floatAnimation = new TranslateTransition(Duration.seconds(2), bubble);
        floatAnimation.setByY(-10);
        floatAnimation.setAutoReverse(true);
        floatAnimation.setCycleCount(TranslateTransition.INDEFINITE);
        floatAnimation.play();

        return bubble;
    }

    private Button createIconButton(String iconPath, Runnable action) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("action-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showDetailsWindow(ReservationPersonalise reservation) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL); // Fenêtre modale
        detailsStage.setTitle("Détails de la réservation personnalisée");

        VBox detailsLayout = new VBox(10);
        detailsLayout.setAlignment(Pos.CENTER);
        detailsLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        // Ajouter tous les détails dans des labels
        Label titleLabel = new Label("Détails de la réservation personnalisée");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label idLabel = new Label("ID: " + reservation.getIdReservationPersonalise());
        Label nomLabel = new Label("Nom: " + reservation.getNom());
        Label prenomLabel = new Label("Prénom: " + reservation.getPrenom());
        Label emailLabel = new Label("Email: " + reservation.getEmail());
        Label numTelLabel = new Label("Num. Téléphone: " + reservation.getNumtel());
        Label descLabel = new Label("Description: " + reservation.getDescription());
        descLabel.setWrapText(true);
        Label dateLabel = new Label("Date: " + reservation.getDate().toString());

        // Ajouter les services associés
        List<Integer> serviceIds = reservation.getServiceIds();
        String serviceTitles = "Aucun service";
        if (serviceIds != null && !serviceIds.isEmpty()) {
            List<Service> allServices = serviceService.RechercherService();
            serviceTitles = serviceIds.stream()
                    .map(id -> allServices.stream()
                            .filter(s -> s.getId() == id)
                            .findFirst()
                            .map(Service::getTitre)
                            .orElse("Service inconnu (" + id + ")"))
                    .collect(Collectors.joining(", "));
        }
        Label servicesLabel = new Label("Services: " + serviceTitles);
        servicesLabel.setWrapText(true);

        // Bouton pour exporter les détails en PDF
        Button exportButton = new Button("Exporter");
        exportButton.setOnAction(e -> handleExport(reservation));

        // Bouton pour fermer la fenêtre
        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> detailsStage.close());

        // Ajouter tous les éléments au layout
        detailsLayout.getChildren().addAll(
                titleLabel, idLabel, nomLabel, prenomLabel,
                emailLabel, numTelLabel, descLabel, dateLabel, servicesLabel, exportButton, closeButton
        );

        Scene scene = new Scene(detailsLayout, 400, 500);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }

    private void filterReservations(String keyword) {
        try {
            reservationGrid.getChildren().clear();
            List<ReservationPersonalise> reservations;
            if (keyword == null || keyword.trim().isEmpty()) {
                reservations = reservationService.rechercherReservationPersonalise();
            } else {
                String lowerCaseKeyword = keyword.toLowerCase();
                reservations = reservationService.rechercherReservationPersonalise().stream()
                        .filter(res -> res.getNom().toLowerCase().contains(lowerCaseKeyword) ||
                                res.getPrenom().toLowerCase().contains(lowerCaseKeyword) ||
                                res.getEmail().toLowerCase().contains(lowerCaseKeyword))
                        .collect(Collectors.toList());
            }
            populateGrid(reservations);
            updateStatistics(reservations);
        } catch (Exception e) {
            showErrorAlert("Impossible de filtrer les réservations", e.getMessage());
        }
    }

    private void handleEdit(ReservationPersonalise reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/ModifierReservationPersonalise.fxml"));
            AnchorPane modifReservationLayout = loader.load();
            ModifierReservationPersonalise controller = loader.getController();
            controller.setReservationToEdit(reservation);
            Scene currentScene = reservationGrid.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la modification", e.getMessage());
        }
    }

    private void handleDelete(ReservationPersonalise reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationService.supprimerReservationPersonalise(reservation);
                    loadReservations();
                } catch (Exception e) {
                    showErrorAlert("Impossible de supprimer la réservation", e.getMessage());
                }
            }
        });
    }

    private void handleExport(ReservationPersonalise reservation) {
        try {
            String homeDir = System.getProperty("user.home");
            String filePath = homeDir + File.separator + "ReservationPersonalise_" + reservation.getIdReservationPersonalise() + ".pdf";

            List<Map<String, String>> data = new ArrayList<>();
            Map<String, String> reservationData = new LinkedHashMap<>();
            reservationData.put("ID", String.valueOf(reservation.getIdReservationPersonalise()));
            reservationData.put("Name", reservation.getNom() + " " + reservation.getPrenom());
            reservationData.put("Email", reservation.getEmail());
            reservationData.put("Phone Number", reservation.getNumtel());
            reservationData.put("Description", reservation.getDescription());
            reservationData.put("Date", reservation.getDate().toString());

            List<Integer> serviceIds = reservation.getServiceIds();
            String serviceTitles = "Aucun service";
            if (serviceIds != null && !serviceIds.isEmpty()) {
                List<Service> allServices = serviceService.RechercherService();
                serviceTitles = serviceIds.stream()
                        .map(id -> allServices.stream()
                                .filter(s -> s.getId() == id)
                                .findFirst()
                                .map(Service::getTitre)
                                .orElse("Service inconnu (" + id + ")"))
                        .collect(Collectors.joining(", "));
            }
            reservationData.put("Services", serviceTitles);

            data.add(reservationData);

            String logoPath = "/Images/EVENTORA.png";

            PdfTemplateUtil.generatePdf(filePath, "Reservation Personalise Details", data, logoPath);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Reservation details exported to " + filePath);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'exportation", e.getMessage());
        }
    }

    private void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addReservationPersonalise() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AjouterReservationPersonalise.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une réservation personnalisée");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de l'ajout", e.getMessage());
        }
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        loadReservations();
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        switchScene("/Reservation/Reservation.fxml", event);
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Reclamation.fxml", event);
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Feedback.fxml", event);
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        switchScene("/Service/Service.fxml", event);
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
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