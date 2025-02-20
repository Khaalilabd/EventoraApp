package Services.Reservation.Crud;

import Models.Reservation.ReservationJointureServices;
import Services.Reservation.Interface.IRJS;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationJointuresServicesService implements IRJS<ReservationJointureServices> {

    private final Connection connection = Mydatasource.getInstance().getConnection();

    // Ajouter une réservation de service
    public void ajouter(ReservationJointureServices reservationService) {
        String req = "INSERT INTO reservation_services (IDReservationPersonalise, IDService) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationService.getIdReservationPersonalise());
            ps.setInt(2, reservationService.getIdService());

            ps.executeUpdate();
            System.out.println("Réservation de service ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation de service : " + e.getMessage());
        } finally {
            fermerPreparedStatement(ps);
        }
    }



    // Supprimer une réservation de service
    public void supprimer(ReservationJointureServices reservationService) {
        String req = "DELETE FROM reservation_services WHERE IDReservationPersonalise=? AND IDService=?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, reservationService.getIdReservationPersonalise());
            ps.setInt(2, reservationService.getIdService());

            ps.executeUpdate();
            System.out.println("Réservation de service supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation de service : " + e.getMessage());
        } finally {
            fermerPreparedStatement(ps);
        }
    }


    // Récupérer toutes les réservations de services
    public List<ReservationJointureServices> rechercher() {
        String req = "SELECT * FROM reservation_services";
        List<ReservationJointureServices> reservationsServices = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(req);
            rs = ps.executeQuery();
            while (rs.next()) {
                ReservationJointureServices reservationService = new ReservationJointureServices(
                        rs.getInt("IDReservationPersonalise"),
                        rs.getInt("IDService")
                );
                reservationsServices.add(reservationService);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations de services : " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerPreparedStatement(ps);
        }
        return reservationsServices;
    }

    // Récupérer tous les services d'une réservation spécifique
    public List<Integer> rechercher(int idReservationPersonalise) {
        String req = "SELECT IDService FROM reservation_services WHERE IDReservationPersonalise=?";
        List<Integer> services = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(req);
            ps.setInt(1, idReservationPersonalise);
            rs = ps.executeQuery();
            while (rs.next()) {
                services.add(rs.getInt("IDService"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des services pour la réservation " + idReservationPersonalise + " : " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerPreparedStatement(ps);
        }
        return services;
    }

    // Fermer PreparedStatement
    private void fermerPreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
            }
        }
    }

    // Fermer ResultSet
    private void fermerResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du ResultSet : " + e.getMessage());
            }
        }
    }
}

