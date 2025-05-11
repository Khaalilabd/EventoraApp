package Services.Service.Interface;

import Models.Service.Location;
import Models.Service.TypeService;

import java.util.List;
import java.util.Map;

public interface Iservice <T>{
    void AjouterService(T t);
    void ModifierService(T t);
    void SupprimerService(T t);
    List<T> RechercherService();


    Map<Location, Integer> getLocationStats();

    Map<TypeService, Integer> getTypeServiceStats();

    List<String> getServiceNames();
}
