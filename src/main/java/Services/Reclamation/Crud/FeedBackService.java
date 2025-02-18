package Services.Reclamation.Crud;

import Models.Reclamation.Feedback;
import Services.Reclamation.Interface.IfeedBack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedBackService implements IfeedBack<Feedback> {

    private final Connection connection;

    public FeedBackService() {
        this.connection = Mydatasource.getInstance().getConnection();
    }

    @Override
    public void AjouterFeedBack(Feedback feedback) {
        String req = "INSERT INTO feedback (idUser, Vote, Description) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getIdUser());
            ps.setInt(2, feedback.getVote());
            ps.setString(3, feedback.getDescription());

            ps.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierFeedBack(Feedback feedback) {
        String req = "UPDATE feedback SET Vote = ?, Description = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getVote());
            ps.setString(2, feedback.getDescription());
            ps.setInt(3, feedback.getId()); // L'ID pour identifier le feedback à modifier

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Feedback mis à jour avec succès !");
            } else {
                System.out.println("Aucun feedback trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerFeedBack(Feedback feedback) {
        String req = "DELETE FROM feedback WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getId()); // L'ID pour identifier le feedback à supprimer

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Feedback supprimé avec succès !");
            } else {
                System.out.println("Aucun feedback trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Feedback> RechercherFeedBack() {
        String req = "SELECT id, idUser, Vote, Description FROM feedback";
        List<Feedback> feedbacks = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setIdUser(rs.getInt("idUser"));
                feedback.setVote(rs.getInt("Vote"));
                feedback.setDescription(rs.getString("Description"));

                feedbacks.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des feedbacks : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }

    @Override
    public List<Feedback> rechercherParUser(int idUser) {
        String req = "SELECT id, idUser, Vote, Description FROM feedback WHERE idUser = ?";
        List<Feedback> feedbacks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setId(rs.getInt("id"));
                    feedback.setIdUser(rs.getInt("idUser"));
                    feedback.setVote(rs.getInt("Vote"));
                    feedback.setDescription(rs.getString("Description"));

                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des feedbacks par utilisateur : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }

    @Override
    public List<Feedback> rechercherParVote(int vote) {
        String req = "SELECT id, idUser, Vote, Description FROM feedback WHERE Vote = ?";
        List<Feedback> feedbacks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, vote);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setId(rs.getInt("id"));
                    feedback.setIdUser(rs.getInt("idUser"));
                    feedback.setVote(rs.getInt("Vote"));
                    feedback.setDescription(rs.getString("Description"));

                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des feedbacks par vote : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }
}
