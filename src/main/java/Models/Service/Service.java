package Models.Service;

public class Service {
    private int id;
    private int id_partenaire;
    private String titre;
    private Location location;
    private TypeService typeService;
    private String description;
    private String prix;

    public Service(int id) {
        this.id = id;
    }

    public Service() {

    }

    public Service(int id, int id_partenaire, String titre, Location location, TypeService typeService, String description, String prix) {
        this.id = id;
        this.id_partenaire = id_partenaire;
        this.titre = titre;
        this.location = location;
        this.typeService = typeService;
        this.description = description;
        this.prix = prix;
    }

    public Service(int id_partenaire, String titre, Location location, TypeService typeService, String description, String prix) {
        this.id_partenaire = id_partenaire;
        this.titre = titre;
        this.location = location;
        this.typeService = typeService;
        this.description = description;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public int getId_partenaire() {
        return id_partenaire;
    }

    public void setId_partenaire(int id_partenaire) {
        this.id_partenaire = id_partenaire;
    }

    @Override
    public String toString() {
        return "G_service{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", location='" + location + '\'' +
                ", typeService='" + typeService + '\'' +
                ", description='" + description + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
}