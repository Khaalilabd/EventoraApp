package Models.Service;

public enum TypeService {
    DECORATION("decoration"),
    LUMIERE("lumiere"),
    SONO("sono"),
    TRAITEUR("traiteur"),
    FLEURISTE("fleuriste"),
    AUTRE("autre");

    private final String label;

    TypeService(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TypeService fromLabel(String label) {
        for (TypeService type : TypeService.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de sponsor invalide : " + label);
    }


}

