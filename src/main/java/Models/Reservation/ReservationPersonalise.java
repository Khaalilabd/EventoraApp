package Models.Reservation;

import java.util.Date;

public class ReservationPersonalise {
    private int idReservationPersonalise;
    private String nom;
    private String prenom;
    private String email;
    private String numtel;
    private String description;
    private Date date;
    private int idService; // Clé étrangère vers la table g_service

    // Constructeurs (incluant idService)
    public ReservationPersonalise(int idReservationPersonalise, String nom, String prenom, String email, String numtel, String description, Date date, int idService) {
        this.idReservationPersonalise = idReservationPersonalise;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.idService = idService;
    }

    public ReservationPersonalise(String nom, String prenom, String email, String numtel, String description, Date date, int idService) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.idService = idService;
    }

    // Constructeur pour la récupération depuis la base de données (incluant idReservationPersonalise)
    public ReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
    }

    public ReservationPersonalise() {
    }

    public ReservationPersonalise(String nom, String prenom, String email, String numTel, String description, Date from) {
    }

    // Getters et setters (incluant idService)
    public int getIdReservationPersonalise() {
        return idReservationPersonalise;
    }

    public void setIdReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
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

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
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

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    @Override
    public String toString() {
        return "ReservationPersonalise{" +
                "idReservationPersonalise=" + idReservationPersonalise +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", numtel='" + numtel + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", idService=" + idService +  // Ajout de l'idService dans le toString
                '}';
    }
}