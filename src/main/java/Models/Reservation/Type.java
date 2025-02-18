package Models.Reservation;

public enum Type {
    PACKAGE("Package"),
    SERVICE("Service");
    private final String label;

    Type(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public static Type fromLabel(String label) {
        for (Type type : Type.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de sponsor invalide : " + label);
    }
}
