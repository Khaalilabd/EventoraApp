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

public class ModifierTypePack {

    @FXML
    private TextField NomTypeField;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private TypePack TypePackToEdit;
    private final TypePackService TypePackService = new TypePackService();

    @FXML
    public void initialize() {


        // Désactivation du bouton tant qu'un pack n'est pas défini
        submitButton.setDisable(true);

    }

    public void setTypePackToEdit(TypePack typePack) {
        this.TypePackToEdit = typePack;

        // Remplissage des champs avec les données existantes
        NomTypeField.setText(typePack.getType());


        // Activation du bouton une fois que les données sont chargées
        submitButton.setDisable(false);
    }

    @FXML
    private void modifierTypePack(ActionEvent event) {
        try {
            String type = NomTypeField.getText();


            if (type.isEmpty()  ) {
                showAlert("Erreur", "les champs doivent être remplis.");
                return;
            }
            if (!type.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
                showAlert("Erreur", "Le type ne doit contenir que des lettres et des espaces.");
                return;
            }
            TypePackToEdit.setType(type);


            TypePack typePackToEdit = TypePackToEdit;


            TypePackService.modifier(typePackToEdit);
            showAlert("Succès", "Type modifié avec succès !");
            goToAfficheTypePack(event);

        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la modification du type.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        goToAfficheTypePack(event);
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
            System.out.println("Erreur lors du chargement de la page d'affichage des Type : " + e.getMessage());
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
