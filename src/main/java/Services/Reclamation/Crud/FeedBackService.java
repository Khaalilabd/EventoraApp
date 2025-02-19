package Services.Reclamation.Crud;

import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
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
        String req = "INSERT INTO feedback (idUser, Vote, Description, Recommend) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getIdUser());
            ps.setInt(2, feedback.getVote());
            ps.setString(3, feedback.getDescription());
            ps.setString(4, feedback.getRecommend().name());  // Enregistrer la valeur de l'énum

            ps.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierFeedBack(Feedback feedback) {
        String req = "UPDATE feedback SET Vote = ?, Description = ?, Recommend = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getVote());
            ps.setString(2, feedback.getDescription());

            // Vérifier si la recommandation est nulle et définir une valeur par défaut si nécessaire
            String recommendValue = (feedback.getRecommend() != null) ? feedback.getRecommend().name() : Recommend.Non.name();
            ps.setString(3, recommendValue);

            ps.setInt(4, feedback.getId());

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
        String req = "SELECT id, idUser, Vote, Description, Recommend FROM feedback";
        List<Feedback> feedbacks = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setIdUser(rs.getInt("idUser"));
                feedback.setVote(rs.getInt("Vote"));
                feedback.setDescription(rs.getString("Description"));
                String recommendLabel = rs.getString("Recommend");
                try {
                    feedback.setRecommend(Recommend.fromLabel(recommendLabel));
                } catch (IllegalArgumentException e) {
                    System.out.println("Valeur invalide pour Recommend : " + recommendLabel + ". Valeur par défaut utilisée.");
                    feedback.setRecommend(Recommend.Non); // Valeur par défaut
                }

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
        return List.of();
    }

    @Override
    public List<Feedback> rechercherParVote(int vote) {
        String req = "SELECT id, idUser, Vote, Description, Recommend FROM feedback WHERE Vote = ?";
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
                    feedback.setRecommend(Recommend.valueOf(rs.getString("Recommend"))); // Conversion du String en enum

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
