package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPack;
import Services.Reservation.Crud.ReservationPackService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AfficheReservationPack {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> statusFilter;
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
        
        // Initialize status filter
        statusFilter.getItems().addAll("En attente", "Validée", "Annulée");
        
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

    private void handleExport(ReservationPack reservation) {
        try {
            // Define the file path (e.g., save to the user's home directory)
            String homeDir = System.getProperty("user.home");
            String filePath = homeDir + File.separator + "ReservationPack_" + reservation.getIdReservationPack() + ".pdf";
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph("Reservation Pack Details")
                    .setFontSize(18)
                    .setBold());

            // Add reservation details
            document.add(new Paragraph("ID: " + reservation.getIdReservationPack()));
            document.add(new Paragraph("Pack ID: " + reservation.getIdPack()));
            document.add(new Paragraph("Name: " + reservation.getNom() + " " + reservation.getPrenom()));
            document.add(new Paragraph("Email: " + reservation.getEmail()));
            document.add(new Paragraph("Phone Number: " + reservation.getNumtel()));
            document.add(new Paragraph("Description: " + reservation.getDescription()));
            document.add(new Paragraph("Date: " + reservation.getDate().toString()));

            // Close the document
            document.close();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Reservation details exported to " + filePath);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

            // Show error message if something goes wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while exporting the reservation: " + e.getMessage());
            alert.showAndWait();
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
    private void searchReservations(ActionEvent event) {
        String searchText = searchField.getText().trim();
        String selectedStatus = statusFilter.getValue();
        
        if (searchText.isEmpty() && selectedStatus == null) {
            loadReservations();
            return;
        }

        tableView.getItems().setAll(
            reservationservice.rechercherReservationPack().stream()
                .filter(res -> {
                    boolean matchesSearch = searchText.isEmpty() ||
                        res.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        res.getPrenom().toLowerCase().contains(searchText.toLowerCase()) ||
                        res.getEmail().toLowerCase().contains(searchText.toLowerCase());
                    
                    boolean matchesStatus = selectedStatus == null ||
                        (res.getStatus() != null && res.getStatus().equals(selectedStatus));
                    
                    return matchesSearch && matchesStatus;
                })
                .toList()
        );
    }

    @FXML
    private void resetFilters(ActionEvent event) {
        searchField.clear();
        statusFilter.setValue(null);
        loadReservations();
    }

    @FXML
    private void showStatistics(ActionEvent event) {
        // Calculate statistics
        long totalReservations = tableView.getItems().size();
        long validatedReservations = tableView.getItems().stream()
            .filter(res -> "Validée".equals(res.getStatus()))
            .count();
        long pendingReservations = tableView.getItems().stream()
            .filter(res -> "En attente".equals(res.getStatus()))
            .count();
        long cancelledReservations = tableView.getItems().stream()
            .filter(res -> "Annulée".equals(res.getStatus()))
            .count();

        // Show statistics in an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistiques des Réservations");
        alert.setHeaderText("Aperçu des Réservations de Packs");
        alert.setContentText(
            String.format("Total des réservations: %d\n" +
                        "Réservations validées: %d\n" +
                        "Réservations en attente: %d\n" +
                        "Réservations annulées: %d",
                totalReservations, validatedReservations, pendingReservations, cancelledReservations)
        );
        alert.showAndWait();
    }

    @FXML
    private void exportData(ActionEvent event) {
        try {
            // Define the file path
            String homeDir = System.getProperty("user.home");
            String filePath = homeDir + File.separator + "ReservationsPack_Export.pdf";
            
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph("Liste des Réservations de Packs")
                    .setFontSize(18)
                    .setBold());

            // Add each reservation
            for (ReservationPack reservation : tableView.getItems()) {
                document.add(new Paragraph("\nRéservation #" + reservation.getIdReservationPack())
                        .setFontSize(14)
                        .setBold());
                document.add(new Paragraph("Pack ID: " + reservation.getIdPack()));
                document.add(new Paragraph("Nom: " + reservation.getNom() + " " + reservation.getPrenom()));
                document.add(new Paragraph("Email: " + reservation.getEmail()));
                document.add(new Paragraph("Téléphone: " + reservation.getNumtel()));
                document.add(new Paragraph("Description: " + reservation.getDescription()));
                document.add(new Paragraph("Date: " + reservation.getDate()));
                document.add(new Paragraph("Statut: " + reservation.getStatus()));
                document.add(new Paragraph("----------------------------------------"));
            }

            // Close the document
            document.close();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Réussi");
            alert.setHeaderText(null);
            alert.setContentText("Les données ont été exportées vers : " + filePath);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'Export");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur est survenue lors de l'export : " + e.getMessage());
            alert.showAndWait();
        }
    }
}