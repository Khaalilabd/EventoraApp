package Services.Reservation.Interface;

import java.util.List;

public interface IRJS<T> {
    void ajouter(T t);
    void supprimer(T t);
    List<T> rechercher();
}
