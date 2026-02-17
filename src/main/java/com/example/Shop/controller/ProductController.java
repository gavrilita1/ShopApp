package com.example.Shop.controller;

import com.example.Shop.entity.Product;
import com.example.Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

//    public ProductController(ProductService service) {
//        this.service = service;
//    }

    @PostMapping
    public Product create(@RequestBody Product product){
        return service.save(product);
    }

    @GetMapping
    public Iterable<Product> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) throws ChangeSetPersister.NotFoundException {
        return service.update(id, product);
    }
}
