package Gui.ChatAi.Controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import Models.ChatAi.GeminiAPI;

import java.util.Arrays;
import java.util.List;

public class ChatAi {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    // Liste de mots-clés relatifs à Eventora
    private List<String> keywords = Arrays.asList(
            "Eventora", "gestion des reservations", "gestion des packs", "gestion des services", "gestion des reclamations", "tableau de bord",
            "statistique", "gestion des événements", "feedback", "partenaire", "profil utilisateur", "expérience utilisateur", "interface utilisateur",
            "evenement", "place","hello","salut"
    );

    @FXML
    public void initialize() {
        // Afficher un message d'introduction au démarrage
        animateMessage(chatArea);

        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            // Afficher le message de l'utilisateur
            chatArea.appendText("Moi : " + userInput + "\n");
            animateMessage(chatArea);

            // Vérifier si la question est en rapport avec Eventora
            if (isEventoraQuestion(userInput)) {
                // Appeler l'API pour obtenir la réponse de Gemini
                String response = GeminiAPI.getResponseFromGemini(userInput);
                // Extraire et afficher seulement la réponse
                String responseText = extractResponseText(response);
                chatArea.appendText("Gemini : " + responseText + "\n\n");
                animateMessage(chatArea);
            } else {
                // Réponse générique si la question n'est pas liée à Eventora
                chatArea.appendText("Gemini : Désolé, je ne peux répondre qu'aux questions concernant Eventora.\n\n");
                animateMessage(chatArea);
            }

            // Vider le champ de texte
            inputField.clear();
        }
    }

    // Fonction pour vérifier si la question contient des mots-clés relatifs à Eventora
    private boolean isEventoraQuestion(String userInput) {
        for (String keyword : keywords) {
            if (userInput.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void animateMessage(TextArea area) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), area);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // Extraire seulement le texte de la réponse de Gemini
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
