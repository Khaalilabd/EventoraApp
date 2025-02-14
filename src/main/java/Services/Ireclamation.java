package Services;

import java.util.List;

public interface Ireclamation<T> {
    void AjouterRec(T t);
    void ModifierRec(T t);
    void SupprimerRec(T t);
    List<T> RechercherRec();
    // T rechercherParId(int id);  // Recherche par ID
}
