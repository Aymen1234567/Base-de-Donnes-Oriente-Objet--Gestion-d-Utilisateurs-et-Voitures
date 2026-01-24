Base de Données Orientée Objet - Gestion d'Utilisateurs et Voitures
Description
Ce projet est une application client-serveur multi-utilisateurs développée en Java, permettant la gestion d'utilisateurs et de leurs voitures. Elle assure la persistance des données via des fichiers texte (utilisateurs.txt et voitures.txt) et gère la concurrence entre plusieurs clients connectés simultanément. Le serveur centralise les données et communique avec les clients via des sockets TCP sur le port 12345.
L'objectif principal est de démontrer les principes d'une base de données orientée objet, avec inscription/connexion des utilisateurs, ajout/liste/modification/suppression de voitures, et synchronisation des accès pour éviter les corruptions de données.
Projet réalisé dans le cadre du cours IE-I4 (Année 2024-2025).

Fonctionnalités

Inscription : Création d'un utilisateur avec nom et mot de passe ; génération d'un ID unique.
Connexion : Authentification via ID et mot de passe.
Gestion des voitures :
Ajout d'une voiture (nom, modèle, année).
Liste des voitures d'un utilisateur (avec indices).
Modification d'une voiture par indice.
Suppression d'une voiture par indice.

Persistance : Données stockées dans des fichiers texte, chargées au démarrage et mises à jour après chaque opération.
Concurrence : Utilisation de verrous (synchronized) pour gérer les accès simultanés et éviter les connexions multiples sur le même compte.
Interface en ligne de commande pour le client.

Architecture
L'application suit une architecture client-serveur :

Serveur : Écoute les connexions, crée un thread par client (GestionnaireClient), gère les données via GestionnaireUtilisateursEtVoitures.
Client : Interface CLI pour envoyer des requêtes (ex. : SIGN_UP, ADD_CAR) et afficher les réponses.
Structures en mémoire :
Utilisateurs : Map<Integer, Utilisateur> (clé : ID).
Voitures : Map<Integer, List<Voiture>> (clé : ID utilisateur).

Structures sur disque :
utilisateurs.txt : Format id,nomUtilisateur,motDePasse (ex. : 1,aymen,123).
voitures.txt : Format id,voiture1;voiture2 où chaque voiture est nom modele annee (ex. : 1,mercedes G-class 2025;Bmw ix3 2024).

Protocole : Échange de chaînes et objets sérialisés via sockets.

Classes Principales

Utilisateur : ID, nomUtilisateur, motDePasse. Implémente Serializable.
Voiture : nom, modele, annee. Implémente Serializable.
GestionnaireUtilisateursEtVoitures : Gère les maps, persistance (charger/sauvegarder), et opérations synchronisées.
Fonctions : Interface pour appeler les méthodes du gestionnaire.
Client : Interface CLI avec menu interactif.
GestionnaireClient : Thread par client, traite les commandes (SIGN_UP, SIGN_IN, ADD_CAR, etc.).
Serveur : Lance le serveur et accepte les connexions.

Installation et Configuration

Assurez-vous d'avoir Java installé (JDK 8+ recommandé).
Clonez le dépôt (si disponible) ou copiez les sources.
Compilez les classes : javac *.java.
Lancez le serveur : java Serveur.
Lancez un ou plusieurs clients : java Client.

Utilisation
Exemple de Scénario

Démarrez le serveur : Serveur démarré sur le port 12345.
Lancez le client et inscrivez-vous :
Choix : 1 (S'inscrire).
Nom : "alice", Mot de passe : "pass123".
Réponse : "Utilisateur ajouté avec l'ID 1".

Connectez-vous :
Choix : 2 (Se connecter).
ID : 1, Mot de passe : "pass123".
Menu gestion voitures apparaît.

Ajoutez une voiture :
Choix : 1, Nom : "Toyota", Modèle : "Corolla", Année : 2020.
Réponse : "Voiture ajoutée avec succès".

Listez les voitures : Choix 2 → Affiche la liste avec indices.
Modifiez/Supprimez : Utilisez les indices pour cibler une voiture.
Déconnectez-vous : Choix 5.

Les données sont persistées dans les fichiers après chaque opération.
Fichiers de données (utilisateurs.txt, voitures.txt) seront créés automatiquement si absents.

Dépendances : Aucune bibliothèque externe ; utilise uniquement les API Java standard (sockets, I/O, collections).
