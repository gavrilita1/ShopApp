package com.example.Shop.service;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    // Repository responsible for database operations
    private final ProductRepository repository;

    // Constructor Injection (preferred Spring pattern)
    @Autowired
    public ProductService(ProductRepository reposity) {
        this.repository = reposity;
    }

    /**
     * ENTITY -> DTO mapping.
     *
     * WHY DO WE MAP?
     * - Entities represent database structure
     * - DTOs represent API responses
     * - We avoid exposing internal DB models to clients
     */
    private ProductDTO toDto(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    /**
     * CREATE PRODUCT
     *
     * INPUT:
     *  ProductDTO received from Controller (API request body)
     *
     * PROCESS:
     *  1. Convert DTO -> Entity
     *  2. Save entity in database
     *  3. Convert saved entity -> DTO
     *
     * OUTPUT:
     *  ProductDTO (saved product)
     */
    public ProductDTO save(ProductDTO productDto) {

        Product product = new Product(
                null, // id generated automatically by DB
                productDto.name(),
                productDto.description(),
                productDto.price(),
                productDto.stock(),
                null
        );

        return toDto(repository.save(product));
    }

    /**
     * READ ALL PRODUCTS
     *
     * PROCESS:
     *  - Fetch all entities
     *  - Map each entity to DTO using stream()
     *
     * OUTPUT:
     *  List<ProductDTO>
     */
    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDto) // method reference mapping
                .toList();
    }

    /**
     * READ PRODUCT BY ID
     *
     * Throws exception if product does not exist.
     */
    public ProductDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() ->
                        new RuntimeException("Product was not found"));
    }

    /**
     * DELETE PRODUCT
     *
     * INPUT: product id
     * OUTPUT: none (void)
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * UPDATE PRODUCT
     *
     * PROCESS:
     *  1. Load existing entity
     *  2. Update fields
     *  3. Save updated entity
     *  4. Map result to DTO
     */
    public ProductDTO update(Long id, ProductDTO productDTO) {

        Product existingProduct = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        existingProduct.setName(productDTO.name());
        existingProduct.setDescription(productDTO.description());
        existingProduct.setPrice(productDTO.price());
        existingProduct.setStock(productDTO.stock());

        return toDto(repository.save(existingProduct));
    }

    /**
     * CUSTOM QUERY:
     * Returns products with price greater than given value.
     */
    public List<ProductDTO> findExpensiveProducts(Double price){
        return repository.findByPriceGreaterThan(price)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * CUSTOM QUERY:
     * Returns products with stock lower than threshold.
     */
    public List<ProductDTO> findLowStock(Integer stock){
        return repository.findLowStock(stock)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * CUSTOM QUERY (Native SQL):
     * Searches products by name.
     */
    public List<ProductDTO> searchByName(String name){
        return repository.searchByNameNative(name)
                .stream()
                .map(this::toDto)
                .toList();
    }
}
