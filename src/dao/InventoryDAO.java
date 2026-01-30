package dao;

import database.DatabaseConnection;
import models.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
     public void addItem(String name, double price, int qty, int storeId) throws SQLException {
         String sql = "INSERT INTO items (name, price, quantity, store_id) VALUES (?,?,?,?)";
         try (Connection conn = DatabaseConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, name);
             stmt.setDouble(2,price);
             stmt.setInt(3,qty);
             stmt.setInt(4, storeId);
             stmt.executeUpdate();
         }
     }

     public List<Item> getItems(int storeId) throws SQLException {
         List<Item> items = new ArrayList<>();
         String sql = "SELECT * FROM items WHERE store_id = ?";
         try (Connection conn = DatabaseConnection.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, storeId);
             ResultSet rs = stmt.executeQuery();
             while (rs.next()) {
                 items.add(new Item(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity")));
             }
         }
         return items;
     }
}
