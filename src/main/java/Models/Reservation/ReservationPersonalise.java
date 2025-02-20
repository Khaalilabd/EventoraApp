package Models.Reservation;

import java.util.Date;

public class ReservationPersonalise {
    private int idReservationPersonalise;
    private String services;
    private String nom;
    private String prenom;
    private String email;
    private String numtel;
    private String description;
    private Date date;

    public ReservationPersonalise(int idReservationPersonalise, String services, String nom, String prenom, String email, String numtel, String description, Date date) {
        this.idReservationPersonalise = idReservationPersonalise;
        this.services = services;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
    }

    public ReservationPersonalise(String services, String nom, String prenom, String email, String numtel, String description, Date date) {
        this.services = services;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
    }

    public ReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
    }
    public ReservationPersonalise() {

    }

    public int getIdReservationPersonalise() {
        return idReservationPersonalise;
    }

    public void setIdReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
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

    @Override
    public String toString() {
        return "ReservationPersonalise{" +
                "idReservationPersonalise=" + idReservationPersonalise +
                ", services='" + services + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", numtel='" + numtel + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
