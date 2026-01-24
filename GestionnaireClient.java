import java.io.*;
import java.net.*;

public class GestionnaireClient extends Thread {
    private Socket socket;
    private GestionnaireUtilisateursEtVoitures gestionnaireUtilisateursEtVoitures;

    public GestionnaireClient(Socket socket, GestionnaireUtilisateursEtVoitures gestionnaireUtilisateursEtVoitures) {
        this.socket = socket;
        this.gestionnaireUtilisateursEtVoitures = gestionnaireUtilisateursEtVoitures;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream sortie = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entree = new ObjectInputStream(socket.getInputStream())) {
            while (true) {
                String commande = (String) entree.readObject(); // dans client : sortie.writeObject("ADD_CAR");

                if ("SIGN_UP".equals(commande)) {
                    String nomUtilisateur = (String) entree.readObject();
                    String motDePasse = (String) entree.readObject();
                    String resultat = gestionnaireUtilisateursEtVoitures.ajouterUtilisateur(nomUtilisateur, motDePasse);
                    sortie.writeObject(resultat);
                } else if ("SIGN_IN".equals(commande)) {
                    int idUtilisateur = (int) entree.readObject();
                    String motDePasse = (String) entree.readObject();
                    boolean estAuthentifie = gestionnaireUtilisateursEtVoitures.authentifierUtilisateur(idUtilisateur,
                            motDePasse);
                    sortie.writeObject(estAuthentifie ? "Succès : Connecté." : "Erreur : Identifiants invalides.");
                } else if ("ADD_CAR".equals(commande)) { // dans client on a par ordre :
                    int idUtilisateur = (int) entree.readObject(); // sortie.writeObject(idUtilisateur);
                    String nomVoiture = (String) entree.readObject(); // sortie.writeObject(nomVoiture);
                    String modele = (String) entree.readObject(); // sortie.writeObject(modele);
                    int annee = (int) entree.readObject(); // sortie.writeObject(annee);
                    String resultat = gestionnaireUtilisateursEtVoitures.ajouterVoiture(idUtilisateur, nomVoiture,
                            modele, annee);
                    sortie.writeObject(resultat);
                } else if ("LIST_CARS".equals(commande)) {
                    int idUtilisateur = (int) entree.readObject();
                    String listeVoitures = gestionnaireUtilisateursEtVoitures.listerVoitures(idUtilisateur);
                    sortie.writeObject(listeVoitures);
                } else if ("MODIFY_CAR".equals(commande)) {
                    int idUtilisateur = (int) entree.readObject();
                    int indiceVoiture = (int) entree.readObject();
                    String nomVoiture = (String) entree.readObject();
                    String modele = (String) entree.readObject();
                    int annee = (int) entree.readObject();
                    String resultat = gestionnaireUtilisateursEtVoitures.modifierVoiture(idUtilisateur, indiceVoiture,
                            nomVoiture,
                            modele, annee);
                    sortie.writeObject(resultat);
                } else if ("DELETE_CAR".equals(commande)) {
                    int idUtilisateur = (int) entree.readObject();
                    int indiceVoiture = (int) entree.readObject();
                    String resultat = gestionnaireUtilisateursEtVoitures.supprimerVoiture(idUtilisateur, indiceVoiture);
                    sortie.writeObject(resultat);
                } else {
                    sortie.writeObject("Commande inconnue.");
                }
                sortie.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
          
        }
    }
}