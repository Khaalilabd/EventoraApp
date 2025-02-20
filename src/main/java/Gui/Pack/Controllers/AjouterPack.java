package Gui.Pack.Controllers;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.TypePack;
import Services.Pack.Crud.PackService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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
    private ComboBox<TypePack> TypeField;
    @FXML
    private TextField NbrGuestsField;
    @FXML
    private TextField ServiceField;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button ReturnToListButton;


    private final PackService packService = new PackService();

    @FXML
    public void initialize() {
        // Initialisation de la ComboBox des Locations
        LocationField.getItems().setAll(Location.values());

        // Chargement des types de packs depuis la base de données
        List<TypePack> typePacks = packService.getAllTypePacks(); // Appel via l'instance
        ObservableList<TypePack> observableTypePacks = FXCollections.observableArrayList(typePacks);
        TypeField.setItems(observableTypePacks);

        submitButton.setOnAction(this::ajouterPack);
        cancelButton.setOnAction(this::annuler);
        ReturnToListButton.setOnAction(this::goToAffichePack);

    }

    @FXML
    private void ajouterPack(ActionEvent event) {
        try {
            String nomPack = NomPackField.getText();
            String description = DescriptionField.getText();
            String prixText = PrixField.getText();
            Location location = LocationField.getValue();
            TypePack type = TypeField.getValue();
            String nbrGuestsText = NbrGuestsField.getText();
            String nomService = ServiceField.getText();

            if (nomPack.isEmpty() || description.isEmpty() || prixText.isEmpty() || location == null || type == null || nbrGuestsText.isEmpty() || nomService.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
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

            Pack newPack = new Pack(nomPack, description, prix, location, type, nbrGuests, nomService);
            packService.ajouter(newPack);
            showAlert("Succès", "Pack ajouté avec succès !");
            goToAffichePack(event);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du pack.");
            e.printStackTrace();
        }
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
        ServiceField.clear();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichePack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page AffichePack.");
            e.printStackTrace();
        }
    }
    @FXML
    private void goToPack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Packs.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Pack : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
