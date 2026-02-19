package com.example.Shop.service;

import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductReposity reposity;

    @Autowired
    public ProductService(ProductReposity reposity) {
        this.reposity = reposity;
    }

    public Product save(Product product) {
        return reposity.save(product);
    }

    public Iterable<Product> findAll() {
        return reposity.findAll();
    }

    public Product findById(Long id) throws ChangeSetPersister.NotFoundException {
        return reposity.findById(id)
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }

    public void deleteById(Long id) {
        reposity.deleteById(id);
    }

    public Product update(Long id, Product product) throws ChangeSetPersister.NotFoundException {

        Product existingProduct = findById(id);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());

        return save(existingProduct);
    }

    public List<Product> findExpensiveProducts(Double price){
        return reposity.findByPriceGreaterThan(price);
    }

    public List<Product> findLowStock(Integer stock){
        return reposity.findLowStock(stock);
    }

    public List<Product> searchByName(String name){
        return reposity.searchByNameNative(name);
    }
}
