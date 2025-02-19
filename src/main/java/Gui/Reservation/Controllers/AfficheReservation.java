package Gui.Reservation.Controllers;

import Models.Reservation.Reservation;
import Services.Reservation.Crud.ReservationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class AfficheReservation {

    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, Integer> colId;

    @FXML
    private TableColumn<Reservation, Integer> colIdoffre;

    @FXML
    private TableColumn<Reservation, String> colNom;

    @FXML
    private TableColumn<Reservation, String> colPrenom;

    @FXML
    private TableColumn<Reservation, String> colEmail;

    @FXML
    private TableColumn<Reservation, String> colnumTel;

    @FXML
    private TableColumn<Reservation, String> colDescription;

    @FXML
    private TableColumn<Reservation, Date> colDate;

    @FXML
    private TableColumn<Reservation, String> colActions;

    private ReservationService reservationservice;

    public AfficheReservation() {
        this.reservationservice = new ReservationService();
    }

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de la classe Reservation
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservation"));
        colIdoffre.setCellValueFactory(new PropertyValueFactory<>("idoffre"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colnumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        addActionsColumn();
        // Charger les données dans le TableView
        loadReservations();
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(param -> new TableCell<Reservation, String>() {
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

    private void handleEdit(Reservation reservation) {
        try {
            // Charger le fichier FXML de la vue ModifierReservation
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            AnchorPane modifReservationLayout = loader.load();

            // Obtenez le contrôleur du fichier FXML
            ModifierReservation controller = loader.getController();
            controller.setReservationToEdit(reservation);  // Utilisation correcte du contrôleur

            // Charger la nouvelle scène
            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDelete(Reservation reservation) {
        System.out.println("Supprimer la réclamation avec ID : " + reservation.getIdReservation());
        reservationservice.supprimer(reservation);
        loadReservations();
    }

    private void loadReservations() {
        // Récupérer les réservations depuis le service et les ajouter au TableView
        tableView.getItems().setAll(reservationservice.rechercher());
    }

    @FXML
    private void addReservation() {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une reservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Afficher l'erreur dans la console
            System.out.println("Erreur lors du chargement de AjouterReservation.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void refreshList() {
        // Recharger les réservations
        loadReservations();
    }

    @FXML
    private void tableRowFactory(TableView<Reservation> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
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
}
