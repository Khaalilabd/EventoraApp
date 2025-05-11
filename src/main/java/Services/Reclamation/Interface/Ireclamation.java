package Services.Reclamation.Interface;

import Models.Reclamation.Reclamation;

import java.util.List;

public interface Ireclamation<T> {
    void AjouterRec(T t);
    void ModifierRec(T t);
    void SupprimerRec(T t);
    List<T> RechercherRec();
    List<T> RechercherRecParMotCle(String motCle); // Ajout de la méthode pour rechercher par mot-clé

    void ModifierStatut(Reclamation reclamation);
}
