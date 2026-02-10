package ui;

import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WhitelistPanel extends JFrame {

    private JTable whitelistTable;
    private DefaultTableModel tableModel;
    private User connectedUser;

    public WhitelistPanel(User user) {

        this.connectedUser = user;

        setTitle("iStore - Whitelist Emails");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Vérification rôle =====
        if (!"ADMIN".equalsIgnoreCase(connectedUser.getRole())) {
            JOptionPane.showMessageDialog(this,
                    "Accès réservé à l'administrateur.");
            dispose();
            return;
        }

        // ===== Titre =====
        JLabel lblTitle = new JLabel("Gestion de la Whitelist", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tableau =====
        tableModel = new DefaultTableModel(
                new Object[]{"Email autorisé"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        whitelistTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(whitelistTable);
        add(scrollPane, BorderLayout.CENTER);

        loadDummyEmails();

        // ===== Boutons =====
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Ajouter Email");
        JButton btnDelete = new JButton("Supprimer");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        btnAdd.addActionListener(e -> addEmail());
        btnDelete.addActionListener(e -> deleteEmail());

        setVisible(true);
    }

    private void loadDummyEmails() {
        tableModel.addRow(new Object[]{"admin@test.com"});
        tableModel.addRow(new Object[]{"employee@test.com"});
    }

    private void addEmail() {

        String email = JOptionPane.showInputDialog(this,
                "Email à autoriser :");

        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Email invalide.");
            return;
        }

        email = email.trim();

        // Vérification format simple
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Format d'email incorrect.");
            return;
        }

        // Vérifier doublon
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(email)) {
                JOptionPane.showMessageDialog(this,
                        "Cet email est déjà whitelisté.");
                return;
            }
        }

        tableModel.addRow(new Object[]{email});
    }

    private void deleteEmail() {

        int row = whitelistTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez un email.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer cet email ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }
}
