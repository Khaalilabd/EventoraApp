package Models.Reclamation;

public class Feedback {
    private int id;
    private int idUser;
    private int vote;
    private String description;
    private Recommend recommend;

    public Feedback(String description, int vote, Recommend recommend) {
        this.description = description;
        this.vote = vote;
        this.recommend = recommend;
    }

    // Constructeur par défaut
    public Feedback() {
    }

    public Feedback(int id, String description, int vote, Recommend recommend) {
        this.id = id;
        this.description = description;
        this.vote = vote;
        this.recommend = recommend;
    }

    // Getters et setters
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

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }
}
