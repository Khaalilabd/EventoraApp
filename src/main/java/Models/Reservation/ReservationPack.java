package Models.Reservation;

import java.util.Date;

public class ReservationPack {
    private int idReservationPack;
    private int idPack;
    private int idMembre;
    private String nom;
    private String prenom;
    private String email;
    private String numtel;
    private String description;
    private Date date;
    private String status;

    public ReservationPack(int idReservationPack) {
        this.idReservationPack = idReservationPack;
    }
    public ReservationPack() {

    }

    public ReservationPack(int idReservationPack, int idPack, int idMembre, String nom, String prenom, String email, String numtel, String description, Date date, String status) {
        this.idReservationPack = idReservationPack;
        this.idPack = idPack;
        this.idMembre = idMembre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public ReservationPack(int idPack, int idMembre, String nom, String prenom, String email, String numtel, String description, Date date, String status) {
        this.idPack = idPack;
        this.idMembre = idMembre;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getIdReservationPack() {
        return idReservationPack;
    }

    public void setIdReservationPack(int idReservationPack) {
        this.idReservationPack = idReservationPack;
    }

    public int getIdPack() {
        return idPack;
    }

    public void setIdPack(int idPack) {
        this.idPack = idPack;
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReservationPack{" +
                "idReservationPack=" + idReservationPack +
                ", idPack=" + idPack +
                ", idMembre=" + idMembre +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", numtel='" + numtel + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}