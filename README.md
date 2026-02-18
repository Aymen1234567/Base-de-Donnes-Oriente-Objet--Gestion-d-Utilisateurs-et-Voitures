# ⚡ Base de Données Orientée Objet – Gestion d’Utilisateurs et Voitures

Projet de **gestion client-serveur multi-utilisateurs** développé en **Java**, démontrant les principes d’une **base de données orientée objet** avec persistance et gestion de la concurrence.

📚 Réalisé dans le cadre du cours **IE-I4**  
🎓 Année universitaire 2024–2025  

---

## 🧩 Présentation du projet

Cette application permet de gérer des **utilisateurs et leurs voitures** via une interface client-serveur, avec :

- Inscription et authentification des utilisateurs  
- Gestion complète des voitures : ajout, liste, modification, suppression  
- Persistance des données dans des fichiers texte (`utilisateurs.txt` et `voitures.txt`)  
- Gestion de la concurrence pour plusieurs clients connectés simultanément  
- Interface en ligne de commande (CLI) intuitive  

Le serveur centralise les données et communique avec les clients via **sockets TCP** sur le port `12345`.

---

## 🎮 Fonctionnalités principales

### 👤 Gestion des utilisateurs
- Inscription avec génération d’un **ID unique**  
- Connexion sécurisée via **ID et mot de passe**  
- Accès synchronisé pour éviter les connexions multiples sur un même compte  

### 🚗 Gestion des voitures
- Ajout d’une voiture (**nom, modèle, année**)  
- Liste des voitures d’un utilisateur avec indices  
- Modification d’une voiture existante via son indice  
- Suppression d’une voiture via son indice  

### 💾 Persistance
- Données stockées dans des fichiers texte  
- Chargement au démarrage et sauvegarde automatique après chaque opération  

### 🔄 Concurrence
- Accès aux données **synchronisés** avec `synchronized`  
- Support multi-utilisateurs sans corruption des données  

---

## 🗂️ Architecture

**Client-serveur** avec threads pour gérer la concurrence :

- **Serveur** :  
  - Écoute les connexions entrantes  
  - Crée un **thread par client** (`GestionnaireClient`)  
  - Gère les données via `GestionnaireUtilisateursEtVoitures`  

- **Client** :  
  - Interface CLI interactive  
  - Envoie les commandes (ex. : `SIGN_UP`, `ADD_CAR`)  
  - Affiche les réponses du serveur  

**Structures de données :**  
- **En mémoire** :  
  - `Map<Integer, Utilisateur>` pour les utilisateurs  
  - `Map<Integer, List<Voiture>>` pour les voitures par utilisateur  

- **Sur disque** :  
  - `utilisateurs.txt` : `id,nomUtilisateur,motDePasse`  
  - `voitures.txt` : `id,voiture1;voiture2` avec chaque voiture `nom modele annee`  

- **Protocole** : Échange de chaînes et objets sérialisés via **sockets TCP**  

---

## 🛠️ Classes principales

| Classe | Description |
|--------|-------------|
| `Utilisateur` | ID, nomUtilisateur, motDePasse. Implémente `Serializable`. |
| `Voiture` | Nom, modèle, année. Implémente `Serializable`. |
| `GestionnaireUtilisateursEtVoitures` | Gère les maps, persistance et opérations synchronisées. |
| `Fonctions` | Interface pour appeler les méthodes du gestionnaire. |
| `Client` | Interface CLI pour les utilisateurs. |
| `GestionnaireClient` | Thread par client, traite les commandes. |
| `Serveur` | Lance le serveur et accepte les connexions. |

---

## 🧭 Installation et utilisation

### 💻 Prérequis

- **Java JDK 8+**  
- Aucun framework externe nécessaire  

### 🔹 Compilation

```bash
javac *.java
🔹 Lancer le serveur
java Serveur

🔹 Lancer un ou plusieurs clients
java Client

🔹 Exemple de scénario

Démarrage du serveur : Serveur actif sur le port 12345

Inscription client :

Choix : 1 (S’inscrire)

Nom : alice, Mot de passe : pass123

Réponse : Utilisateur ajouté avec l’ID 1

Connexion client :

Choix : 2 (Se connecter)

ID : 1, Mot de passe : pass123

Menu gestion voitures apparaît

Ajout d’une voiture :

Choix : 1, Nom : Toyota, Modèle : Corolla, Année : 2020

Réponse : Voiture ajoutée avec succès

Liste / modification / suppression : Utilisez les indices pour gérer vos voitures

Déconnexion : Choix 5

Les fichiers utilisateurs.txt et voitures.txt sont mis à jour automatiquement après chaque opération.

⚙️ Dépendances

Java standard API : Sockets, I/O, Collections

Aucune bibliothèque externe
