package Gui.Service.Controllers;

import Models.Chat.OpenAIChat;  // Assure-toi que cette classe existe et contient la méthode getChatbotResponse
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Chat {

    @FXML
    private TextField userInputField;   // Champ où l'utilisateur entre la question
    @FXML
    private TextArea responseArea;      // Zone où la réponse du chatbot s'affiche
    @FXML
    private Button sendButton;          // Bouton pour envoyer la question

    private OpenAIChat openAIChat = new OpenAIChat();  // Instancier correctement la classe qui gère l'API du chatbot

    @FXML
    public void initialize() {
        // Définir l'action à effectuer lorsque le bouton "Envoyer" est cliqué
        sendButton.setOnAction(event -> {
            String userInput = userInputField.getText();  // Récupérer l'entrée de l'utilisateur
            String response = openAIChat.getChatbotResponse(userInput);  // Appeler la méthode qui interagit avec l'API
            responseArea.appendText("Vous: " + userInput + "\n");  // Afficher la question dans la zone de texte
            responseArea.appendText("Chatbot: " + response + "\n");  // Afficher la réponse dans la zone de texte
            userInputField.clear();  // Vider le champ de saisie après envoi
        });
    }
}
