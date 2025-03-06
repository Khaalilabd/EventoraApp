package Gui.Reclamation.Controllers;

import Models.Reclamation.Statut;
import Services.Reclamation.Crud.ReclamationService;
import Models.Reclamation.Reclamation;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficheRec {

    @FXML private GridPane reclamationGrid;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Label totalLabel, resolvedLabel, inProgressLabel, pendingLabel, rejectedLabel;

    private ReclamationService reclamationService;

    public AfficheRec() {
        this.reclamationService = new ReclamationService();
    }

    @FXML
    public void initialize() {
        reclamationGrid.getStyleClass().add("transparent-grid");
        loadReclamations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchReclamation(newValue));
        statusFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterByStatus(newVal));
        statusFilter.getSelectionModel().selectFirst();
    }

    private void loadReclamations() {
        try {
            reclamationGrid.getChildren().clear();
            List<Reclamation> reclamations = reclamationService.RechercherRec();
            populateGrid(reclamations);
            updateStatistics(reclamations);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Impossible de charger les réclamations", e.getMessage());
        }
    }

    private void populateGrid(List<Reclamation> reclamations) {
        reclamationGrid.getChildren().clear();
        reclamationGrid.getColumnConstraints().clear();
        int column = 0;
        int row = 0;
        for (Reclamation reclamation : reclamations) {
            VBox card = createReclamationCard(reclamation);
            reclamationGrid.add(card, column, row);
            column++;
            if (column >= 8) { // Limite à 3 cartes par ligne
                column = 0;
                row++;
            }
        }
        for (int i = 0; i < 8; i++) { // Définit 3 colonnes
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(33.33); // Chaque colonne prend 1/3 de la largeur
            reclamationGrid.getColumnConstraints().add(colConst);
        }
    }

    private void updateStatistics(List<Reclamation> reclamations) {
        long total = reclamations.size();
        long resolved = reclamations.stream().filter(r -> r.getStatut() == Statut.RESOLUE).count();
        long inProgress = reclamations.stream().filter(r -> r.getStatut() == Statut.EN_COURS).count();
        long pending = reclamations.stream().filter(r -> r.getStatut() == Statut.EN_ATTENTE).count();
        long rejected = reclamations.stream().filter(r -> r.getStatut() == Statut.REJETEE).count();

        totalLabel.setText("Total: " + total);
        resolvedLabel.setText("Résolues: " + resolved);
        inProgressLabel.setText("En cours: " + inProgress);
        pendingLabel.setText("En attente: " + pending);
        rejectedLabel.setText("Rejetées: " + rejected);
    }

    private VBox createReclamationCard(Reclamation reclamation) {
        VBox card = new VBox(10);
        card.getStyleClass().add("reclamation-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(300);

        Label idLabel = new Label("ID: " + reclamation.getId());
        idLabel.getStyleClass().add("card-id");
        Label titleLabel = new Label("Titre: " + reclamation.getTitre());
        titleLabel.getStyleClass().add("card-title");
        Label descLabel = new Label("Desc: " + reclamation.getDescription());
        descLabel.getStyleClass().add("card-description");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(50);
        Label typeLabel = new Label("Type: " + reclamation.getType().getLabel());
        typeLabel.getStyleClass().add("card-type");
        Label statutLabel = new Label("Statut: " + reclamation.getStatut().getLabel());
        statutLabel.getStyleClass().add("card-statut");

        switch (reclamation.getStatut()) {
            case RESOLUE: statutLabel.getStyleClass().add("statut-resolue"); break;
            case EN_COURS: statutLabel.getStyleClass().add("statut-en-cours"); break;
            case EN_ATTENTE: statutLabel.getStyleClass().add("statut-en-attente"); break;
            case REJETEE: statutLabel.getStyleClass().add("statut-rejetee"); break;
        }

        HBox actionButtons = new HBox(5);
        actionButtons.setAlignment(Pos.CENTER);
        Button editButton = createIconButton("/Images/modif.png", () -> handleEdit(reclamation));
        Button deleteButton = createIconButton("/Images/supp.png", () -> handleDelete(reclamation));
        Button btnResolved = new Button("✅");
        Button btnInProgress = new Button("⏳");
        Button btnRejected = new Button("❌");
        btnResolved.setOnAction(e -> handleStatutChange(reclamation, Statut.RESOLUE));
        btnInProgress.setOnAction(e -> handleStatutChange(reclamation, Statut.EN_COURS));
        btnRejected.setOnAction(e -> handleStatutChange(reclamation, Statut.REJETEE));
        actionButtons.getChildren().addAll(editButton, deleteButton, btnResolved, btnInProgress, btnRejected);

        Button qrButton = new Button("Voir QR");
        qrButton.getStyleClass().add("qr-button");
        qrButton.setOnAction(e -> showQRCode(reclamation));

        card.getChildren().addAll(idLabel, titleLabel, descLabel, typeLabel, statutLabel, actionButtons, qrButton);
        return card;
    }

    private Button createIconButton(String iconPath, Runnable action) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("table-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void handleStatutChange(Reclamation reclamation, Statut newStatut) {
        if (reclamation != null) {
            reclamation.setStatut(newStatut);
            try {
                reclamationService.ModifierStatut(reclamation);
                loadReclamations();
            } catch (Exception e) {
                showErrorAlert("Impossible de modifier le statut", e.getMessage());
            }
        }
    }

    private void showQRCode(Reclamation reclamation) {
        try {
            File qrFile = new File("qr_reclamation_" + reclamation.getId() + ".png");
            if (qrFile.exists()) {
                Image qrImage = new Image(qrFile.toURI().toString());
                ImageView imageView = new ImageView(qrImage);
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                Button scanButton = new Button("Simuler le scan");
                scanButton.setOnAction(e -> showSuivi(reclamation));
                scanButton.getStyleClass().add("card-button");
                VBox content = new VBox(10, imageView, scanButton);
                content.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("QR Code de suivi");
                alert.setHeaderText("Scannez ce QR code pour suivre la réclamation");
                alert.getDialogPane().setContent(content);
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                alert.showAndWait();
            } else {
                System.out.println("Fichier QR non trouvé pour l'ID : " + reclamation.getId());
            }
        } catch (Exception e) {
            showErrorAlert("Impossible d'afficher le QR Code", e.getMessage());
        }
    }

    private void showSuivi(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/SuiviRec.fxml"));
            AnchorPane suiviLayout = loader.load();
            SuiviRec controller = loader.getController();
            controller.setReclamation(reclamation);
            Scene suiviScene = new Scene(suiviLayout, 500, 400);
            Stage suiviStage = new Stage();
            suiviStage.setTitle("Suivi de la réclamation");
            suiviStage.setScene(suiviScene);
            suiviStage.show();
        } catch (IOException e) {
            showErrorAlert("Impossible de charger la fenêtre de suivi", e.getMessage());
        }
    }

    private void handleEdit(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ModifierRec.fxml"));
            AnchorPane modifRecLayout = loader.load();
            ModifierRec controller = loader.getController();
            controller.setReclamationToEdit(reclamation);
            Scene currentScene = reclamationGrid.getScene();
            currentScene.setRoot(modifRecLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(Reclamation reclamation) {
        if (reclamation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        reclamationService.SupprimerRec(reclamation);
                        loadReclamations();
                    } catch (Exception e) {
                        showErrorAlert("Impossible de supprimer la réclamation", e.getMessage());
                    }
                }
            });
        }
    }

    private void searchReclamation(String motCle) {
        try {
            reclamationGrid.getChildren().clear();
            List<Reclamation> resultatRecherche = reclamationService.RechercherRecParMotCle(motCle.toLowerCase());
            String selectedStatus = statusFilter.getValue();
            if (selectedStatus != null && !"Tous".equals(selectedStatus)) {
                resultatRecherche = resultatRecherche.stream()
                        .filter(r -> r.getStatut().getLabel().equals(selectedStatus))
                        .collect(Collectors.toList());
            }
            populateGrid(resultatRecherche);
            updateStatistics(resultatRecherche);
        } catch (Exception e) {
            showErrorAlert("Impossible de rechercher les réclamations", e.getMessage());
        }
    }

    private void filterByStatus(String status) {
        try {
            reclamationGrid.getChildren().clear();
            List<Reclamation> reclamations;
            if ("Tous".equals(status)) {
                reclamations = reclamationService.RechercherRec();
            } else {
                reclamations = reclamationService.RechercherRec().stream()
                        .filter(r -> r.getStatut().getLabel().equals(status))
                        .collect(Collectors.toList());
            }
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                reclamations = reclamations.stream()
                        .filter(r -> r.getTitre().toLowerCase().contains(searchText.toLowerCase()) ||
                                r.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
            }
            populateGrid(reclamations);
            updateStatistics(reclamations);
        } catch (Exception e) {
            showErrorAlert("Impossible de filtrer les réclamations", e.getMessage());
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
    private void addReclamation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AjouterRec.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ajouter une réclamation");
        stage.show();
    }

    @FXML
    private void refreshList() {
        searchField.clear();
        statusFilter.getSelectionModel().selectFirst(); // Reset à "Tous"
        loadReclamations();
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
    private void goToReservation(ActionEvent event) throws IOException {
        switchScene("/Reservation/Reservation.fxml", event);
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        switchScene("/Service/Service.fxml", event);
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
    }

    @FXML
    private void goToAccueil(ActionEvent event) throws IOException {
        switchScene("/EventoraAPP/EventoraAPP.fxml", event);
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