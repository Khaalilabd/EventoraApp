package Models.Service;


public class Partenaire {
    private int id_partenaire;
    private String nom_partenaire;
    private String email_partenaire;
    private String telephone_partenaire;
    private String adresse_partenaire;
    private String site_web;
    private TypePartenaire type_partenaire;
    private double latitude;
    private double longitude;
    // Autres champs existants...

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Partenaire() {

    }
    public Partenaire(int id_partenaire) {
        this.id_partenaire = id_partenaire;
    }

    public Partenaire(int id_partenaire, String nom_partenaire, String email_partenaire, String telephone_partenaire, String adresse_partenaire, String site_web, TypePartenaire type_partenaire) {
        this.id_partenaire = id_partenaire;
        this.nom_partenaire = nom_partenaire;
        this.email_partenaire = email_partenaire;
        this.telephone_partenaire = telephone_partenaire;
        this.adresse_partenaire = adresse_partenaire;
        this.site_web = site_web;
        this.type_partenaire = type_partenaire;
    }

    public Partenaire(String nom_partenaire, String email_partenaire, String telephone_partenaire, String adresse_partenaire, String site_web, TypePartenaire type_partenaire) {
        this.nom_partenaire = nom_partenaire;
        this.email_partenaire = email_partenaire;
        this.telephone_partenaire = telephone_partenaire;
        this.adresse_partenaire = adresse_partenaire;
        this.site_web = site_web;
        this.type_partenaire = type_partenaire;
    }

    public int getId_partenaire() {
        return id_partenaire;
    }

    public void setId_partenaire(int id_partenaire) {
        this.id_partenaire = id_partenaire;
    }

    public String getNom_partenaire() {
        return nom_partenaire;
    }

    public void setNom_partenaire(String nom_partenaire) {
        this.nom_partenaire = nom_partenaire;
    }

    public String getEmail_partenaire() {
        return email_partenaire;
    }

    public void setEmail_partenaire(String email_partenaire) {
        this.email_partenaire = email_partenaire;
    }

    public String getTelephone_partenaire() {
        return telephone_partenaire;
    }

    public void setTelephone_partenaire(String telephone_partenaire) {
        this.telephone_partenaire = telephone_partenaire;
    }

    public String getAdresse_partenaire() {
        return adresse_partenaire;
    }

    public void setAdresse_partenaire(String adresse_partenaire) {
        this.adresse_partenaire = adresse_partenaire;
    }

    public String getSite_web() {
        return site_web;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }



    public TypePartenaire getType_partenaire() {
        return type_partenaire;
    }

    public void setType_partenaire(TypePartenaire type_partenaire) {
        this.type_partenaire = type_partenaire;
    }


}

