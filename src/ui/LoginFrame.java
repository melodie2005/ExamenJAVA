package ui;

import dao.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de connexion.
 *
 * C'est la première fenêtre affichée au lancement de l'application.
 * Elle permet à un utilisateur de se connecter avec son email
 * et son mot de passe.
 */
public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    // DAO utilisé pour l'authentification
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("iStore - Connexion");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        /*
         * Couleur de fond sombre pour un style simple
         * et cohérent avec les autres fenêtres.
         */
        getContentPane().setBackground(new Color(44, 62, 80));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //  Titre
        JLabel lblTitle = new JLabel("Connexion iStore", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);
        gbc.gridwidth = 1;

        //  Champ Email
        addLabel("Email :", gbc, 1, 0);
        txtEmail = new JTextField(20);
        addField(txtEmail, gbc, 1, 1);

        //  Champ Mot de passe
        addLabel("Mot de passe :", gbc, 2, 0);
        txtPassword = new JPasswordField(20);
        addField(txtPassword, gbc, 2, 1);

        //  Boutons
        JButton btnLogin = new JButton("Connexion");
        JButton btnRegister = new JButton("Créer un compte");

        stylePrimaryButton(btnLogin);
        styleSecondaryButton(btnRegister);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(btnRegister, gbc);

        gbc.gridx = 1;
        add(btnLogin, gbc);

        // Actions
        btnLogin.addActionListener(e -> loginAction());
        btnRegister.addActionListener(e -> openRegister());
    }

    /**
     * Action appelée lors du clic sur "Connexion".
     * Elle vérifie les champs et appelle le UserDAO.
     */
    private void loginAction() {
        try {
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                showError("Veuillez remplir tous les champs.");
                return;
            }

            /*
             * Appel au DAO pour vérifier les identifiants.
             * Si la connexion réussit, un objet User est retourné.
             */
            User user = userDAO.login(email, password);

            // Ouverture de la fenêtre principale
            new MainFrame(user).setVisible(true);
            dispose();

        } catch (Exception ex) {
            // Message clair en cas d'erreur (login incorrect, etc.)
            showError(ex.getMessage());
        }
    }

    /**
     * Ouvre la fenêtre de création de compte.
     */
    private void openRegister() {
        new RegisterFrame().setVisible(true);
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
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(46, 204, 113));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Point d'entrée de l'application.
     */
    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}
