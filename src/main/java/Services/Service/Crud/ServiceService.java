package Services.Service.Crud;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.TypeService;
import Services.Service.Interface.Iservice;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceService implements Iservice<Service> {
    Connection connection = Mydatasource.getInstance().getConnection();




    @Override
    public void AjouterService(Service service) {
        String req = "INSERT INTO `g_service`(`id_partenaire`,`titre`, `location`, `type_service`, `description`, `prix`) VALUES (?,?,?,?,?,?)";

        try {
            // 🔄 Vérification de la connexion
            if (connection == null || connection.isClosed()) {
                System.out.println("🔴 ERREUR : Connexion fermée, tentative de reconnexion...");
                connection = Mydatasource.getInstance().getConnection();
            } else {
                System.out.println("✅ Connexion toujours ouverte.");
            }

            // 🔍 Vérification des valeurs
            System.out.println("id_partenaire: " + service.getId_partenaire());
            System.out.println("titre: " + service.getTitre());
            System.out.println("location: " + service.getLocation());
            System.out.println("type_service: " + service.getTypeService());
            System.out.println("description: " + service.getDescription());
            System.out.println("prix: " + service.getPrix());

            // ✅ Préparation de la requête
            PreparedStatement gs = connection.prepareStatement(req);
            gs.setInt(1, service.getId_partenaire());
            gs.setString(2, service.getTitre());
            gs.setString(3, service.getLocation().getLabel());
            gs.setString(4, service.getTypeService().getLabel());
            gs.setString(5, service.getDescription());
            gs.setString(6, service.getPrix());
            // 📌 Exécution de la requête
            gs.executeUpdate();
            System.out.println("✅ Service ajouté avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void ModifierService(Service service) {


        String req = "UPDATE `g_service` SET `id_partenaire`= ?,`titre`= ?, `location`= ?, `type_service`= ?, `description`= ?, `prix`= ? WHERE `id`= ?";
        try {
            PreparedStatement gs = connection.prepareStatement(req);
            gs.setInt(1, service.getId_partenaire());
            gs.setString(2, service.getTitre());
            gs.setString(3, service.getLocation().getLabel());
            gs.setString(4, service.getTypeService().getLabel());
            gs.setString(5, service.getDescription());
            gs.setString(6, service.getPrix());
            gs.setInt(7, service.getId());

            int rowsUpdated = gs.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Service modifié avec succès.");
            } else {
                System.out.println("Aucun service trouvé avec l'ID " + service.getId() + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du service : " + e.getMessage());
        }
    }


    @Override
    public void SupprimerService(Service service) {
        String req = "DELETE FROM `g_service` WHERE `id` = ?";
        try {
            PreparedStatement gs = connection.prepareStatement(req);
            gs.setInt(1, service.getId());

            int rowsDeleted = gs.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Service supprimé avec succès.");
            } else {
                System.out.println("Aucun service trouvé avec l'ID " + service.getId() + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du service : " + e.getMessage());
        }
    }






    @Override
    public List<Service> RechercherService() {
        String req = "SELECT * FROM `g_service` ";
        List<Service> g_services = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(req);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service gs = new Service();
                gs.setId(rs.getInt("id"));
                gs.setId_partenaire(rs.getInt("id_partenaire"));
                gs.setTitre(rs.getString("titre"));
                gs.setLocation(Location.fromLabel(rs.getString("location")));
                gs.setTypeService(TypeService.fromLabel(rs.getString("type_service")));
                gs.setDescription(rs.getString("description"));
                gs.setPrix(rs.getString("prix"));
                g_services.add(gs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de services : " + e.getMessage());
        }
        return g_services;
    }

    // Récupérer les statistiques par Location
    public Map<Location, Integer> getLocationStats() {
        Map<Location, Integer> locationStats = new HashMap<>();
        String query = "SELECT location, COUNT(*) AS count FROM g_service GROUP BY location";

        try (Connection connection = Mydatasource.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String locationLabel = resultSet.getString("location");
                Location location = Location.fromLabel(locationLabel);
                int count = resultSet.getInt("count");
                locationStats.put(location, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locationStats;
    }

    // Récupérer les statistiques par TypeService
    public Map<TypeService, Integer> getTypeServiceStats() {
        Map<TypeService, Integer> typeServiceStats = new HashMap<>();
        String query = "SELECT type_service, COUNT(*) AS count FROM g_service GROUP BY type_service";


        try (Connection connection = Mydatasource.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String typeServiceLabel = resultSet.getString("type_service");
                TypeService typeService = TypeService.fromLabel(typeServiceLabel);
                int count = resultSet.getInt("count");
                typeServiceStats.put(typeService, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeServiceStats;
    }
}
