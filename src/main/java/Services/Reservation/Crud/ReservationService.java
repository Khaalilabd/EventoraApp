package Services.Reservation.Crud;

import Services.Reservation.Interface.IReservation;
import Models.Reservation.Reservation;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IReservation<Reservation> {

    private final Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(Reservation reservation) {
        String req = "INSERT INTO reservation (idoffre, nom, prenom, email, numTel, description, date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdoffre()); // Ajout de idoffre
            ps.setString(2, reservation.getNom());
            ps.setString(3, reservation.getPrenom());
            ps.setString(4, reservation.getEmail());
            ps.setString(5, reservation.getNumTel());
            ps.setString(6, reservation.getDescription());
            ps.setDate(7, new java.sql.Date(reservation.getDate().getTime()));

            ps.executeUpdate();
            System.out.println("Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
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
    public void modifier(Reservation reservation) {
        String req = "UPDATE reservation SET idoffre=?, nom=?, prenom=?, email=?, numTel=?, description=?, date=? WHERE idReservation=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdoffre()); // Ajout de idoffre
            ps.setString(2, reservation.getNom());
            ps.setString(3, reservation.getPrenom());
            ps.setString(4, reservation.getEmail());
            ps.setString(5, reservation.getNumTel());
            ps.setString(6, reservation.getDescription());
            ps.setDate(7, new java.sql.Date(reservation.getDate().getTime()));
            ps.setInt(8, reservation.getIdReservation());

            ps.executeUpdate();
            System.out.println("Réservation modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réservation : " + e.getMessage());
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
    public void supprimer(Reservation reservation) {
        String req = "DELETE FROM reservation WHERE idReservation=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservation.getIdReservation());
            ps.executeUpdate();
            System.out.println("Réservation supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
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
    public List<Reservation> rechercher() {
        String req = "SELECT * FROM reservation";
        List<Reservation> reservations = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(req);
            rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("idReservation"));
                reservation.setIdoffre(rs.getInt("idoffre")); // Ajout de idoffre
                reservation.setNom(rs.getString("nom"));
                reservation.setPrenom(rs.getString("prenom"));
                reservation.setEmail(rs.getString("email"));
                reservation.setNumTel(rs.getString("numTel"));
                reservation.setDescription(rs.getString("description"));
                reservation.setDate(rs.getDate("date"));

                reservations.add(reservation);
            }
            System.out.println(reservations);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
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
        return reservations;
    }
}