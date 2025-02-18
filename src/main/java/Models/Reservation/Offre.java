package Models.Reservation;

import java.util.Date;

public class Offre {
    private int idoffre;
    private Type typeoffre;
    private Date datecreation;

    // Constructeur par défaut
    public Offre() {
    }

    public Offre(int idoffre) {
        this.idoffre = idoffre;
    }

    // Constructeur avec tous les champs
    public Offre(int idoffre, Type typeOffre, Date dateCreation) {
        this.idoffre = idoffre;
        this.typeoffre = typeOffre;
        this.datecreation = dateCreation;
    }

    // Constructeur sans idoffre (utile pour l'insertion en base de données)
    public Offre(Type typeOffre, Date dateCreation) {
        this.typeoffre = typeOffre;
        this.datecreation = dateCreation;
    }

    // Getters et Setters
    public int getIdoffre() {
        return idoffre;
    }

    public void setIdoffre(int idoffre) {
        this.idoffre = idoffre;
    }

    public Type getTypeOffre() {
        return typeoffre;
    }

    public void setTypeOffre(Type typeOffre) {
        this.typeoffre = typeOffre;
    }

    public Date getDateCreation() {
        return datecreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.datecreation = dateCreation;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Offre{" +
                "idoffre=" + idoffre +
                ", typeOffre='" + typeoffre + '\'' +
                ", dateCreation=" + datecreation +
                '}';
    }
}