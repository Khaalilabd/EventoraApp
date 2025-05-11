package Models.Pack;

import Models.Service.Service;

import java.util.ArrayList;
import java.util.List;

public class Pack {
    private int id;
    private String nomPack;
    private String description;
    private double prix;
    private Location location;
    private Evenement type;
    private int nbrGuests;
    private List<Service> nomServices; // Changed to List<Service>

    // Default constructor
    public Pack() {
        this.nomServices = new ArrayList<>();
    }

    // Constructor with id
    public Pack(int id, String nomPack, String description, double prix, Location location, Evenement type, int nbrGuests, List<Service> nomServices) {
        this.id = id;
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;
        this.nomServices = nomServices != null ? nomServices : new ArrayList<>();
    }

    // Constructor without id
    public Pack(String nomPack, String description, double prix, Location location, Evenement type, int nbrGuests, List<Service> nomServices) {
        this.nomPack = nomPack;
        this.description = description;
        this.prix = prix;
        this.location = location;
        this.type = type;
        this.nbrGuests = nbrGuests;
        this.nomServices = nomServices != null ? nomServices : new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomPack() {
        return nomPack;
    }

    public void setNomPack(String nomPack) {
        this.nomPack = nomPack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Evenement getType() {
        return type;
    }

    public void setType(Evenement type) {
        this.type = type;
    }

    public int getNbrGuests() {
        return nbrGuests;
    }

    public void setNbrGuests(int nbrGuests) {
        this.nbrGuests = nbrGuests;
    }

    public List<Service> getNomServices() { // Updated getter
        return nomServices;
    }

    public void setNomServices(List<Service> nomServices) { // Updated setter
        this.nomServices = nomServices != null ? nomServices : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", nomPack='" + nomPack + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", location=" + location +
                ", type=" + type +
                ", nbrGuests=" + nbrGuests +
                ", nomServices=" + nomServices +
                '}';
    }
}