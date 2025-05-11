package Models.Reclamation;

public class ReservationPack {

    private int id;
    private int idUser;
    private String titre;
    private String description;
    private TypeReclamation type;

    // Constructeurs
    public ReservationPack() {
    }

    public ReservationPack(int idUser, String titre, String description, TypeReclamation type) {
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
    }

    public ReservationPack(int id, int idUser, String titre, String description, TypeReclamation type) {
        this.id = id;
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
    }

    public ReservationPack(int id) {
        this.id = id;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeReclamation getType() {
        return type;
    }

    public void setType(TypeReclamation type) {
        this.type = type;
    }

    // Méthode toString pour afficher les détails de la réclamation
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type.getLabel() + '\'' +
                '}';
    }
}
