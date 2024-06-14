package test;

import main.Product;
import main.ProductManager;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;
import org.junit.*;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

@RunWith(Theories.class)
public class ProductManagementTest {

    private final ProductManager productManager = new ProductManager();

    @DataPoints
    public static Product[] products() {
        return new Product[] {
                new Product(1, "Product A", 10.0, 5, true),
                new Product(2, "Product B", 20.0, 3, true),
                new Product(3, "Product C", 15.0, 0, false),
                new Product(4, "Product D", 50.0, 2, true)
        };
    }

    @DataPoints
    public static String[] productNames() {
        return new String[] {"Product A", "Product B", "Product C", "Product D"};
    }

    @DataPoints
    public static int[] productIds() {
        return new int[] {1, 2, 3, 4, 5};
    }

    @DataPoints
    public static boolean[] availability() {
        return new boolean[] {true, false};
    }

    @DataPoints
    public static double[] prices() {
        return new double[] {10.0, 20.0, 15.0, 50.0, 0.0};
    }

    @Theory
    public void testAddProduct(Product product) {
        productManager.addProduct(product);
        assertEquals(product, productManager.getProduct(product.getId()));
    }

    @Theory
    public void testRemoveProduct(Product product) {
        productManager.addProduct(product);
        productManager.removeProduct(product.getId());
        assertNull(productManager.getProduct(product.getId()));
    }

    @Theory
    public void testUpdateProduct(Product product) {
        productManager.addProduct(product);
        Product updatedProduct = new Product(product.getId(), product.getName() + " Updated", product.getPrice() + 5, product.getQuantity() + 1, !product.isAvailable());
        productManager.updateProduct(updatedProduct);
        assertEquals(updatedProduct, productManager.getProduct(product.getId()));
    }

    @Theory
    public void testGetProduct(int productId, Product product) {
        Assume.assumeTrue(productId == product.getId());
        productManager.addProduct(product);
        assertEquals(product, productManager.getProduct(productId));
    }

    @Theory
    public void testGetTotalValueInStock(Product[] products) {
        productManager.getProducts().clear();
        Arrays.stream(products).forEach(productManager::addProduct);
        double expectedTotalValue = Arrays.stream(products)
                .filter(Product::isAvailable)
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
        assertEquals(expectedTotalValue, productManager.getTotalValueInStock(), 0.001);
    }

    @Test
    public void testAddMultipleProducts() {
        List<Product> productList = Arrays.asList(
                new Product(1, "Product A", 10.0, 5, true),
                new Product(2, "Product B", 20.0, 3, true)
        );
        productList.forEach(productManager::addProduct);
        assertEquals(productList.size(), productManager.getProducts().size());
    }
}
