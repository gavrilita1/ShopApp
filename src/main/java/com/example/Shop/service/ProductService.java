package com.example.Shop.service;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<ProductDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ProductDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public ProductDTO save(ProductDTO dto) {
        Product product = new Product(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.stock(),
                null
        );

        return toDto(repository.save(product));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setPrice(dto.price());
        existing.setStock(dto.stock());

        return toDto(repository.save(existing));
    }

    public List<ProductDTO> findExpensiveProducts(Double price) {
        return repository.findByPriceGreaterThan(price)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductDTO> findLowStock(Integer stock) {
        return repository.findLowStock(stock)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductDTO> searchByName(String name) {
        return repository.searchByName(name)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ---- Mapper ----
    private ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
}