package Models.Reclamation;

public class Feedback {

    private int id;
    private int idUser; // Clé étrangère vers la table Membres
    private int vote;
    private String description;
    private byte[] image; // Nouveau champ pour l'image (en tant que tableau de bytes)

    // Constructeur par défaut
    public Feedback() {
    }

    // Constructeur sans id (lors de la création)
    public Feedback(int idUser, int vote, String description, byte[] image) {
        this.idUser = idUser;
        this.vote = vote;
        this.description = description;
        this.image = image;
    }

    // Constructeur avec id (lors de la modification ou récupération de l'élément)
    public Feedback(int id, int idUser, int vote, String description, byte[] image) {
        this.id = id;
        this.idUser = idUser;
        this.vote = vote;
        this.description = description;
        this.image = image;
    }

    // Constructeur avec id uniquement (pour certaines situations)
    public Feedback(int id) {
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

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", vote=" + vote +
                ", description='" + description + '\'' +
                ", image=" + (image != null ? "Image exists" : "No image") + // Afficher un message si l'image existe
                '}';
    }

    public void setRecommend(boolean recommend) {
    }
}
