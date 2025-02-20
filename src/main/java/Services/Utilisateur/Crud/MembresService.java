package Services.Utilisateur.Crud;

import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Interface.Imembres;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembresService implements Imembres<Utilisateurs> {

    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void AjouterMem(Utilisateurs membre) {
        String req = "INSERT INTO Membres (Nom, Prénom, Email, CIN, Adresse, NumTel, MotDePasse) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setString(4, membre.getCin());
            ps.setString(5, membre.getAdresse());
            ps.setString(6, membre.getNumTel());
            ps.setString(7, membre.getMotDePasse());

            ps.executeUpdate();
            System.out.println("Membre ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierMem(Utilisateurs membre) {
        String req = "UPDATE Membres SET Nom = ?, Prénom = ?, Email = ?, CIN = ?, Adresse = ?, NumTel = ?, MotDePasse = ? WHERE Id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setString(4, membre.getCin());
            ps.setString(5, membre.getAdresse());
            ps.setString(6, membre.getNumTel());
            ps.setString(7, membre.getMotDePasse()); // Correction ici
            ps.setInt(8, membre.getId()); // Correction ici

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
    public void SupprimerMem(Utilisateurs membre) {
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
    public List<Utilisateurs> RechercherMem() {
        String req = "SELECT * FROM Membres";
        List<Utilisateurs> membres = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Utilisateurs m = new Utilisateurs();
                m.setId(rs.getInt("Id"));
                m.setNom(rs.getString("Nom"));
                m.setPrenom(rs.getString("Prénom"));
                m.setEmail(rs.getString("Email"));
                m.setCin(rs.getString("CIN"));
                m.setAdresse(rs.getString("Adresse"));
                m.setNumTel(rs.getString("NumTel"));
                m.setMotDePasse(rs.getString("MotDePasse"));

                membres.add(m);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des membres : " + e.getMessage());
            e.printStackTrace();
        }

        return membres;
    }

    public Utilisateurs rechercherMem(int id) {
        String req = "SELECT * FROM Membres WHERE Id = ?";
        Utilisateurs membre = null;

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                membre = new Utilisateurs();
                membre.setId(rs.getInt("Id"));
                membre.setNom(rs.getString("Nom"));
                membre.setPrenom(rs.getString("Prénom"));
                membre.setEmail(rs.getString("Email"));
                membre.setCin(rs.getString("CIN"));
                membre.setAdresse(rs.getString("Adresse"));
                membre.setNumTel(rs.getString("NumTel"));
                membre.setMotDePasse(rs.getString("MotDePasse"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du membre par ID : " + e.getMessage());
            e.printStackTrace();
        }

        return membre;
    }

    public Utilisateurs rechercherMemParNom(String username) {
        String req = "SELECT * FROM Membres WHERE Nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateurs utilisateur = new Utilisateurs();
                    utilisateur.setId(rs.getInt("Id"));
                    utilisateur.setNom(rs.getString("Nom"));
                    utilisateur.setPrenom(rs.getString("Prénom"));
                    utilisateur.setEmail(rs.getString("Email"));
                    utilisateur.setCin(rs.getString("CIN"));
                    utilisateur.setAdresse(rs.getString("Adresse"));
                    utilisateur.setNumTel(rs.getString("NumTel"));
                    utilisateur.setMotDePasse(rs.getString("MotDePasse"));
                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
