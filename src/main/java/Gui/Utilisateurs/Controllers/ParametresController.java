package Gui.Utilisateurs.Controllers;

import Utils.SessionManager;
import Models.Utilisateur.Utilisateurs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ParametresController {

    @FXML private TabPane tabPane;
    @FXML private GridPane membreGrid;
    @FXML private VBox reclamationContainer;
    @FXML private VBox feedbackContainer;
    @FXML private VBox reservationContainer;
    @FXML private Button backButton;

    private Utilisateurs utilisateurConnecte;

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté
        utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateurConnecte == null) {
            showError("Erreur", "Aucun utilisateur connecté. Veuillez vous connecter.");
            return;
        }

        // Initialiser l'onglet Membre par défaut
        chargerMembres();

        // Écouter les changements d'onglet pour charger les données
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                switch (newTab.getText()) {
                    case "Membre":
                        chargerMembres();
                        break;
                    case "Réclamations":
                        chargerReclamations();
                        break;
                    case "Feedbacks":
                        chargerFeedbacks();
                        break;
                    case "Réservations":
                        chargerReservations();
                        break;
                }
            }
        });
    }

    private void chargerMembres() {
        membreGrid.getChildren().clear();

        // Afficher uniquement la carte de l'utilisateur connecté, sans image
        VBox userCard = new VBox(10);
        userCard.getStyleClass().add("user-card");
        userCard.setPrefWidth(300);
        userCard.setPrefHeight(200);

        // Nom et prénom
        Label nameLabel = new Label(utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
        nameLabel.getStyleClass().add("card-name");

        // Rôle
        Label roleLabel = new Label("Rôle: " + utilisateurConnecte.getRole().toString());
        roleLabel.getStyleClass().add("card-role");

        // Email
        Label emailLabel = new Label("Email: " + utilisateurConnecte.getEmail());
        emailLabel.getStyleClass().add("card-role");

        // Boutons d'action (Modifier, Supprimer)
        HBox actionButtons = new HBox(10);
        Button modifierButton = new Button("Modifier");
        modifierButton.getStyleClass().add("card-button");
        modifierButton.setOnAction(e -> modifierUtilisateur(utilisateurConnecte));

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.getStyleClass().add("card-button");
        supprimerButton.setOnAction(e -> supprimerUtilisateur(utilisateurConnecte));

        actionButtons.getChildren().addAll(modifierButton, supprimerButton);

        // Ajouter les éléments à la carte (sans ImageView)
        userCard.getChildren().addAll(nameLabel, roleLabel, emailLabel, actionButtons);
        userCard.setAlignment(javafx.geometry.Pos.CENTER);

        // Ajouter la carte au GridPane (centrée)
        membreGrid.add(userCard, 0, 0);
    }

    private void chargerReclamations() {
        reclamationContainer.getChildren().clear();
        // TODO: Implémenter la logique pour afficher les réclamations de l'utilisateur connecté
        // Utiliser reclamationContainer (VBox) pour ajouter des cartes ou des éléments
    }

    private void chargerFeedbacks() {
        feedbackContainer.getChildren().clear();
        // TODO: Implémenter la logique pour afficher les feedbacks de l'utilisateur connecté
        // Utiliser feedbackContainer (VBox) pour ajouter des cartes ou des éléments
    }

    private void chargerReservations() {
        reservationContainer.getChildren().clear();
        // TODO: Implémenter la logique pour afficher les réservations de l'utilisateur connecté
        // Utiliser reservationContainer (VBox) pour ajouter des cartes ou des éléments
    }

    private void modifierUtilisateur(Utilisateurs utilisateur) {
        // TODO: Implémenter la logique pour modifier l'utilisateur
        // Exemple: Ouvrir un formulaire de modification avec les données de l'utilisateur
        showError("Info", "Fonctionnalité de modification à implémenter pour l'utilisateur: " + utilisateur.getNom());
    }

    private void supprimerUtilisateur(Utilisateurs utilisateur) {
        // TODO: Implémenter la logique pour supprimer l'utilisateur
        // Exemple: Appeler utilisateurService.deleteUtilisateur(utilisateur.getId())
        showError("Info", "Fonctionnalité de suppression à implémenter pour l'utilisateur: " + utilisateur.getNom());
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
    }

    @FXML
    private void goToUtilisateur(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
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
    private void deconnexion(ActionEvent event) {
        // Effacer la session
        SessionManager.getInstance().clearSession();
        // Rediriger vers la page de connexion
        switchScene(event, "/Login/Login.fxml");
    }

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

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}