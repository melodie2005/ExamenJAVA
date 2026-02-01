package main;
import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import models.User;

public class LoginFrame extends JFrame {
    JTextField txtEmail = new JTextField();
    JPasswordField txtPass = new JPasswordField();
    JButton btnLogin = new JButton("Connexion");
    UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Connexion");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Pass:")); add(txtPass);
        add(btnLogin);
        setLocationRelativeTo(null);

        btnLogin.addActionListener(e -> {
            try {
                String email = txtEmail.getText();
                if (!userDAO.isWhitelisted(email)) {
                    JOptionPane.showMessageDialog(this, "Email non autorisé (Whitelist) !");
                    return;
                }
                User user = userDAO.login(email, new String(txtPass.getPassword()));
                if (user != null) {
                    new main.MainFrame(user).setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiants faux.");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });
    }
    public static void main(String[] args) { new LoginFrame().setVisible(true); }
}