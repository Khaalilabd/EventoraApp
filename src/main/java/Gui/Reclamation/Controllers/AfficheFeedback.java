package Gui.Reclamation.Controllers;

import Services.Reclamation.Crud.FeedBackService;
import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import Models.Utilisateur.Utilisateurs;
import Utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AfficheFeedback {

    @FXML private GridPane feedbackGrid;
    @FXML private TextField searchField;
    @FXML private Button goToAjouterFeedback;
    @FXML private Button refreshList;
    @FXML private Button searchFeedback;
    @FXML private Button handleShowPieChart;

    private FeedBackService feedbackService;

    public AfficheFeedback() {
        this.feedbackService = new FeedBackService();
    }

    @FXML
    public void initialize() {
        System.out.println("Initialisation de AfficheFeedback...");
        loadFeedbacks();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchFeedback(newValue));
    }

    private void loadFeedbacks() {
        System.out.println("Chargement des feedbacks...");
        try {
            List<Feedback> feedbackList = getFeedbacksForCurrentUser();
            System.out.println("Nombre de feedbacks chargés : " + feedbackList.size());
            populateGrid(feedbackList);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Impossible de charger les feedbacks", e.getMessage());
        }
    }

    private List<Feedback> getFeedbacksForCurrentUser() {
        Utilisateurs utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
        if (utilisateurConnecte == null) {
            showErrorAlert("Erreur", "Aucun utilisateur connecté.");
            return List.of();
        }

        if (utilisateurConnecte.getRole() == Models.Utilisateur.Role.ADMIN) {
            return feedbackService.RechercherFeedBack();
        } else {
            return feedbackService.rechercherParUser(utilisateurConnecte.getId());
        }
    }

    private void populateGrid(List<Feedback> feedbacks) {
        System.out.println("Population du GridPane avec " + feedbacks.size() + " feedbacks...");
        feedbackGrid.getChildren().clear();
        feedbackGrid.getColumnConstraints().clear();

        int column = 0;
        int row = 0;
        for (Feedback feedback : feedbacks) {
            VBox starContainer = createFeedbackStar(feedback);
            if (starContainer != null) {
                feedbackGrid.add(starContainer, column, row);
                System.out.println("Ajout d'une étoile à la position (" + column + ", " + row + ") pour feedback ID: " + feedback.getId());
            } else {
                System.out.println("Échec du chargement de l'étoile pour feedback ID: " + feedback.getId());
            }
            column++;
            if (column >= 10) {
                column = 0;
                row++;
            }
        }

        for (int i = 0; i < 10; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(25.0);
            feedbackGrid.getColumnConstraints().add(colConst);
        }
    }

    private VBox createFeedbackStar(Feedback feedback) {
        VBox starContainer = new VBox(15);
        starContainer.setAlignment(Pos.CENTER);
        starContainer.getStyleClass().add("feedback-card");

        Label idLabel = new Label(String.valueOf(feedback.getId()));
        idLabel.getStyleClass().add("card-title");
        idLabel.setMaxWidth(100);
        idLabel.setAlignment(Pos.CENTER);

        Label userLabel = new Label(getUserName(feedback.getIdUser()));
        userLabel.getStyleClass().add("card-subtitle");
        userLabel.setMaxWidth(100);
        userLabel.setAlignment(Pos.CENTER);

        // Optionnel : Afficher la date sur la carte
        // Label dateLabel = new Label(feedback.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        // dateLabel.getStyleClass().add("card-description");
        // dateLabel.setMaxWidth(100);
        // dateLabel.setAlignment(Pos.CENTER);

        Pane star = new Pane();
        star.setPrefSize(80, 80);
        star.getStyleClass().add("feedback-star");

        if (feedback.getRecommend() == Recommend.Oui) {
            star.getStyleClass().add("star-recommended");
        } else {
            star.getStyleClass().add("star-not-recommended");
        }

        star.setOnMouseClicked(event -> showFeedbackDetails(feedback));
        starContainer.getChildren().addAll(idLabel, userLabel, star); // Ajouter dateLabel si souhaité
        return starContainer;
    }

    private String getUserName(int userId) {
        return feedbackService.getUserNameById(userId);
    }

    private void showFeedbackDetails(Feedback feedback) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du Feedback");
        alert.setHeaderText("Feedback ID: " + feedback.getId());

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-padding: 10px; -fx-background-color: #ffffff;");

        Label userLabel = new Label("Utilisateur: " + getUserName(feedback.getIdUser()));
        userLabel.getStyleClass().add("card-title");
        Label voteLabel = new Label("Vote: " + feedback.getVote());
        voteLabel.getStyleClass().add("card-description");
        Label descLabel = new Label("Description: " + feedback.getDescription());
        descLabel.getStyleClass().add("card-description");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(400);
        Label dateLabel = new Label("Date: " + feedback.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dateLabel.getStyleClass().add("card-description");
        Label recommendLabel = new Label("Recommande: " + feedback.getRecommend().getLabel());
        recommendLabel.getStyleClass().add("card-statut");

        switch (feedback.getRecommend()) {
            case Oui:
                recommendLabel.getStyleClass().add("statut-resolue");
                break;
            case Non:
                recommendLabel.getStyleClass().add("statut-rejetee");
                break;
        }

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("card-button");
        editButton.setOnAction(e -> {
            try {
                alert.close();
                handleEdit(feedback);
            } catch (IOException ex) {
                showErrorAlert("Erreur lors de l'édition", ex.getMessage());
            }
        });

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("card-button");
        deleteButton.setOnAction(e -> {
            alert.close();
            handleDelete(feedback);
        });

        actionButtons.getChildren().addAll(editButton, deleteButton);
        content.getChildren().addAll(userLabel, voteLabel, descLabel, dateLabel, recommendLabel, actionButtons);

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.showAndWait();
    }

    private void handleEdit(Feedback feedback) throws IOException {
        System.out.println("Tentative de modification du feedback ID: " + feedback.getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reclamation/ModifierFeedback.fxml"));
            Parent layout = loader.load();
            ModifierFeedback controller = loader.getController();
            if (controller != null) {
                controller.setFeedback(feedback);
            } else {
                System.out.println("Controller ModifierFeedback est null");
            }
            Scene newScene = new Scene(layout);
            Stage currentStage = (Stage) feedbackGrid.getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de ModifierFeedback.fxml : " + e.getMessage());
            showErrorAlert("Erreur lors du chargement de la page de modification", e.getMessage());
            throw e;
        }
    }

    private void handleDelete(Feedback feedback) {
        if (feedback != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);

            VBox content = new VBox(10);
            content.setAlignment(Pos.CENTER);
            content.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px;");
            content.getChildren().add(new Label("Confirmation de suppression de Feedback ID: " + feedback.getId()));
            confirmationAlert.getDialogPane().setContent(content);
            confirmationAlert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    feedbackService.SupprimerFeedBack(feedback);
                    loadFeedbacks();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Le feedback a été supprimé avec succès.");
                    successAlert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
                    successAlert.showAndWait();
                }
            });
        }
    }

    @FXML
    private void goToAjouterFeedback(ActionEvent event) throws IOException {
        switchScene("/Reclamation/AjouterFeedback.fxml", event);
    }

    @FXML
    private void refreshList() {
        loadFeedbacks();
    }

    @FXML
    private void handleShowPieChart(ActionEvent event) {
        List<Feedback> feedbacks = getFeedbacksForCurrentUser();
        int ouiCount = (int) feedbacks.stream()
                .filter(f -> f.getRecommend() == Recommend.Oui).count();
        int nonCount = (int) feedbacks.stream()
                .filter(f -> f.getRecommend() == Recommend.Non).count();

        PieChart.Data ouiData = new PieChart.Data("Oui (" + ouiCount + ")", ouiCount);
        PieChart.Data nonData = new PieChart.Data("Non (" + nonCount + ")", nonCount);
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(ouiData, nonData);
        pieChart.setTitle("Répartition des Recommandations");
        pieChart.getData().forEach(data -> {
            if (data.getName().startsWith("Oui")) {
                data.getNode().setStyle("-fx-pie-color: pink;");
            } else if (data.getName().startsWith("Non")) {
                data.getNode().setStyle("-fx-pie-color: #11a791;");
            }
        });

        Stage chartStage = new Stage();
        chartStage.setTitle("Graphique des Recommandations");
        chartStage.setScene(new Scene(pieChart, 600, 400));
        chartStage.show();
    }

    @FXML
    private void goToFeedback(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Feedback.fxml", event);
    }

    @FXML
    private void searchFeedback(ActionEvent event) {
        String motCle = searchField.getText();
        searchFeedback(motCle);
    }

    private void searchFeedback(String motCle) {
        try {
            List<Feedback> resultatRecherche = feedbackService.rechercherParMotCle(motCle.toLowerCase());
            Utilisateurs utilisateurConnecte = SessionManager.getInstance().getUtilisateurConnecte();
            if (utilisateurConnecte != null && utilisateurConnecte.getRole() != Models.Utilisateur.Role.ADMIN) {
                resultatRecherche = resultatRecherche.stream()
                        .filter(f -> f.getIdUser() == utilisateurConnecte.getId())
                        .collect(Collectors.toList());
            }
            populateGrid(resultatRecherche);
        } catch (Exception e) {
            showErrorAlert("Erreur lors de la recherche", e.getMessage());
        }
    }

    @FXML
    private void goToReclamation(ActionEvent event) throws IOException {
        switchScene("/Reclamation/Reclamation.fxml", event);
    }

    @FXML
    private void goToReservation(ActionEvent event) throws IOException {
        switchScene("/Reservation/Reservation.fxml", event);
    }
    @FXML
    private void goToUser(ActionEvent event) throws IOException {
        switchScene("/Utilisateurs/AfficherUtilisateur.fxml", event);
    }

    @FXML
    private void goToParams(ActionEvent event) throws IOException {
        switchScene("/Utilisateurs/Parametres.fxml", event);
    }

    @FXML
    private void goToService(ActionEvent event) throws IOException {
        switchScene("/Service/Service.fxml", event);
    }

    @FXML
    private void goToPack(ActionEvent event) throws IOException {
        switchScene("/Pack/Packs.fxml", event);
    }

    @FXML
    private void goToAccueil(ActionEvent event) throws IOException {
        switchScene("/EventoraAPP/EventoraAPP.fxml", event);
    }

    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent layout = loader.load();
            Scene newScene = new Scene(layout);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            showErrorAlert("Erreur lors du changement de scène", e.getMessage());
            throw e;
        }
    }

    private void switchScene(String fxmlFile, Feedback feedback) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent layout = loader.load();
            if (fxmlFile.equals("/Reclamation/ModifierFeedback.fxml")) {
                ModifierFeedback controller = loader.getController();
                if (controller != null) {
                    controller.setFeedback(feedback);
                } else {
                    System.out.println("Controller ModifierFeedback est null");
                }
            }
            Scene newScene = new Scene(layout);
            Stage currentStage = (Stage) feedbackGrid.getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            showErrorAlert("Erreur lors du changement de scène", e.getMessage());
            throw e;
        }
    }

    private void showErrorAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/alert-style.css").toExternalForm());
        alert.showAndWait();
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