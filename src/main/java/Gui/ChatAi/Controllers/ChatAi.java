package Gui.ChatAi.Controllers;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import Models.ChatAI.GeminiAPI;

import java.util.Arrays;
import java.util.List;

public class ChatAi {

    @FXML
    private VBox chatContainer; // Conteneur des messages

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    private List<String> keywords = Arrays.asList(
            "Eventora", "gestion des reservations", "gestion des packs", "gestion des services", "gestion des reclamations", "tableau de bord",
            "statistique", "gestion des événements", "feedback", "partenaire", "profil utilisateur", "expérience utilisateur", "interface utilisateur",
            "evenement", "place", "hello", "salut"
    );

    private Image userAvatar = new Image(getClass().getResource("/Images/user.png").toString());
    private Image aiAvatar = new Image(getClass().getResource("/Images/assistant.jpg").toString());

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            addMessage(userInput, true); // Message utilisateur

            String response;
            if (isEventoraQuestion(userInput)) {
                response = GeminiAPI.getResponseFromGemini(userInput);
                response = extractResponseText(response);
            } else {
                response = "Désolé, je ne peux répondre qu'aux questions concernant Eventora.";
            }

            addMessage(response, false); // Réponse AI

            inputField.clear();
        }
    }

    private void addMessage(String message, boolean isUser) {
        HBox messageBox = new HBox(10);
        messageBox.setStyle("-fx-padding: 5;");

        ImageView avatar = new ImageView(isUser ? userAvatar : aiAvatar);
        avatar.setFitHeight(40);
        avatar.setFitWidth(40);

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: " + (isUser ? "#0078D7; -fx-text-fill: white;" : "#E0E0E0;") +
                " -fx-padding: 8; -fx-background-radius: 10;");
        textFlow.setMaxWidth(200);

        if (isUser) {
            messageBox.getChildren().addAll(textFlow, avatar);
            messageBox.setStyle("-fx-alignment: CENTER_RIGHT;");
        } else {
            messageBox.getChildren().addAll(avatar, textFlow);
            messageBox.setStyle("-fx-alignment: CENTER_LEFT;");
        }

        chatContainer.getChildren().add(messageBox);
        animateMessage(messageBox);
    }

    private boolean isEventoraQuestion(String userInput) {
        return keywords.stream().anyMatch(userInput.toLowerCase()::contains);
    }

    private void animateMessage(HBox messageBox) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private String extractResponseText(String jsonResponse) {
        com.google.gson.JsonObject responseObj = new com.google.gson.JsonParser().parse(jsonResponse).getAsJsonObject();
        return responseObj.getAsJsonArray("candidates")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0)
                .getAsJsonObject()
                .get("text")
                .getAsString();
    }
}
