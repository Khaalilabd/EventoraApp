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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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
    private CheckComboBox<Service> ServiceField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button uploadImageButton;
    @FXML
    private ImageView imagePreview;

    private Pack PackToEdit;
    private final PackService PackService = new PackService();
    private final ServiceService serviceService = new ServiceService();
    private File selectedImageFile;

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
                return null;
            }
        });

        submitButton.setDisable(true);
        uploadImageButton.setOnAction(this::uploadImage);
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
        uploadImageButton.setText(pack.getImagePath() != null ? "Image : " + Paths.get(pack.getImagePath()).getFileName() : "Uploader une image");
        if (pack.getImagePath() != null && !pack.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResource(pack.getImagePath()).toExternalForm(), 100, 100, true, true);
                imagePreview.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image existante : " + e.getMessage());
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

            String imagePath = PackToEdit.getImagePath();
            if (selectedImageFile != null) {
                imagePath = saveImage(selectedImageFile, nomPack);
            }

            PackToEdit.setNomPack(nomPack);
            PackToEdit.setDescription(description);
            PackToEdit.setPrix(prix);
            PackToEdit.setLocation(location);
            PackToEdit.setType(type);
            PackToEdit.setNbrGuests(nbrGuests);
            PackToEdit.setNomServices(new ArrayList<>(selectedServices));
            PackToEdit.setImagePath(imagePath);

            PackService.modifier(PackToEdit);
            showAlert("Succès", "Pack modifié avec succès !");
            switchScene(event, "/Pack/AffichePack.fxml");

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la modification du pack.");
            e.printStackTrace();
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour le pack");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        selectedImageFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedImageFile != null) {
            uploadImageButton.setText("Image sélectionnée : " + selectedImageFile.getName());
            Image image = new Image(selectedImageFile.toURI().toString(), 100, 100, true, true);
            imagePreview.setImage(image);
        }
    }

    private String saveImage(File imageFile, String nomPack) throws IOException {
        String uploadDir = "src/main/resources/images/packs/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileExtension = imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
        String newFileName = nomPack.replaceAll("[^a-zA-Z0-9]", "_") + "_" + System.currentTimeMillis() + fileExtension;
        Path targetPath = Paths.get(uploadDir, newFileName);

        Files.copy(imageFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return "/images/packs/" + newFileName;
    }

    @FXML
    private void annuler(ActionEvent event) {
        switchScene(event, "/Pack/AffichePack.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }


    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    // Méthode switchScene suivant le style mémorisé
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            showError("Erreur de chargement", "Impossible d'afficher la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}