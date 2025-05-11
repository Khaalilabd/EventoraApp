package Models.Reclamation;

import java.time.LocalDateTime; // Ajout de l'import pour LocalDateTime

public class Reclamation {

    private int id;
    private int idUser;
    private String titre;
    private String description;
    private TypeReclamation type;
    private Statut statut;
    private String qrCodeUrl;
    private LocalDateTime date; // Nouveau champ pour la date

    public Reclamation() {
        this.statut = Statut.EN_ATTENTE;
        this.date = LocalDateTime.now(); // Valeur par défaut : date actuelle
    }

    public Reclamation(int idUser, String titre, String description, TypeReclamation type) {
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.statut = Statut.EN_ATTENTE;
        this.date = LocalDateTime.now(); // Initialiser la date à la création
    }

    public Reclamation(int id, int idUser, String titre, String description, TypeReclamation type, Statut statut) {
        this.id = id;
        this.idUser = idUser;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.statut = statut;
        this.date = LocalDateTime.now(); // Initialiser la date à la création
    }

    public Reclamation(int id) {
        this.id = id;
        this.statut = Statut.EN_ATTENTE;
        this.date = LocalDateTime.now();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeReclamation getType() {
        return type;
    }

    public void setType(TypeReclamation type) {
        this.type = type;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Méthode toString pour afficher les détails de la réclamation
    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type.getLabel() + '\'' +
                ", statut='" + statut.getLabel() + '\'' +
                ", date=" + date + // Ajout de la date dans toString
                '}';
    }
}