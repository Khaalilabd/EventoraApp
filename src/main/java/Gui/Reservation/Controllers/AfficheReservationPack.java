package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import Gui.Reservation.Controllers.PdfTemplateUtil;
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

public class AfficheReservationPack {

    @FXML
    private TextField searchField;
    @FXML
    private GridPane reservationGrid;
    @FXML
    private Label totalLabel;

    private final ReservationPackService reservationservice;

    public AfficheReservationPack() {
        this.reservationservice = new ReservationPackService();
    }

    @FXML
    public void initialize() {
        loadReservations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterReservations(newValue));
    }

    private void loadReservations() {
        try {
            reservationGrid.getChildren().clear();
            List<ReservationPack> reservations = reservationservice.rechercherReservationPack();
            populateGrid(reservations);
            updateStatistics(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Impossible de charger les réservations", e.getMessage());
        }
    }

    private void populateGrid(List<ReservationPack> reservations) {
        reservationGrid.getChildren().clear();
        reservationGrid.getColumnConstraints().clear();
        int column = 0;
        int row = 0;
        for (ReservationPack reservation : reservations) {
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

    private void updateStatistics(List<ReservationPack> reservations) {
        long total = reservations.size();
        totalLabel.setText("Total: " + total);
    }

    private VBox createBubble(ReservationPack reservation) {
        VBox bubble = new VBox(10);
        bubble.getStyleClass().add("bubble");
        bubble.setAlignment(Pos.CENTER);
        bubble.setPrefWidth(200);
        bubble.setPrefHeight(300);

        Label idLabel = new Label("ID: " + reservation.getIdReservationPack());
        idLabel.getStyleClass().add("bubble-label");
        Label packLabel = new Label("Pack ID: " + reservation.getIdPack());
        packLabel.getStyleClass().add("bubble-label");
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

        HBox actionButtons = new HBox(5);
        actionButtons.setAlignment(Pos.CENTER);
        Button editButton = createIconButton("/Images/modif.png", () -> handleEdit(reservation));
        Button deleteButton = createIconButton("/Images/supp.png", () -> handleDelete(reservation));
        Button exportButton = createIconButton("/Images/pdf.png", () -> handleExport(reservation));
        actionButtons.getChildren().addAll(editButton, deleteButton, exportButton);

        bubble.getChildren().addAll(idLabel, packLabel, nomLabel, prenomLabel, emailLabel, numTelLabel, descLabel, dateLabel, actionButtons);

        bubble.setOnMouseClicked(event -> showDetailsWindow(reservation));

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

    private void showDetailsWindow(ReservationPack reservation) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("Détails de la réservation");

        VBox detailsLayout = new VBox(10);
        detailsLayout.setAlignment(Pos.CENTER);
        detailsLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("Détails de la réservation");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label idLabel = new Label("ID: " + reservation.getIdReservationPack());
        Label packLabel = new Label("Pack ID: " + reservation.getIdPack());
        Label nomLabel = new Label("Nom: " + reservation.getNom());
        Label prenomLabel = new Label("Prénom: " + reservation.getPrenom());
        Label emailLabel = new Label("Email: " + reservation.getEmail());
        Label numTelLabel = new Label("Num. Téléphone: " + reservation.getNumtel());
        Label descLabel = new Label("Description: " + reservation.getDescription());
        descLabel.setWrapText(true);
        Label dateLabel = new Label("Date: " + reservation.getDate().toString());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button exportButton = new Button("Exporter");
        exportButton.setOnAction(e -> handleExport(reservation));

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> detailsStage.close());

        buttonBox.getChildren().addAll(exportButton, closeButton);

        detailsLayout.getChildren().addAll(
                titleLabel, idLabel, packLabel, nomLabel, prenomLabel,
                emailLabel, numTelLabel, descLabel, dateLabel, buttonBox
        );

        Scene scene = new Scene(detailsLayout, 400, 500);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }

    private void filterReservations(String keyword) {
        try {
            reservationGrid.getChildren().clear();
            List<ReservationPack> reservations;
            if (keyword == null || keyword.trim().isEmpty()) {
                reservations = reservationservice.rechercherReservationPack();
            } else {
                String lowerCaseKeyword = keyword.toLowerCase();
                reservations = reservationservice.rechercherReservationPack().stream()
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

    private void handleEdit(ReservationPack reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/ModifierReservationPack.fxml"));
            AnchorPane modifReservationLayout = loader.load();
            ModifierReservationPack controller = loader.getController();
            controller.setReservationToEdit(reservation);
            Scene currentScene = reservationGrid.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement de la modification", e.getMessage());
        }
    }

    private void handleDelete(ReservationPack reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationservice.supprimerReservationPack(reservation);
                    loadReservations();
                } catch (Exception e) {
                    showErrorAlert("Impossible de supprimer la réservation", e.getMessage());
                }
            }
        });
    }

    private void handleExport(ReservationPack reservation) {
        try {
            String homeDir = System.getProperty("user.home");
            String filePath = homeDir + File.separator + "ReservationPack_" + reservation.getIdReservationPack() + ".pdf";

            List<Map<String, String>> data = new ArrayList<>();
            Map<String, String> reservationData = new LinkedHashMap<>();
            reservationData.put("ID", String.valueOf(reservation.getIdReservationPack()));
            reservationData.put("Pack ID", String.valueOf(reservation.getIdPack()));
            reservationData.put("Name", reservation.getNom() + " " + reservation.getPrenom());
            reservationData.put("Email", reservation.getEmail());
            reservationData.put("Phone Number", reservation.getNumtel());
            reservationData.put("Description", reservation.getDescription());
            reservationData.put("Date", reservation.getDate().toString());
            data.add(reservationData);

            String logoPath = "/Images/EVENTORA.png";

            PdfTemplateUtil.generatePdf(filePath, "Reservation Pack Details", data, logoPath);

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
    private void addReservationPack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AjouterReservationPack.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une réservation");
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