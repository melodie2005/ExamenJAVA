package ui;

import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'application iStore.
 *
 * Cette fenêtre est affichée après une connexion réussie.
 * Elle reçoit l'utilisateur connecté depuis LoginFrame
 * et adapte l'interface en fonction de son rôle :
 *  - ADMIN : accès à toutes les fonctionnalités
 *  - EMPLOYEE : accès limité aux fonctions autorisées
 *
 * Cette classe gère UNIQUEMENT l'interface graphique,
 * aucune logique métier ou accès base de données ici.
 */
public class MainFrame extends JFrame {

    /**
     * Utilisateur actuellement connecté.
     * Cet objet est fourni par LoginFrame après authentification.
     */
    private final User connectedUser;

    /**
     * Constructeur de la fenêtre principale.
     *
     * @param user utilisateur authentifié
     */
    public MainFrame(User user) {
        this.connectedUser = user;

        setTitle("iStore - Accueil");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        /*
         * BorderLayout est utilisé car il est bien adapté
         * à une fenêtre principale :
         *  - NORTH : informations générales
         *  - CENTER : contenu principal
         *  - SOUTH : actions globales (déconnexion)
         */
        setLayout(new BorderLayout());


        // Bandeau supérieur

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setPreferredSize(new Dimension(800, 60));

        /*
         * Le bandeau affiche le pseudo et le rôle
         * de l'utilisateur connecté.
         */
        JLabel lblWelcome = new JLabel(
                "Connecté : " + connectedUser.getPseudo()
                        + " | Rôle : " + connectedUser.getRole()
        );
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 15));

        topPanel.add(lblWelcome);
        add(topPanel, BorderLayout.NORTH);


        // Zone centrale c'est a dire (contenu)

        JPanel centerPanel = new JPanel(new BorderLayout());

        /*
         * Selon le rôle, on affiche un panneau différent.
         * Cette logique respecte le sujet :
         * - ADMIN : qui a un accès complet
         * - EMPLOYEE : qui a un accès restreint
         */
        if ("ADMIN".equalsIgnoreCase(connectedUser.getRole())) {
            centerPanel.add(createAdminPanel(), BorderLayout.CENTER);
        } else {
            centerPanel.add(createEmployeePanel(), BorderLayout.CENTER);
        }

        add(centerPanel, BorderLayout.CENTER);


        // Bandeau inférieur (déconnexion)

        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> logout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(btnLogout);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Panneau affiché pour un administrateur.
     *
     * L'administrateur peut :
     *  - gérer les magasins
     *  - gérer les utilisateurs
     *  - gérer l'inventaire
     *  - gérer la whitelist des emails
     */
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton btnStores = new JButton("Gestion des magasins");
        JButton btnUsers = new JButton("Gestion des utilisateurs");
        JButton btnInventory = new JButton("Gestion de l'inventaire");
        JButton btnWhitelist = new JButton("Whitelist des emails");

        panel.add(btnStores);
        panel.add(btnUsers);
        panel.add(btnInventory);
        panel.add(btnWhitelist);


        return panel;
    }

    /**
     * Panneau affiché pour un employé.
     *
     * L'employé a accès uniquement :
     *  - aux magasins auxquels il est affecté
     *  - à l'inventaire de ces magasins
     */
    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 25, 25));
        panel.setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));

        JButton btnMyStores = new JButton("Mes magasins");
        JButton btnInventory = new JButton("Inventaire");

        panel.add(btnMyStores);
        panel.add(btnInventory);

        return panel;
    }

    /**
     * Déconnexion de l'utilisateur.
     *
     * On ferme la fenêtre actuelle
     * et on retourne à la fenêtre de connexion.
     */
    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
