package Tests;

import Services.Reclamation.Interface.Ireclamation;
import Services.Reclamation.Crud.ReclamationService;
import Models.Reclamation.Reclamation;
import Models.Reclamation.TypeReclamation;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Ireclamation<Reclamation> reclamationService = new ReclamationService();
        Scanner scanner = new Scanner(System.in);
        int choix;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Ajouter un membre");
            System.out.println("2. Modifier un membre");
            System.out.println("3. Supprimer un membre");
            System.out.println("4. Afficher tous les membres");
            System.out.println("5. Ajouter une réclamation");
            System.out.println("6. Modifier une réclamation");
            System.out.println("7. Supprimer une réclamation");
            System.out.println("8. Afficher toutes les réclamations");
            System.out.println("0. Quitter");
            System.out.print("Choisissez une option: ");
            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

            switch (choix) {
                case 1:


                case 2:


                case 3:

                    break;

                case 4:

                    break;

                case 5:
                    // Ajouter une réclamation
                    System.out.print("Entrez le titre de la réclamation: ");
                    String titre = scanner.nextLine();
                    System.out.print("Entrez la description de la réclamation: ");
                    String description = scanner.nextLine();
                    System.out.println("Choisissez un type de réclamation: ");
                    for (TypeReclamation type : TypeReclamation.values()) {
                        System.out.println(type.ordinal() + ". " + type);
                    }
                    System.out.print("Votre choix: ");
                    int typeIndex = scanner.nextInt();
                    scanner.nextLine();
                    TypeReclamation type = TypeReclamation.values()[typeIndex];
                    Reclamation nouvelleReclamation = new Reclamation(0, titre, description, type);
                    reclamationService.AjouterRec(nouvelleReclamation);
                    break;

                case 6:
                    // Modifier une réclamation
                    System.out.print("Entrez l'ID de la réclamation à modifier: ");
                    int idRecMod = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Entrez le nouveau titre de la réclamation: ");
                    String newTitre = scanner.nextLine();
                    System.out.print("Entrez la nouvelle description de la réclamation: ");
                    String newDescription = scanner.nextLine();
                    System.out.println("Choisissez un nouveau type de réclamation: ");
                    for (TypeReclamation t : TypeReclamation.values()) {
                        System.out.println(t.ordinal() + ". " + t);
                    }
                    System.out.print("Votre choix: ");
                    int newTypeIndex = scanner.nextInt();
                    scanner.nextLine();
                    TypeReclamation newType = TypeReclamation.values()[newTypeIndex];
                    Reclamation reclamationModifiee = new Reclamation(idRecMod, newTitre, newDescription, newType);
                    reclamationService.ModifierRec(reclamationModifiee);
                    break;


                case 8:
                    // Afficher toutes les réclamations
                    System.out.println("Liste des réclamations: " + reclamationService.RechercherRec());
                    break;

                case 0:
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 0);

        scanner.close();
    }
}
