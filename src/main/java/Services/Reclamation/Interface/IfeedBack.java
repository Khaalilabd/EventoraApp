package Services.Reclamation.Interface;

import Models.Reclamation.Feedback;
import java.util.List;

public interface IfeedBack<T> {

    void AjouterFeedBack(T t);
    void ModifierFeedBack(T t);
    void SupprimerFeedBack(T t);
    List<T> RechercherFeedBack();

    List<Feedback> rechercherParUser(int idUser);

    List<Feedback> rechercherParVote(int vote);

    // Nouvelle méthode de recherche par mot-clé
    List<Feedback> rechercherParMotCle(String motCle);
}
