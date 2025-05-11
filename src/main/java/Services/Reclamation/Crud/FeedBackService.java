package Services.Reclamation.Crud;

import Models.Reclamation.Feedback;
import Models.Reclamation.Recommend;
import Services.Reclamation.Interface.IfeedBack;
import Utils.Mydatasource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedBackService implements IfeedBack<Feedback> {

    private final Connection connection;

    public FeedBackService() {
        this.connection = Mydatasource.getInstance().getConnection();
    }

    @Override
    public void AjouterFeedBack(Feedback feedback) {
        String req = "INSERT INTO feedback (idUser, Vote, Description, Recommend, date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getIdUser());
            ps.setInt(2, feedback.getVote());
            ps.setString(3, feedback.getDescription());
            ps.setString(4, feedback.getRecommend().name());
            ps.setTimestamp(5, Timestamp.valueOf(feedback.getDate()));

            ps.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du feedback : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierFeedBack(Feedback feedback) {
        String req = "UPDATE feedback SET Vote = ?, Description = ?, Recommend = ?, date = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, feedback.getVote());
            ps.setString(2, feedback.getDescription());
            String recommendValue = (feedback.getRecommend() != null) ? feedback.getRecommend().name() : Recommend.Non.name();
            ps.setString(3, recommendValue);
            ps.setTimestamp(4, Timestamp.valueOf(feedback.getDate()));
            ps.setInt(5, feedback.getId());

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
            ps.setInt(1, feedback.getId());

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
        String req = "SELECT id, idUser, Vote, Description, Recommend, date FROM feedback";
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
                    feedback.setRecommend(Recommend.Non);
                }
                feedback.setDate(rs.getTimestamp("date").toLocalDateTime());

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
        String req = "SELECT id, idUser, Vote, Description, Recommend, date FROM feedback WHERE idUser = ?";
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
                    String recommendLabel = rs.getString("Recommend");
                    try {
                        feedback.setRecommend(Recommend.fromLabel(recommendLabel));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Valeur invalide pour Recommend : " + recommendLabel + ". Valeur par défaut utilisée.");
                        feedback.setRecommend(Recommend.Non);
                    }
                    feedback.setDate(rs.getTimestamp("date").toLocalDateTime());
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
        String req = "SELECT id, idUser, Vote, Description, Recommend, date FROM feedback WHERE Vote = ?";
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
                    feedback.setRecommend(Recommend.valueOf(rs.getString("Recommend")));
                    feedback.setDate(rs.getTimestamp("date").toLocalDateTime());
                    feedbacks.add(feedback);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des feedbacks par vote : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }

    @Override
    public List<Feedback> rechercherParMotCle(String motCle) {
        String req = "SELECT id, idUser, Vote, Description, Recommend, date FROM Feedback WHERE description LIKE ?";
        List<Feedback> feedbacks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Feedback f = new Feedback();
                f.setId(rs.getInt("id"));
                f.setIdUser(rs.getInt("idUser"));
                f.setVote(rs.getInt("vote"));
                f.setDescription(rs.getString("description"));
                String recommendLabel = rs.getString("Recommend");
                try {
                    f.setRecommend(Recommend.fromLabel(recommendLabel));
                } catch (IllegalArgumentException e) {
                    System.out.println("Valeur invalide pour Recommend : " + recommendLabel + ". Valeur par défaut utilisée.");
                    f.setRecommend(Recommend.Non);
                }
                f.setDate(rs.getTimestamp("date").toLocalDateTime());
                feedbacks.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche par mot clé : " + e.getMessage());
            e.printStackTrace();
        }

        return feedbacks;
    }

    public String getUserNameById(int userId) {
        String req = "SELECT nom FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            } else {
                return "Utilisateur inconnu";
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return "Erreur utilisateur";
        }
    }
}