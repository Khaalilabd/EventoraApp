package Models.Reclamation;

public class Reclamation {

    private int id;
    private int idUser;
    private String titre;
    private String description;
    private TypeReclamation type;
    private Statut statut;  // Utilisation de l'Enum Statut

    // Constructeurs
    public Reclamation() {
        this.statut = Statut.EN_ATTENTE;
    }

    public Reclamation(int idUser, String titre, String description, TypeReclamation type) {
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.statut = Statut.EN_ATTENTE;  // Valeur par défaut
    }

    public Reclamation(int id, int idUser, String titre, String description, TypeReclamation type, Statut statut) {
        this.id = id;
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.statut = statut;
    }


    public Reclamation(int id) {
        this.id = id;
        this.statut = Statut.EN_ATTENTE;  // Valeur par défaut
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

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
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
                ", statut='" + statut.getLabel() + '\'' +  // Affichage du statut
                '}';
    }
}
