package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau d'administration.
 *
 * Ce JPanel représente l'espace réservé à un administrateur.
 * Il regroupe toutes les fonctionnalités accessibles uniquement
 * par un ADMIN selon le sujet du projet.
 *
 * Ce panneau est destiné à être affiché dans le MainFrame.
 */
public class AdminPanel extends JPanel {

    public AdminPanel() {

        /*
         * On utilise BorderLayout pour structurer le panneau :
         * - NORTH : titre
         * - CENTER : boutons d'actions
         */
        setLayout(new BorderLayout());


        // Titre du panneau

        JLabel lblTitle = new JLabel("Espace Administrateur", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(lblTitle, BorderLayout.NORTH);


        // Zone centrale : actions admin

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(2, 2, 25, 25));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        /*
         * Chaque bouton à une fonctionnalité

         */
        JButton btnManageUsers = new JButton("Gestion des utilisateurs");
        JButton btnWhitelist = new JButton("Whitelist des emails");
        JButton btnManageStores = new JButton("Gestion des magasins");
        JButton btnManageInventory = new JButton("Gestion de l'inventaire");

        actionPanel.add(btnManageUsers);
        actionPanel.add(btnWhitelist);
        actionPanel.add(btnManageStores);
        actionPanel.add(btnManageInventory);

        add(actionPanel, BorderLayout.CENTER);


    }
}
