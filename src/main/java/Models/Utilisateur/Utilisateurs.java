package Models.Utilisateur;

import java.util.UUID;

public class Utilisateurs {
    private int id;
    private String nom;
    private String prenom;
    private String cin;
    private String email;
    private String adresse;
    private String numTel;
    private Role role;
    private String motDePasse;
    private String image;
    private String token; // Token pour confirmation email ou reset password
    private boolean isConfirmed; // Indique si l'utilisateur a confirmé son email

    // Constructeur avec tous les paramètres sauf l'ID et le token
    public Utilisateurs(String nom, String prenom, String cin, String email, String adresse, String numTel, String motDePasse, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = Role.MEMBRE; // Rôle par défaut
        this.motDePasse = motDePasse;
        this.image = image;
        this.token = UUID.randomUUID().toString(); // Génération d'un token unique
        this.isConfirmed = false; // L'utilisateur n'est pas confirmé au départ
    }

    // Constructeur avec ID
    public Utilisateurs(int id, String nom, String prenom, String cin, String email, String adresse, String numTel, Role role, String motDePasse, String image, String token, boolean isConfirmed) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = (role != null) ? role : Role.MEMBRE;
        this.motDePasse = motDePasse;
        this.image = image;
        this.token = token;
        this.isConfirmed = isConfirmed;
    }

    // Constructeur sans paramètres
    public Utilisateurs() {}

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}