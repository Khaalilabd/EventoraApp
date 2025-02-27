package Gui.Reclamation.Controllers;

import Models.Reclamation.Statut;
import Services.Reclamation.Crud.ReclamationService;
import Models.Reclamation.Reclamation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AfficheRec {

    @FXML private TableView<Reclamation> tableView;
    @FXML private TableColumn<Reclamation, Integer> colId;
    @FXML private TableColumn<Reclamation, String> colTitre;
    @FXML private TableColumn<Reclamation, String> colDescription;
    @FXML private TableColumn<Reclamation, String> colType;
    @FXML private TableColumn<Reclamation, String> colActions;
    @FXML private TableColumn<Reclamation, String> colStatut;
    @FXML private TableColumn<Reclamation, String> colQrCode;
    @FXML private TextField searchField;

    private ReclamationService reclamationService;

    public AfficheRec() {
        this.reclamationService = new ReclamationService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getLabel()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().getLabel()));
        addActionsColumn();
        addQrCodeColumn();
        loadReclamations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchReclamation(newValue));
    }

    private void loadReclamations() {
        try {
            List<Reclamation> reclamations = reclamationService.RechercherRec();
            tableView.getItems().setAll(reclamations);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger les réclamations");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reclamation, String> call(TableColumn<Reclamation, String> param) {
                return new TableCell<>() {
                    final Button editButton = new Button();
                    final Button deleteButton = new Button();
                    final Button btnResolved = new Button("✅");
                    final Button btnInProgress = new Button("⏳");
                    final Button btnRejected = new Button("❌");
                    final HBox hBox = new HBox(10);

                    {
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
                        editButton.getStyleClass().add("table-button");
                        deleteButton.getStyleClass().addAll("table-button", "delete");
                        btnResolved.setOnAction(event -> handleStatutChange(getTableRow().getItem(), Statut.RESOLUE));
                        btnInProgress.setOnAction(event -> handleStatutChange(getTableRow().getItem(), Statut.EN_COURS));
                        btnRejected.setOnAction(event -> handleStatutChange(getTableRow().getItem(), Statut.REJETEE));
                        hBox.getChildren().addAll(editButton, deleteButton, btnResolved, btnRejected, btnInProgress);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                            deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                            setGraphic(hBox);
                        }
                    }
                    private void handleStatutChange(Reclamation reclamation, Statut newStatut) {
                        if (reclamation != null) {
                            reclamation.setStatut(newStatut);
                            try {
                                reclamationService.ModifierStatut(reclamation);
                                loadReclamations();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Impossible de modifier le statut");
                                alert.setContentText(e.getMessage());
                                alert.showAndWait();
                            }
                        }
                    }
                };
            }
        });
    }

    private void addQrCodeColumn() {
        colQrCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQrCodeUrl()));
        colQrCode.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reclamation, String> call(TableColumn<Reclamation, String> param) {
                return new TableCell<>() {
                    final Button qrButton = new Button("Voir QR");
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            qrButton.setOnAction(event -> showQRCode(getTableRow().getItem()));
                            setGraphic(qrButton);
                        }
                    }
                };
            }
        });
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'afficher le QR Code");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger la fenêtre de suivi");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleEdit(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ModifierRec.fxml"));
            AnchorPane modifRecLayout = loader.load();
            ModifierRec controller = loader.getController();
            controller.setReclamationToEdit(reclamation);
            Scene currentScene = tableView.getScene();
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
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Erreur");
                        errorAlert.setHeaderText("Impossible de supprimer la réclamation");
                        errorAlert.setContentText(e.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        }
    }

    private void searchReclamation(String motCle) {
        try {
            List<Reclamation> resultatRecherche = reclamationService.RechercherRecParMotCle(motCle.toLowerCase());
            ObservableList<Reclamation> data = FXCollections.observableArrayList(resultatRecherche);
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de rechercher les réclamations");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
        loadReclamations();
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
    private void goToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Feedback.fxml"));
            AnchorPane feedbackLayout = loader.load();
            Scene feedbackScene = new Scene(feedbackLayout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(feedbackScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
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
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAccueil(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventoraAPP/EventoraAPP.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}