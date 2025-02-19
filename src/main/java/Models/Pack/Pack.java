package Models.Pack;

public class Pack {
    private int id;
    private String nomPack;
    private String description;
    private double prix;
    private Location location;
    private TypePack type;
    private int nbrGuests;
    private String nomService;

    public Pack() {
    }

    public Pack(int id, String nomPack, String description, double prix, Location location, TypePack type, int nbrGuests, String nomService) {
        this.id = id;
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;
        this.nomService = nomService;
    }

    public Pack( String nomPack, String description, double prix, Location location, TypePack type, int nbrGuests, String nomService) {
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;
        this.nomService = nomService;


    }

    public Pack(String nomPack) {
        this.nomPack = nomPack;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TypePack getType() {
        return type;
    }

    public void setType(TypePack type) {
        this.type = type;
    }

    public int getNbrGuests() {
        return nbrGuests;
    }

    public void setNbrGuests(int nbrGuests) {
        this.nbrGuests = nbrGuests;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", nomPack='" + nomPack + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", location=" + location +
                ", type=" + type +
                ", nbrGuests=" + nbrGuests +
                ", nomService='" + nomService + '\'' +
                '}';
    }
}
