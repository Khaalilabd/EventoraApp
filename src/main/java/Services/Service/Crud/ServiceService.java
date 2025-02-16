package Services.Service.Crud;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.Sponsors;
import Services.Service.Interface.Iservice;
import Utils.Mydatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceService implements Iservice<Service> {
    Connection connection = Mydatasource.getInstance().getConnection();


    @Override
    public void AjouterService(Service service) {
        String req = "INSERT INTO `g_service`(`titre`, `location`, `sponsors`, `description`, `prix`) VALUES (?,?,?,?,?)";
        try {

            PreparedStatement gs = connection.prepareStatement(req);
            gs.setString(1,service.getTitre());
            gs.setString(2,service.getLocation().getLabel());
            gs.setString(3,service.getSponsors().getLabel());
            gs.setString(4,service.getDescription());
            gs.setString(5,service.getPrix());
            gs.executeUpdate();
            System.out.println("service ajouter ");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierService(Service service) {
        String req = "UPDATE `g_service` SET `titre`= ?, `location`= ?, `sponsors`= ?, `description`= ?, `prix`= ? WHERE `id`= ?";
        try {
            PreparedStatement gs = connection.prepareStatement(req);
            gs.setString(1, service.getTitre());
            gs.setString(2, service.getLocation().getLabel());
            gs.setString(3, service.getSponsors().getLabel());
            gs.setString(4, service.getDescription());
            gs.setString(5, service.getPrix());
            gs.setInt(6, service.getId());
            gs.executeUpdate();
            System.out.println("service modifié");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void SupprimerService(Service service) {
        String req = "DELETE FROM `g_service` WHERE `id` = ?";
        try {
            PreparedStatement gs = connection.prepareStatement(req);
            gs.setInt(1,service.getId());
            gs.executeUpdate();
            System.out.println("service supprime ");

        }
        catch (Exception e){
            e.getMessage();

        }


    }

    @Override
    public List RechercherService() {
        String req = "SELECT * FROM `g_service`";
        List<Service> g_services = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Service gs = new Service();
                gs.setId(rs.getInt("id"));
                gs.setTitre(rs.getString("titre"));
                gs.setLocation(Location.fromLabel(rs.getString("location")));
                gs.setSponsors(Sponsors.fromLabel(rs.getString("sponsors")));
                gs.setDescription(rs.getString("description"));
                gs.setPrix(rs.getString("prix"));
                g_services.add(gs);
                System.out.println(g_services);

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  g_services;
    }

}
