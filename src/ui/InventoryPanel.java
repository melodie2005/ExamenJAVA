package ui;

import dao.InventoryDAO;
import models.Item;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JFrame {

    private JTable itemTable;
    private DefaultTableModel tableModel;
    private User connectedUser;

    // DAO ajouté
    private InventoryDAO inventoryDAO = new InventoryDAO();

    // ⚠️ Pour l'instant on simule storeId = 1
    private int storeId = 1;

    public InventoryPanel(User user) {

        this.connectedUser = user;

        setTitle("iStore - Gestion de l'Inventaire");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Bandeau haut =====
        JLabel lblTitle = new JLabel("Gestion de l'Inventaire", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tableau =====
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Prix (€)", "Quantité"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(itemTable);
        add(scrollPane, BorderLayout.CENTER);

        // ⚠️ On charge depuis la base
        loadItemsFromDatabase();

        // ===== Boutons =====
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Ajouter Article");
        JButton btnIncrease = new JButton("+");
        JButton btnDecrease = new JButton("-");
        JButton btnDelete = new JButton("Supprimer");

        bottomPanel.add(btnIncrease);
        bottomPanel.add(btnDecrease);

        if (connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            bottomPanel.add(btnAdd);
            bottomPanel.add(btnDelete);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        btnAdd.addActionListener(e -> addItem());
        btnIncrease.addActionListener(e -> increaseQuantity());
        btnDecrease.addActionListener(e -> decreaseQuantity());
        btnDelete.addActionListener(e -> deleteItem());

        setVisible(true);
    }

    // ===============================
    // Chargement depuis la base
    // ===============================
    private void loadItemsFromDatabase() {
        try {
            tableModel.setRowCount(0); // vide le tableau

            List<Item> items = inventoryDAO.getItems(storeId);

            for (Item item : items) {
                tableModel.addRow(new Object[]{
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur chargement inventaire : " + e.getMessage());
        }
    }

    private void addItem() {

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                    "Seul un administrateur peut ajouter un article.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Nom de l'article :");

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom invalide.");
            return;
        }

        String priceStr = JOptionPane.showInputDialog(this, "Prix :");

        try {
            double price = Double.parseDouble(priceStr);

            inventoryDAO.addItem(name.trim(), price, 0, storeId);

            loadItemsFromDatabase(); // refresh

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void increaseQuantity() {

        int row = itemTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un article.");
            return;
        }

        try {
            int itemId = (int) tableModel.getValueAt(row, 0);
            int qty = (int) tableModel.getValueAt(row, 3);

            inventoryDAO.updateQuantity(itemId, qty + 1);

            loadItemsFromDatabase();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void decreaseQuantity() {

        int row = itemTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un article.");
            return;
        }

        try {
            int itemId = (int) tableModel.getValueAt(row, 0);
            int qty = (int) tableModel.getValueAt(row, 3);

            if (qty == 0) {
                JOptionPane.showMessageDialog(this, "Stock insuffisant.");
                return;
            }

            inventoryDAO.updateQuantity(itemId, qty - 1);

            loadItemsFromDatabase();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void deleteItem() {

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                    "Seul un administrateur peut supprimer un article.");
            return;
        }

        int row = itemTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un article.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer cet article ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int itemId = (int) tableModel.getValueAt(row, 0);
                inventoryDAO.deleteItem(itemId);

                loadItemsFromDatabase();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }
}
