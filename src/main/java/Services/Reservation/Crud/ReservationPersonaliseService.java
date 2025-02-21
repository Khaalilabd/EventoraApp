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
        String req = "INSERT INTO reservationpersonnalise (nom, prenom, email, numtel, description, date, service) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reservationPersonalise.getNom());
            ps.setString(2, reservationPersonalise.getPrenom());
            ps.setString(3, reservationPersonalise.getEmail());
            ps.setString(4, reservationPersonalise.getNumtel());
            ps.setString(5, reservationPersonalise.getDescription());
            ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));
            ps.setInt(7, reservationPersonalise.getIdService());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                reservationPersonalise.setIdReservationPersonalise(generatedId);
                System.out.println("Réservation personnalisée ajoutée avec succès ! ID: " + generatedId);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation personnalisée : " + e.getMessage());
        }
    }

    @Override
    public void modifierReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String req = "UPDATE reservationpersonnalise SET nom=?, prenom=?, email=?, numtel=?, description=?, date=?, service=? WHERE idReservationPersonalise=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, reservationPersonalise.getNom());
            ps.setString(2, reservationPersonalise.getPrenom());
            ps.setString(3, reservationPersonalise.getEmail());
            ps.setString(4, reservationPersonalise.getNumtel());
            ps.setString(5, reservationPersonalise.getDescription());
            ps.setDate(6, new java.sql.Date(reservationPersonalise.getDate().getTime()));
            ps.setInt(7, reservationPersonalise.getIdService());
            ps.setInt(8, reservationPersonalise.getIdReservationPersonalise());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réservation personnalisée modifiée avec succès !");
            } else {
                System.out.println("Aucune réservation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la réservation personnalisée : " + e.getMessage());
        }
    }

    @Override
    public void supprimerReservationPersonalise(ReservationPersonalise reservationPersonalise) {
        String req = "DELETE FROM reservationpersonnalise WHERE idReservationPersonalise=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reservationPersonalise.getIdReservationPersonalise());
            ps.executeUpdate();
            System.out.println("Réservation personnalisée supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation personnalisée : " + e.getMessage());
        }
    }

    @Override
    public List<ReservationPersonalise> rechercherReservationPersonalise() {
        String req = "SELECT * FROM reservationpersonnalise";
        List<ReservationPersonalise> reservationPersonalises = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Utiliser le constructeur avec tous les paramètres
                ReservationPersonalise reservationPersonalise = new ReservationPersonalise(
                        rs.getInt("idReservationPersonalise"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("numtel"),
                        rs.getString("description"),
                        rs.getDate("date"),
                        rs.getInt("service")
                );
                reservationPersonalises.add(reservationPersonalise);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations personnalisées : " + e.getMessage());
        }
        return reservationPersonalises;
    }
}