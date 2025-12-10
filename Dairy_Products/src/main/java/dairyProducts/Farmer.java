package dairyProducts;

import java.util.ArrayList;

public class Farmer {
    private ArrayList<Product> products = new ArrayList<>();

    public void addMilk(String name, double qty, double price) {
        products.add(new Product(name, qty, price));
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}