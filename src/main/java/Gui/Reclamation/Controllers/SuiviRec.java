package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Models.Reclamation.Statut;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SuiviRec{

    @FXML private Label titleLabel;
    @FXML private Label titreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label statutLabel;
    @FXML private Button backButton;

    private Reclamation reclamation;

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
        updateUI();
    }

    @FXML
    public void initialize() {
        // Rien à initialiser ici
    }

    private void updateUI() {
        if (reclamation != null) {
            titleLabel.setText("⛔ Suivi de la Réclamation #" + reclamation.getId() + " ⛔");
            titreLabel.setText("Titre : " + reclamation.getTitre());
            descriptionLabel.setText("Description : " + reclamation.getDescription());
            statutLabel.setText("Statut : " + reclamation.getStatut().getLabel());

            // Réinitialiser toutes les classes sauf card-button (base)
            statutLabel.getStyleClass().removeAll("statut-resolue", "statut-rejetee", "statut-en-cours", "statut-en-attente");

            // Appliquer la classe CSS selon le statut
            switch (reclamation.getStatut()) {
                case RESOLUE:
                    statutLabel.getStyleClass().add("statut-resolue");
                    break;
                case REJETEE:
                    statutLabel.getStyleClass().add("statut-rejetee");
                    break;
                case EN_COURS:
                    statutLabel.getStyleClass().add("statut-en-cours");
                    break;
                case EN_ATTENTE:
                    statutLabel.getStyleClass().add("statut-en-attente");
                    break;
                default:
                    System.out.println("Statut inconnu : " + reclamation.getStatut());
                    break;
            }
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}