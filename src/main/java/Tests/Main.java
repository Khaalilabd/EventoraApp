package Tests;

import Models.Pack.Location;
import Models.Pack.Pack;
import Models.Pack.TypePack;
import Models.Utilisateur.Utilisateurs;
import Services.Pack.Crud.PackService;
import Services.Pack.Crud.TypePackService;
import Services.Utilisateur.MembresService;
import Services.Reclamation.Interface.Ireclamation;
import Services.Reclamation.Crud.ReclamationService;
import Models.Reclamation.Reclamation;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TypePackService ps = new TypePackService();
        //ps.ajouter(new TypePack("birthday"));

        // Type existant
        //TypePack typePack = new TypePack("mariage");
        PackService p = new PackService();

        // Création d'un pack avec ce type
        //p.ajouter(new Pack("Luxurious wedding", "A fairytail like wedding", 500.0, Location.HOTEL, typePack, 100));
        //p.ajouter(new Pack("Dream Birthday", "Celebrate your birthday in style", 200.0, Location.SALLE_DE_FETE, new TypePack("birthday"), 50));
        //p.supprimer(new Pack("Dream Birthday"));

        p.rechercher();
        //ps.modifier(new TypePack(7,"Baby shower"));
        p.modifier(new Pack(1,"Luxurious wedding", "A fairytail like wedding", 500.0, Location.ESPACE_VERT, new TypePack("mariage"), 100,"traiteur"));

    }
}
