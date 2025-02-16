package Models.Reclamation;

public class FeedBack {

    private int id;
    private int idUser; // Clé étrangère vers la table Membres
    private int vote;
    private String description;

    public FeedBack() {
    }

    public FeedBack(int idUser, int vote, String description) {
        this.idUser = idUser;
        this.vote = vote;
        this.description = description;
    }

    public FeedBack(int id, int idUser, int vote, String description) {
        this.id = id;
        this.idUser = idUser;
        this.vote = vote;
        this.description = description;
    }

    public FeedBack(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "FeedBack{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", vote=" + vote +
                ", description='" + description + '\'' +
                '}';
    }
}
