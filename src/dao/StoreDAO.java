package dao;
import database.DatabaseConnection;
import models.Store;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {
    public void createStore(String name) throws SQLException {
        String sql = "INSERT INTO stores (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }
    public List<Store> getAllStores() throws SQLException {
        List<Store> list = new ArrayList();
        String sql = "SELECT * FROM stores";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(new Store(rs.getInt("id"), rs.getString("name")));
        }
        return list;
    }


}
