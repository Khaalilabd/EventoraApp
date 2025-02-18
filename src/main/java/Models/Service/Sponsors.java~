package Models.Service;

public enum Sponsors {
    DECORATION("decoration"),
    LUMIERE("lumiere"),
    SONO("sono"),
    TRAITEUR("traiteur"),
    FLEURISTE("fleuriste"),
    AUTRE("autre");

    private final String label;

    Sponsors(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Sponsors fromLabel(String label) {
        for (Sponsors type : Sponsors.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de sponsor invalide : " + label);
    }

    @Override
    public String toString() {
        return "Sponsors{" +
                "label='" + label + '\'' +
                '}';
    }
}

