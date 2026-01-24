import java.io.Serializable;

public class Utilisateur implements Serializable {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;

    public Utilisateur(int id, String nomUtilisateur, String motDePasse) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    public int obtenirId() {
        return id;
    }

    public String obtenirNomUtilisateur() {
        return nomUtilisateur;
    }

    public String obtenirMotDePasse() {
        return motDePasse;
    }

    @Override
    public String toString() {
        return id + "," + nomUtilisateur + "," + motDePasse;
    }
}