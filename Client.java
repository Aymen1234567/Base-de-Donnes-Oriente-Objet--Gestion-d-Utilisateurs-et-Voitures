import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String ADRESSE_SERVEUR = "localhost";
    private static final int PORT_SERVEUR = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(ADRESSE_SERVEUR, PORT_SERVEUR);
                ObjectOutputStream sortie = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entree = new ObjectInputStream(socket.getInputStream());
                Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connexion au serveur...");
            int idUtilisateur = -1;
            String nomUtilisateur = "";
            boolean authentifie = false;

            while (!authentifie) {
                System.out.println("1. S'inscrire");
                System.out.println("2. Se connecter");
                System.out.print("Choisissez une option : ");
                int choix;
                try {
                    choix = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : Veuillez entrer un nombre valide.");
                    scanner.nextLine();
                    continue;
                }

                if (choix == 1) {
                    do {
                        System.out.print("Nom d'utilisateur : ");
                        nomUtilisateur = scanner.nextLine();
                        if (nomUtilisateur.trim().isEmpty()) {
                            System.out.println("Erreur : Le nom d'utilisateur ne peut pas être vide.");
                        }
                    } while (nomUtilisateur.trim().isEmpty());
                
                    String motDePasse;
                    do {
                        System.out.print("Mot de passe : ");
                        motDePasse = scanner.nextLine();
                        if (motDePasse.trim().isEmpty()) {
                            System.out.println("Erreur : Le mot de passe ne peut pas être vide.");
                        }
                    } while (motDePasse.trim().isEmpty());
                
                    sortie.writeObject("SIGN_UP");
                    sortie.writeObject(nomUtilisateur);
                    sortie.writeObject(motDePasse);
                    sortie.flush();
                
                    String reponse = (String) entree.readObject();
                    System.out.println(reponse);
                
                    
                    if (reponse.startsWith("Succès")) {
                        String[] parties = reponse.split(" ");
                        idUtilisateur = Integer.parseInt(parties[6]); 
                        authentifie = true;
                    } else {
                        System.out.println("Échec de l'inscription. Veuillez réessayer avec un nom d'utilisateur différent.");
                    }
                } else if (choix == 2) {
                    do {
                        System.out.print("ID utilisateur : ");
                        try {
                            idUtilisateur = scanner.nextInt();
                            if (idUtilisateur < 0) {
                                System.out.println("Erreur : L'ID utilisateur doit être positif.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Erreur : Veuillez entrer un nombre valide.");
                            scanner.nextLine();
                            idUtilisateur = -1;
                        }
                    } while (idUtilisateur < 0);
                    scanner.nextLine();

                    String motDePasse;
                    do {
                        System.out.print("Mot de passe : ");
                        motDePasse = scanner.nextLine();
                        if (motDePasse.trim().isEmpty()) {
                            System.out.println("Erreur : Le mot de passe ne peut pas être vide.");
                        }
                    } while (motDePasse.trim().isEmpty());

                    sortie.writeObject("SIGN_IN");
                    sortie.writeObject(idUtilisateur);
                    sortie.writeObject(motDePasse);
                    sortie.flush();
                    String reponse = (String) entree.readObject();
                    System.out.println(reponse);
                    authentifie = reponse.startsWith("Succès");
                } else {
                    System.out.println("Option invalide. Choisissez 1 ou 2.");
                }
            }

            while (true) {
                System.out.println("1. Ajouter une voiture");
                System.out.println("2. Lister mes voitures");
                System.out.println("3. Modifier une voiture");
                System.out.println("4. Supprimer une voiture");
                System.out.println("5. Quitter");
                System.out.print("Choisissez une option : ");
                int choix;
                try {
                    choix = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Erreur : Veuillez entrer un nombre valide.");
                    scanner.nextLine();
                    continue;
                }

                switch (choix) {
                    case 1: // Ajouter une voiture
                        String nomVoiture;
                        do {
                            System.out.print("Nom de la voiture : ");
                            nomVoiture = scanner.nextLine();
                            if (nomVoiture.trim().isEmpty()) {
                                System.out.println("Erreur : Le nom de la voiture ne peut pas être vide.");
                            }
                        } while (nomVoiture.trim().isEmpty());

                        String modele;
                        do {
                            System.out.print("Modèle : ");
                            modele = scanner.nextLine();
                            if (modele.trim().isEmpty()) {
                                System.out.println("Erreur : Le modèle ne peut pas être vide.");
                            }
                        } while (modele.trim().isEmpty());

                        int annee;
                        do {
                            System.out.print("Année : ");
                            try {
                                annee = scanner.nextInt();
                                if (annee < 1900 || annee > 2025) {
                                    System.out.println("Erreur : L'année doit être entre 1900 et 2025.");
                                    annee = -1;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : Veuillez entrer un nombre valide pour l'année.");
                                annee = -1;
                            }
                            scanner.nextLine();
                        } while (annee < 0);

                        sortie.writeObject("ADD_CAR");
                        sortie.writeObject(idUtilisateur);
                        sortie.writeObject(nomVoiture);
                        sortie.writeObject(modele);
                        sortie.writeObject(annee);
                        sortie.flush();
                        System.out.println((String) entree.readObject());
                        break;

                    case 2: // Lister les voitures
                        sortie.writeObject("LIST_CARS");
                        sortie.writeObject(idUtilisateur);
                        sortie.flush();
                        String listeVoitures = (String) entree.readObject();
                        System.out.println("Vos voitures : \n" + listeVoitures);
                        break;

                    case 3: // Modifier une voiture
                        int indiceModification;
                        do {
                            System.out.print("Indice de la voiture à modifier (basé sur 0) : ");
                            try {
                                indiceModification = scanner.nextInt();
                                if (indiceModification < 0) {
                                    System.out.println("Erreur : L'indice doit être positif ou zéro.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : Veuillez entrer un nombre valide.");
                                indiceModification = -1;
                            }
                            scanner.nextLine();
                        } while (indiceModification < 0);

                        String nouveauNomVoiture;
                        do {
                            System.out.print("Nouveau nom de la voiture : ");
                            nouveauNomVoiture = scanner.nextLine();
                            if (nouveauNomVoiture.trim().isEmpty()) {
                                System.out.println("Erreur : Le nom de la voiture ne peut pas être vide.");
                            }
                        } while (nouveauNomVoiture.trim().isEmpty());

                        String nouveauModele;
                        do {
                            System.out.print("Nouveau modèle : ");
                            nouveauModele = scanner.nextLine();
                            if (nouveauModele.trim().isEmpty()) {
                                System.out.println("Erreur : Le modèle ne peut pas être vide.");
                            }
                        } while (nouveauModele.trim().isEmpty());

                        int nouvelleAnnee;
                        do {
                            System.out.print("Nouvelle année : ");
                            try {
                                nouvelleAnnee = scanner.nextInt();
                                if (nouvelleAnnee < 1900 || nouvelleAnnee > 2025) {
                                    System.out.println("Erreur : L'année doit être entre 1900 et 2025.");
                                    nouvelleAnnee = -1;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : Veuillez entrer un nombre valide pour l'année.");
                                nouvelleAnnee = -1;
                            }
                            scanner.nextLine();
                        } while (nouvelleAnnee < 0);

                        sortie.writeObject("MODIFY_CAR");
                        sortie.writeObject(idUtilisateur);
                        sortie.writeObject(indiceModification);
                        sortie.writeObject(nouveauNomVoiture);
                        sortie.writeObject(nouveauModele);
                        sortie.writeObject(nouvelleAnnee);
                        sortie.flush();
                        System.out.println((String) entree.readObject());
                        break;

                    case 4: // Supprimer une voiture
                        int indiceSuppression;
                        do {
                            System.out.print("Indice de la voiture à supprimer (basé sur 0) : ");
                            try {
                                indiceSuppression = scanner.nextInt();
                                if (indiceSuppression < 0) {
                                    System.out.println("Erreur : L'indice doit être positif ou zéro.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Erreur : Veuillez entrer un nombre valide.");
                                indiceSuppression = -1;
                            }
                            scanner.nextLine();
                        } while (indiceSuppression < 0);

                        sortie.writeObject("DELETE_CAR");
                        sortie.writeObject(idUtilisateur);
                        sortie.writeObject(indiceSuppression);
                        sortie.flush();
                        System.out.println((String) entree.readObject());
                        break;

                    case 5:
                        System.out.println("Déconnexion...");
                        return;

                    default:
                        System.out.println("Option invalide.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}