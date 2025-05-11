package Services.Service.Interface;

import java.util.List;

public interface Ipartenaire<T>{
    void AjouterPartenaire(T t);
    void ModifierPartenaire(T t);
    void SupprimerPartenaire(T t);
    List<T> RechercherPartenaire();
}
