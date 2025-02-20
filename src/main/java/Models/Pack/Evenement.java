package Models.Pack;

public class Evenement {
    private int id;
    private String typeEvenement;

    public Evenement() {
    }

    public Evenement(int id, String typeEvenement) {
        this.id = id;
        this.typeEvenement = typeEvenement;
    }

    public Evenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    @Override
    public String toString() {
        return typeEvenement;
    }
}
