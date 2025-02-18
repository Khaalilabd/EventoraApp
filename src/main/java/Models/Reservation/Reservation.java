package Models.Reservation;

import java.util.Date;

public class Reservation {
    private int idReservation;
    private int idoffre; // Nouveau champ ajouté
    private String nom;
    private String prenom;
    private String email;
    private String numTel;
    private String description;
    private Date date;

    // Constructeur par défaut
    public Reservation() {
    }

    // Constructeur avec idReservation uniquement
    public Reservation(int idReservation) {
        this.idReservation = idReservation;
    }

    // Constructeur avec tous les champs sauf idReservation et idoffre
    public Reservation(int idoffre,String nom, String prenom, String email, String numTel, String description, Date date) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.description = description;
        this.date = date;
        this.idoffre = idoffre;
    }

    // Constructeur avec tous les champs
    public Reservation(int idReservation, int idoffre, String nom, String prenom, String email, String numTel, String description, Date date) {
        this.idReservation = idReservation;
        this.idoffre = idoffre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
        this.description = description;
        this.date = date;
    }

    // Getters et Setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdoffre() {
        return idoffre;
    }

    public void setIdoffre(int idoffre) {
        this.idoffre = idoffre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", idoffre=" + idoffre + // Ajout du champ idoffre
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", numTel='" + numTel + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}