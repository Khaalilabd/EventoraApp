package Services.Reservation.Crud;

import Services.Reservation.Interface.IReservation;
import Models.Reservation.Offre;
import Models.Reservation.Type;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreService implements IReservation<Offre> {

    private final Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(Offre offre) {
        String req = "INSERT INTO offre (typeOffre, dateCreation) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, offre.getTypeOffre().getLabel());
            ps.setDate(2, new java.sql.Date(offre.getDateCreation().getTime()));

            ps.executeUpdate();
            System.out.println("Offre ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'offre : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Offre offre) {
        String req = "UPDATE offre SET typeOffre=?, dateCreation=? WHERE idoffre=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, offre.getTypeOffre().getLabel());
            ps.setDate(2, new java.sql.Date(offre.getDateCreation().getTime()));
            ps.setInt(3, offre.getIdoffre());

            ps.executeUpdate();
            System.out.println("Offre modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'offre : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Offre offre) {
        String req = "DELETE FROM offre WHERE idoffre=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, offre.getIdoffre());

            ps.executeUpdate();
            System.out.println("Offre supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'offre : " + e.getMessage());
        }
    }

    @Override
    public List<Offre> rechercher() {
        String req = "SELECT * FROM offre";
        List<Offre> offres = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Offre offre = new Offre();
                offre.setIdoffre(rs.getInt("idoffre"));
                offre.setTypeOffre(Type.fromLabel(rs.getString("typeOffre")));
                offre.setDateCreation(rs.getDate("dateCreation"));

                offres.add(offre);
            }
            System.out.println(offres);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des offres : " + e.getMessage());
        }
        return offres;
    }

    // Méthode pour rechercher une offre par son ID
    public Offre rechercherParId(int idoffre) {
        String req = "SELECT * FROM offre WHERE idoffre=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, idoffre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Offre offre = new Offre();
                    offre.setIdoffre(rs.getInt("idoffre"));
                    offre.setTypeOffre(Type.fromLabel(rs.getString("typeOffre")));
                    offre.setDateCreation(rs.getDate("dateCreation"));
                    return offre;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'offre par ID : " + e.getMessage());
        }
        return null;
    }
}