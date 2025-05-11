package Gui.Utilisateurs.Controllers;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Crud.MembresService;
import Utils.SessionManager;
import com.google.zxing.BarcodeFormat;
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
import java.io.IOException;
import java.io.InputStream;
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
    private Utilisateurs utilisateurConnecte; // Utilisateur connecté

    @FXML
    public void initialize() {
        // Récupérer l'utilisateur connecté via SessionManager
        utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateurConnecte == null) {
            showError("Erreur", "Aucun utilisateur connecté. Veuillez vous connecter.");
            return;
        }
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

        // Filtrer les utilisateurs selon le rôle de l'utilisateur connecté
        if (utilisateurConnecte.getRole() == Role.MEMBRE) {
            // Si l'utilisateur est un Membre, ne charger que sa propre carte
            listeUtilisateurs = listeUtilisateurs.stream()
                    .filter(u -> u.getId() == utilisateurConnecte.getId())
                    .collect(Collectors.toList());
        } else if (utilisateurConnecte.getRole() == Role.ADMIN) {
            // Si l'utilisateur est un Admin, charger tous les utilisateurs
            // Pas de filtrage, on garde toute la liste
        } else {
            // Par défaut, aucune carte si le rôle n'est pas reconnu
            listeUtilisateurs = List.of();
        }

        utilisateurs = FXCollections.observableArrayList(listeUtilisateurs);
        afficherCartes();
    }

    private void afficherCartes() {
        utilisateurGrid.getChildren().clear();
        utilisateurGrid.getColumnConstraints().clear();
        int column = 0;
        int row = 0;

        if (utilisateurs.isEmpty()) {
            // Afficher un message si aucune carte n'est disponible
            Label emptyLabel = new Label("Aucun utilisateur trouvé.");
            emptyLabel.getStyleClass().add("page-description");
            utilisateurGrid.add(emptyLabel, 0, 0, 6, 1);
        } else {
            for (Utilisateurs utilisateur : utilisateurs) {
                // Conteneur principal de la carte
                VBox card = new VBox(8);
                card.getStyleClass().add("user-card");
                // Ajouter la classe spécifique au rôle
                String roleClass = "user-card-" + (utilisateur.getRole() != null ? utilisateur.getRole().toString().toLowerCase() : "unknown");
                card.getStyleClass().add(roleClass);
                card.setAlignment(Pos.CENTER);
                card.setPrefWidth(200);
                card.setPrefHeight(250);
                card.setPadding(new Insets(10));

                // Photo de l'utilisateur
                ImageView userPhoto = new ImageView();
                userPhoto.setFitHeight(60);
                userPhoto.setFitWidth(60);
                userPhoto.getStyleClass().add("user-photo");

                try {
                    String imagePath = utilisateur.getImage();
                    System.out.println("Tentative de chargement de l'image pour l'utilisateur " + utilisateur.getId() + " : " + imagePath);
                    if (imagePath != null && !imagePath.isEmpty()) {
                        InputStream imageStream = getClass().getResourceAsStream(imagePath);
                        if (imageStream != null) {
                            userPhoto.setImage(new Image(imageStream));
                        } else {
                            System.err.println("Image introuvable dans les ressources : " + imagePath);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Exception lors du chargement de l'image pour l'utilisateur " + utilisateur.getId() + " : " + e.getMessage());
                }

                if (userPhoto.getImage() == null) {
                    InputStream defaultImageStream = getClass().getResourceAsStream("/Images/user-icon.png");
                    if (defaultImageStream != null) {
                        try {
                            userPhoto.setImage(new Image(defaultImageStream));
                        } catch (Exception e) {
                            System.err.println("Exception lors du chargement de l'image par défaut : " + e.getMessage());
                        }
                    } else {
                        System.err.println("Image par défaut introuvable : /Images/user-icon.png");
                        userPhoto.setImage(null); // Laisser l'ImageView vide
                    }
                }

                // Nom de l'utilisateur
                Label nomLabel = new Label(utilisateur.getNom() + " " + utilisateur.getPrenom());
                nomLabel.getStyleClass().add("card-name");
                nomLabel.setWrapText(true);
                nomLabel.setMaxWidth(180);

                // Rôle de l'utilisateur
                Label roleLabel = new Label(getRoleDisplayName(utilisateur.getRole()));
                roleLabel.getStyleClass().add("card-role");

                // Code-barres
                ImageView barcodeView = generateBarcode(utilisateur);
                barcodeView.setFitHeight(30);
                barcodeView.setFitWidth(160);
                barcodeView.setOnMouseClicked(e -> showUserDetails(utilisateur));

                // Boutons d'action (seulement pour les Admins)
                HBox actionButtons = new HBox(8);
                actionButtons.setAlignment(Pos.CENTER);

                if (utilisateurConnecte.getRole() == Role.ADMIN) {
                    Button modifierButton = createIconButton("/Images/edit.png", () -> modifierUtilisateur(utilisateur));
                    Button supprimerButton = createIconButton("/Images/delete.png", () -> supprimerUtilisateur(utilisateur));
                    actionButtons.getChildren().addAll(modifierButton, supprimerButton);
                }

                // Ajouter tous les éléments à la carte
                card.getChildren().addAll(userPhoto, nomLabel, roleLabel, barcodeView, actionButtons);

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
                if (column >= 6) { // 6 cartes par ligne
                    column = 0;
                    row++;
                }
            }
        }

        // Définir les contraintes de colonnes pour une grille responsive
        for (int i = 0; i < 6; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 6);
            utilisateurGrid.getColumnConstraints().add(colConst);
        }
    }

    private Button createIconButton(String iconPath, Runnable action) {
        Button button = new Button();
        button.getStyleClass().add("action-button");
        button.setOnAction(e -> action.run());

        try {
            InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream != null) {
                Image image = new Image(stream);
                ImageView icon = new ImageView(image);
                icon.setFitHeight(16);
                icon.setFitWidth(16);
                button.setGraphic(icon);
            } else {
                button.setText(iconPath.contains("edit") ? "✏️" : "🗑️");
            }
        } catch (Exception e) {
            button.setText(iconPath.contains("edit") ? "✏️" : "🗑️");
        }

        return button;
    }

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
            String barcodeData = String.valueOf(utilisateur.getId());
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.CODE_128, 150, 30);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pngOutputStream.toByteArray());
            Image barcodeImage = new Image(byteArrayInputStream);
            return new ImageView(barcodeImage);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageView();
        }
    }

    private void showUserDetails(Utilisateurs utilisateur) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Détails de l'utilisateur");

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.getStyleClass().add("popup-content");

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
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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
        clientLabel.setText("Agents: " + agents);
        blockedLabel.setText("Membres: " + membres);
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

        // Appliquer le filtre selon le rôle de l'utilisateur connecté
        if (utilisateurConnecte.getRole() == Role.MEMBRE) {
            filteredList = filteredList.stream()
                    .filter(u -> u.getId() == utilisateurConnecte.getId())
                    .collect(Collectors.toList());
        }

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


    @FXML
    private void goToParams(ActionEvent event) {
        switchScene(event, "/Utilisateurs/Parametres.fxml");
    }

    @FXML
    private void goToUser(ActionEvent event) {
        switchScene(event, "/Utilisateurs/AfficherUtilisateur.fxml");
    }
    @FXML
    private void deconnexion(ActionEvent event) {
        // Effacer la session
        SessionManager.getInstance().clearSession();
        // Rediriger vers la page de connexion
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Utilisateurs/Authentification.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}