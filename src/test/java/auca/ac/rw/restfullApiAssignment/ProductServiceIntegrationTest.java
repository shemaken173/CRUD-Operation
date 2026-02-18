package auca.ac.rw.restfullApiAssignment;

import auca.ac.rw.restfullApiAssignment.modal.Product;
import auca.ac.rw.restfullApiAssignment.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Product Service Integration Tests")
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(5001L);
        testProduct.setName("Integration Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
        testProduct.setCategory("Test");
        testProduct.setStockQuantity(10);
    }

    @Test
    @DisplayName("End-to-End CRUD Workflow")
    void testCompleteWorkflow() {
        // 1. CREATE - Save product
        String saveResult = productService.saveProduct(testProduct);
        assertTrue(saveResult.contains("saved successfully"), "Product should be saved successfully");

        // 2. READ - Get product by ID
        Optional<Product> retrieved = productService.getProductById(testProduct.getId());
        assertTrue(retrieved.isPresent(), "Product should be found");
        assertEquals("Integration Test Product", retrieved.get().getName());

        // 3. UPDATE - Modify product
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Integration Product");
        updatedProduct.setPrice(199.99);
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setCategory("Updated");
        updatedProduct.setStockQuantity(20);

        String updateResult = productService.updateProduct(testProduct.getId(), updatedProduct);
        assertEquals("Product updated successfully.", updateResult);

        // Verify update
        Optional<Product> verifyUpdate = productService.getProductById(testProduct.getId());
        assertTrue(verifyUpdate.isPresent());
        assertEquals("Updated Integration Product", verifyUpdate.get().getName());
        assertEquals(199.99, verifyUpdate.get().getPrice());

        // 4. DELETE - Remove product
        String deleteResult = productService.deleteProduct(testProduct.getId());
        assertEquals("Product deleted successfully.", deleteResult);

        // 5. VERIFY DELETE - Confirm product is gone
        Optional<Product> verifyDelete = productService.getProductById(testProduct.getId());
        assertFalse(verifyDelete.isPresent(), "Product should be deleted");
    }
    
    @Test
    @DisplayName("Bulk Save Products")
    void testBulkSave() {
        Product product1 = new Product();
        product1.setId(5002L);
        product1.setName("Bulk Product 1");
        product1.setPrice(50.0);
        product1.setCategory("Bulk");
        product1.setStockQuantity(5);

        Product product2 = new Product();
        product2.setId(5003L);
        product2.setName("Bulk Product 2");
        product2.setPrice(75.0);
        product2.setCategory("Bulk");
        product2.setStockQuantity(8);

        List<Product> products = Arrays.asList(product1, product2);
        List<Product> savedProducts = productService.saveAll(products);

        assertEquals(2, savedProducts.size(), "Should save 2 products");

        // Verify first product
        Optional<Product> verify1 = productService.getProductById(5002L);
        assertTrue(verify1.isPresent());
        assertEquals("Bulk Product 1", verify1.get().getName());

        // Verify second product
        Optional<Product> verify2 = productService.getProductById(5003L);
        assertTrue(verify2.isPresent());
        assertEquals("Bulk Product 2", verify2.get().getName());
    }

    @Test
    @DisplayName("Get All Products")
    void testGetAllProducts() {
        // Insert a test product
        productService.saveProduct(testProduct);

        // Get all
        List<Product> allProducts = productService.getAllProducts();
        assertTrue(allProducts.size() > 0, "Should have at least one product");

        boolean found = allProducts.stream()
                .anyMatch(p -> p.getId().equals(testProduct.getId()));
        assertTrue(found, "Test product should be in the list");
    }

    @Test
    @DisplayName("Duplicate Product ID Prevention")
    void testDuplicateProductPrevention() {
        // Save first product
        String firstSave = productService.saveProduct(testProduct);
        assertEquals("Product saved successfully.", firstSave);

        // Try to save again with same ID
        String secondSave = productService.saveProduct(testProduct);
        assertTrue(secondSave.contains("already exists"), "Should reject duplicate ID");
    }

    @Test
    @DisplayName("Update Non-Existent Product")
    void testUpdateNonExistent() {
        Product updateProduct = new Product();
        updateProduct.setName("Should Fail");
        updateProduct.setPrice(100.0);

        String result = productService.updateProduct(99999L, updateProduct);
        assertEquals("Product not found.", result);
    }

    @Test
    @DisplayName("Delete Non-Existent Product")
    void testDeleteNonExistent() {
        String result = productService.deleteProduct(99999L);
        assertEquals("Product not found.", result);
    }
}
