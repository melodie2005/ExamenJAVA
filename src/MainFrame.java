package main;

import javax.swing.*;
import java.awt.*;
import models.User;

public class MainFrame extends JFrame {

    public MainFrame(User user) {
        // Configuration de la fenêtre
        setTitle("Gestion de Stock - Session de " + user.getPseudo());
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + user.getPseudo() + " [" + user.getRole() + "]");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel);

        // BOUTON 1 : Toujours visible (Consultation)
        JButton btnViewStock = new JButton("Consulter les Stocks");
        panel.add(btnViewStock);

        // BOUTON 2 : Restriction d'accès (ADMIN uniquement)
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            JButton btnAdminTools = new JButton("ADMIN : Gérer les Utilisateurs");
            btnAdminTools.setBackground(new Color(255, 200, 200)); // Rouge léger pour distinguer
            panel.add(btnAdminTools);
        } else {
            // Pour un simple utilisateur, on affiche un message ou un bouton limité
            panel.add(new JLabel("Accès limité au magasin assigné", SwingConstants.CENTER));
        }

        JButton btnLogout = new JButton("Déconnexion");
        panel.add(btnLogout);

        add(panel);

        // Action de déconnexion
        btnLogout.addActionListener(e -> {
            new main.LoginFrame().setVisible(true);
            this.dispose();
        });
    }
}