package Tests;

import Models.Reservation.ReservationJointureServices;
import Models.Reservation.ReservationPack;
import Models.Reservation.ReservationPersonalise;
import Models.Utilisateur.Utilisateurs;
import Services.Reservation.Crud.ReservationJointuresServicesService;
import Services.Reservation.Crud.ReservationPackService;
import Services.Reservation.Crud.ReservationPersonaliseService;
import Services.Utilisateur.MembresService;
import Services.Reclamation.Interface.Ireclamation;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Création d'un objet ReservationPack
       // ReservationPack reservationPack = new ReservationPack(1, 101, "trad", "rayen", "rayen@esprit.com", "123456789", "Pack Standard", new Date());

        // Service ReservationPack
     //   ReservationPackService servicePack = new ReservationPackService();

        // Test d'ajout
        //servicePack.ajouterReservationPack(reservationPack);

        // Test de modification
      //  reservationPack.setNom("rayuno");
      //  reservationPack.setPrenom("tradisto");
      //  servicePack.modifierReservationPack(reservationPack);

        // Test de recherche
       // servicePack.rechercherReservationPack();

        // Test de suppression
       // servicePack.supprimerReservationPack(reservationPack);
        // Création d'un objet ReservationPersonalise
      // ReservationPersonalise reservationPersonalise = new ReservationPersonalise("birthday", "hmida", "nadhem", "nadhem@esprit.com", "123456789", "Séance de relaxation", new Date());

        // Service ReservationPersonalise
      //  ReservationPersonaliseService servicePersonalise = new ReservationPersonaliseService();

        // Test d'ajout
       // servicePersonalise.ajouterReservationPersonalise(reservationPersonalise);

        // Test de modification

      // servicePersonalise.modifierReservationPersonalise(new ReservationPersonalise(2,"birthday", "trad", "nadhem", "nadhem@esprit.com", "00000000", "Séance de relaxation", new Date()));
        // Test de recherche
       // System.out.println(servicePersonalise.rechercherReservationPersonalise());

        // Test de suppression
       //servicePersonalise.supprimerReservationPersonalise(new ReservationPersonalise(2));

        }
}
