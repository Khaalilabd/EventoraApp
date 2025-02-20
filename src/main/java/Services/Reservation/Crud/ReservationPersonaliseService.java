package Services.Reservation.Crud;

import Models.Reservation.ReservationPersonalise;
import Services.Reservation.Interface.IReservationPersonalise;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationPersonaliseService implements IReservationPersonalise<ReservationPersonalise> {

    private final Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouterReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String req = "INSERT INTO reservationpersonnalise ( nom, prenom, email, numtel, description, date) VALUES ( ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setString(1, reservationPersonalise.getNom());
            ps.setString(2, reservationPersonalise.getPrenom());
            ps.setString(3, reservationPersonalise.getEmail());
            ps.setString(4, reservationPersonalise.getNumtel());
            ps.setString(5, reservationPersonalise.getDescription());
            ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));

            ps.executeUpdate();
            System.out.println("Réservation personnalisée ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation personnalisée : " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void modifierReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String req = "UPDATE reservationpersonnalise SET nom=?, prenom=?, email=?, numtel=?, description=?, date=? WHERE idReservationPersonalise=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setString(1, reservationPersonalise.getNom());
            ps.setString(2, reservationPersonalise.getPrenom());
            ps.setString(3, reservationPersonalise.getEmail());
            ps.setString(4, reservationPersonalise.getNumtel());
            ps.setString(5, reservationPersonalise.getDescription());
            ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));
            ps.setInt(7, reservationPersonalise.getIdReservationPersonalise());

            ps.executeUpdate();
            System.out.println("Réservation personnalisée modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réservation personnalisée : " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void supprimerReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String req = "DELETE FROM reservationpersonnalise WHERE idReservationPersonalise=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationPersonalise.getIdReservationPersonalise());
            ps.executeUpdate();
            System.out.println("Réservation personnalisée supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation personnalisée : " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
                }
            }
        }
    }

    @Override
    public List<ReservationPersonalise> rechercherReservationPersonalise() {
        String req = "SELECT * FROM reservationpersonnalise";
        List<ReservationPersonalise> reservationPersonalises = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(req);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationPersonalise reservationPersonalise = new ReservationPersonalise();
                reservationPersonalise.setIdReservationPersonalise(rs.getInt("idReservationPersonalise"));
                reservationPersonalise.setNom(rs.getString("nom"));
                reservationPersonalise.setPrenom(rs.getString("prenom"));
                reservationPersonalise.setEmail(rs.getString("email"));
                reservationPersonalise.setNumtel(rs.getString("numtel"));
                reservationPersonalise.setDescription(rs.getString("description"));
                reservationPersonalise.setDate(rs.getDate("date"));

                reservationPersonalises.add(reservationPersonalise);
            }
            System.out.println(reservationPersonalises);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations personnalisées : " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture du ResultSet : " + e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
                }
            }
        }
        return reservationPersonalises;
    }
}
