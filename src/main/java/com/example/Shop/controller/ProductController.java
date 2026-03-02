package com.example.Shop.controller;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
// All endpoints start with /api/products
@RequiredArgsConstructor
public class ProductController {

    // Controller should NEVER contain business logic.
    // It only delegates work to the service.
    private final ProductService service;

    /**
     * CREATE PRODUCT
     *
     * POST /api/products
     *
     * Receives JSON body and converts it automatically to ProductDTO.
     */
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDto){
        return service.save(productDto);
    }

    /**
     * GET ALL PRODUCTS
     *
     * GET /api/products
     */
    @GetMapping
    public List<ProductDTO> findAll(){
        return service.findAll();
    }

    /**
     * GET PRODUCT BY ID
     *
     * GET /api/products/{id}
     *
     * @PathVariable extracts value from URL.
     */
    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    /**
     * DELETE PRODUCT
     *
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    /**
     * UPDATE PRODUCT
     *
     * PUT /api/products/{id}
     *
     * Combines:
     *  - path variable (id)
     *  - request body (updated data)
     */
    @PutMapping("/{id}")
    public ProductDTO updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO){
        return service.update(id, productDTO);
    }

    /**
     * CUSTOM QUERY:
     * Products more expensive than given price.
     *
     * GET /api/products/expensive/{price}
     */
    @GetMapping("/expensive/{price}")
    public List<ProductDTO> findExpensive(@PathVariable Double price){
        return service.findExpensiveProducts(price);
    }

    /**
     * CUSTOM QUERY:
     * Products with low stock.
     *
     * GET /api/products/low-stock/{stock}
     */
    @GetMapping("/low-stock/{stock}")
    public List<ProductDTO> findLowStock(@PathVariable Integer stock){
        return service.findLowStock(stock);
    }

    /**
     * SEARCH PRODUCTS BY NAME
     *
     * GET /api/products/search/{name}
     */
    @GetMapping("/search/{name}")
    public List<ProductDTO> searchByName(@PathVariable String name){
        return service.searchByName(name);
    }
}
