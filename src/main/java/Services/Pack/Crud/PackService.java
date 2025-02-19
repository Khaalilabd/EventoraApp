package Services.Pack.Crud;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.TypePack;
import Services.Pack.Interface.IPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackService implements IPack <Pack> {
    Connection connection = Mydatasource.getInstance().getConnection();
    @Override
    public void ajouter(Pack pack) {
        // Vérifier si le type existe déjà
        TypePackService typeService = new TypePackService();
        TypePack existingType = typeService.getTypePackByName(pack.getType().getType());

        if (existingType == null) {
            System.out.println("Le type " + pack.getType().getType() + " n'existe pas !");
            return;
        }

        String req ="INSERT INTO `pack`(`nomPack`, `description`, `prix`, `location`, `type`, `nbrGuests`,`nomService`) VALUES (?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, pack.getNomPack());
            ps.setString(2, pack.getDescription());
            ps.setDouble(3,pack.getPrix());
            ps.setString(4, pack.getLocation().getLabel());
            ps.setString(5,existingType.getType());
            ps.setInt(6, pack.getNbrGuests());
            ps.setString(7,pack.getNomService());


            ps.executeUpdate();
            System.out.println("Pack "+ pack.getNomPack()+ " Successfully added");

        }catch(Exception e){
            e.getMessage();
        }
    }

    @Override
    public void supprimer(Pack pack) {
        String req ="DELETE FROM `pack` WHERE `nomPack` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, pack.getNomPack());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Le Pack " + pack.getNomPack() +" est supprimé avec succès !");
            } else {
                System.out.println("Aucun pack trouvé avec ce nom!");
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void modifier(Pack pack) {
        String req ="UPDATE `pack` SET `nomPack`= ?,`description`= ?,`prix`= ?,`location`= ?,`type`= ?,`nbrGuests`= ?, `nomService` = ? WHERE `id` = ?";
        try{

            // Print values for debugging
            System.out.println("Modifying pack with ID: " + pack.getId());
            System.out.println("New values: ");
            System.out.println("Name: " + pack.getNomPack());
            System.out.println("Description: " + pack.getDescription());
            System.out.println("Price: " + pack.getPrix());
            System.out.println("Location: " + pack.getLocation());
            System.out.println("Type: " + pack.getType().getType());
            System.out.println("Guests: " + pack.getNbrGuests());
            System.out.println("Service: " + pack.getNomService());

            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, pack.getNomPack());
            ps.setString(2, pack.getDescription());
            ps.setDouble(3, pack.getPrix());
            ps.setString(4, pack.getLocation().getLabel());
            ps.setString(5,pack.getType().getType());
            ps.setInt(6,pack.getNbrGuests());
            ps.setString(7,pack.getNomService());
            ps.setInt(8,pack.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Pack " + pack.getNomPack() + " est mis à jour avec succès !");
            } else {
                System.out.println("Aucun Pack trouvé avec ce nom !");
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public List<Pack> rechercher() {
        String req ="SELECT * FROM `pack`";
        List<Pack> packs = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()){
                    packs.add(new Pack( rs.getInt("id"), rs.getString("nomPack"),rs.getString("description"),rs.getDouble("prix"), Location.valueOf(rs.getString("location")),new TypePack(rs.getString("type")),rs.getInt("nbrGuests"),rs.getString("nomService")));
            }                System.out.println(packs);

        } catch (SQLException e) {
            e.getMessage();
        }

        return packs;
    }
}
