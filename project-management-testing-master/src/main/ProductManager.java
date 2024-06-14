package main;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int productId) {
        products.removeIf(product -> product.getId() == productId);
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    public Product getProduct(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public double getTotalValueInStock() {
        double totalValue = 0.0;
        for (Product product : products) {
            if (product.isAvailable()) {
                totalValue += product.getPrice() * product.getQuantity();
            }
        }
        return totalValue;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
