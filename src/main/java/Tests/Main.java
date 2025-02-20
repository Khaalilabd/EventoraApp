package Tests;

import Models.Pack.TypePack;
import Services.Pack.Crud.TypePackService;

public class Main {
    public static void main(String[] args) {
        TypePackService ps = new TypePackService();
          ps.ajouter(new TypePack("Thour"));
//        ps.ajouter(new TypePack("mariage"));
//        ps.ajouter(new TypePack("BabyShower"));
//        ps.ajouter(new TypePack("Graduation"));
//        ps.ajouter(new TypePack("Bachelorette Party"));


        // Type existant
        //TypePack typePack = new TypePack("mariage");
        //PackService p = new PackService();

        // Création d'un pack avec ce type
        //p.ajouter(new Pack("Luxurious wedding", "A fairytail like wedding", 500.0, Location.HOTEL, typePack, 100,"traiteur"));
        //p.ajouter(new Pack("Dream Birthday", "Celebrate your birthday in style", 200.0, Location.SALLE_DE_FETE, new TypePack("birthday"), 50,"Dj"));
        //p.supprimer(new Pack("Dream Birthday"));

        //p.rechercher();
        //ps.modifier(new TypePack(7,"Baby shower"));
        //p.modifier(new Pack(1,"Luxurious wedding", "A fairytail like wedding", 500.0, Location.ESPACE_VERT, new TypePack("mariage"), 100,"traiteur"));
         //ps.modifier(new TypePack(5,"Bachelorette"));
    }
}
