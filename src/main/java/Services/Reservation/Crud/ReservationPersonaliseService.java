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
        // Existing implementation remains unchanged
        String insertReservation = "INSERT INTO reservationpersonnalise (nom, prenom, email, numtel, description, date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String insertServiceLink = "INSERT INTO reservation_personalise_service (reservation_id, service_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(insertReservation, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, reservationPersonalise.getNom());
                ps.setString(2, reservationPersonalise.getPrenom());
                ps.setString(3, reservationPersonalise.getEmail());
                ps.setString(4, reservationPersonalise.getNumtel());
                ps.setString(5, reservationPersonalise.getDescription());
                ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));
                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int reservationId = generatedKeys.getInt(1);
                    reservationPersonalise.setIdReservationPersonalise(reservationId);

                    try (PreparedStatement psLink = connection.prepareStatement(insertServiceLink)) {
                        for (Integer serviceId : reservationPersonalise.getServiceIds()) {
                            psLink.setInt(1, reservationId);
                            psLink.setInt(2, serviceId);
                            psLink.addBatch();
                        }
                        psLink.executeBatch();
                    }
                }
            }
            connection.commit();
            System.out.println("Réservation personnalisée ajoutée avec succès ! ID: " + reservationPersonalise.getIdReservationPersonalise());
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            System.err.println("Erreur lors de l'ajout de la réservation personnalisée : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la réactivation de l'auto-commit : " + e.getMessage());
            }
        }
    }

    @Override
    public ReservationPersonalise modifierReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String updateReservation = "UPDATE reservationpersonnalise SET nom=?, prenom=?, email=?, numtel=?, description=?, date=? " +
                "WHERE idReservationPersonalise=?";
        String deleteServices = "DELETE FROM reservation_personalise_service WHERE reservation_id=?";
        String insertServiceLink = "INSERT INTO reservation_personalise_service (reservation_id, service_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            // Mettre à jour la réservation
            try (PreparedStatement ps = connection.prepareStatement(updateReservation)) {
                ps.setString(1, reservationPersonalise.getNom());
                ps.setString(2, reservationPersonalise.getPrenom());
                ps.setString(3, reservationPersonalise.getEmail());
                ps.setString(4, reservationPersonalise.getNumtel());
                ps.setString(5, reservationPersonalise.getDescription());
                ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));
                ps.setInt(7, reservationPersonalise.getIdReservationPersonalise());
                ps.executeUpdate();
            }

            // Supprimer les anciens liens de services
            try (PreparedStatement psDelete = connection.prepareStatement(deleteServices)) {
                psDelete.setInt(1, reservationPersonalise.getIdReservationPersonalise());
                psDelete.executeUpdate();
            }

            // Insérer les nouveaux liens de services
            try (PreparedStatement psLink = connection.prepareStatement(insertServiceLink)) {
                for (Integer serviceId : reservationPersonalise.getServiceIds()) {
                    psLink.setInt(1, reservationPersonalise.getIdReservationPersonalise());
                    psLink.setInt(2, serviceId);
                    psLink.addBatch();
                }
                psLink.executeBatch();
            }

            connection.commit();
            System.out.println("Réservation personnalisée modifiée avec succès !");
            return reservationPersonalise; // Return the modified reservation
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            System.err.println("Erreur lors de la modification de la réservation personnalisée : " + e.getMessage());
            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la réactivation de l'auto-commit : " + e.getMessage());
            }
        }
    }

    @Override
    public void supprimerReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        // Existing implementation remains unchanged
        String deleteReservation = "DELETE FROM reservationpersonnalise WHERE idReservationPersonalise=?";
        try (PreparedStatement ps = connection.prepareStatement(deleteReservation)) {
            ps.setInt(1, reservationPersonalise.getIdReservationPersonalise());
            ps.executeUpdate();
            System.out.println("Réservation personnalisée supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation personnalisée : " + e.getMessage());
        }
    }

    @Override
    public List<ReservationPersonalise> rechercherReservationPersonalise() {
        // Existing implementation remains unchanged
        String req = "SELECT r.*, GROUP_CONCAT(rps.service_id) as service_ids " +
                "FROM reservationpersonnalise r " +
                "LEFT JOIN reservation_personalise_service rps ON r.idReservationPersonalise = rps.reservation_id " +
                "GROUP BY r.idReservationPersonalise";
        List<ReservationPersonalise> reservations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservationPersonalise reservation = new ReservationPersonalise(
                        rs.getInt("idReservationPersonalise"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("numtel"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        parseServiceIds(rs.getString("service_ids"))
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations personnalisées : " + e.getMessage());
        }
        return reservations;
    }

    private List<Integer> parseServiceIds(String serviceIdsString) {
        // Existing implementation remains unchanged
        List<Integer> serviceIds = new ArrayList<>();
        if (serviceIdsString != null && !serviceIdsString.trim().isEmpty()) {
            String[] ids = serviceIdsString.split(",");
            for (String id : ids) {
                try {
                    serviceIds.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de parsing pour l'ID de service : " + id + " - " + e.getMessage());
                }
            }
        }
        return serviceIds;
    }
}