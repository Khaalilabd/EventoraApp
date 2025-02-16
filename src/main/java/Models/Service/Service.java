package Models.Service;

public class Service {
    private int id;
    private String titre;
    private Location location;
    private Sponsors sponsors;
    private String description;
    private String prix;

    public Service(int id) {
        this.id = id;
    }

    public Service() {

    }

    public Service(int id, String titre, Location location, Sponsors sponsors, String description, String prix) {
        this.id = id;
        this.titre = titre;
        this.location = location;
        this.sponsors = sponsors;
        this.description = description;
        this.prix = prix;
    }

    public Service(String titre, Location location, Sponsors sponsors, String description, String prix) {
        this.titre = titre;
        this.location = location;
        this.sponsors = sponsors;
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

    public Sponsors getSponsors() {
        return sponsors;
    }

    public void setSponsors(Sponsors sponsors) {
        this.sponsors = sponsors;
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

    @Override
    public String toString() {
        return "G_service{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", location='" + location + '\'' +
                ", sponsors='" + sponsors + '\'' +
                ", description='" + description + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
}