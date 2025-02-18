package Models.Service;

public enum Location {
    HOTEL("hotel"),
    MAISON_D_HOTE("maison_d_hote"),
    ESPACE_VERT("espace_vert"),
    SALLE_DE_FETE("salle_de_fete"),
    AUTRE("autre");

    private final String label;

    Location(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Location fromLabel(String label) {
        for (Location type : Location.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de location invalide : " + label);
    }


}
