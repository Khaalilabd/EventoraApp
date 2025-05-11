package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherUtilisateur {

    @FXML private GridPane utilisateurGrid;
    @FXML private Button supprimerButton;
    @FXML private Button modifierButton;
    @FXML private Button addUtilisateur;
    @FXML private Button refreshList;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleFilter;
    @FXML private Label totalLabel;
    @FXML private Label adminLabel;
    @FXML private Label clientLabel;
    @FXML private Label blockedLabel;

    private final MembresService membresService = new MembresService();
    private ObservableList<Utilisateurs> utilisateurs;
    private Utilisateurs selectedUtilisateur;

    @FXML
    public void initialize() {
        chargerUtilisateurs();
        updateStats();

        supprimerButton.setDisable(true);
        modifierButton.setDisable(true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterUtilisateurs());
        roleFilter.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object == null ? "" : object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });
        roleFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterUtilisateurs());
    }

    private void chargerUtilisateurs() {
        List<Utilisateurs> listeUtilisateurs = membresService.RechercherMem();
        utilisateurs = FXCollections.observableArrayList(listeUtilisateurs);
        afficherCartes();
    }

    private void afficherCartes() {
        utilisateurGrid.getChildren().clear();
        int column = 0;
        int row = 0;

        for (Utilisateurs utilisateur : utilisateurs) {
            // Utilisation d'un VBox comme conteneur principal de la carte
            VBox card = new VBox(10);
            card.getStyleClass().add("user-card");
            card.setAlignment(Pos.CENTER);

            // Conteneur pour la photo (HBox pour centrer)
            HBox photoContainer = new HBox();
            photoContainer.setAlignment(Pos.CENTER);

            // Photo de l'utilisateur (cercle)
            ImageView userPhoto = new ImageView();
            userPhoto.setFitHeight(80); // Taille par défaut
            userPhoto.setFitWidth(80);  // Taille par défaut
            userPhoto.getStyleClass().add("user-photo");

            boolean hasImage = false;
            try {
                String imagePath = utilisateur.getImage();
                System.out.println("Tentative de chargement de l'image pour l'utilisateur " + utilisateur.getId() + " : " + imagePath);
                if (imagePath != null && !imagePath.isEmpty()) {
                    String absolutePath = "src/main/resources" + imagePath;
                    File imageFile = new File(absolutePath);
                    if (imageFile.exists()) {
                        userPhoto.setImage(new Image(imageFile.toURI().toString()));
                        hasImage = true;
                    } else {
                        System.err.println("Fichier image introuvable : " + absolutePath);
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception lors du chargement de l'image pour l'utilisateur " + utilisateur.getId() + " : " + e.getMessage());
                e.printStackTrace();
            }

            // Si aucune image, utiliser une icône par défaut
            if (!hasImage) {
                userPhoto.setImage(new Image(getClass().getResourceAsStream("/Images/user-icon.png")));
            }

            photoContainer.getChildren().add(userPhoto);

            // Nom de l'utilisateur
            Label nomLabel = new Label(utilisateur.getNom() + " " + utilisateur.getPrenom());
            nomLabel.getStyleClass().add("card-label");

            // Code-barres (Code 128)
            VBox barcodeContainer = new VBox(5);
            barcodeContainer.setAlignment(Pos.CENTER);

            ImageView barcodeView = generateBarcode(utilisateur);
            barcodeView.setFitHeight(40); // Taille par défaut
            barcodeView.setFitWidth(180); // Taille par défaut
            barcodeView.setOnMouseClicked(e -> showUserDetails(utilisateur));

            // Rôle au lieu de l'ID sous le code-barres
            Label roleLabel = new Label(getRoleDisplayName(utilisateur.getRole()));
            roleLabel.getStyleClass().add("barcode-number");

            barcodeContainer.getChildren().addAll(barcodeView, roleLabel);

            // Boutons d'action
            HBox actionButtons = new HBox(10);
            actionButtons.setPadding(new Insets(5, 0, 0, 0));
            actionButtons.setAlignment(Pos.CENTER);

            Button modifierButton = new Button("✏️ Modifier");
            modifierButton.getStyleClass().add("action-button");
            modifierButton.setOnAction(e -> modifierUtilisateur(utilisateur));

            Button supprimerButton = new Button("🗑️ Supprimer");
            supprimerButton.getStyleClass().add("action-button");
            supprimerButton.setOnAction(e -> supprimerUtilisateur(utilisateur));

            actionButtons.getChildren().addAll(modifierButton, supprimerButton);

            // Ajouter tous les éléments à la carte
            card.getChildren().addAll(photoContainer, nomLabel, barcodeContainer, actionButtons);

            // Sélection de la carte
            card.setOnMouseClicked(e -> {
                selectedUtilisateur = utilisateur;
                this.supprimerButton.setDisable(false);
                this.modifierButton.setDisable(false);
                utilisateurGrid.getChildren().forEach(node -> node.getStyleClass().remove("card-selected"));
                card.getStyleClass().add("card-selected");
            });

            utilisateurGrid.add(card, column, row);
            column++;
            if (column == 5) { // Retour à 5 colonnes par défaut
                column = 0;
                row++;
            }
        }
    }

    // Nouvelle méthode pour obtenir un nom lisible du rôle
    private String getRoleDisplayName(Role role) {
        if (role == null) return "Inconnu";
        switch (role) {
            case ADMIN:
                return "Admin";
            case AGENT:
                return "Agent";
            case MEMBRE:
                return "Membre";
            default:
                return role.toString();
        }
    }

    private ImageView generateBarcode(Utilisateurs utilisateur) {
        try {
            // Utiliser l'ID de l'utilisateur comme contenu du code-barres
            String barcodeData = String.valueOf(utilisateur.getId());

            // Générer le code-barres (Code 128)
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, 150, 40);

            // Convertir le BitMatrix en image
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());
            Image barcodeImage = new Image(byteArrayInputStream);

            return new ImageView(barcodeImage);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageView(); // Retourner une ImageView vide en cas d'erreur
        }
    }

    private void showUserDetails(Utilisateurs utilisateur) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Détails de l'utilisateur");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        content.getChildren().addAll(
                new Label("ID: " + utilisateur.getId()),
                new Label("Nom: " + utilisateur.getNom()),
                new Label("Prénom: " + utilisateur.getPrenom()),
                new Label("Email: " + utilisateur.getEmail()),
                new Label("CIN: " + utilisateur.getCin()),
                new Label("Adresse: " + utilisateur.getAdresse()),
                new Label("Num Téléphone: " + utilisateur.getNumTel()),
                new Label("Rôle: " + getRoleDisplayName(utilisateur.getRole()))
        );

        Button closeButton = new Button("Fermer");
        closeButton.getStyleClass().add("card-button");
        closeButton.setOnAction(e -> popup.close());
        content.getChildren().add(closeButton);

        Scene scene = new Scene(content, 300, 350);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private void updateStats() {
        int total = utilisateurs.size();
        int admins = (int) utilisateurs.stream().filter(u -> "Admin".equals(getRoleDisplayName(u.getRole()))).count();
        int agents = (int) utilisateurs.stream().filter(u -> "Agent".equals(getRoleDisplayName(u.getRole()))).count();
        int membres = (int) utilisateurs.stream().filter(u -> "Membre".equals(getRoleDisplayName(u.getRole()))).count();

        totalLabel.setText("Total: " + total);
        adminLabel.setText("Admins: " + admins);
        clientLabel.setText("Agents: " + agents); // Mise à jour pour refléter "Agent"
        blockedLabel.setText("Membres: " + membres); // Mise à jour pour refléter "Membre"
    }

    private void filterUtilisateurs() {
        String searchText = searchField.getText().toLowerCase();
        String selectedRole = roleFilter.getSelectionModel().getSelectedItem();

        List<Utilisateurs> filteredList = utilisateurs.stream()
                .filter(u -> (searchText.isEmpty() ||
                        u.getNom().toLowerCase().contains(searchText) ||
                        u.getPrenom().toLowerCase().contains(searchText) ||
                        u.getEmail().toLowerCase().contains(searchText)))
                .filter(u -> (selectedRole == null || "Tous".equals(selectedRole) || getRoleDisplayName(u.getRole()).equals(selectedRole)))
                .collect(Collectors.toList());

        utilisateurs.setAll(filteredList);
        afficherCartes();
        updateStats();
    }

    @FXML
    private void ajouterUtilisateur(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/AjouterUtilisateur.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter Utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                chargerUtilisateurs();
                updateStats();
            });
            stage.showAndWait();
        } catch (IOException e) {
            showError("Erreur", "Erreur lors du chargement de la fenêtre d'ajout.");
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshList() {
        chargerUtilisateurs();
        searchField.clear();
        roleFilter.getSelectionModel().clearSelection();
        updateStats();
    }

    private void supprimerUtilisateur(Utilisateurs utilisateur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        alert.setContentText(utilisateur.getNom() + " " + utilisateur.getPrenom());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                membresService.SupprimerMem(utilisateur);
                chargerUtilisateurs();
                updateStats();
                supprimerButton.setDisable(true);
                modifierButton.setDisable(true);
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setTitle("Suppression réussie");
                confirmation.setContentText("L'utilisateur a été supprimé avec succès.");
                confirmation.showAndWait();
            }
        });
    }

    private void modifierUtilisateur(Utilisateurs utilisateur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/ModifierUtilisateur.fxml"));
            Parent root = loader.load();
            ModifierUtilisateur controller = loader.getController();
            controller.setUtilisateur(utilisateur);
            Stage stage = new Stage();
            stage.setTitle("Modifier Utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> {
                chargerUtilisateurs();
                updateStats();
            });
            stage.showAndWait();
        } catch (IOException e) {
            showError("Erreur", "Erreur lors du chargement de la fenêtre de modification.");
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerUtilisateur() {
        if (selectedUtilisateur != null) {
            supprimerUtilisateur(selectedUtilisateur);
        }
    }

    @FXML
    private void modifierUtilisateur() {
        if (selectedUtilisateur != null) {
            modifierUtilisateur(selectedUtilisateur);
        }
    }

    @FXML
    private void goToReclamation(ActionEvent event) {
        switchScene(event, "/Reclamation/Reclamation.fxml");
    }

    @FXML
    private void goToFeedback(ActionEvent event) {
        switchScene(event, "/Reclamation/Feedback.fxml");
    }

    @FXML
    private void goToService(ActionEvent event) {
        switchScene(event, "/Service/Service.fxml");
    }

    @FXML
    private void goToReservation(ActionEvent event) {
        switchScene(event, "/Reservation/Reservation.fxml");
    }

    @FXML
    private void goToPack(ActionEvent event) {
        switchScene(event, "/Pack/Packs.fxml");
    }

    @FXML
    private void goToAccueil(ActionEvent event) {
        switchScene(event, "/EventoraAPP/EventoraAPP.fxml");
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