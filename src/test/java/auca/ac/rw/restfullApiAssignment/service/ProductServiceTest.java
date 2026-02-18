package auca.ac.rw.restfullApiAssignment.service;

import auca.ac.rw.restfullApiAssignment.modal.Product;
import auca.ac.rw.restfullApiAssignment.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(99.99);
        testProduct.setCategory("Electronics");
        testProduct.setStockQuantity(10);
    }

    @Test
    @DisplayName("Save Product Successfully")
    void testSaveProduct_Success() {
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.empty());
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        String result = productService.saveProduct(testProduct);

        assertEquals("Product saved successfully.", result);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Save Product with Duplicate ID - Fails")
    void testSaveProduct_DuplicateId() {
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        String result = productService.saveProduct(testProduct);

        assertTrue(result.contains("already exists"));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get All Products")
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals(testProduct.getName(), result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get Product by ID - Found")
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals(testProduct.getId(), result.get().getId());
        assertEquals(testProduct.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Product by ID - Not Found")
    void testGetProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(999L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Update Product - Success")
    void testUpdateProduct_Success() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(149.99);
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setCategory("Updated Category");
        updatedProduct.setStockQuantity(20);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        String result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Product updated successfully.", result);
        verify(productRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update Product - Not Found")
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        String result = productService.updateProduct(999L, testProduct);

        assertEquals("Product not found.", result);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete Product - Success")
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        String result = productService.deleteProduct(1L);

        assertEquals("Product deleted successfully.", result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete Product - Not Found")
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        String result = productService.deleteProduct(999L);

        assertEquals("Product not found.", result);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Save All Products - Bulk Insert")
    void testSaveAll_BulkInsert() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.saveAll(products)).thenReturn(products);

        List<Product> result = productService.saveAll(products);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).saveAll(products);
    }
}
