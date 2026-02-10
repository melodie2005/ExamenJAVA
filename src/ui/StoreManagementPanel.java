package ui;

import dao.StoreDAO;
import models.Store;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StoreManagementPanel extends JFrame {

    private JTable storeTable;
    private DefaultTableModel tableModel;
    private User connectedUser;

    private StoreDAO storeDAO = new StoreDAO();

    public StoreManagementPanel(User user) {

        this.connectedUser = user;

        setTitle("iStore - Gestion des Magasins");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Titre =====
        JLabel lblTitle = new JLabel("Gestion des Magasins", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tableau =====
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom du magasin", "Nombre d'employés"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        storeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(storeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Chargement réel depuis la base
        loadStoresFromDatabase();

        // ===== Boutons =====
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Ajouter Magasin");
        JButton btnEmployees = new JButton("Voir Employés");
        JButton btnDelete = new JButton("Supprimer");

        if (connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            bottomPanel.add(btnAdd);
            bottomPanel.add(btnEmployees);
            bottomPanel.add(btnDelete);
        } else {
            bottomPanel.add(btnEmployees);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        btnAdd.addActionListener(e -> addStore());
        btnDelete.addActionListener(e -> deleteStore());
        btnEmployees.addActionListener(e -> showEmployees());

        setVisible(true);
    }

    // ===============================
    // Chargement depuis la base
    // ===============================
    private void loadStoresFromDatabase() {

        try {
            tableModel.setRowCount(0);

            List<Store> stores = storeDAO.getAllStores();

            for (Store store : stores) {
                tableModel.addRow(new Object[]{
                        store.getId(),
                        store.getName(),
                        0   // Pour l'instant on laisse 0 employés
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur chargement magasins : " + e.getMessage());
        }
    }

    private void addStore() {

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                    "Seul un administrateur peut créer un magasin.");
            return;
        }

        String name = JOptionPane.showInputDialog(this,
                "Nom du nouveau magasin :");

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nom invalide.");
            return;
        }

        try {
            storeDAO.createStore(name.trim());
            loadStoresFromDatabase();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur création magasin : " + e.getMessage());
        }
    }

    private void deleteStore() {

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                    "Seul un administrateur peut supprimer un magasin.");
            return;
        }

        int row = storeTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez un magasin.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer ce magasin ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                int storeId = (int) tableModel.getValueAt(row, 0);
                storeDAO.deleteStore(storeId);
                loadStoresFromDatabase();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur suppression : " + e.getMessage());
            }
        }
    }

    private void showEmployees() {

        int row = storeTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez un magasin.");
            return;
        }

        String storeName = tableModel.getValueAt(row, 1).toString();

        JOptionPane.showMessageDialog(this,
                "Employés du magasin " + storeName + " :\n\n"
                        + "(fonction à connecter plus tard à store_access)",
                "Liste des employés",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
