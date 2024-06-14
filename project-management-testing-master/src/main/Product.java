package main;

public class Product {
    private int id;
    private String name;
    private double price;
    private boolean isAvailable;
    private int quantity;

    public Product(int id, String name, double price, int quantity, boolean isAvailable) {
        this.id = id;
        setName(name);  // Use the setter to apply validation
        this.price = price;
        this.isAvailable = isAvailable;
        setQuantity(quantity);  // Use the setter to apply validation
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.length() < 5) {
            throw new IllegalArgumentException("Product name must contain at least 5 letters");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        } else if (quantity == 0) {
            if (this.isAvailable) {
                throw new IllegalArgumentException("Product cannot be available when quantity is 0");
            }
            this.quantity = quantity;
        } else {
            if (!this.isAvailable) {
                throw new IllegalArgumentException("Product must be available when quantity is greater than 0");
            }
            this.quantity = quantity;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        if (isAvailable && this.quantity == 0) {
            throw new IllegalArgumentException("Product cannot be available when quantity is 0");
        }
        this.isAvailable = isAvailable;
    }
}
