package Services.Pack.Crud;

import Models.Pack.Evenement;
import Services.Pack.Interface.IPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IPack<Evenement> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(Evenement evenement) {
        if (getEvenementByName(evenement.getTypeEvenement()) != null) {
            System.out.println("L'événement '" + evenement.getTypeEvenement() + "' existe déjà !");
            return;
        }
        String req = "INSERT INTO `typepack`(`type`) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, evenement.getTypeEvenement());
            ps.executeUpdate();
            System.out.println("Evenement " + evenement.getTypeEvenement() + " ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Evenement evenement) {
        String req = "DELETE FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, evenement.getTypeEvenement());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Evénement " + evenement.getTypeEvenement() + " supprimé avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec ce nom !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Evenement evenement) {
        String req = "UPDATE `typepack` SET `type`= ? WHERE `id` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, evenement.getTypeEvenement());
            ps.setInt(2, evenement.getId());
            System.out.println("Updating Evenement with ID: " + evenement.getId() + " and Type: " + evenement.getTypeEvenement());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Evénement " + evenement.getTypeEvenement() + " mis à jour avec succès !");
            } else {
                System.out.println("Aucun Evenement trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification de l'événement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Evenement> rechercher() {
        String req = "SELECT * FROM `typepack`";
        List<Evenement> evenements = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                evenements.add(new Evenement(rs.getInt("id"), rs.getString("type")));
            }
            System.out.println(evenements);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements : " + e.getMessage());
            e.printStackTrace();
        }
        return evenements;
    }

    public Evenement getEvenementByName(String eventName) {
        String req = "SELECT * FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, eventName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Evenement(rs.getInt("id"), rs.getString("type"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Evenement> RechercherEvenementParMotCle(String motCle) {
        String req = "SELECT id, type FROM typepack WHERE type LIKE ? ";

        List<Evenement> evenements = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evenement evenement = new Evenement(rs.getInt("id"), rs.getString("type"));
                    evenements.add(evenement);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements packs par mot-clé : " + e.getMessage());
            e.printStackTrace();
        }
        return evenements;
    }
}
