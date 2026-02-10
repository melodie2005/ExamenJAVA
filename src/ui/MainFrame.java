package ui;

import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale affichée après connexion.
 * Elle adapte les boutons selon le rôle (ADMIN ou EMPLOYEE).
 */
public class MainFrame extends JFrame {

    private final User connectedUser;

    public MainFrame(User user) {
        this.connectedUser = user;

        setTitle("iStore - Portail Principal");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ======= Bandeau supérieur =======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 47, 61));
        topPanel.setPreferredSize(new Dimension(900, 80));

        JLabel lblTitle = new JLabel("Bienvenue dans iStore");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel lblRole = new JLabel("Connecté en tant que : " + connectedUser.getRole());
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(33, 47, 61));
        rightPanel.add(lblRole);
        rightPanel.add(btnLogout);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ======= Zone centrale =======
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        // Boutons principaux
        JButton btnUsers = new JButton("Manage Users");
        JButton btnStores = new JButton("Manage Stores");
        JButton btnInventory = new JButton("Manage Inventory");
        JButton btnWhitelist = new JButton("Whitelist Emails");

        // Actions réelles
        btnUsers.addActionListener(e -> openUserManagement());
        btnStores.addActionListener(e -> openStoreManagement());
        btnInventory.addActionListener(e -> openInventory());
        btnWhitelist.addActionListener(e -> openWhitelist());

        // Affichage selon rôle
        if ("ADMIN".equalsIgnoreCase(connectedUser.getRole())) {
            centerPanel.add(btnUsers);
            centerPanel.add(btnStores);
            centerPanel.add(btnInventory);
            centerPanel.add(btnWhitelist);
        } else {
            // EMPLOYEE : accès limité
            centerPanel.add(btnInventory);
            centerPanel.add(new JLabel()); // espace vide
            centerPanel.add(new JLabel());
            centerPanel.add(new JLabel());
        }

        add(centerPanel, BorderLayout.CENTER);
    }

    // ======= Méthodes d'ouverture =======

    private void openUserManagement() {
        new UserManagementPanel(connectedUser);
    }

    private void openStoreManagement() {
        new StoreManagementPanel(connectedUser);
    }

    private void openInventory() {
        new InventoryPanel(connectedUser);
    }

    private void openWhitelist() {
        JOptionPane.showMessageDialog(this,
                "Fonction whitelist à implémenter dans AdminPanel.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
