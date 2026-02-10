package dao;

import database.DatabaseConnection;
import models.User;
import utils.SecurityUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        if (!isWhitelisted(email))
            throw new Exception("Email non whitelisté !");

        String sql = "INSERT INTO users (email, pseudo, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, pseudo);
            stmt.setString(3, SecurityUtils.hashPassword(password));
            stmt.setString(4, role);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Erreur lors de la creation : " + e.getMessage());
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

                    return new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("pseudo"),
                            "",
                            rs.getString("role")
                    );
                }
            }
        }

        throw new Exception("Email ou mot de passe incorrect.");
    }


    // Partie ajoutée par Ruth


    public List<User> getAllUsers() throws SQLException {

        List<User> list = new ArrayList<>();

        String sql = "SELECT id, email, pseudo, role FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("pseudo"),
                        "",
                        rs.getString("role")
                ));
            }
        }

        return list;
    }

    public void updateUser(int id, String pseudo, String role) throws SQLException {

        String sql = "UPDATE users SET pseudo = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pseudo);
            stmt.setString(2, role);
            stmt.setInt(3, id);

            stmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
