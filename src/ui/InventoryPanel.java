package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panneau de gestion de l'inventaire.
 *
 * Ce panneau permet d'afficher les items d'un inventaire
 * et de modifier leur quantité (vente / réception).
 *
 * Il est accessible :
 *  - par l'ADMIN
 *  - par les EMPLOYEES ayant accès au store
 *
 * Aucune logique de base de données ici :
 * ce panneau gère uniquement l'interface graphique.
 */
public class InventoryPanel extends JPanel {

    private JTable itemTable;
    private DefaultTableModel tableModel;

    public InventoryPanel() {

        /*
         * BorderLayout permet une structure claire :
         * - NORTH : titre
         * - CENTER : tableau des items
         * - SOUTH : boutons d'action
         */
        setLayout(new BorderLayout());


        // Titre

        JLabel lblTitle = new JLabel("Inventaire du magasin", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);


        // Tableau des items


        /*
         * La JTable est utilisée pour afficher les items
         * avec leurs propriétés :
         * id, nom, prix, quantité.
         */
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Prix (€)", "Quantité"},
                0
        ) {
            // Les cellules ne sont pas éditables directement
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        add(scrollPane, BorderLayout.CENTER);


        loadDummyItems();


        // Boutons d'action

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnIncrease = new JButton("+ Ajouter");
        JButton btnDecrease = new JButton("- Retirer");

        actionPanel.add(btnIncrease);
        actionPanel.add(btnDecrease);

        add(actionPanel, BorderLayout.SOUTH);


        // Actions


        btnIncrease.addActionListener(e -> increaseQuantity());
        btnDecrease.addActionListener(e -> decreaseQuantity());
    }

    /**
     * Chargement d'items fictifs.
     * on teste l’interface avec des données de test.
     * Demain, ces données viendront de la base.
     */
    private void loadDummyItems() {
        tableModel.addRow(new Object[]{1, "Clavier", 49.99, 20});
        tableModel.addRow(new Object[]{2, "Souris", 19.99, 35});
        tableModel.addRow(new Object[]{3, "Écran", 199.99, 10});
        tableModel.addRow(new Object[]{4, "PC Portable", 899.99, 5});
    }

    /**
     * Augmentation de la quantité d'un item.
     *
     * Correspond à une réception de stock.
     */
    private void increaseQuantity() {
        int selectedRow = itemTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un item.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int currentQty = (int) tableModel.getValueAt(selectedRow, 3);
        tableModel.setValueAt(currentQty + 1, selectedRow, 3);
    }

    /**
     * Diminution de la quantité d'un item.
     *
     * Correspond à une vente.
     * La quantité ne peut jamais être inférieure à 0.
     */
    private void decreaseQuantity() {
        int selectedRow = itemTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un item.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int currentQty = (int) tableModel.getValueAt(selectedRow, 3);

        if (currentQty == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Stock insuffisant.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        tableModel.setValueAt(currentQty - 1, selectedRow, 3);
    }
}
