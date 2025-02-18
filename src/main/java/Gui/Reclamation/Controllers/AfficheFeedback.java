package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.FeedBackService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private TableColumn<Feedback, String> colRecommend;
    @FXML
    private TableColumn<Feedback, String> colActions;

    private FeedBackService feedbackService;

    public AfficheFeedback() {
        this.feedbackService = new FeedBackService();
    }

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colUser.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject());
        colVote.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVote()).asObject());
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        colRecommend.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRecommend().name()));

        addActionsColumn();
        loadFeedbacks();
    }

    private void loadFeedbacks() {
        try {
            List<Feedback> feedbackList = feedbackService.RechercherFeedBack();
            Platform.runLater(() -> feedbackTable.getItems().setAll(feedbackList));
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur de récupération des feedbacks
        }
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(new Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>() {
            @Override
            public TableCell<Feedback, String> call(TableColumn<Feedback, String> param) {
                return new TableCell<Feedback, String>() {
                    final Button editButton = new Button("Modifier");
                    final Button deleteButton = new Button("Supprimer");
                    final HBox hBox = new HBox(10);

                    {
                        hBox.getChildren().addAll(editButton, deleteButton);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            Feedback feedback = getTableRow().getItem();
                            editButton.setOnAction(event -> handleEdit(feedback));
                            deleteButton.setOnAction(event -> handleDelete(feedback));
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void handleEdit(Feedback feedback) {
        // Gérer la modification du feedback
        System.out.println("Modifier le feedback avec ID : " + feedback.getId());
    }

    private void handleDelete(Feedback feedback) {
        // Gérer la suppression du feedback
        System.out.println("Supprimer le feedback avec ID : " + feedback.getId());
        feedbackService.SupprimerFeedBack(feedback);
        loadFeedbacks();  // Recharger les feedbacks après suppression

        // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Suppression réussie");
        alert.setHeaderText(null);
        alert.setContentText("Le feedback a été supprimé avec succès.");
        alert.showAndWait();
    }

    @FXML
    private void goToAjouterFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFeedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene scene = new Scene(feedbackLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void refreshList() {
        loadFeedbacks();  // Recharger les feedbacks
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
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
