package main;
import dao.*;
import models.*;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args){
        UserDAO userDAO = new UserDAO();
        StoreDAO storeDAO = new StoreDAO();
        InventoryDAO invetoryDAO = new InventoryDAO();
        Scanner sc = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            if (currentUser == null){
                System.out.println("\n--- BIENVENUE ---");
                System.out.println("1. Se connecter\n2. Créer un compte\n0. Quitter");
                int choice = sc.nextInt(); sc.nextLine();

                if (choice == 1) {
                    System.out.print("Email: ");
                    String em = sc.nextLine();
                    System.out.print("Password: ");
                    String pw = sc.nextLine();

                    try {
                        currentUser = userDAO.login(em, pw);
                        System.out.println("Connecté !");
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }

                } else if (choice == 2) {
                    System.out.print("Email: ");
                    String em = sc.nextLine();
                    System.out.print("Pseudo: ");
                    String ps = sc.nextLine();
                    System.out.print("Password: ");
                    String pw = sc.nextLine();

                    try {
                        userDAO.creatUser(em, ps, pw, "USER");
                        System.out.println("Compte créé !");
                    } catch (Exception e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                } else if (choice == 0 ) {
                    System.out.println("Au revoir !");
                    System.exit(0);
                }

                while (currentUser != null) {
                    System.out.println("\n=== MENU PRINCIPAL (" + currentUser.getRole() + ") ===");
                    System.out.println("1.Voir les magasins");
                    System.out.println("2. Voir les stocks d'un magasin");

                    if (currentUser.getRole().equals("ADMIN")){
                        System.out.println("3. [ADMIN] créer un magasin");
                        System.out.println("4. [ADMIN] Whitelister un utilisateur");
                    }

                    System.out.println("0.Se déconnecter");
                    System.out.print("Choix :");
                    int action = sc.nextInt(); sc.nextLine();

                    try {
                        if (action == 1) {
                            List<Store> stores = storeDAO.getAllStores();
                            System.out.println("\n--- Liste des Magasins ---");
                            for (Store s : stores) {
                                System.out.println("- ID: " + s.getId() + "| Nom: " + s.getName())
                            }
                        }
                        else if (action == 2) {
                            System.out.print("Entrez l'ID du magasin :");
                            int storeId = sc.nextInt(); sc.nextLine();
                            List<Item> items = invetoryDAO.getItems(storeId);
                            System.out.println("\n--- Inventaire ---");
                            for (Item i : items) System.out.println(i);
                        }
                    }
                }
            }
        }
    }

}
