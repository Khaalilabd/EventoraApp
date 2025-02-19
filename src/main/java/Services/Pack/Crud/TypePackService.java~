package Services.Pack.Crud;

import Models.Pack.TypePack;
import Services.Pack.Interface.IPack;
import Utils.Mydatasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypePackService implements IPack <TypePack> {
    Connection connection = Mydatasource.getInstance().getConnection();

    @Override
    public void ajouter(TypePack typePack) {

        // Vérifier si le type existe déjà
        if (getTypePackByName(typePack.getType()) != null) {
            System.out.println("Le type de pack '" + typePack.getType() + "' existe déjà !");
            return;
        }
        String req ="INSERT INTO `typepack`(`type`) VALUES (?)";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            ps.executeUpdate();
            System.out.println("Pack Type " + typePack.getType()+ " Successfully added");
        }catch(Exception e){
            e.getMessage();
        }

    }

    @Override
    public void supprimer(TypePack typePack) {
        String req ="DELETE FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Type Pack " + typePack.getType() +" est supprimé avec succès !");
            } else {
                System.out.println("Aucun type trouvé avec ce nom!");
            }
        }catch (Exception e){
            e.getMessage();
        }

    }

    @Override
    public void modifier(TypePack typePack) {
        String req ="UPDATE `typepack` SET `type`= ? WHERE `type` = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typePack.getType());
            ps.setString(2, typePack.getType()); //condition where
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Type Pack" + typePack.getType() + " est mis à jour avec succès !");
            } else {
                System.out.println("Aucun Type trouvé avec cet ID !");
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public List<TypePack> rechercher() {
        String req ="SELECT * FROM `typepack`";
        List<TypePack> typePacks = new ArrayList<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while(rs.next()){
                typePacks.add(new TypePack(rs.getInt("id"),rs.getString("type")));
            }                System.out.println(typePacks);

        } catch (SQLException e) {
            e.getMessage();
        }
        return typePacks;
    }

    public TypePack getTypePackByName(String typeName) {
        String req = "SELECT * FROM `typepack` WHERE type = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { // Si un résultat est trouvé
                return new TypePack(rs.getInt("id"), rs.getString("type"));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null; // Retourne null si le type n'existe pas
    }

}
