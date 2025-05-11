package Gui.Chatbot.Controllers;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class Chat {

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatMessages;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private double currentFontSize = 14;

    @FXML
    public void initialize() {
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sendButton.setOnAction(event -> sendMessage());
    }

    @FXML
    public void sendMessage() {
        String userText = userInput.getText().trim();
        if (!userText.isEmpty()) {
            // Affichage immédiat du message utilisateur
            HBox userMessageBox = new HBox(5);

            Label userMessage = new Label("Vous: " + userText);
            userMessage.setStyle("-fx-font-size: " + currentFontSize + "px; -fx-background-color: lightgray; -fx-border-color: transparent; -fx-padding: 5px;");

            ImageView userImage = new ImageView(new Image(getClass().getResourceAsStream("/Images/user.png")));
            userImage.setFitHeight(50);
            userImage.setFitWidth(50);

            userMessageBox.getChildren().addAll(userMessage, userImage);
            userMessageBox.setAlignment(Pos.CENTER_RIGHT);

            chatMessages.getChildren().add(userMessageBox);

            String botResponse = generateResponse(userText);
            animateBotResponse(botResponse);

            userInput.clear();
            chatScrollPane.setVvalue(1);
        }
    }

    private void animateBotResponse(String response) {
        HBox botMessageBox = new HBox(5);
        ImageView botImage = new ImageView(new Image(getClass().getResourceAsStream("/Images/assistant.jpg")));
        botImage.setFitHeight(50);
        botImage.setFitWidth(50);
        Label botMessage = new Label("Evi: ");
        botMessage.setStyle("-fx-font-size: " + currentFontSize + "px; -fx-background-color: lightblue; -fx-border-color: transparent; -fx-padding: 5px;");
        botMessageBox.getChildren().addAll(botImage, botMessage);
        botMessageBox.setAlignment(Pos.CENTER_LEFT);

        Platform.runLater(() -> chatMessages.getChildren().add(botMessageBox));

        // Animation progressive du texte pour la réponse de l'assistant
        Timeline timeline = new Timeline();
        for (int i = 0; i < response.length(); i++) {
            int currentCharIndex = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(25 * i), e -> {
                Platform.runLater(() -> {
                    botMessage.setText("Evi: " + response.substring(0, currentCharIndex + 1));
                    chatScrollPane.setVvalue(1);
                });
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(1);
        timeline.play();
    }
    private String generateResponse(String input) {
        input = input.toLowerCase().trim();
        if (input.contains("eventora")) {
            return "Eventora est une plateforme complète qui révolutionne la gestion \n " +
                    "d’événements, en simplifiant chaque étape du processus.\n" +
                    "standards. Elle permet aux utilisateurs de consulter et réserver\n " +
                    " des packs ou personnalisés en fonction de leurs besoins,\n " +
                    "sans avoir à gérer la complexité de l’organisation. Chaque membre dispose\n " +
                    "d’un espace personnel pour suivre ses réservations,\n" +
                    " modifier son profil, soumettre des réclamations et laisser des avis sur les\n" +
                    " services utilisés. Pour garantir une expérience fluide,\n" +
                    " un système de notifications informe en temps réel des mises à jour et statuts\n" +
                    " des demandes. Les organisateurs et administrateurs ont \n" +
                    "accès à un tableau de bord interactif qui leur permet de gérer les services,\n" +
                    " les packs, les réservations et même les réclamations des clients.\n " +
                    "Ils peuvent également suivre la performance de la plateforme grâce à des \n" +
                    "indicateurs clés et assurer un service optimal. Avec Eventora,\n " +
                    "l’organisation d’événements devient intuitive, rapide et efficace, en \n" +
                    "garantissant une gestion optimisée aussi bien pour les clients que pour les\n" +
                    " organisateurs resumme et simplifie";
        } else if (input.contains("evi")) {
            return "salut,comment je peux vous aider ?";
        } else if (input.contains("cv")) {
            return "oui cv ,et toi ?";
        }

        return "Désolé, je ne comprends pas votre demande.";
    }
}
