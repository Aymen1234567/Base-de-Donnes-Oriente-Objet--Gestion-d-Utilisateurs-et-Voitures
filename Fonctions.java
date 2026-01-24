//import java.util.*;

public class Fonctions {
    private GestionnaireUtilisateursEtVoitures gestionnaireUtilisateursEtVoitures;

    public Fonctions(GestionnaireUtilisateursEtVoitures gestionnaireUtilisateursEtVoitures) {
        this.gestionnaireUtilisateursEtVoitures = gestionnaireUtilisateursEtVoitures;
    }

    public String ajouterUtilisateur(String nomUtilisateur, String motDePasse) {
        return gestionnaireUtilisateursEtVoitures.ajouterUtilisateur(nomUtilisateur, motDePasse);
    }

    public boolean authentifierUtilisateur(int idUtilisateur, String motDePasse) {
        return gestionnaireUtilisateursEtVoitures.authentifierUtilisateur(idUtilisateur, motDePasse);
    }

    public String ajouterVoiture(int idUtilisateur, String nomVoiture, String modele, int annee) {
        return gestionnaireUtilisateursEtVoitures.ajouterVoiture(idUtilisateur, nomVoiture, modele, annee);
    }

    public String listerVoitures(int idUtilisateur) {
        return gestionnaireUtilisateursEtVoitures.listerVoitures(idUtilisateur);
    }

    public String modifierVoiture(int idUtilisateur, int indiceVoiture, String nomVoiture, String modele, int annee) {
        return gestionnaireUtilisateursEtVoitures.modifierVoiture(idUtilisateur, indiceVoiture, nomVoiture, modele,
                annee);
    }

    public String supprimerVoiture(int idUtilisateur, int indiceVoiture) {
        return gestionnaireUtilisateursEtVoitures.supprimerVoiture(idUtilisateur, indiceVoiture);
    }
}