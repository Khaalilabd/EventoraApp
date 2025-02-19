package Services.Reclamation.Crud;

import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import Services.Reclamation.Interface.Ireclamation;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements Ireclamation<Reclamation> {

    private final Connection connection;

    public ReclamationService() {
        this.connection = Mydatasource.getInstance().getConnection();
    }

    @Override
    public void AjouterRec(Reclamation reclamation) {
        String req = "INSERT INTO Reclamation (idUser, titre, description, type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reclamation.getIdUser());
            ps.setString(2, reclamation.getTitre());
            ps.setString(3, reclamation.getDescription());
            ps.setString(4, reclamation.getType().getLabel());

            ps.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierRec(Reclamation reclamation) {
        // Modifier la requête pour ne pas toucher à l'idUser ni l'id
        String req = "UPDATE Reclamation SET titre = ?, description = ?, type = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            // Ne pas modifier l'idUser, donc ne pas l'inclure dans la requête
            ps.setString(1, reclamation.getTitre());
            ps.setString(2, reclamation.getDescription());
            ps.setString(3, reclamation.getType().getLabel());
            ps.setInt(4, reclamation.getId());  // Le paramètre id reste pour identifier la réclamation à modifier

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Reclamation> RechercherRec() {
        String req = "SELECT Id, idUser, Titre, Description, Type FROM Reclamation";
        List<Reclamation> reclamations = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("Id"));
                r.setIdUser(rs.getInt("idUser"));
                r.setTitre(rs.getString("Titre"));
                r.setDescription(rs.getString("Description"));
                r.setType(TypeReclamation.fromLabel(rs.getString("Type"))); // Conversion du label en TypeReclamation

                reclamations.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            e.printStackTrace();
        }

        return reclamations;
    }
}
