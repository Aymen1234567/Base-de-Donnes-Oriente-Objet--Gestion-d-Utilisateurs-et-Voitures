import java.io.Serializable;

public class Voiture implements Serializable {
    private String nom;
    private String modele;
    private int annee;

    public Voiture(String nom, String modele, int annee) {
        this.nom = nom;
        this.modele = modele;
        this.annee = annee;
    }

    public String obtenirNom() {
        return nom;
    }

    public String obtenirModele() {
        return modele;
    }

    public int obtenirAnnee() {
        return annee;
    }

    @Override
    public String toString() {
        return nom + " " + modele + " " + annee;
    }
}