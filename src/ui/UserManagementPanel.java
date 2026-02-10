package ui;

import dao.UserDAO;
import models.User;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private User connectedUser;

    private UserDAO userDAO = new UserDAO();

    public UserManagementPanel(User user) {

        this.connectedUser = user;

        setTitle("iStore - Gestion des Utilisateurs");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Titre =====
        JLabel lblTitle = new JLabel("Gestion des Utilisateurs", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== Tableau =====
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Email", "Rôle"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        loadUsersFromDatabase();

        // ===== Boutons =====
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        btnAdd.addActionListener(e -> addUser());
        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());

        setVisible(true);
    }

    // ===============================
    // Chargement depuis la base
    // ===============================
    private void loadUsersFromDatabase() {

        try {
            tableModel.setRowCount(0);

            List<User> users = userDAO.getAllUsers();

            for (User u : users) {
                tableModel.addRow(new Object[]{
                        u.getId(),
                        u.getPseudo(),
                        u.getEmail(),
                        u.getRole()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur chargement utilisateurs : " + e.getMessage());
        }
    }

    private void addUser() {

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                    "Seul un administrateur peut ajouter un utilisateur.");
            return;
        }

        JTextField txtName = new JTextField();
        JTextField txtEmail = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Nom :"));
        panel.add(txtName);
        panel.add(new JLabel("Email :"));
        panel.add(txtEmail);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(txtPassword);
        panel.add(new JLabel("Rôle :"));
        panel.add(cbRole);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Ajouter un utilisateur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                userDAO.creatUser(
                        txtEmail.getText().trim(),
                        txtName.getText().trim(),
                        new String(txtPassword.getPassword()),
                        cbRole.getSelectedItem().toString()
                );

                loadUsersFromDatabase();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur : " + e.getMessage());
            }
        }
    }

    private void editUser() {

        int row = userTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String selectedEmail = tableModel.getValueAt(row, 2).toString();

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")
                && !connectedUser.getEmail().equalsIgnoreCase(selectedEmail)) {

            JOptionPane.showMessageDialog(this,
                    "Vous ne pouvez modifier que votre propre compte.");
            return;
        }

        String currentName = tableModel.getValueAt(row, 1).toString();
        String currentRole = tableModel.getValueAt(row, 3).toString();

        JTextField txtName = new JTextField(currentName);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        cbRole.setSelectedItem(currentRole);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Nom :"));
        panel.add(txtName);
        panel.add(new JLabel("Rôle :"));
        panel.add(cbRole);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Modifier utilisateur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            try {

                String newRole = currentRole;

                if (connectedUser.getRole().equalsIgnoreCase("ADMIN")) {
                    newRole = cbRole.getSelectedItem().toString();
                }

                userDAO.updateUser(userId,
                        txtName.getText().trim(),
                        newRole);

                loadUsersFromDatabase();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur modification : " + e.getMessage());
            }
        }
    }

    private void deleteUser() {

        int row = userTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String selectedEmail = tableModel.getValueAt(row, 2).toString();

        if (!connectedUser.getRole().equalsIgnoreCase("ADMIN")
                && !connectedUser.getEmail().equalsIgnoreCase(selectedEmail)) {

            JOptionPane.showMessageDialog(this,
                    "Vous ne pouvez supprimer que votre propre compte.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer cet utilisateur ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                userDAO.deleteUser(userId);
                loadUsersFromDatabase();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur suppression : " + e.getMessage());
            }
        }
    }
}
