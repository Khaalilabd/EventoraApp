package Services.Pack.Crud;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.Evenement;
import Models.Service.Service;
import Services.Pack.Interface.IPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackService implements IPack<Pack> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(Pack pack) {
        EvenementService typeService = new EvenementService();
        Evenement existingType = typeService.getEvenementByName(pack.getType().getTypeEvenement());

        if (existingType == null) {
            System.out.println("Le type " + pack.getType().getTypeEvenement() + " n'existe pas !");
            return;
        }

        String req = "INSERT INTO `pack`(`nomPack`, `description`, `prix`, `location`, `type`, `nbrGuests`) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pack.getNomPack());
            ps.setString(2, pack.getDescription());
            ps.setDouble(3, pack.getPrix());
            ps.setString(4, pack.getLocation().getLabel());
            ps.setString(5, existingType.getTypeEvenement());
            ps.setInt(6, pack.getNbrGuests());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int packId = rs.getInt(1);
                pack.setId(packId);
                addServicesToPack(packId, pack.getNomServices());
            }
            System.out.println("Pack " + pack.getNomPack() + " Successfully added");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addServicesToPack(int packId, List<Service> services) {
        String req = "INSERT INTO `pack_service`(`pack_id`, `service_titre`) VALUES (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            for (Service service : services) {
                ps.setInt(1, packId);
                ps.setString(2, service.getTitre());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout des services au pack : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Pack pack) {
        String req = "DELETE FROM `pack` WHERE `nomPack` = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, pack.getNomPack());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le Pack " + pack.getNomPack() + " est supprimé avec succès !");
            } else {
                System.out.println("Aucun pack trouvé avec ce nom!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Pack pack) {
        String req = "UPDATE `pack` SET `nomPack`= ?,`description`= ?,`prix`= ?,`location`= ?,`type`= ?,`nbrGuests`= ? WHERE `id` = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            System.out.println("Modifying pack with ID: " + pack.getId());
            System.out.println("New values: ");
            System.out.println("Name: " + pack.getNomPack());
            System.out.println("Description: " + pack.getDescription());
            System.out.println("Price: " + pack.getPrix());
            System.out.println("Location: " + pack.getLocation());
            System.out.println("Type: " + pack.getType().getTypeEvenement());
            System.out.println("Guests: " + pack.getNbrGuests());
            System.out.println("Services: " + pack.getNomServices());

            ps.setString(1, pack.getNomPack());
            ps.setString(2, pack.getDescription());
            ps.setDouble(3, pack.getPrix());
            ps.setString(4, pack.getLocation().getLabel());
            ps.setString(5, pack.getType().getTypeEvenement());
            ps.setInt(6, pack.getNbrGuests());
            ps.setInt(7, pack.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                // Update the services in the junction table
                removeServicesFromPack(pack.getId());
                addServicesToPack(pack.getId(), pack.getNomServices());
                System.out.println("Pack " + pack.getNomPack() + " est mis à jour avec succès !");
            } else {
                System.out.println("Aucun Pack trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeServicesFromPack(int packId) {
        String req = "DELETE FROM `pack_service` WHERE `pack_id` = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, packId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des services du pack : " + e.getMessage());
        }
    }

    @Override
    public List<Pack> rechercher() {
        String req = "SELECT p.*, ps.service_titre FROM `pack` p LEFT JOIN `pack_service` ps ON p.id = ps.pack_id";
        List<Pack> packs = new ArrayList<>();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(req)) {
            Pack currentPack = null;
            while (rs.next()) {
                int packId = rs.getInt("id");
                if (currentPack == null || currentPack.getId() != packId) {
                    currentPack = new Pack(
                            packId,
                            rs.getString("nomPack"),
                            rs.getString("description"),
                            rs.getDouble("prix"),
                            Location.valueOf(rs.getString("location")),
                            new Evenement(rs.getString("type")),
                            rs.getInt("nbrGuests"),
                            new ArrayList<>()
                    );
                    packs.add(currentPack);
                }
                String serviceTitre = rs.getString("service_titre");
                if (serviceTitre != null) {
                    Service service = new Service();
                    service.setTitre(serviceTitre);
                    currentPack.getNomServices().add(service);
                }
            }
            System.out.println(packs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return packs;
    }

    // Other methods remain unchanged (getAllEvenements, RechercherPackParMotCle, etc.)
    public List<Evenement> getAllEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM typepack";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evenement evenement = new Evenement(rs.getInt("id"), rs.getString("type"));
                evenements.add(evenement);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des types de packs : " + e.getMessage());
        }
        return evenements;
    }

    public List<Pack> RechercherPackParMotCle(String motCle) {
        String req = "SELECT p.id, p.nomPack, p.description, p.prix, p.location, p.type, p.nbrGuests, ps.service_titre " +
                "FROM pack p LEFT JOIN pack_service ps ON p.id = ps.pack_id " +
                "WHERE p.nomPack LIKE ? OR p.description LIKE ? OR p.prix LIKE ? OR p.location LIKE ? OR " +
                "p.type LIKE ? OR p.nbrGuests LIKE ? OR ps.service_titre LIKE ?";
        List<Pack> packs = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, "%" + motCle + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                Pack currentPack = null;
                while (rs.next()) {
                    int packId = rs.getInt("id");
                    if (currentPack == null || currentPack.getId() != packId) {
                        currentPack = new Pack();
                        currentPack.setId(packId);
                        currentPack.setNomPack(rs.getString("nomPack"));
                        currentPack.setDescription(rs.getString("description"));
                        currentPack.setPrix(rs.getDouble("prix"));
                        currentPack.setLocation(Location.valueOf(rs.getString("location")));
                        currentPack.setType(new Evenement(rs.getString("type")));
                        currentPack.setNbrGuests(rs.getInt("nbrGuests"));
                        currentPack.setNomServices(new ArrayList<>());
                        packs.add(currentPack);
                    }
                    String serviceTitre = rs.getString("service_titre");
                    if (serviceTitre != null) {
                        Service service = new Service();
                        service.setTitre(serviceTitre);
                        currentPack.getNomServices().add(service);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des packs par mot-clé : " + e.getMessage());
            e.printStackTrace();
        }
        return packs;
    }

    public String getNomPackById(int id) {
        String nomPack = null;
        String query = "SELECT nomPack FROM pack WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nomPack = rs.getString("nomPack");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom du pack par ID : " + e.getMessage());
        }
        return nomPack;
    }

    public int getIdPackByNom(String nomPack) {
        int idPack = -1;
        String query = "SELECT id FROM pack WHERE LOWER(nomPack) = LOWER(?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nomPack);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idPack = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'ID du pack par nom : " + e.getMessage());
        }
        return idPack;
    }
}