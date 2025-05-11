package Utils;

import Models.Utilisateur.Utilisateurs;

public class SessionManager {
    private static SessionManager instance;
    private Utilisateurs utilisateurConnecte;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUtilisateurConnecte(Utilisateurs utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    public Utilisateurs getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void clearSession() {
        utilisateurConnecte = null;
    }
}