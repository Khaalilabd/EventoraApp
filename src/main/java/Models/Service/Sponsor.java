package Models.Service;

import java.util.Date;

public class Sponsor {
    private int id_sponsors;
    private String nom_sponsors;
    private String email_sponsors;
    private String telephone_sponsors;
    private String adresse_sponsors;
    private String site_web;
    private String montant_sponsors;
    private TypeSponsors type_sponsors;
    private Date date_sponsoring;

    public Sponsor() {

    }
    public Sponsor(int id_sponsors) {
        this.id_sponsors = id_sponsors;
    }

    public Sponsor(int id_sponsors, String nom_sponsors, String email_sponsors, String telephone_sponsors, String adresse_sponsors, String site_web, String montant_sponsors, TypeSponsors type, Date date_sponsoring) {
        this.id_sponsors = id_sponsors;
        this.nom_sponsors = nom_sponsors;
        this.email_sponsors = email_sponsors;
        this.telephone_sponsors = telephone_sponsors;
        this.adresse_sponsors = adresse_sponsors;
        this.site_web = site_web;
        this.montant_sponsors = montant_sponsors;
        this.type_sponsors = type_sponsors;
        this.date_sponsoring = date_sponsoring;
    }

    public Sponsor(String nom_sponsors, String email_sponsors, String telephone_sponsors, String adresse_sponsors, String site_web, String montant_sponsors, TypeSponsors type, Date date_sponsoring) {
        this.nom_sponsors = nom_sponsors;
        this.email_sponsors = email_sponsors;
        this.telephone_sponsors = telephone_sponsors;
        this.adresse_sponsors = adresse_sponsors;
        this.site_web = site_web;
        this.montant_sponsors = montant_sponsors;
        this.type_sponsors = type_sponsors;
        this.date_sponsoring = date_sponsoring;
    }

    public int getId_sponsors() {
        return id_sponsors;
    }

    public void setId_sponsors(int id_sponsors) {
        this.id_sponsors = id_sponsors;
    }

    public String getNom_sponsors() {
        return nom_sponsors;
    }

    public void setNom_sponsors(String nom_sponsors) {
        this.nom_sponsors = nom_sponsors;
    }

    public String getEmail_sponsors() {
        return email_sponsors;
    }

    public void setEmail_sponsors(String email_sponsors) {
        this.email_sponsors = email_sponsors;
    }

    public String getTelephone_sponsors() {
        return telephone_sponsors;
    }

    public void setTelephone_sponsors(String telephone_sponsors) {
        this.telephone_sponsors = telephone_sponsors;
    }

    public String getAdresse_sponsors() {
        return adresse_sponsors;
    }

    public void setAdresse_sponsors(String adresse_sponsors) {
        this.adresse_sponsors = adresse_sponsors;
    }

    public String getSite_web() {
        return site_web;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }

    public String getMontant_sponsors() {
        return montant_sponsors;
    }

    public void setMontant_sponsors(String montant_sponsors) {
        this.montant_sponsors = montant_sponsors;
    }

    public TypeSponsors getType_sponsors() {
        return type_sponsors;
    }

    public void setType_sponsors(TypeSponsors type_sponsors) {
        this.type_sponsors = type_sponsors;
    }

    public Date getDate_sponsoring() {
        return date_sponsoring;
    }

    public void setDate_sponsoring(Date date_sponsoring) {
        this.date_sponsoring = date_sponsoring;
    }
}

