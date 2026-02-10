package models;

public class Item {

    private int id, quantity;
    private String name;
    private double price;

    public Item(int id, String name, double price, int quantity){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // ===== Getters ajoutés par Ruth =====

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | Prix: " + price + "€ | Stock: " + quantity;
    }
}
