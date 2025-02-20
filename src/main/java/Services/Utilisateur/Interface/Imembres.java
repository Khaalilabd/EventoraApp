package Services.Utilisateur.Interface;

import java.util.List;

public interface Imembres<T> {
    void AjouterMem(T t);
    void ModifierMem(T t);
    void SupprimerMem(T t);
    List<T> RechercherMem();

    int getIdByNomPrenom(String nomPrenom);
    // T rechercherMem(int id);  // Recherche par ID
}
