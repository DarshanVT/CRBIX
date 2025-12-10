package dairyProducts;

public class Product {
    String name;
    double quantity;
    double price;

    public Product(String name, double quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return name + " | Qty: " + quantity + " L | Price: ₹" + price;
    }
}
