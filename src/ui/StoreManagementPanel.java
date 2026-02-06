package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de gestion des magasins (Stores).
 *
 * Ce panneau est accessible UNIQUEMENT par un administrateur.
 * Il permet :
 *  - d'afficher la liste des magasins existants
 *  - de créer un nouveau magasin
 *  - de supprimer un magasin
 */
public class StoreManagementPanel extends JPanel {

    // Modèle de données pour la liste des magasins
    private DefaultListModel<String> storeListModel;

    // Liste graphique des magasins
    private JList<String> storeList;

    public StoreManagementPanel() {

        /*
         * BorderLayout est utilisé pour structurer le panneau :
         * - NORTH : titre
         * - CENTER : liste des magasins
         * - SOUTH : boutons d'action
         */
        setLayout(new BorderLayout());


        // Titre

        JLabel lblTitle = new JLabel("Gestion des magasins", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(lblTitle, BorderLayout.NORTH);


        // Centre : liste des magasins


        /*
         * DefaultListModel est utilisé car il permet
         * d'ajouter et supprimer dynamiquement des éléments,
         * ce qui correspond bien à une liste de magasins.
         */
        storeListModel = new DefaultListModel<>();

        /*
         * JList affiche les magasins.
         * Pour le moment, on utilise des données fictives,
         * qui seront remplacées plus tard par les données DAO.
         */
        storeList = new JList<>(storeListModel);
        storeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Données d'exemple (pour démonstration UI)
        storeListModel.addElement("Store Paris");
        storeListModel.addElement("Store Lyon");
        storeListModel.addElement("Store Marseille");

        JScrollPane scrollPane = new JScrollPane(storeList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        add(scrollPane, BorderLayout.CENTER);


        // Bas : boutons d'action

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAddStore = new JButton("Créer un magasin");
        JButton btnDeleteStore = new JButton("Supprimer le magasin");

        actionPanel.add(btnAddStore);
        actionPanel.add(btnDeleteStore);

        add(actionPanel, BorderLayout.SOUTH);


        // Actions des boutons


        /*
         * Bouton de création d'un magasin.
         * Une boîte de dialogue est utilisée pour saisir le nom,
         */
        btnAddStore.addActionListener(e -> addStore());

        /*
         * Bouton de suppression d'un magasin sélectionné.
         * Une confirmation est demandée avant suppression,
         */
        btnDeleteStore.addActionListener(e -> deleteStore());
    }

    /**
     * Ajout d'un magasin.
     *
     * Cette méthode gère uniquement l'interface.
     */
    private void addStore() {
        String storeName = JOptionPane.showInputDialog(
                this,
                "Nom du nouveau magasin :",
                "Créer un magasin",
                JOptionPane.PLAIN_MESSAGE
        );

        if (storeName == null || storeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Le nom du magasin ne peut pas être vide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Ajout dans la liste
        storeListModel.addElement(storeName.trim());
    }

    /**
     * Suppression d'un magasin sélectionné.
     *
     * L'utilisateur doit sélectionner un magasin dans la liste.
     */
    private void deleteStore() {
        int selectedIndex = storeList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un magasin à supprimer.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String storeName = storeListModel.getElementAt(selectedIndex);

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer le magasin : " + storeName + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            storeListModel.remove(selectedIndex);
        }
    }
}
