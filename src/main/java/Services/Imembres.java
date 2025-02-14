package Services;

import java.util.List;

public interface Imembres<T> {
    void AjouterMem(T t);
    void ModifierMem(T t);
    void SupprimerMem(T t);
    List<T> RechercherMem();
    // T rechercherMem(int id);  // Recherche par ID
}
