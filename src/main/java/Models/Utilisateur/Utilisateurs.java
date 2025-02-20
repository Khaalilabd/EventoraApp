package Models.Utilisateur;

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

    // Constructeur avec tous les paramètres
    public Utilisateurs(String nom, String prenom, String cin, String email, String adresse, String numTel, Role role, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = role;
        this.motDePasse = motDePasse;
    }

    // Constructeur avec id (9 paramètres)
    public Utilisateurs(int id, String nom, String prenom, String cin, String email, String adresse, String numTel, Role role, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = role;
        this.motDePasse = motDePasse;
    }

    // Constructeur sans paramètres (optionnel)
    public Utilisateurs() {}

    // Getters et setters
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

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cin='" + cin + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numTel='" + numTel + '\'' +
                ", role=" + role +
                ", motDePasse='" + motDePasse + '\'' +
                '}';
    }
}
