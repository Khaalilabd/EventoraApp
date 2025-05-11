package Services.Reservation.Interface;

import Models.Reservation.ReservationPersonalise;

import java.util.List;

public interface IReservationPersonalise<T> {
    void ajouterReservationPersonalise(T t);
    ReservationPersonalise modifierReservationPersonalise(T t);
    void supprimerReservationPersonalise(T t);
    List<T> rechercherReservationPersonalise();
}
