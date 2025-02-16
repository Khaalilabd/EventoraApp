package Gui.Reclamation.Controllers;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class AjouterFeedback {

    @FXML
    private JFXTextArea descField;

    @FXML
    private JFXCheckBox recommendCheck;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton star1, star2, star3, star4, star5;

    private int rating = 0;

    @FXML
    private void initialize() {
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            int ratingValue = i + 1;
            stars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> updateRating(ratingValue));
        }

        submitButton.setOnAction(event -> handleSubmit());
    }

    private void updateRating(int value) {
        rating = value;
        JFXButton[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            stars[i].setStyle(i < rating ? "-fx-text-fill: gold;" : "-fx-text-fill: gray;");
        }
    }

    private void handleSubmit() {
        String description = descField.getText();
        boolean recommend = recommendCheck.isSelected();

        if (description.trim().isEmpty()) {
            showAlert("Error", "Please provide feedback.");
            return;
        }

        System.out.println("Rating: " + rating);
        System.out.println("Feedback: " + description);
        System.out.println("Recommend: " + (recommend ? "Yes" : "No"));

        showAlert("Success", "Thank you for your feedback!");
    }

    private void showAlert(String title, String message) {
        JFXAlert alert = new JFXAlert<>();
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(message));
        alert.setContent(layout);
        alert.show();
    }
}
