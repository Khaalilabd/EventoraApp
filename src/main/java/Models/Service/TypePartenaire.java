package Models.Service;

public enum TypePartenaire {
    DECORATION("Decoration"),
    PHOTOGRAPHE("Photographe"),
    TRAITEUR("Traiteur"), // Ajout de l'option "Média"
    SONO("Sono"),
    AUTRES("Autres");

    private final String label;

    TypePartenaire(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TypePartenaire fromLabel(String label) {
        for (TypePartenaire type : TypePartenaire.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de partenaire invalide : " + label);
    }
}
