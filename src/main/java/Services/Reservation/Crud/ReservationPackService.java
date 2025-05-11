package Services.Reservation.Crud;

import Models.Reservation.ReservationPack;
import Services.Reservation.Interface.IReservationPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationPackService implements IReservationPack<ReservationPack> {

    private final Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouterReservationPack(ReservationPack reservationPack) {
        String req = "INSERT INTO reservationpack (idPack, idMembre, nom, prenom, email, numtel, description, date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationPack.getIdPack());
            ps.setInt(2, 1); // idMembre par défaut = 1
            ps.setString(3, reservationPack.getNom());
            ps.setString(4, reservationPack.getPrenom());
            ps.setString(5, reservationPack.getEmail());
            ps.setString(6, reservationPack.getNumtel());
            ps.setString(7, reservationPack.getDescription());
            ps.setDate(8, new java.sql.Date(reservationPack.getDate().getTime()));
            ps.setString(9, "En attente"); // status par défaut

            ps.executeUpdate();
            System.out.println("Réservation de pack ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation de pack : " + e.getMessage());
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
    public void modifierReservationPack(ReservationPack reservationPack) {
        String req = "UPDATE reservationpack SET idPack=?, idMembre=?, nom=?, prenom=?, email=?, numtel=?, description=?, date=?, status=? WHERE idReservationPack=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationPack.getIdPack());
            ps.setInt(2, reservationPack.getIdMembre());
            ps.setString(3, reservationPack.getNom());
            ps.setString(4, reservationPack.getPrenom());
            ps.setString(5, reservationPack.getEmail());
            ps.setString(6, reservationPack.getNumtel());
            ps.setString(7, reservationPack.getDescription());
            ps.setDate(8, new java.sql.Date(reservationPack.getDate().getTime()));
            ps.setString(9, reservationPack.getStatus());
            ps.setInt(10, reservationPack.getIdReservationPack());

            ps.executeUpdate();
            System.out.println("Réservation de pack modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réservation de pack : " + e.getMessage());
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
    public void supprimerReservationPack(ReservationPack reservationPack) {
        String req = "DELETE FROM reservationpack WHERE idReservationPack=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationPack.getIdReservationPack());
            ps.executeUpdate();
            System.out.println("Réservation de pack supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation de pack : " + e.getMessage());
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
    public List<ReservationPack> rechercherReservationPack() {
        String req = "SELECT * FROM reservationpack";
        List<ReservationPack> reservationPacks = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(req);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationPack reservationPack = new ReservationPack();
                reservationPack.setIdReservationPack(rs.getInt("idReservationPack"));
                reservationPack.setIdPack(rs.getInt("idPack"));
                reservationPack.setIdMembre(rs.getInt("idMembre"));
                reservationPack.setNom(rs.getString("nom"));
                reservationPack.setPrenom(rs.getString("prenom"));
                reservationPack.setEmail(rs.getString("email"));
                reservationPack.setNumtel(rs.getString("numtel"));
                reservationPack.setDescription(rs.getString("description"));
                reservationPack.setDate(rs.getDate("date"));
                reservationPack.setStatus(rs.getString("status"));

                reservationPacks.add(reservationPack);
            }
            System.out.println(reservationPacks);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations de pack : " + e.getMessage());
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
        return reservationPacks;
    }
}