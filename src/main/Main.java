package main;

import dao.UserDAO;
import dao.StoreDAO;
import dao.InventoryDAO;
import models.User;
import models.Store;
import models.Item;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Initialisation des objets de base
        UserDAO userDAO = new UserDAO();
        StoreDAO storeDAO = new StoreDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();
        Scanner sc = new Scanner(System.in);
        User currentUser = null;

        // 2. Boucle principale
        while (true) {
            if (currentUser == null) {
                // MENU CONNEXION
                System.out.println("\n--- ACCUEIL ---");
                System.out.println("1. Se connecter\n0. Quitter");
                System.out.print("Choix : ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    System.out.print("Email : "); String em = sc.nextLine();
                    System.out.print("Pass : "); String pw = sc.nextLine();
                    try {
                        currentUser = userDAO.login(em, pw);
                        if (currentUser != null) System.out.println("Bienvenue " + currentUser.getPseudo());
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                } else if (choice == 0) {
                    break;
                }
            } else {
                // MENU ACTIONS (Une fois connecté)
                System.out.println("\n--- MENU ---");
                System.out.println("1. Voir les magasins\n2. Voir les stocks\n0. Déconnexion");
                System.out.print("Choix : ");
                int action = sc.nextInt();
                sc.nextLine();

                try {
                    if (action == 1) {
                        // Utilisation du nom complet pour éviter les erreurs de List
                        java.util.List<models.Store> stores = storeDAO.getAllStores();
                        System.out.println("\n--- MAGASINS ---");
                        for (models.Store s : stores) {
                            System.out.println("ID: " + s.getId() + " | Nom: " + s.getName());
                        }
                    }
                    else if (action == 2) {
                        System.out.print("ID du magasin : ");
                        int storeId = sc.nextInt();
                        sc.nextLine();

                        // Récupération de l'inventaire
                        java.util.List<models.Item> items = inventoryDAO.getItems(storeId);
                        System.out.println("\n--- STOCKS ---");
                        for (models.Item i : items) {
                            // On affiche l'objet 'i' directement, ce qui appellera ton toString()
                            System.out.println(i);
                        }
                    }
                    else if (action == 0) {
                        currentUser = null;
                        System.out.println("👋 Déconnecté.");
                    }
                } catch (Exception e) {
                    System.out.println(" Erreur SQL : " + e.getMessage());
                }
            }
        }
        sc.close();
    }
}