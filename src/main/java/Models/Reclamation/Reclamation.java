package Models.Reclamation;

public class Reclamation {

    private int id;
    private int idUser; // Clé étrangère vers la table Membres
    private String titre;
    private String description;

    // Constructeur par défaut
    public Reclamation() {
    }

    // Constructeur sans ID (pour l'ajout)
    public Reclamation(int idUser, String titre, String description) {
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
    }

    // Constructeur avec ID (pour la mise à jour ou la récupération)
    public Reclamation(int id, int idUser, String titre, String description) {
        this.id = id;
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
    }

    // Constructeur avec uniquement l'ID (pour la suppression ou la recherche)
    public Reclamation(int id) {
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

    // Méthode toString pour afficher les détails de la réclamation
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}