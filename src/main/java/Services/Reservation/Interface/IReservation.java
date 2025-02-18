package Services.Reservation.Interface;

import java.util.List;

public interface IReservation<T> {

    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    List<T> rechercher();

}
