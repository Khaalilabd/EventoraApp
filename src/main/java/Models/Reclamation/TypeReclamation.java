package Models.Reclamation;

public enum TypeReclamation {
    PACKS("Pack"),
    SERVICE("Service"),
    PROBLEME_TECHNIQUE("Problème Technique"),
    PLAINTE_AGENT_CONTROLE("Plainte entre un Agent de contrôle"),
    AUTRE("Autre");

    private final String label;

    TypeReclamation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Convertit une chaîne en TypeReclamation
    public static TypeReclamation fromLabel(String label) {
        for (TypeReclamation type : TypeReclamation.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de réclamation invalide : " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
