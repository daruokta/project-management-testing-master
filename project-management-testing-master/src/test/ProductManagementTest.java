package test;

import main.Product;
import main.ProductManager;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;
import org.junit.*;
import org.junit.rules.TestName;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class ProductManagementTest {

    private final ProductManager productManager = new ProductManager();

    @Rule
    public TestName testName = new TestName();

    @DataPoints
    public static Product[] products() {
        return new Product[]{
                new Product(1, "Product A", 10.0, 5, true),
                new Product(2, "Product B", 20.0, 3, true),
                new Product(3, "Product C", 15.0, 0, false),

        };
    }

    @DataPoints
    public static String[] productNames() {
        return new String[]{"Product A", "Product B", "Product C", "Product D"};
    }

    @DataPoints
    public static int[] productIds() {
        return new int[]{1, 2, 3, 4, 5};
    }

    @DataPoints
    public static boolean[] availability() {
        return new boolean[]{true, false};
    }

    @DataPoints
    public static double[] prices() {
        return new double[]{10.0, 20.0, 15.0, 50.0, 0.0};
    }

    @DataPoints
    public static Product[][] productsArrays() {
        return new Product[][]{
                {
                        new Product(1, "Product A", 10.0, 5, true),
                        new Product(2, "Product B", 20.0, 3, true),
                        new Product(3, "Product C", 15.0, 0, false),
                        new Product(4, "Product D", 50.0, 0, true)
                },
        };
    }

    @Theory
    public void testAddProduct(Product product) {
        System.out.println("Running " + testName.getMethodName() + " with product: " + product);
        logProductDetails(product);

        // Handle validation for product name length
        if (product.getName().length() < 5) {
            System.out.println("Skipping test due to invalid product name: " + product.getName());
            return;
        }

        productManager.addProduct(product);
        Product retrievedProduct = productManager.getProduct(product.getId());
        System.out.println("Expected: " + product);
        System.out.println("Actual: " + retrievedProduct);
        assertEquals(product, retrievedProduct);
        System.out.println("Test passed!");
    }

    @Theory
    public void testRemoveProduct(Product product) {
        System.out.println("Running " + testName.getMethodName() + " with product: " + product);
        logProductDetails(product);
        productManager.addProduct(product);
        productManager.removeProduct(product.getId());
        Product retrievedProduct = productManager.getProduct(product.getId());
        System.out.println("Expected: null");
        System.out.println("Actual: " + retrievedProduct);
        assertNull(retrievedProduct);
        System.out.println("Test passed!");
    }

    @Theory
    public void testUpdateProduct(Product product) {
        System.out.println("Running " + testName.getMethodName() + " with product: " + product);
        logProductDetails(product);

        productManager.addProduct(product);

        boolean newAvailability = product.getQuantity() > 0 ? true : !product.isAvailable();
        Product updatedProduct = new Product(product.getId(), product.getName() + " Updated", product.getPrice() + 5, product.getQuantity() + 1, newAvailability);

        productManager.updateProduct(updatedProduct);
        Product retrievedProduct = productManager.getProduct(product.getId());

        System.out.println("Expected: " + updatedProduct);
        System.out.println("Actual: " + retrievedProduct);

        assertEquals(updatedProduct, retrievedProduct);
        System.out.println("Test passed!");
    }

    @Theory
    public void testGetProduct(int productId, Product product) {
        System.out.println("Running " + testName.getMethodName() + " with productId: " + productId + " and product: " + product);
        logProductDetails(product);
        Assume.assumeTrue(productId == product.getId());
        productManager.addProduct(product);
        Product retrievedProduct = productManager.getProduct(productId);
        System.out.println("Expected: " + product);
        System.out.println("Actual: " + retrievedProduct);
        assertEquals(product, retrievedProduct);
        System.out.println("Test passed!");
    }

    @Theory
    public void testGetTotalValueInStock(Product[] products) {
        System.out.println("Running " + testName.getMethodName() + " with products: " + Arrays.toString(products));
        Arrays.stream(products).forEach(this::logProductDetails);
        productManager.getProducts().clear();
        Arrays.stream(products).forEach(productManager::addProduct);
        double expectedTotalValue = Arrays.stream(products)
                .mapToDouble(product -> product.isAvailable() ? product.getPrice() * product.getQuantity() : 0)
                .sum();
        double actualTotalValue = productManager.getTotalValueInStock();
        System.out.println("Expected: " + expectedTotalValue);
        System.out.println("Actual: " + actualTotalValue);
        assertEquals(expectedTotalValue, actualTotalValue, 0.001);
        System.out.println("Test passed!");
    }

    @Theory
    public void testAddMultipleProducts() {
        System.out.println("Running " + testName.getMethodName());
        List<Product> productList = Arrays.asList(
                new Product(1, "Product A", 10.0, 5, true),
                new Product(2, "Product B", 20.0, 3, true)
        );
        productList.forEach(this::logProductDetails);
        productList.forEach(productManager::addProduct);
        int actualSize = productManager.getProducts().size();
        System.out.println("Expected: " + productList.size());
        System.out.println("Actual: " + actualSize);
        assertEquals(productList.size(), actualSize);
        System.out.println("Test passed!");
    }

    @Theory
    public void testGetTotalQuantityInStock(Product[] products) {
        System.out.println("Running " + testName.getMethodName() + " with products: " + Arrays.toString(products));
        Arrays.stream(products).forEach(this::logProductDetails);
        productManager.getProducts().clear();
        Arrays.stream(products).forEach(productManager::addProduct);
        int expectedTotalQuantity = Arrays.stream(products)
                .filter(Product::isAvailable)
                .mapToInt(Product::getQuantity)
                .sum();
        int actualTotalQuantity = productManager.getTotalQuantityInStock();
        System.out.println("Expected: " + expectedTotalQuantity);
        System.out.println("Actual: " + actualTotalQuantity);
        assertEquals(expectedTotalQuantity, actualTotalQuantity);
        System.out.println("Test passed!");
    }

    @Theory
    public void testGetProductCountByAvailability(Product[] products) {
        System.out.println("Running " + testName.getMethodName() + " with products: " + Arrays.toString(products));
        Arrays.stream(products).forEach(this::logProductDetails);
        productManager.getProducts().clear();
        Arrays.stream(products).forEach(productManager::addProduct);
        long availableProductCount = Arrays.stream(products).filter(Product::isAvailable).count();
        long unavailableProductCount = Arrays.stream(products).filter(product -> !product.isAvailable()).count();
        long actualAvailableProductCount = productManager.getProductCountByAvailability().get(true);
        long actualUnavailableProductCount = productManager.getProductCountByAvailability().get(false);
        System.out.println("Expected available: " + availableProductCount);
        System.out.println("Actual available: " + actualAvailableProductCount);
        System.out.println("Expected unavailable: " + unavailableProductCount);
        System.out.println("Actual unavailable: " + actualUnavailableProductCount);
        assertEquals(availableProductCount, actualAvailableProductCount);
        assertEquals(unavailableProductCount, actualUnavailableProductCount);
        System.out.println("Test passed!");
    }

    private void logProductDetails(Product product) {
        System.out.println("Product details: [id=" + product.getId() + ", name=" + product.getName() +
                ", price=" + product.getPrice() + ", quantity=" + product.getQuantity() +
                ", isAvailable=" + product.isAvailable() + "]");
    }
}
