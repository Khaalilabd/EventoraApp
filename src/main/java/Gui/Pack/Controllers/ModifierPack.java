package Gui.Pack.Controllers;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.Evenement;
import Models.Service.Service;
import Services.Pack.Crud.PackService;
import Services.Service.Crud.ServiceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox; // Import CheckComboBox

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModifierPack {

    @FXML
    private TextField NomPackField;
    @FXML
    private TextArea DescriptionField;
    @FXML
    private TextField PrixField;
    @FXML
    private ComboBox<Location> LocationField;
    @FXML
    private ComboBox<Evenement> TypeField;
    @FXML
    private TextField NbrGuestsField;
    @FXML
    private CheckComboBox<Service> ServiceField; // Changed to CheckComboBox<Service>
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private Pack PackToEdit;
    private final PackService PackService = new PackService();
    private final ServiceService serviceService = new ServiceService();

    @FXML
    public void initialize() {
        LocationField.getItems().setAll(Location.values());

        List<Evenement> evenements = PackService.getAllEvenements();
        ObservableList<Evenement> observableEvenements = FXCollections.observableArrayList(evenements);
        TypeField.setItems(observableEvenements);

        List<Service> services = serviceService.RechercherService();
        ObservableList<Service> observableServices = FXCollections.observableArrayList(services);
        ServiceField.getItems().addAll(observableServices);
        ServiceField.setConverter(new javafx.util.StringConverter<Service>() {
            @Override
            public String toString(Service service) {
                return service != null ? service.getTitre() : "";
            }

            @Override
            public Service fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        submitButton.setDisable(true);
    }

    public void setPackToEdit(Pack pack) {
        this.PackToEdit = pack;

        NomPackField.setText(pack.getNomPack());
        DescriptionField.setText(pack.getDescription());
        PrixField.setText(String.valueOf(pack.getPrix()));
        LocationField.setValue(pack.getLocation());
        TypeField.setValue(pack.getType());
        NbrGuestsField.setText(String.valueOf(pack.getNbrGuests()));
        ServiceField.getCheckModel().clearChecks();
        for (Service service : pack.getNomServices()) {
            Service matchingService = ServiceField.getItems().stream()
                    .filter(s -> s.getTitre().equals(service.getTitre()))
                    .findFirst()
                    .orElse(null);
            if (matchingService != null) {
                ServiceField.getCheckModel().check(matchingService);
            }
        }

        submitButton.setDisable(false);
    }

    @FXML
    private void modifierPack(ActionEvent event) {
        try {
            String nomPack = NomPackField.getText().trim();
            String description = DescriptionField.getText().trim();
            String prixText = PrixField.getText().trim();
            Location location = LocationField.getValue();
            Evenement type = TypeField.getValue();
            String nbrGuestsText = NbrGuestsField.getText().trim();
            ObservableList<Service> selectedServices = ServiceField.getCheckModel().getCheckedItems();

            if (nomPack.isEmpty() || description.isEmpty() || prixText.isEmpty() || location == null || type == null || nbrGuestsText.isEmpty() || selectedServices.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis, y compris au moins un service.");
                return;
            }
// Check if nomPack already exists (exclude current Pack)
            int existingPackId = PackService.getIdPackByNom(nomPack);
            if (existingPackId != -1 && existingPackId != PackToEdit.getId()) {
                showAlert("Erreur", "Un pack avec le nom '" + nomPack + "' existe déjà ! Veuillez choisir un autre nom.");
                return;
            }

            double prix;
            int nbrGuests;
            try {
                prix = Double.parseDouble(prixText);
                nbrGuests = Integer.parseInt(nbrGuestsText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour le prix et le nombre d'invités.");
                return;
            }

            PackToEdit.setNomPack(nomPack);
            PackToEdit.setDescription(description);
            PackToEdit.setPrix(prix);
            PackToEdit.setLocation(location);
            PackToEdit.setType(type);
            PackToEdit.setNbrGuests(nbrGuests);
            PackToEdit.setNomServices(new ArrayList<>(selectedServices));

            PackService.modifier(PackToEdit);
            showAlert("Succès", "Pack modifié avec succès !");
            goToAffichePack(event);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la modification du pack.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        goToAffichePack(event);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void goToAffichePack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/AffichePack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page d'affichage des packs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToPack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack/Packs.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Pack : " + e.getMessage());
            e.printStackTrace();
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
}