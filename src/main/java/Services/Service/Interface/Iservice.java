package Services.Service.Interface;

import java.util.List;

public interface Iservice <T>{
    void AjouterService(T t);
    void ModifierService(T t);
    void SupprimerService(T t);
    List<T> RechercherService();


}
