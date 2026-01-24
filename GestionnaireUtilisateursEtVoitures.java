import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GestionnaireUtilisateursEtVoitures { // la classe la plus compliqué
    private static final String FICHIER_UTILISATEURS = "utilisateurs.txt";
    private static final String FICHIER_VOITURES = "voitures.txt";
    private Map<Integer, Utilisateur> utilisateurs = new HashMap<>(); // ID -> Utilisateur
    private Map<Integer, List<Voiture>> voituresUtilisateur = new HashMap<>(); // ID -> Liste d'objets Voiture
    private int prochainId = 1; // Générateur d'ID simple
    private final Object verrouFichierVoitures = new Object(); // Verrou pour les modifications du fichier voitures
    
    private final Object verrouUtilisateurs = new Object(); // Verrou pour les modifications des utilisateurs
    

    public GestionnaireUtilisateursEtVoitures() {
        chargerUtilisateurs();
        chargerVoitures();
    }

    // Les méthodes pour les Utilisateurs
    private void chargerUtilisateurs() {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_UTILISATEURS))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                String[] parties = ligne.split(",");
                if (parties.length == 3) {
                    int id = Integer.parseInt(parties[0]);
                    utilisateurs.put(id, new Utilisateur(id, parties[1], parties[2]));
                    prochainId = Math.max(prochainId, id + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Aucun fichier utilisateur trouvé, création d'un nouveau.");
        }
    }

    private void chargerVoitures() {
        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_VOITURES))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.trim().isEmpty()) {
                    continue;
                }
                String[] parties = ligne.split(",", 2);
                if (parties.length == 2 && !parties[1].trim().isEmpty()) {
                    try {
                        int id = Integer.parseInt(parties[0]);
                        String[] chainesVoitures = parties[1].split(";");
                        List<Voiture> voitures = new ArrayList<>();
                        for (String chaineVoiture : chainesVoitures) {
                            String[] partiesVoiture = chaineVoiture.trim().split(" ", 3);
                            if (partiesVoiture.length == 3) {
                                try {
                                    String nom = partiesVoiture[0];
                                    String modele = partiesVoiture[1];
                                    int annee = Integer.parseInt(partiesVoiture[2]);
                                    voitures.add(new Voiture(nom, modele, annee));
                                } catch (NumberFormatException e) {
                                    // Ignorer une année invalide
                                }
                            }
                        }
                        if (!voitures.isEmpty()) {
                            voituresUtilisateur.put(id, voitures);
                        }
                    } catch (NumberFormatException e) {
                        // Ignorer un ID invalide
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Aucun fichier de voitures trouvé, création d'un nouveau.");
        }
    }

    public String ajouterUtilisateur(String nomUtilisateur, String motDePasse) {
        synchronized (verrouUtilisateurs) {
            for (Utilisateur utilisateur : utilisateurs.values()) {
                if (utilisateur.obtenirNomUtilisateur().equals(nomUtilisateur)) {
                    return "Erreur : L'utilisateur existe déjà";
                }
            }
            int id = prochainId++;
            utilisateurs.put(id, new Utilisateur(id, nomUtilisateur, motDePasse));
            sauvegarderUtilisateurs();
            return "Succès : Utilisateur enregistré avec ID " + id;
        }
    }

    public boolean authentifierUtilisateur(int id, String motDePasse) {
        
        Utilisateur utilisateur = utilisateurs.get(id);
        return utilisateur != null && utilisateur.obtenirMotDePasse().equals(motDePasse);
    }

    private void sauvegarderUtilisateurs() {
        synchronized (verrouUtilisateurs) {
            try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_UTILISATEURS))) {
                for (Utilisateur utilisateur : utilisateurs.values()) {
                    ecrivain.write(utilisateur.toString());
                    ecrivain.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Les méthodes pour les Voitures
    public String ajouterVoiture(int idUtilisateur, String nomVoiture, String modele, int annee) {
        synchronized (verrouFichierVoitures) {
            if (!utilisateurs.containsKey(idUtilisateur))
                return "Erreur : Utilisateur non trouvé.";
            Voiture nouvelleVoiture = new Voiture(nomVoiture, modele, annee);
            voituresUtilisateur.computeIfAbsent(idUtilisateur, k -> new ArrayList<>()).add(nouvelleVoiture);
            sauvegarderVoitures();
            return "Voiture ajoutée.";
        }
    }

    public String listerVoitures(int idUtilisateur) {
        synchronized (verrouFichierVoitures) {
            List<Voiture> voitures = voituresUtilisateur.getOrDefault(idUtilisateur, Collections.emptyList());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < voitures.size(); i++) {
                sb.append(i).append(": ").append(voitures.get(i).toString()).append("\n");
            }
            return sb.toString().isEmpty() ? "Aucune voiture trouvée." : sb.toString();
        }
    }

    public String modifierVoiture(int idUtilisateur, int indiceVoiture, String nomVoiture, String modele, int annee) {
        synchronized (verrouFichierVoitures) {
            if (!utilisateurs.containsKey(idUtilisateur))
                return "Erreur : Utilisateur non trouvé.";
            List<Voiture> voitures = voituresUtilisateur.get(idUtilisateur);
            if (voitures == null || indiceVoiture < 0 || indiceVoiture >= voitures.size()) {
                return "Erreur : Voiture non trouvée.";
            }
            Voiture nouvelleVoiture = new Voiture(nomVoiture, modele, annee);
            voitures.set(indiceVoiture, nouvelleVoiture);
            sauvegarderVoitures();
            return "Voiture modifiée.";
        }
    }

    public String supprimerVoiture(int idUtilisateur, int indiceVoiture) {
        synchronized (verrouFichierVoitures) {
            if (!utilisateurs.containsKey(idUtilisateur))
                return "Erreur : Utilisateur non trouvé.";
            List<Voiture> voitures = voituresUtilisateur.get(idUtilisateur);
            if (voitures == null || indiceVoiture < 0 || indiceVoiture >= voitures.size()) {
                return "Erreur : Voiture non trouvée.";
            }
            voitures.remove(indiceVoiture);
            sauvegarderVoitures();
            return "Voiture supprimée.";
        }
    }

    private void sauvegarderVoitures() {
        synchronized (verrouFichierVoitures) {
            try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_VOITURES))) {
                for (Map.Entry<Integer, List<Voiture>> entree : voituresUtilisateur.entrySet()) {
                    String chaineVoitures = entree.getValue().stream()
                            .map(Voiture::toString)
                            .collect(Collectors.joining(";"));
                    ecrivain.write(entree.getKey() + "," + chaineVoitures);
                    ecrivain.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
