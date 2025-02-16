package Services.Reclamation.Crud;

import Models.Reclamation.FeedBack;
import Services.Reclamation.Interface.IfeedBack;
import Utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FeedBackService implements IfeedBack<FeedBack> {

    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void AjouterFeedBack(FeedBack feedback) {
        String req = "INSERT INTO FeedBack (idUser, Vote, Description) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, feedback.getIdUser()); // idUser
            ps.setInt(2, feedback.getVote()); // Vote
            ps.setString(3, feedback.getDescription()); // Description

            ps.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierFeedBack(FeedBack feedback) {
        String req = "UPDATE FeedBack SET idUser = ?, Vote = ?, Description = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, feedback.getIdUser());
            ps.setInt(2, feedback.getVote());
            ps.setString(3, feedback.getDescription());
            ps.setInt(4, feedback.getId()); // Condition WHERE

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Feedback mis à jour avec succès !");
            } else {
                System.out.println("Aucun feedback trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerFeedBack(FeedBack feedback) {
        String req = "DELETE FROM FeedBack WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, feedback.getId()); // Condition WHERE

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Feedback supprimé avec succès !");
            } else {
                System.out.println("Aucun feedback trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<FeedBack> RechercherFeedBack() {
        String req = "SELECT f.id, f.idUser, f.Vote, f.Description, m.Nom, m.Prénom, m.Email " +
                "FROM FeedBack f " +
                "JOIN Membres m ON f.idUser = m.Id";
        List<FeedBack> feedbacks = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                FeedBack f = new FeedBack();
                f.setId(rs.getInt("id"));
                f.setIdUser(rs.getInt("idUser"));
                f.setVote(rs.getInt("Vote"));
                f.setDescription(rs.getString("Description"));

                // Récupérer les informations du membre
                String nomMembre = rs.getString("Nom");
                String prenomMembre = rs.getString("Prénom");
                String emailMembre = rs.getString("Email");

                // Afficher les informations du membre
                System.out.println("Feedback de : " + nomMembre + " " + prenomMembre + " (" + emailMembre + ")");

                feedbacks.add(f);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des feedbacks : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }
}
