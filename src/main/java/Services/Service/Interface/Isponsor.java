package Services.Service.Interface;

import java.util.List;

public interface Isponsor <T>{
    void AjouterSponsor(T t);
    void ModifierSponsor(T t);
    void SupprimerSponsor(T t);
    List<T> RechercherSponsor();
}
