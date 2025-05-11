package Gui.Pack.Controllers;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.Evenement;
import Models.Service.Service;
import Services.Pack.Crud.PackService;
import Services.Service.Crud.ServiceService;
import Services.Utilisateur.Crud.MembresService;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AjouterPack {

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
    private Button ReturnToListButton;
    @FXML
    private Button uploadImageButton;
    @FXML
    private ImageView imagePreview;

    private final PackService packService = new PackService();
    private final ServiceService serviceService = new ServiceService();
    private final MembresService membresService = new MembresService();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private File selectedImageFile;

    @FXML
    public void initialize() {
        LocationField.getItems().setAll(Location.values());
        List<Evenement> evenements = packService.getAllEvenements();
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
        ServiceField.setTitle("Sélectionner les services");

        submitButton.setOnAction(this::ajouterPack);
        cancelButton.setOnAction(this::annuler);
        ReturnToListButton.setOnAction(this::goToAffichePack);
        uploadImageButton.setOnAction(this::uploadImage);
    }

    @FXML
    private void ajouterPack(ActionEvent event) {
        try {
            String nomPack = NomPackField.getText().trim();
            String description = DescriptionField.getText();
            String prixText = PrixField.getText();
            Location location = LocationField.getValue();
            Evenement type = TypeField.getValue();
            String nbrGuestsText = NbrGuestsField.getText();
            ObservableList<Service> selectedServices = ServiceField.getCheckModel().getCheckedItems();

            if (nomPack.isEmpty() || description.isEmpty() || prixText.isEmpty() || location == null || type == null || nbrGuestsText.isEmpty() || selectedServices.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis, y compris au moins un service.");
                return;
            }

            int existingPackId = packService.getIdPackByNom(nomPack);
            if (existingPackId != -1) {
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

            String imagePath = null;
            if (selectedImageFile != null) {
                imagePath = saveImage(selectedImageFile, nomPack);
            }

            Pack newPack = new Pack(nomPack, description, prix, location, type, nbrGuests, new ArrayList<>(selectedServices), imagePath);
            packService.ajouter(newPack);

            try {
                boolean emailSentSuccessfully = sendEmailNotificationToUsers(nomPack);
                if (!emailSentSuccessfully) {
                    showAlert("Avertissement", "Pack ajouté, mais une erreur est survenue lors de l'envoi des notifications par email uniquement aux adresses @esprit.tn. Veuillez vérifier les logs.");
                }
            } catch (IOException e) {
                showAlert("Avertissement", "Pack ajouté, mais une erreur d'entrée/sortie est survenue lors de l'envoi des notifications par email : " + e.getMessage());
            } catch (InterruptedException e) {
                showAlert("Avertissement", "Pack ajouté, mais une erreur d'interruption est survenue lors de l'envoi des notifications par email : " + e.getMessage());
            } catch (Exception e) {
                showAlert("Avertissement", "Pack ajouté, mais une erreur inattendue est survenue lors de l'envoi des notifications : " + e.getMessage());
            }

            showAlert("Succès", "Pack ajouté avec succès !");
            switchScene(event, "/Pack/AffichePack.fxml");

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
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

    private boolean sendEmailNotificationToUsers(String nomPack) throws IOException, InterruptedException {
        List<String> userEmails = membresService.getAllUserEmails();
        if (userEmails.isEmpty()) {
            System.out.println("Aucune utilisateur trouvé pour envoyer des notifications.");
            return true;
        }

        String apiToken = System.getenv("POSTMARK_API_TOKEN");
        if (apiToken == null || apiToken.trim().isEmpty()) {
            System.out.println("Erreur : POSTMARK_API_TOKEN environment variable is not set or empty.");
            return false;
        }

        List<String> espritEmails = userEmails.stream()
                .filter(email -> email.toLowerCase().endsWith("@esprit.tn"))
                .collect(Collectors.toList());

        userEmails.stream()
                .filter(email -> !email.toLowerCase().endsWith("@esprit.tn"))
                .forEach(email -> System.out.println("Email ignoré (domaine non @esprit.tn) : " + email));

        if (espritEmails.isEmpty()) {
            System.out.println("Aucune adresse email @esprit.tn trouvée pour envoyer des notifications.");
            return true;
        }

        String fromEmail = "khalil.abdelmoumen@esprit.tn";
        String subject = "Nouveau Pack Ajouté : " + nomPack;
        String htmlContent = "<h1>Nouveau Pack Ajouté : " + escapeJsonString(nomPack) + "</h1>" +
                "<p>Un nouveau pack nommé '" + escapeJsonString(nomPack) + "' a été ajouté à notre application.</p>" +
                "<p>Allez le découvrir dès maintenant sur la plateforme Eventora !</p>" +
                "<p>Cordialement,<br>L'équipe Eventora</p>";
        String textContent = "Un nouveau pack nommé '" + escapeJsonString(nomPack) + "' a été ajouté à notre application ! " +
                "Allez le découvrir dès maintenant sur notre plateforme. " +
                "Cordialement, L'équipe Eventora";

        boolean allEmailsSentSuccessfully = true;
        for (String toEmail : espritEmails) {
            if (!toEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Erreur : Email invalide détecté - " + toEmail);
                allEmailsSentSuccessfully = false;
                continue;
            }

            String json = String.format(
                    "{\"From\":\"%s\",\"To\":\"%s\",\"Subject\":\"%s\",\"TextBody\":\"%s\",\"HtmlBody\":\"%s\",\"MessageStream\":\"outbound\"}",
                    fromEmail, toEmail, subject, escapeJsonString(textContent), escapeJsonString(htmlContent)
            );

            System.out.println("Envoi de l'email à " + toEmail + " avec le payload JSON : " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.postmarkapp.com/email"))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("X-Postmark-Server-Token", apiToken)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    System.out.println("Email envoyé à " + toEmail);
                } else {
                    System.out.println("Erreur lors de l'envoi de l'email à " + toEmail + " : " + response.body());
                    allEmailsSentSuccessfully = false;
                }
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                System.out.println("Erreur lors de l'envoi de l'email à " + toEmail + " : " + e.getMessage());
                e.printStackTrace();
                allEmailsSentSuccessfully = false;
            } catch (Exception e) {
                System.out.println("Exception inattendue lors de l'envoi de l'email à " + toEmail + " : " + e.getMessage());
                e.printStackTrace();
                allEmailsSentSuccessfully = false;
            }
        }
        return allEmailsSentSuccessfully;
    }

    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @FXML
    private void annuler(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        NomPackField.clear();
        DescriptionField.clear();
        PrixField.clear();
        LocationField.setValue(null);
        TypeField.setValue(null);
        NbrGuestsField.clear();
        ServiceField.getCheckModel().clearChecks();
        uploadImageButton.setText("Uploader une image");
        selectedImageFile = null;
        imagePreview.setImage(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Erreur") || title.equals("Avertissement") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goToAffichePack(ActionEvent event) {
        switchScene(event, "/Pack/AffichePack.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }
    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
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