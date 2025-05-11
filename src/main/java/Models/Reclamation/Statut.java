package Models.Reclamation;

public enum Statut {
    EN_ATTENTE("En_Attente"),
    EN_COURS("En_Cours"),
    RESOLUE("Resolue"),
    REJETEE("Rejetée");

    private final String label;

    Statut(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Convertit une chaîne en Statut
    public static Statut fromLabel(String label) {
        for (Statut statut : Statut.values()) {
            if (statut.label.equalsIgnoreCase(label)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Statut invalide : " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
