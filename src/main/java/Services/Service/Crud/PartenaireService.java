package Services.Service.Crud;

import Models.Service.Partenaire;
import Models.Service.TypePartenaire;
import Services.Service.Interface.Ipartenaire;
import Utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartenaireService implements Ipartenaire<Partenaire> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void AjouterPartenaire(Partenaire partenaire) {
        String req= "INSERT INTO `sponsors`( `nom_sponsors`, `email_sponsors`, `telephone_sponsors`, `adresse_sponsors`, `site_web`, `montant_sponsors`, `type_sponsors`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, partenaire.getNom_sponsors());
            ps.setString(2, partenaire.getEmail_sponsors());
            ps.setString(3,partenaire.getTelephone_sponsors());
            ps.setString(4, partenaire.getAdresse_sponsors());
            ps.setString(5, partenaire.getSite_web());
            ps.setString(6, partenaire.getMontant_sponsors());
            ps.setString(7, partenaire.getType_sponsors().getLabel());
            ps.executeUpdate();
            System.out.println("Partenaire ajouter");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void ModifierPartenaire(Partenaire partenaire) {
        String req = "UPDATE `sponsors` SET ,`nom_sponsors`=?,`email_sponsors`=?,`telephone_sponsors`=?,`adresse_sponsors`=?,`site_web`=?,`montant_sponsors`=?,`type_sponsors`=? WHERE `id_sponsors`=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, partenaire.getNom_sponsors());
            ps.setString(2, partenaire.getEmail_sponsors());
            ps.setString(3,partenaire.getTelephone_sponsors());
            ps.setString(4, partenaire.getAdresse_sponsors());
            ps.setString(5, partenaire.getSite_web());
            ps.setString(6, partenaire.getMontant_sponsors());
            ps.setString(7, partenaire.getType_sponsors().getLabel());
            ps.setInt(9, partenaire.getId_sponsors());
            ps.executeUpdate();
            System.out.println("Partenaire modifier");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SupprimerPartenaire(Partenaire partenaire) {
        String req = "DELETE FROM `sponsors` WHERE `id_sponsors` = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, partenaire.getId_sponsors());
            ps.executeUpdate();
            System.out.println("Partenaire supprimer");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Partenaire> RechercherPartenaire() {
        String req = "SELECT * FROM `sponsors`";
        List<Partenaire> partenaires = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery(req);
            while (rs.next()) {
                Partenaire partenaire = new Partenaire();
                partenaire.setId_sponsors(rs.getInt("id_sponsors"));
                partenaire.setNom_sponsors(rs.getString("nom_sponsors"));
                partenaire.setEmail_sponsors(rs.getString("email_sponsors"));
                partenaire.setTelephone_sponsors(rs.getString("telephone_sponsors"));
                partenaire.setAdresse_sponsors(rs.getString("adresse_sponsors"));
                partenaire.setSite_web(rs.getString("site_web"));
                partenaire.setMontant_sponsors(rs.getString("montant_sponsors"));
                partenaire.setType_sponsors(TypePartenaire.fromLabel(rs.getString("type_sponsors")));
                partenaires.add(partenaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partenaires;
    }

}
