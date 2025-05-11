package Services.Reservation.Interface;

import java.util.List;

public interface IReservationPersonalise<T> {
    void ajouterReservationPersonalise(T t);
    void modifierReservationPersonalise(T t);
    void supprimerReservationPersonalise(T t);
    List<T> rechercherReservationPersonalise();
}
