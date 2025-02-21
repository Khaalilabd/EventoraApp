package Services.Service.Crud;

import Models.Service.Location;
import Models.Service.Service;
import Models.Service.TypeService;
import Services.Service.Interface.Iservice;
import Utils.Mydatasource;

import java.math.BigDecimal; // Import BigDecimal
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

        try (PreparedStatement gs = connection.prepareStatement(req)) { // Try-with-resources
            gs.setInt(1, service.getId_partenaire());
            gs.setString(2, service.getTitre());
            gs.setString(3, service.getLocation().getLabel());
            gs.setString(4, service.getTypeService().getLabel());
            gs.setString(5, service.getDescription());
            gs.setString(6, service.getPrix()); // Utiliser setBigDecimal

            gs.executeUpdate();
            System.out.println("Service ajouté avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ModifierService(Service service) {
        String req = "UPDATE `g_service` SET `id_partenaire`= ?,`titre`= ?, `location`= ?, `type_service`= ?, `description`= ?, `prix`= ? WHERE `id`= ?";
        try (PreparedStatement gs = connection.prepareStatement(req)) {
            gs.setInt(1, service.getId_partenaire());
            gs.setString(2, service.getTitre());
            gs.setString(3, service.getLocation().getLabel());
            gs.setString(4, service.getTypeService().getLabel());
            gs.setString(5, service.getDescription());
            gs.setString(6, service.getPrix()); // Utiliser setBigDecimal
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
        try (PreparedStatement gs = connection.prepareStatement(req)) {
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
        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service gs = new Service(rs.getInt("id"), rs.getInt("id_partenaire"), rs.getString("titre"),
                        Location.fromLabel(rs.getString("location")), TypeService.fromLabel(rs.getString("type_service")),
                        rs.getString("description"), rs.getString("prix")); // Utiliser getBigDecimal
                g_services.add(gs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de services : " + e.getMessage());
        }
        return g_services;
    }
    @Override

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

    @Override
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

    @Override
    public List<String> getServiceNames() {
        List<String> serviceNames = new ArrayList<>();
        String req = "SELECT titre FROM g_service";

        try (PreparedStatement ps = connection.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                serviceNames.add(rs.getString("titre"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des noms de service: " + e.getMessage());
        }

        return serviceNames;
    }
}