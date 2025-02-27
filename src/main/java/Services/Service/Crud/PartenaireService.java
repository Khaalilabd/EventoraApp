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
        String req= "INSERT INTO `sponsors`( `nom_partenaire`, `email_partenaire`, `telephone_partenaire`, `adresse_partenaire`, `site_web`, `type_partenaire`) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, partenaire.getNom_partenaire());
            ps.setString(2, partenaire.getEmail_partenaire());
            ps.setString(3,partenaire.getTelephone_partenaire());
            ps.setString(4, partenaire.getAdresse_partenaire());
            ps.setString(5, partenaire.getSite_web());
            ps.setString(6, partenaire.getType_partenaire().getLabel());
            ps.executeUpdate();
            System.out.println("Partenaire ajouter");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void ModifierPartenaire(Partenaire partenaire) {
        String req = "UPDATE `sponsors` SET `nom_partenaire`=?,`email_partenaire`=?,`telephone_partenaire`=?,`adresse_partenaire`=?,`site_web`=?,`type_partenaire`=? WHERE `id_partenaire`=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, partenaire.getNom_partenaire());
            ps.setString(2, partenaire.getEmail_partenaire());
            ps.setString(3,partenaire.getTelephone_partenaire());
            ps.setString(4, partenaire.getAdresse_partenaire());
            ps.setString(5, partenaire.getSite_web());
            ps.setString(6, partenaire.getType_partenaire().getLabel());
            ps.setInt(7, partenaire.getId_partenaire());
            ps.executeUpdate();
            System.out.println("Partenaire modifier");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SupprimerPartenaire(Partenaire partenaire) {
        String req = "DELETE FROM `sponsors` WHERE `id_partenaire` = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, partenaire.getId_partenaire());
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
                partenaire.setId_partenaire(rs.getInt("id_partenaire"));
                partenaire.setNom_partenaire(rs.getString("nom_partenaire"));
                partenaire.setEmail_partenaire(rs.getString("email_partenaire"));
                partenaire.setTelephone_partenaire(rs.getString("telephone_partenaire"));
                partenaire.setAdresse_partenaire(rs.getString("adresse_partenaire"));
                partenaire.setSite_web(rs.getString("site_web"));
                partenaire.setType_partenaire(TypePartenaire.fromLabel(rs.getString("type_partenaire")));
                partenaires.add(partenaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partenaires;
    }


}
