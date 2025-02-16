package Services.Service.Crud;

import Models.Service.Sponsor;
import Models.Service.TypeSponsors;
import Services.Service.Interface.Isponsor;
import Utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SponsorService implements Isponsor<Sponsor> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void AjouterSponsor(Sponsor sponsor) {
        String req= "INSERT INTO `sponsors`( `nom_sponsors`, `email_sponsors`, `telephone_sponsors`, `adresse_sponsors`, `site_web`, `montant_sponsors`, `type_sponsors`, `date_sponsoring`) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, sponsor.getNom_sponsors());
            ps.setString(2, sponsor.getEmail_sponsors());
            ps.setString(3,sponsor.getTelephone_sponsors());
            ps.setString(4, sponsor.getAdresse_sponsors());
            ps.setString(5, sponsor.getSite_web());
            ps.setString(6, sponsor.getMontant_sponsors());
            ps.setString(7, sponsor.getType_sponsors().getLabel());
            ps.setDate(8, new java.sql.Date(sponsor.getDate_sponsoring().getTime()));
            ps.executeUpdate();
            System.out.println("sponsors ajouter");
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void ModifierSponsor(Sponsor sponsor) {
        String req = "UPDATE `sponsors` SET ,`nom_sponsors`=?,`email_sponsors`=?,`telephone_sponsors`=?,`adresse_sponsors`=?,`site_web`=?,`montant_sponsors`=?,`type_sponsors`=?,`date_sponsoring`=? WHERE `id_sponsors`=?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, sponsor.getNom_sponsors());
            ps.setString(2, sponsor.getEmail_sponsors());
            ps.setString(3,sponsor.getTelephone_sponsors());
            ps.setString(4, sponsor.getAdresse_sponsors());
            ps.setString(5, sponsor.getSite_web());
            ps.setString(6, sponsor.getMontant_sponsors());
            ps.setString(7, sponsor.getType_sponsors().getLabel());
            ps.setDate(8, new java.sql.Date(sponsor.getDate_sponsoring().getTime()));
            ps.setInt(9, sponsor.getId_sponsors());
            ps.executeUpdate();
            System.out.println("sponsors modifier");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SupprimerSponsor(Sponsor sponsor) {
        String req = "DELETE FROM `sponsors` WHERE `id_sponsors` = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, sponsor.getId_sponsors());
            ps.executeUpdate();
            System.out.println("sponsors supprimer");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sponsor> RechercherSponsor() {
        String req = "SELECT * FROM `sponsors`";
        List<Sponsor> sponsors = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery(req);
            while (rs.next()) {
                Sponsor sponsor = new Sponsor();
                sponsor.setId_sponsors(rs.getInt("id_sponsors"));
                sponsor.setNom_sponsors(rs.getString("nom_sponsors"));
                sponsor.setEmail_sponsors(rs.getString("email_sponsors"));
                sponsor.setTelephone_sponsors(rs.getString("telephone_sponsors"));
                sponsor.setAdresse_sponsors(rs.getString("adresse_sponsors"));
                sponsor.setSite_web(rs.getString("site_web"));
                sponsor.setMontant_sponsors(rs.getString("montant_sponsors"));
                sponsor.setType_sponsors(TypeSponsors.fromLabel(rs.getString("type_sponsors")));
                sponsor.setDate_sponsoring(rs.getDate("date_sponsoring"));
                sponsors.add(sponsor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sponsors;
    }

}
