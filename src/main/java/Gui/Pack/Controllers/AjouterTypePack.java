package Gui.Pack.Controllers;
import Models.Pack.TypePack;
import Services.Pack.Crud.TypePackService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterTypePack {

    @FXML
    private TextField NomTypeField;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button ReturnToListButton;


    private final TypePackService typePackService = new TypePackService();

    @FXML
    public void initialize() {


        submitButton.setOnAction(this::ajouterTypePack);
        cancelButton.setOnAction(this::annuler);
        ReturnToListButton.setOnAction(this::goToAfficheTypePack);

    }

    @FXML
    private void ajouterTypePack(ActionEvent event) {
        try {
            String type = NomTypeField.getText();


            if (type.isEmpty()) {
                showAlert("Erreur", "Le champ doit être remplis.");
                return;
            }

            if (!type.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
                showAlert("Erreur", "Le type ne doit contenir que des lettres et des espaces.");
                return;
            }


                TypePack newTypePack = new TypePack(type);
            typePackService.ajouter(newTypePack);
            showAlert("Succès", "Type ajouté avec succès !");
            goToAfficheTypePack(event);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout du Type.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        NomTypeField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void goToAfficheTypePack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficheTypePack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page AfficheTypePack.");
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
