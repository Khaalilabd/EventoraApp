package Services.Pack.Crud;

import Models.Pack.TypePack;
import Services.Pack.Interface.IPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypePackService implements IPack<TypePack> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(TypePack typePack) {
        // Vérifier si le type existe déjà
        if (getTypePackByName(typePack.getType()) != null) {
            System.out.println("Le type de pack '" + typePack.getType() + "' existe déjà !");
            return;
        }

        String req = "INSERT INTO `typepack`(`type`) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            ps.executeUpdate();
            System.out.println("Pack Type " + typePack.getType() + " ajouté avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du TypePack : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(TypePack typePack) {
        String req = "DELETE FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Type Pack " + typePack.getType() + " supprimé avec succès !");
            } else {
                System.out.println("Aucun type trouvé avec ce nom !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du TypePack : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(TypePack typePack) {
        String req = "UPDATE `typepack` SET `type`= ? WHERE `id` = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            ps.setInt(2, typePack.getId());
            System.out.println("Updating TypePack with ID: " + typePack.getId() + " and Type: " + typePack.getType());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Type Pack " + typePack.getType() + " mis à jour avec succès !");
            } else {
                System.out.println("Aucun Type trouvé avec cet ID !");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification du TypePack : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<TypePack> rechercher() {
        String req = "SELECT * FROM `typepack`";
        List<TypePack> typePacks = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                typePacks.add(new TypePack(rs.getInt("id"), rs.getString("type")));
            }
            System.out.println(typePacks);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des TypePacks : " + e.getMessage());
            e.printStackTrace();
        }
        return typePacks;
    }

    public TypePack getTypePackByName(String typeName) {
        String req = "SELECT * FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TypePack(rs.getInt("id"), rs.getString("type"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du TypePack : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<TypePack> RechercherTypeParMotCle(String motCle) {
        String req = "SELECT id, type, sousType FROM typepack WHERE type LIKE ? ";

        List<TypePack> typePacks = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TypePack typePack = new TypePack(rs.getInt("id"), rs.getString("type"));
                    typePacks.add(typePack);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des types packs par mot-clé : " + e.getMessage());
            e.printStackTrace();
        }
        return typePacks;
    }
}
