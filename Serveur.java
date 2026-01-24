import java.io.*;
import java.net.*;

public class Serveur {
    private static final int PORT = 12345;
    private static GestionnaireUtilisateursEtVoitures gestionnaireUtilisateursEtVoitures = new GestionnaireUtilisateursEtVoitures();

    public static void main(String[] args) {
        try (ServerSocket socketServeur = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            while (true) {
                Socket socketClient = socketServeur.accept();
                new GestionnaireClient(socketClient, gestionnaireUtilisateursEtVoitures).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}