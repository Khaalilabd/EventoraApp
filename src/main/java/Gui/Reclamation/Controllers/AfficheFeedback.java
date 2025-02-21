package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.FeedBackService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.util.Callback;
import java.io.IOException;
import java.util.List;

public class AfficheFeedback {
    @FXML
    private TableView<Feedback> feedbackTable;
    @FXML
    private TableColumn<Feedback, Integer> colId;
    @FXML
    private TableColumn<Feedback, Integer> colUser;
    @FXML
    private TableColumn<Feedback, Integer> colVote;
    @FXML
    private TableColumn<Feedback, String> colDescription;
    @FXML
    private TableColumn<Feedback, Recommend> colRecommend;
    @FXML
    private TableColumn<Feedback, String> colActions;
    @FXML
    private TextField searchField;

    private FeedBackService feedbackService;

    public AfficheFeedback() {
        this.feedbackService = new FeedBackService();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colUser.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject());
        colVote.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVote()).asObject());
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        colRecommend.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRecommend()));
        colRecommend.setCellFactory(param -> new TableCell<Feedback, Recommend>() {
            @Override
            protected void updateItem(Recommend item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLabel());
                }
            }
        });
        addActionsColumn();
        loadFeedbacks();
    }


    private void loadFeedbacks() {
        try {
            List<Feedback> feedbackList = feedbackService.RechercherFeedBack();
            Platform.runLater(() -> feedbackTable.getItems().setAll(feedbackList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));
        colActions.setCellFactory(new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>() {
            @Override
            public TableCell<Feedback, String> call(TableColumn<Feedback, String> param) {
                return new TableCell<Feedback, String>() {
                    final ImageView editImageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/modif.png")));
                    final ImageView deleteImageView = new ImageView(new Image(getClass().getResourceAsStream("/Images/supp.png")));
                    final HBox hBox = new HBox(10);
                    {
                        editImageView.setPreserveRatio(true);
                        deleteImageView.setPreserveRatio(true);
                        hBox.getChildren().addAll(editImageView, deleteImageView);
                        hBox.setAlignment(Pos.CENTER);
                        hBox.setSpacing(15);
                        editImageView.getStyleClass().add("table-icon");
                        deleteImageView.getStyleClass().addAll("table-icon", "delete");
                        editImageView.fitWidthProperty().bind(widthProperty().multiply(0.4));
                        deleteImageView.fitWidthProperty().bind(widthProperty().multiply(0.4));
                        editImageView.fitHeightProperty().bind(heightProperty());
                        deleteImageView.fitHeightProperty().bind(heightProperty());
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            Feedback feedback = getTableRow().getItem();
                            editImageView.setOnMouseClicked(event -> handleEdit(feedback));
                            deleteImageView.setOnMouseClicked(event -> handleDelete(feedback));
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }


    private void handleEdit(Feedback feedback) {
        System.out.println("Modifier le feedback avec ID : " + feedback.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ModifierFeedback.fxml"));
            AnchorPane modifierFeedbackLayout = loader.load();
            ModifierFeedback controller = loader.getController();
            controller.setFeedback(feedback); // Passer l'objet Feedback au contrôleur de modification
            Scene scene = new Scene(modifierFeedbackLayout);
            Stage stage = (Stage) feedbackTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Feedback feedback) {
        if (feedback != null) {
            // Créer une fenêtre de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce feedback ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Supprimer le feedback avec ID : " + feedback.getId());
                    feedbackService.SupprimerFeedBack(feedback);
                    loadFeedbacks();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Le feedback a été supprimé avec succès.");
                    successAlert.showAndWait();
                }
            });
        }
    }


    @FXML
    private void goToAjouterFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/AjouterFeedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene scene = new Scene(feedbackLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void refreshList() {
        loadFeedbacks();
    }
    @FXML
    private void handleShowPieChart(ActionEvent event) {
        int ouiCount = 0;
        int nonCount = 0;

        for (Feedback feedback : feedbackTable.getItems()) {
            if (feedback.getRecommend() == Recommend.Oui) {
                ouiCount++;
            } else if (feedback.getRecommend() == Recommend.Non) {
                nonCount++;
            }
        }
        PieChart.Data ouiData = new PieChart.Data("Oui (" + ouiCount + ")", ouiCount);
        PieChart.Data nonData = new PieChart.Data("Non (" + nonCount + ")", nonCount);
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(ouiData, nonData);
        pieChart.setTitle("Répartition des Recommandations");
        for (PieChart.Data data : pieChart.getData()) {
            if (data.getName().startsWith("Oui")) {
                data.getNode().setStyle("-fx-pie-color: pink;");
            } else if (data.getName().startsWith("Non")) {
                data.getNode().setStyle("-fx-pie-color: #11a791;");
            }
        }
        Stage chartStage = new Stage();
        chartStage.setTitle("Graphique des Recommandations");
        chartStage.setScene(new Scene(pieChart, 600, 400));
        chartStage.show();
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
    private void searchFeedback(ActionEvent event) {
        String motCle = searchField.getText().toLowerCase();
        List<Feedback> resultatRecherche = feedbackService.rechercherParMotCle(motCle);
        ObservableList<Feedback> data = FXCollections.observableArrayList(resultatRecherche);
        feedbackTable.setItems(data);
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
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
