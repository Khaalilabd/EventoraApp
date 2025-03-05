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

    private String motDePasse;
    private String image; // Nouveau champ

    // Constructeur avec tous les paramètres sauf l'ID
    public Utilisateurs(String nom, String prenom, String cin, String email, String adresse, String numTel, String motDePasse, String image) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = Role.MEMBRE; // Définir le rôle par défaut comme MEMBRE
        this.motDePasse = motDePasse;
        this.image = image; // Ajout du champ image
    }

    // Constructeur avec tous les paramètres, y compris l'ID
    public Utilisateurs(int id, String nom, String prenom, String cin, String email, String adresse, String numTel, Role role, String motDePasse, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.adresse = adresse;
        this.numTel = numTel;
        this.role = role != null ? role : Role.MEMBRE; // Définir le rôle par défaut comme MEMBRE si non spécifié
        this.motDePasse = motDePasse;
        this.image = image; // Ajout du champ image
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

}