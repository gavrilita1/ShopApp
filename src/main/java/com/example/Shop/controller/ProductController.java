package com.example.Shop.controller;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        ProductDTO saved = service.save(dto);
        return ResponseEntity
                .created(URI.create("/api/products/" + saved.id()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Get product by id",
            description = "Returns a single product based on its id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                             @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expensive/{price}")
    public ResponseEntity<List<ProductDTO>> findExpensive(@PathVariable Double price) {
        return ResponseEntity.ok(service.findExpensiveProducts(price));
    }

    @GetMapping("/low-stock/{stock}")
    public ResponseEntity<List<ProductDTO>> findLowStock(@PathVariable Integer stock) {
        return ResponseEntity.ok(service.findLowStock(stock));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDTO>> searchByName(@PathVariable String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }
}