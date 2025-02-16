package Gui.Reclamation.Controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AjouterFeedBack {

    @FXML
    private VBox sidebar;

    @FXML
    private Button toggleButton;

    @FXML
    private HBox ratingStars;

    @FXML
    private TextArea feedbackArea;

    private boolean sidebarVisible = true;
    private int rating = 0;

    @FXML
    public void toggleSidebar() {
        if (sidebar == null) {
            System.out.println("Erreur : sidebar non trouvée !");
            return;
        }

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), sidebar);
        if (sidebarVisible) {
            transition.setToX(-250); // Cache la sidebar
        } else {
            transition.setToX(0); // Montre la sidebar
        }
        sidebarVisible = !sidebarVisible;
        transition.play();
    }

    @FXML
    public void rateApp(javafx.event.ActionEvent event) {
        Button clickedStar = (Button) event.getSource();
        int selectedIndex = ratingStars.getChildren().indexOf(clickedStar) + 1;
        rating = selectedIndex;

        for (int i = 0; i < ratingStars.getChildren().size(); i++) {
            Button star = (Button) ratingStars.getChildren().get(i);
            star.setText(i < rating ? "★" : "☆");
        }
    }

    @FXML
    public void submitFeedback() {
        String feedbackText = feedbackArea.getText();
        System.out.println("Feedback: " + feedbackText);
        System.out.println("Note: " + rating + " étoiles");
    }
}
