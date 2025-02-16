package Models.Service;

public enum TypeSponsors {
    FINANCIER("Financier"),
    TECHNIQUE("Technique"),
    MEDIA("Media"),
    LOGISTIQUE("Logistique"),
    PARTENARIAT("Partenariat"),
    AUTRE("Autre");

    private final String label;

    TypeSponsors(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TypeSponsors fromLabel(String label) {
        for (TypeSponsors type : TypeSponsors.values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de sponsors invalide : " + label);
    }

    @Override
    public String toString() {
        return "type_sponsors{" +
                "label='" + label + '\'' +
                '}';
    }
}
