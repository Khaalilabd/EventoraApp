package Models.Reclamation;

public enum Recommend {
    Oui("Oui"),
    Non("Non");

    private final String label;

    // Constructeur pour lier chaque énumération à son libellé
    Recommend(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Méthode statique pour convertir un libellé en valeur enum
    public static Recommend fromLabel(String label) {
        // Vérifier si le label est nul ou vide avant de continuer
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé ne peut pas être vide ou nul.");
        }

        for (Recommend r : Recommend.values()) {
            // Comparaison stricte des labels (en ignorant la casse)
            if (r.label.equalsIgnoreCase(label.trim())) {
                return r;
            }
        }

        // Liste des labels valides dans le message d'erreur
        throw new IllegalArgumentException("Valeur invalide pour Recommend : " + label + ". Valeurs valides : Oui, Non.");
    }

    // Redéfinition de toString pour retourner le label
    @Override
    public String toString() {
        return label;
    }
}
