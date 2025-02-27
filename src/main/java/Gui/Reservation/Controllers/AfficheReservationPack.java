package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class AfficheReservationPack {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<ReservationPack> tableView;

    @FXML
    private TableColumn<ReservationPack, Integer> colId;

    @FXML
    private TableColumn<ReservationPack, Integer> colIdPack;

    @FXML
    private TableColumn<ReservationPack, String> colNom;

    @FXML
    private TableColumn<ReservationPack, String> colPrenom;

    @FXML
    private TableColumn<ReservationPack, String> colEmail;

    @FXML
    private TableColumn<ReservationPack, String> colnumTel;

    @FXML
    private TableColumn<ReservationPack, String> colDescription;

    @FXML
    private TableColumn<ReservationPack, Date> colDate;

    @FXML
    private TableColumn<ReservationPack, String> colActions;

    private final ReservationPackService reservationservice;

    public AfficheReservationPack() {
        this.reservationservice = new ReservationPackService();
    }

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de la classe Reservation
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservationPack"));
        colIdPack.setCellValueFactory(new PropertyValueFactory<>("idPack"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colnumTel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        addActionsColumn();
        // Charger les données dans le TableView
        loadReservations();
        // Ajout du filtre de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterReservations(newValue));
    }
    private void filterReservations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadReservations(); // Recharge toutes les réservations si la recherche est vide
            return;
        }

        String lowerCaseKeyword = keyword.toLowerCase();
        tableView.getItems().setAll(
                reservationservice.rechercherReservationPack().stream()
                        .filter(res -> res.getNom().toLowerCase().contains(lowerCaseKeyword) ||
                                res.getPrenom().toLowerCase().contains(lowerCaseKeyword))
                        .toList()
        );
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(param -> new TableCell<ReservationPack, String>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final HBox hBox = new HBox(10);

            {
                // Chargement des icônes dans les boutons
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));

                // Icônes pour les boutons
                ImageView editImageView = new ImageView(editIcon);
                ImageView deleteImageView = new ImageView(deleteIcon);

                editImageView.setFitHeight(20);
                editImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteImageView.setFitWidth(20);

                // Ajout des icônes dans les boutons
                editButton.setGraphic(editImageView);
                deleteButton.setGraphic(deleteImageView);

                // Style des boutons
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");

                hBox.getChildren().addAll(editButton, deleteButton);
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
        });
    }

    private void handleEdit(ReservationPack reservation) {
        try {
            // Charger le fichier FXML de la vue ModifierReservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/ModifierReservationPack.fxml"));
            AnchorPane modifReservationLayout = loader.load();

            // Obtenez le contrôleur du fichier FXML
            ModifierReservationPack controller = loader.getController();
            controller.setReservationToEdit(reservation);  // Utilisation correcte du contrôleur

            // Charger la nouvelle scène
            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(ReservationPack reservation) {
        System.out.println("Supprimer la réclamation avec ID : " + reservation.getIdReservationPack());
        reservationservice.supprimerReservationPack(reservation);
        loadReservations();
    }

    private void loadReservations() {
        // Récupérer les réservations depuis le service et les ajouter au TableView
        tableView.getItems().setAll(reservationservice.rechercherReservationPack());
    }

    @FXML
    private void addReservationPack() {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AjouterReservationPack.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une reservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur dans la console
            System.out.println("Erreur lors du chargement de AjouterReservationPack.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void refreshList() {
        // Recharger les réservations
        loadReservations();
    }

    @FXML
    private void tableRowFactory(TableView<ReservationPack> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<ReservationPack> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
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
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
    private void goToPack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToPartenaire(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Partenaire.fxml"));
        AnchorPane partenaireLayout = loader.load();

        // Récupérer la fenêtre via l'élément déclencheur
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Scene scene = new Scene(partenaireLayout);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void goToService(ActionEvent event) throws IOException {
        Stage stage;

        // Vérifier si l'élément source est un MenuItem ou un Node
        if (event.getSource() instanceof MenuItem) {
            stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        } else {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Service/Service.fxml"));
        AnchorPane serviceLayout = loader.load();
        Scene scene = new Scene(serviceLayout);

        stage.setScene(scene);
        stage.show();
    }
}
