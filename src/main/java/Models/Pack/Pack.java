package Models.Pack;

public class Pack {
    private int id;
    private String nomPack;
    private String description;
    private double prix;
    private Location location;
    private TypePack type;
    private int nbrGuests;

    public Pack() {
    }

    public Pack(int id, String nomPack, String description, double prix, Location location, TypePack type, int nbrGuests) {
        this.id = id;
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;
    }

    public Pack( String nomPack, String description, double prix, Location location, TypePack type, int nbrGuests) {
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;

    }

    public Pack(String nomPack,String nomPack2) {
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
                '}';
    }
}
