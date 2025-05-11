package Services.Pack.Interface;

import java.util.List;

public interface IPack <T> {
    void ajouter(T t);
    void supprimer(T t);
    void modifier(T t);
    List<T> rechercher();
}
