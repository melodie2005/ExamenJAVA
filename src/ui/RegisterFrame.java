package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de création de compte.
 *
 * Cette fenêtre permet à un utilisateur de créer un compte
 * si son email a été préalablement whitelisté par un administrateur.
 *
 * Toute la logique liée à la base de données est gérée
 * dans le UserDAO, conformément au cours.
 */
public class RegisterFrame extends JFrame {

    private JTextField txtEmail;
    private JTextField txtPseudo;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    // DAO utilisé pour communiquer avec la base de données
    private final UserDAO userDAO = new UserDAO();

    public RegisterFrame() {
        setTitle("iStore - Création de compte");
        setSize(450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        /*
         * Couleur de fond sombre pour un meilleur rendu
         * et cohérent avec la fenêtre de connexion.
         */
        getContentPane().setBackground(new Color(44, 62, 80));

        /*
         * GridBagLayout est utilisé car il est adapté
         * aux formulaires avec plusieurs champs.
         */
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //  Titre
        JLabel lblTitle = new JLabel("Créer un compte iStore", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);
        gbc.gridwidth = 1;

        // Email
        addLabel("Email :", gbc, 1, 0);
        txtEmail = new JTextField(20);
        addField(txtEmail, gbc, 1, 1);

        //  Pseudo
        addLabel("Pseudo :", gbc, 2, 0);
        txtPseudo = new JTextField(20);
        addField(txtPseudo, gbc, 2, 1);

        //  Mot de passe
        addLabel("Mot de passe :", gbc, 3, 0);
        txtPassword = new JPasswordField(20);
        addField(txtPassword, gbc, 3, 1);

        //  Confirmation
        addLabel("Confirmer mot de passe :", gbc, 4, 0);
        txtConfirmPassword = new JPasswordField(20);
        addField(txtConfirmPassword, gbc, 4, 1);

        //  Boutons
        JButton btnRegister = new JButton("Créer le compte");
        JButton btnCancel = new JButton("Annuler");

        stylePrimaryButton(btnRegister);
        styleSecondaryButton(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(btnCancel, gbc);

        gbc.gridx = 1;
        add(btnRegister, gbc);

        btnRegister.addActionListener(e -> registerAction());
        btnCancel.addActionListener(e -> dispose());
    }

    /**
     * Action déclenchée lors du clic sur "Créer le compte".
     */
    private void registerAction() {
        try {
            String email = txtEmail.getText().trim();
            String pseudo = txtPseudo.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());

            if (email.isEmpty() || pseudo.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                showError("Tous les champs doivent être remplis.");
                return;
            }

            if (!password.equals(confirm)) {
                showError("Les mots de passe ne correspondent pas.");
                return;
            }

            if (userDAO.isWhitelisted(email)) {
                showError("Cet email n'est pas autorisé à créer un compte.");
                return;
            }

            /*
             * Appel direct au DAO.
             * Le mot de passe est hashé côté DAO,
             * le but est de respecter les consignes de sécurité.
             */
            userDAO.creatUser(email, pseudo, password, "EMPLOYEE");

            JOptionPane.showMessageDialog(
                    this,
                    "Compte créé avec succès. Vous pouvez maintenant vous connecter.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // Méthodes utilitaires

    private void addLabel(String text, GridBagConstraints gbc, int y, int x) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        gbc.gridx = x;
        gbc.gridy = y;
        add(label, gbc);
    }

    private void addField(JComponent field, GridBagConstraints gbc, int y, int x) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(field, gbc);
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(39, 174, 96));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
