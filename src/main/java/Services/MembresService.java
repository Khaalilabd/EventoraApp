package Services;

import Models.Membres;
import utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MembresService implements Imembres<Membres> {

    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void AjouterMem(Membres membre) {
        String req = "INSERT INTO Membres (Nom, Prénom, Email, CIN, Adresse, NumTel) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setString(4, membre.getCin());
            ps.setString(5, membre.getAdresse());
            ps.setString(6, membre.getNumTel());

            ps.executeUpdate();
            System.out.println("Membre ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierMem(Membres membre) {
        String req = "UPDATE Membres SET Nom = ?, Prénom = ?, Email = ?, CIN = ?, Adresse = ?, NumTel = ? WHERE Id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setString(4, membre.getCin());
            ps.setString(5, membre.getAdresse());
            ps.setString(6, membre.getNumTel());
            ps.setInt(7, membre.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Membre mis à jour avec succès !");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerMem(Membres membre) {
        String req = "DELETE FROM Membres WHERE Id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, membre.getId());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Membre supprimé avec succès !");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Membres> RechercherMem() {
        String req = "SELECT * FROM Membres";
        List<Membres> membres = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Membres m = new Membres();
                m.setId(rs.getInt("Id"));
                m.setNom(rs.getString("Nom"));
                m.setPrenom(rs.getString("Prénom"));
                m.setEmail(rs.getString("Email"));
                m.setCin(rs.getString("CIN"));
                m.setAdresse(rs.getString("Adresse"));
                m.setNumTel(rs.getString("NumTel"));

                membres.add(m);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des membres : " + e.getMessage());
            e.printStackTrace();
        }

        return membres;
    }

    // Méthode optionnelle pour rechercher un membre par ID
    public Membres rechercherMem(int id) {
        String req = "SELECT * FROM Membres WHERE Id = ?";
        Membres membre = null;

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                membre = new Membres();
                membre.setId(rs.getInt("Id"));
                membre.setNom(rs.getString("Nom"));
                membre.setPrenom(rs.getString("Prénom"));
                membre.setEmail(rs.getString("Email"));
                membre.setCin(rs.getString("CIN"));
                membre.setAdresse(rs.getString("Adresse"));
                membre.setNumTel(rs.getString("NumTel"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du membre par ID : " + e.getMessage());
            e.printStackTrace();
        }

        return membre;
    }
}