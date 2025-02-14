package Tests;

import Models.Membres;
import Services.MembresService;
import Services.Ireclamation;
import Services.ReclamationService;
import Models.Reclamation;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MembresService membresService = new MembresService();
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
                    // Ajouter un membre
                    System.out.print("Entrez le nom du membre: ");
                    String nom = scanner.nextLine();
                    System.out.print("Entrez le prénom du membre: ");
                    String prenom = scanner.nextLine();
                    System.out.print("Entrez l'email du membre: ");
                    String email = scanner.nextLine();
                    System.out.print("Entrez le CIN du membre: ");
                    String cin = scanner.nextLine();
                    System.out.print("Entrez l'adresse du membre: ");
                    String adresse = scanner.nextLine();
                    System.out.print("Entrez le numéro de téléphone du membre: ");
                    String numTel = scanner.nextLine();
                    Membres nouveauMembre = new Membres(nom, prenom, email, cin, adresse, numTel);
                    membresService.AjouterMem(nouveauMembre);
                    break;

                case 2:
                    // Modifier un membre
                    System.out.print("Entrez l'ID du membre à modifier: ");
                    int idMemMod = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Entrez la nouvelle adresse du membre: ");
                    String newAdresse = scanner.nextLine();
                    Membres membreModifie = new Membres();
                    membreModifie.setId(idMemMod);
                    membreModifie.setAdresse(newAdresse);
                    membresService.ModifierMem(membreModifie);
                    break;

                case 3:
                    // Supprimer un membre
                    System.out.print("Entrez l'ID du membre à supprimer: ");
                    int idMemSupp = scanner.nextInt();
                    scanner.nextLine();
                    Membres membreASupprimer = new Membres();
                    membreASupprimer.setId(idMemSupp);
                    membresService.SupprimerMem(membreASupprimer);
                    break;

                case 4:
                    // Afficher tous les membres
                    List<Membres> membres = membresService.RechercherMem();
                    for (Membres m : membres) {
                        System.out.println(m);
                    }
                    break;

                case 5:
                    // Ajouter une réclamation
                    System.out.print("Entrez l'ID de la réclamation: ");
                    int idRec = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Entrez le titre de la réclamation: ");
                    String titre = scanner.nextLine();
                    System.out.print("Entrez la description de la réclamation: ");
                    String description = scanner.nextLine();
                    Reclamation nouvelleReclamation = new Reclamation(idRec, titre, description);
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
                    Reclamation reclamationModifiee = new Reclamation(idRecMod, newTitre, newDescription);
                    reclamationService.ModifierRec(reclamationModifiee);
                    break;

                case 7:
                    // Supprimer une réclamation
                    System.out.print("Entrez l'ID de la réclamation à supprimer: ");
                    int idRecSupp = scanner.nextInt();
                    scanner.nextLine();
                    Reclamation reclamationASupprimer = new Reclamation(idRecSupp, "", "");
                    reclamationService.SupprimerRec(reclamationASupprimer);
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
