package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.FeedBackService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;  // Importer l'énumération Recommend
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.util.Callback;
import javafx.util.StringConverter;

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
    private TableColumn<Feedback, Recommend> colRecommend;  // Utiliser Recommend au lieu de String
    @FXML
    private TableColumn<Feedback, String> colActions;

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

        // Lier colRecommend à l'énumération Recommend et formater l'affichage avec un StringConverter
        colRecommend.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRecommend()));
        colRecommend.setCellFactory(param -> new TableCell<Feedback, Recommend>() {
            @Override
            protected void updateItem(Recommend item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLabel());  // Afficher le libellé de l'énumération
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
            // Gérer l'erreur de récupération des feedbacks
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
                        // Redimensionner les icônes pour qu'elles occupent toute la taille de la cellule
                        editImageView.setPreserveRatio(true);
                        deleteImageView.setPreserveRatio(true);

                        // Ajouter les icônes dans le HBox
                        hBox.getChildren().addAll(editImageView, deleteImageView);

                        // Centrer les icônes dans la cellule
                        hBox.setAlignment(Pos.CENTER);
                        hBox.setSpacing(15);

                        // Appliquer un style (si nécessaire)
                        editImageView.getStyleClass().add("table-icon");
                        deleteImageView.getStyleClass().addAll("table-icon", "delete");

                        // Appliquer un redimensionnement automatique des images pour remplir la cellule
                        editImageView.fitWidthProperty().bind(widthProperty().multiply(0.4)); // Ajuste la largeur de l'image
                        deleteImageView.fitWidthProperty().bind(widthProperty().multiply(0.4)); // Ajuste la largeur de l'image
                        editImageView.fitHeightProperty().bind(heightProperty()); // Redimensionner en fonction de la hauteur
                        deleteImageView.fitHeightProperty().bind(heightProperty()); // Redimensionner en fonction de la hauteur
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            Feedback feedback = getTableRow().getItem();

                            // Ajouter les événements sur les icônes
                            editImageView.setOnMouseClicked(event -> handleEdit(feedback));
                            deleteImageView.setOnMouseClicked(event -> handleDelete(feedback));

                            // Définir le graphique de la cellule
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

        try {
            // Charger le fichier FXML pour l'interface de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFeedback.fxml"));
            AnchorPane modifierFeedbackLayout = loader.load();

            // Passer l'objet Feedback à l'interface ModifierFeedback
            ModifierFeedback controller = loader.getController();
            controller.setFeedback(feedback);  // Passer le feedback à l'interface de modification

            // Créer une nouvelle scène pour l'interface de modification
            Scene scene = new Scene(modifierFeedbackLayout);
            Stage stage = (Stage) feedbackTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement de l'interface
        }
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
