package Models.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationPersonalise {
    private int idReservationPersonalise;
    private String nom;
    private String prenom;
    private String email;
    private String numtel;
    private String description;
    private Date date;
    private List<Integer> serviceIds; // Nouvelle liste pour stocker plusieurs IDs de services

    // Constructeurs (incluant serviceIds)
    public ReservationPersonalise(int idReservationPersonalise, String nom, String prenom, String email, String numtel, String description, Date date, List<Integer> serviceIds) {
        this.idReservationPersonalise = idReservationPersonalise;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.serviceIds = serviceIds != null ? new ArrayList<>(serviceIds) : new ArrayList<>(); // Copie défensive
    }

    public ReservationPersonalise(String nom, String prenom, String email, String numtel, String description, Date date, List<Integer> serviceIds) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numtel;
        this.description = description;
        this.date = date;
        this.serviceIds = serviceIds != null ? new ArrayList<>(serviceIds) : new ArrayList<>(); // Copie défensive
    }

    // Constructeur pour la récupération depuis la base de données (incluant idReservationPersonalise)
    public ReservationPersonalise(int idReservationPersonalise) {
        this.idReservationPersonalise = idReservationPersonalise;
        this.serviceIds = new ArrayList<>(); // Initialisation par défaut
    }

    public ReservationPersonalise() {
        this.serviceIds = new ArrayList<>(); // Initialisation par défaut
    }

    public ReservationPersonalise(String nom, String prenom, String email, String numTel, String description, Date date) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numtel = numTel;
        this.description = description;
        this.date = date;
        this.serviceIds = new ArrayList<>(); // Initialisation par défaut
    }

    // Getters et setters (incluant serviceIds)
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

    public List<Integer> getServiceIds() {
        return serviceIds != null ? new ArrayList<>(serviceIds) : new ArrayList<>(); // Retourner une copie défensive
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds != null ? new ArrayList<>(serviceIds) : new ArrayList<>(); // Copie défensive
    }

    public void addServiceId(int serviceId) {
        if (this.serviceIds == null) {
            this.serviceIds = new ArrayList<>();
        }
        this.serviceIds.add(serviceId);
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
                ", serviceIds=" + serviceIds + // Mise à jour pour afficher la liste des IDs
                '}';
    }
}