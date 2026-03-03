package com.example.Shop.service;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST for ProductService
 *
 * IMPORTANT IDEA:
 * We test ONLY the service logic.
 * The database layer (repository) is mocked.
 *
 * No Spring Boot context is started here.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    /**
     * Mock repository.
     * Mockito creates a fake implementation of ProductRepository.
     * No real database is used.
     */
    @Mock
    private ProductRepository repository;

    /**
     * Class under test.
     * Mockito injects mocked dependencies automatically.
     */
    @InjectMocks
    private ProductService productService;

    // Test data reused across tests
    private Product product;
    private ProductDTO dto;

    /**
     * Runs before EACH test method.
     * Creates reusable fake objects.
     */
    @BeforeEach
    void setUp() {

        // Entity (represents DB object)
        product = new Product(
                1L,
                "Laptop",
                "Gaming laptop",
                5000.0,
                10,
                null
        );

        // DTO (represents API input/output)
        dto = new ProductDTO(
                1L,
                "Laptop",
                "Gaming laptop",
                5000.0,
                10
        );
    }

    // ========= SAVE =========

    /**
     * Tests product creation flow.
     *
     * PROCESS:
     * 1. DTO received
     * 2. Service converts DTO -> Entity
     * 3. Repository saves entity
     * 4. Entity mapped back -> DTO
     */
    @Test
    void save_ShouldReturnSavedProduct() {

        // Define mock behavior
        when(repository.save(any(Product.class))).thenReturn(product);

        // Execute service logic
        ProductDTO saved = productService.save(dto);

        // Validate output DTO
        assertNotNull(saved);
        assertEquals("Laptop", saved.name());

        // Verify repository interaction
        verify(repository).save(any(Product.class));
    }

    // ========= FIND ALL =========

    /**
     * Tests retrieval of all products.
     *
     * Verifies:
     * - repository call
     * - entity -> DTO mapping
     */
    @Test
    void findAll_ShouldReturnProducts() {

        when(repository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> products = productService.findAll();

        assertEquals(1, products.size());

        // ensure repository was called
        verify(repository).findAll();
    }

    // ========= FIND BY ID =========

    /**
     * Case: product exists.
     * Service should return mapped DTO.
     */
    @Test
    void findById_ShouldReturnProduct_WhenExists() {

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO found = productService.findById(1L);

        assertEquals("Laptop", found.name());

        verify(repository).findById(1L);
    }

    /**
     * Case: product missing.
     *
     * Service must throw exception.
     */
    @Test
    void findById_ShouldThrowException_WhenMissing() {

        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productService.findById(2L));
    }

    // ========= DELETE =========

    /**
     * Tests delete operation.
     *
     * We verify only interaction,
     * because delete has no return value.
     */
    @Test
    void deleteById_ShouldCallRepository() {

        doNothing().when(repository).deleteById(1L);

        productService.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    // ========= UPDATE =========

    /**
     * Tests update logic.
     *
     * PROCESS:
     * 1. Load existing entity
     * 2. Modify fields
     * 3. Save updated entity
     * 4. Return DTO
     */
    @Test
    void update_ShouldModifyProduct() {

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        ProductDTO updated = productService.update(1L, dto);

        assertEquals("Laptop", updated.name());

        verify(repository).save(product);
    }

    // ========= CUSTOM QUERIES =========

    /**
     * Tests custom repository query:
     * find products with price greater than value.
     */
    @Test
    void findExpensiveProducts_ShouldReturnList() {

        when(repository.findByPriceGreaterThan(4000.0))
                .thenReturn(List.of(product));

        List<ProductDTO> result =
                productService.findExpensiveProducts(4000.0);

        assertFalse(result.isEmpty());

        verify(repository).findByPriceGreaterThan(4000.0);
    }

    /**
     * Tests low stock filtering logic.
     */
    @Test
    void findLowStock_ShouldReturnProducts() {

        when(repository.findLowStock(20))
                .thenReturn(List.of(product));

        List<ProductDTO> result =
                productService.findLowStock(20);

        assertEquals(1, result.size());

        verify(repository).findLowStock(20);
    }

    /**
     * Tests native search query.
     * Ensures mapping still works after custom query.
     */
    @Test
    void searchByName_ShouldReturnProducts() {

        when(repository.searchByNameNative("Lap"))
                .thenReturn(List.of(product));

        List<ProductDTO> result =
                productService.searchByName("Lap");

        assertEquals("Laptop", result.get(0).name());

        verify(repository).searchByNameNative("Lap");
    }
}