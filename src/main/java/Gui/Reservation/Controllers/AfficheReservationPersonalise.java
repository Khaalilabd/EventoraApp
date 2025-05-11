package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Services.Reservation.Crud.ReservationPersonaliseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

public class AfficheReservationPersonalise implements Initializable {
    @FXML
    private TableView<ReservationPersonalise> tableView;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private TableColumn<ReservationPersonalise, Integer> colId;
    @FXML
    private TableColumn<ReservationPersonalise, String> colNom;
    @FXML
    private TableColumn<ReservationPersonalise, String> colPrenom;
    @FXML
    private TableColumn<ReservationPersonalise, String> colEmail;
    @FXML
    private TableColumn<ReservationPersonalise, String> colNumTel;
    @FXML
    private TableColumn<ReservationPersonalise, String> colDescription;
    @FXML
    private TableColumn<ReservationPersonalise, String> colDate;
    @FXML
    private TableColumn<ReservationPersonalise, String> colStatus;
    @FXML
    private TableColumn<ReservationPersonalise, String> colActions;

    private final ReservationPersonaliseService reservationService;
    private ObservableList<ReservationPersonalise> reservationList;

    public AfficheReservationPersonalise() {
        this.reservationService = new ReservationPersonaliseService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservationPersonalise"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add actions column
        addActionsColumn();
        
        // Initialize status filter
        statusFilter.getItems().addAll("En attente", "Validée", "Annulée");
        
        // Load initial data
        loadReservations();
        
        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReservations();
        });
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(param -> new TableCell<ReservationPersonalise, String>() {
            final Button editButton = new Button();
            final Button deleteButton = new Button();
            final Button exportReservationButton = new Button();
            final HBox hBox = new HBox(10);

            {
                // Chargement des icônes dans les boutons
                Image editIcon = new Image(getClass().getResourceAsStream("/Images/modif.png"));
                Image deleteIcon = new Image(getClass().getResourceAsStream("/Images/supp.png"));
                Image exportReservationIcon = new Image(getClass().getResourceAsStream("/Images/pdf.png"));

                // Icônes pour les boutons
                ImageView editImageView = new ImageView(editIcon);
                ImageView deleteImageView = new ImageView(deleteIcon);
                ImageView exportReservationImageView = new ImageView(exportReservationIcon);

                editImageView.setFitHeight(20);
                editImageView.setFitWidth(20);
                deleteImageView.setFitHeight(20);
                deleteImageView.setFitWidth(20);
                exportReservationImageView.setFitHeight(20);
                exportReservationImageView.setFitWidth(20);

                // Ajout des icônes dans les boutons
                editButton.setGraphic(editImageView);
                deleteButton.setGraphic(deleteImageView);
                exportReservationButton.setGraphic(exportReservationImageView);

                // Style des boutons
                editButton.getStyleClass().add("table-button");
                deleteButton.getStyleClass().addAll("table-button", "delete");
                exportReservationButton.getStyleClass().add("table-button");

                // Add all buttons to the HBox
                hBox.getChildren().addAll(editButton, deleteButton, exportReservationButton);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editButton.setOnAction(event -> handleEdit(getTableRow().getItem()));
                    deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
                    exportReservationButton.setOnAction(event -> handleExport(getTableRow().getItem()));
                    setGraphic(hBox);
                }
            }
        });
    }

    private void handleEdit(ReservationPersonalise reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Reservation/ModifierReservationPersonalise.fxml"));
            Parent root = loader.load();
            
            ModifierReservationPersonalise controller = loader.getController();
            controller.setReservationToEdit(reservation);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            
            // Refresh the table after editing
            stage.setOnHidden(event -> loadReservations());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    private void handleDelete(ReservationPersonalise reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            reservationService.supprimerReservationPersonalise(reservation);
            loadReservations();
        }
    }

    private void handleExport(ReservationPersonalise reservation) {
        // TODO: Implement export functionality
        showAlert("Information", "Fonctionnalité d'export en cours de développement");
    }

    private void loadReservations() {
        reservationList = FXCollections.observableArrayList(reservationService.rechercherReservationPersonalise());
        tableView.setItems(reservationList);
    }

    @FXML
    private void searchReservations() {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = statusFilter.getValue();

        ObservableList<ReservationPersonalise> filteredList = FXCollections.observableArrayList();
        
        for (ReservationPersonalise reservation : reservationService.rechercherReservationPersonalise()) {
            boolean matchesSearch = searchText.isEmpty() ||
                    reservation.getNom().toLowerCase().contains(searchText) ||
                    reservation.getPrenom().toLowerCase().contains(searchText) ||
                    reservation.getEmail().toLowerCase().contains(searchText);
            
            boolean matchesStatus = selectedStatus == null || 
                    (reservation.getStatus() != null && reservation.getStatus().equals(selectedStatus));

            if (matchesSearch && matchesStatus) {
                filteredList.add(reservation);
            }
        }

        tableView.setItems(filteredList);
    }

    @FXML
    private void resetFilters() {
        searchField.clear();
        statusFilter.setValue(null);
        loadReservations();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Reservation/Reservation.fxml"));
        AnchorPane reservationLayout = loader.load();
        Scene scene = new Scene(reservationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Service/Service.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Pack/Packs.fxml"));
        AnchorPane packLayout = loader.load();
        Scene scene = new Scene(packLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Reclamation/Feedback.fxml"));
        AnchorPane feedbackLayout = loader.load();
        Scene feedbackScene = new Scene(feedbackLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(feedbackScene);
        currentStage.show();
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Reclamation/Reclamation.fxml"));
        AnchorPane reclamationLayout = loader.load();
        Scene scene = new Scene(reclamationLayout);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addReservationPersonalise(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Reservation/AjouterReservationPersonalise.fxml"));
        AnchorPane addResLayout = loader.load();
        Scene addResScene = new Scene(addResLayout);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(addResScene);
        currentStage.show();
    }

    @FXML
    private void refreshList() {
        loadReservations();
    }
} 