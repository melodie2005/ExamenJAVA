package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panneau de gestion des utilisateurs.
 *
 * Ce panneau est réservé à l'administrateur.
 * Il permet :
 *  - d'afficher la liste des utilisateurs
 *  - de modifier un utilisateur (pseudo / rôle)
 *  - de supprimer un utilisateur
 *
 * Les mots de passe ne sont JAMAIS affichés,
 * conformément aux règles de sécurité du sujet.
 */
public class UserManagementPanel extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel() {

        /*
         * BorderLayout permet de séparer clairement :
         * - le titre
         * - la table
         * - les boutons d'action
         */
        setLayout(new BorderLayout());


        // Titre

        JLabel lblTitle = new JLabel("Gestion des utilisateurs", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);


        // Table des utilisateurs

        /*
         * La JTable est utilisée car elle est adaptée
         * pour afficher des données structurées (utilisateurs).
         */
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Email", "Pseudo", "Rôle"},
                0
        ) {
            // On empêche la modification directe des cellules
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        add(scrollPane, BorderLayout.CENTER);

        // démonstration UI
        loadDummyUsers();


        // Boutons d'action

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");

        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        add(actionPanel, BorderLayout.SOUTH);


        // Actions


        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());
    }

    /**
     * Chargement de données fictives.
     *
     * Ces données simulent les utilisateurs
     * qui seront plus tard chargés depuis la base.
     */
    private void loadDummyUsers() {
        tableModel.addRow(new Object[]{1, "admin@istore.com", "admin", "ADMIN"});
        tableModel.addRow(new Object[]{2, "employee1@istore.com", "employee1", "EMPLOYEE"});
        tableModel.addRow(new Object[]{3, "employee2@istore.com", "employee2", "EMPLOYEE"});
    }

    /**
     * Modification d'un utilisateur.
     *
     * On permet ici de modifier le pseudo et le rôle,
     * ce qui correspond aux droits d'un administrateur.
     */
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un utilisateur.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String currentPseudo = tableModel.getValueAt(selectedRow, 2).toString();
        String currentRole = tableModel.getValueAt(selectedRow, 3).toString();

        JTextField txtPseudo = new JTextField(currentPseudo);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"ADMIN", "EMPLOYEE"});
        cbRole.setSelectedItem(currentRole);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.add(new JLabel("Pseudo :"));
        form.add(txtPseudo);
        form.add(new JLabel("Rôle :"));
        form.add(cbRole);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Modifier l'utilisateur",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            tableModel.setValueAt(txtPseudo.getText().trim(), selectedRow, 2);
            tableModel.setValueAt(cbRole.getSelectedItem(), selectedRow, 3);
        }
    }

    /**
     * Suppression d'un utilisateur.
     *
     * Une confirmation est demandée avant suppression.
     */
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un utilisateur.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer cet utilisateur ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }
}
