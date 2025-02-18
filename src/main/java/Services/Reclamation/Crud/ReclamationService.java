package Services.Reclamation.Crud;

import Models.Reclamation.Reclamation;
import Services.Reclamation.Interface.Ireclamation;
import Utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements Ireclamation<Reclamation> {

    private Connection connection;

    public ReclamationService() {
        this.connection = Mydatasource.getInstance().getConnection();
    }

    @Override
    public void AjouterRec(Reclamation reclamation) {
        String req = "INSERT INTO Reclamation (idUser, titre, description) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reclamation.getIdUser()); // idUser
            ps.setString(2, reclamation.getTitre()); // titre
            ps.setString(3, reclamation.getDescription()); // description

            ps.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierRec(Reclamation reclamation) {
        String req = "UPDATE Reclamation SET idUser = ?, titre = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reclamation.getIdUser());
            ps.setString(2, reclamation.getTitre());
            ps.setString(3, reclamation.getDescription());
            ps.setInt(4, reclamation.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerRec(Reclamation reclamation) {
        String req = "DELETE FROM Reclamation WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reclamation.getId());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Reclamation> RechercherRec() {
        // Requête SQL avec jointure entre Reclamation et Membres
        String req = "SELECT r.Id, r.idUser, r.Titre, r.Description, m.Nom, m.Prénom, m.Email " +
                "FROM Reclamation r " +
                "JOIN Membres m ON r.idUser = m.Id";
        List<Reclamation> reclamations = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("Id"));
                r.setIdUser(rs.getInt("idUser"));
                r.setTitre(rs.getString("Titre"));
                r.setDescription(rs.getString("Description"));

                // Récupérer les informations du membre
                String nomMembre = rs.getString("Nom");
                String prenomMembre = rs.getString("Prénom");
                String emailMembre = rs.getString("Email");

                // Afficher les informations du membre
                System.out.println("Réclamation de : " + nomMembre + " " + prenomMembre + " (" + emailMembre + ")");

                reclamations.add(r);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            e.printStackTrace();
        }

        return reclamations;
    }
}