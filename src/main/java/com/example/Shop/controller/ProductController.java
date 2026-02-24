package com.example.Shop.controller;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDto){
        return service.save(productDto);
    }

    @GetMapping
    public List<ProductDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws ChangeSetPersister.NotFoundException {
        return service.update(id, productDTO);
    }

    @GetMapping("/expensive/{price}")
    public List<ProductDTO> findExpensive(@PathVariable Double price){
        return service.findExpensiveProducts(price);
    }

    @GetMapping("/low-stock/{stock}")
    public List<ProductDTO> findLowStock(@PathVariable Integer stock){
        return service.findLowStock(stock);
    }

    @GetMapping("/search/{name}")
    public List<ProductDTO> searchByName(@PathVariable String name){
        return service.searchByName(name);
    }
}
