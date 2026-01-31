package dao;

import database.DatabaseConnection;
import models.User;
import utils.SecurityUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean isWhitelisted(String email) throws SQLException {
        String sql = "SELECT email FROM whitelist WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        }
    }

    public void creatUser(String email, String pseudo, String password, String role) throws Exception {
        if (!isWhitelisted(email)) throw new Exception("Email non whiitelisté !");

        String sql = "INSERT INTO users (email, pseudo, password, role) VALUES (?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 2. On configure les paramètres à l'INTERIEUR des accolades
            stmt.setString(1, email);
            stmt.setString(2, pseudo);
            stmt.setString(3, SecurityUtils.hashPassword(password));
            stmt.setString(4, role);

            // 3. On exécute
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Erreur lors de la creation de l'utilisateur : " + e.getMessage());
        }
    }
    public User login(String email, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashed = SecurityUtils.hashPassword(password);
                if (rs.getString("password").equals(hashed)) {
                    return new User(rs.getInt("id"), rs.getString("email"), rs.getString("pseudo"), "", rs.getString("role"));
                }
            }
        }
        throw new Exception("Email ou mot de passe incorrect.");
      }
    }

