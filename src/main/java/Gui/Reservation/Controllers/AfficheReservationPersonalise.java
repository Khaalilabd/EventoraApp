package Gui.Reservation.Controllers;

import Models.Reservation.ReservationPersonalise;
import Models.Service.Service;
import Services.Reservation.Crud.ReservationPersonaliseService;
import Services.Service.Crud.ServiceService;
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
import java.util.List;
import java.util.stream.Collectors;

public class AfficheReservationPersonalise {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ReservationPersonalise> tableView;

    @FXML
    private TableColumn<ReservationPersonalise, Integer> colId;

    @FXML
    private TableColumn<ReservationPersonalise, String> colNom;

    @FXML
    private TableColumn<ReservationPersonalise, String> colPrenom;

    @FXML
    private TableColumn<ReservationPersonalise, String> colEmail;

    @FXML
    private TableColumn<ReservationPersonalise, String> colnumTel;

    @FXML
    private TableColumn<ReservationPersonalise, String> colDescription;

    @FXML
    private TableColumn<ReservationPersonalise, Date> colDate;

    @FXML
    private TableColumn<ReservationPersonalise, String> colServices; // Nouvelle colonne

    @FXML
    private TableColumn<ReservationPersonalise, String> colActions;

    private final ReservationPersonaliseService reservationService;
    private final ServiceService serviceService;

    public AfficheReservationPersonalise() {
        this.reservationService = new ReservationPersonaliseService();
        this.serviceService = new ServiceService();
    }

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de la classe ReservationPersonalise
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservationPersonalise"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colnumTel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Configuration de la colonne Services
        colServices.setCellValueFactory(cellData -> {
            ReservationPersonalise reservation = cellData.getValue();
            List<Integer> serviceIds = reservation.getServiceIds();
            if (serviceIds == null || serviceIds.isEmpty()) {
                return new SimpleStringProperty("Aucun service");
            }

            // Récupérer les titres des services depuis ServiceService
            List<Service> allServices = serviceService.RechercherService();
            String serviceTitles = serviceIds.stream()
                    .map(id -> allServices.stream()
                            .filter(s -> s.getId() == id)
                            .findFirst()
                            .map(Service::getTitre)
                            .orElse("Service inconnu (" + id + ")"))
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(serviceTitles);
        });

        // Ajout de la colonne Actions
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
                reservationService.rechercherReservationPersonalise().stream()
                        .filter(res -> res.getNom().toLowerCase().contains(lowerCaseKeyword) ||
                                res.getPrenom().toLowerCase().contains(lowerCaseKeyword))
                        .toList()
        );
    }

    private void addActionsColumn() {
        colActions.setCellValueFactory(cellData -> new SimpleStringProperty("Actions"));

        colActions.setCellFactory(param -> new TableCell<>() {
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
            // Charger le fichier FXML de la vue ModifierReservationPersonalise
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/ModifierReservationPersonalise.fxml"));
            AnchorPane modifReservationLayout = loader.load();

            // Obtenez le contrôleur du fichier FXML
            ModifierReservationPersonalise controller = loader.getController();
            controller.setReservationToEdit(reservation);  // Passer la réservation à modifier

            // Charger la nouvelle scène
            Scene currentScene = tableView.getScene();
            currentScene.setRoot(modifReservationLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExport(ReservationPersonalise reservation) {
        try {
            // Define the file path (e.g., save to the user's home directory)
            String homeDir = System.getProperty("user.home");
            String filePath = homeDir + File.separator + "ReservationPersonalise_" + reservation.getIdReservationPersonalise() + ".pdf";

            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph("Reservation Personalise Details")
                    .setFontSize(18)
                    .setBold());

            // Add reservation details
            document.add(new Paragraph("ID: " + reservation.getIdReservationPersonalise()));
            document.add(new Paragraph("Name: " + reservation.getNom() + " " + reservation.getPrenom()));
            document.add(new Paragraph("Email: " + reservation.getEmail()));
            document.add(new Paragraph("Phone Number: " + reservation.getNumtel()));
            document.add(new Paragraph("Description: " + reservation.getDescription()));
            document.add(new Paragraph("Date: " + reservation.getDate().toString()));
            // Add services
            List<Integer> serviceIds = reservation.getServiceIds();
            if (serviceIds != null && !serviceIds.isEmpty()) {
                List<Service> allServices = serviceService.RechercherService();
                String serviceTitles = serviceIds.stream()
                        .map(id -> allServices.stream()
                                .filter(s -> s.getId() == id)
                                .findFirst()
                                .map(Service::getTitre)
                                .orElse("Service inconnu (" + id + ")"))
                        .collect(Collectors.joining(", "));
                document.add(new Paragraph("Services: " + serviceTitles));
            } else {
                document.add(new Paragraph("Services: Aucun service"));
            }

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

    private void handleDelete(ReservationPersonalise reservation) {
        System.out.println("Supprimer la Reservation avec ID : " + reservation.getIdReservationPersonalise());
        reservationService.supprimerReservationPersonalise(reservation);
        loadReservations();
    }

    private void loadReservations() {
        // Récupérer les réservations depuis le service et les ajouter au TableView
        tableView.getItems().setAll(reservationService.rechercherReservationPersonalise());
    }

    @FXML
    private void addReservationPersonalise() {
        try {
            // Vérifier le chemin correct du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/AjouterReservationPersonalise.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Ajouter une reservation Personnalisée");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de AjouterReservationPersonalise.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void refreshList() {
        // Recharger les réservations
        loadReservations();
    }

    @FXML
    private void tableRowFactory(TableView<ReservationPersonalise> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<ReservationPersonalise> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #BDC3C7;"));
            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: transparent;"));
            return row;
        });
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
}