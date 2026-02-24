package com.example.Shop.service;

import com.example.Shop.dto.ProductDTO;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository reposity) {
        this.repository = reposity;
    }

    private ProductDTO toDto(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    public ProductDTO save(ProductDTO productDto) {
        Product product =  new Product(
                null,
                productDto.name(),
                productDto.description(),
                productDto.price(),
                productDto.stock(),
                null
        );
        return toDto(repository.save(product));
    }

    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
//                .map(product -> toDto(product))
                .toList();
    }

    public ProductDTO findById(Long id) throws ChangeSetPersister.NotFoundException {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Product was not found"));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ProductDTO update(Long id, ProductDTO productDTO) throws ChangeSetPersister.NotFoundException {

        Product existingProduct = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(productDTO.name());
        existingProduct.setDescription(productDTO.description());
        existingProduct.setPrice(productDTO.price());
        existingProduct.setStock(productDTO.stock());

        return toDto(repository.save(existingProduct));
    }

    public List<ProductDTO> findExpensiveProducts(Double price){
        return repository.findByPriceGreaterThan(price)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductDTO> findLowStock(Integer stock){
        return repository.findLowStock(stock)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ProductDTO> searchByName(String name){
        return repository.searchByNameNative(name)
                .stream()
                .map(this::toDto)
                .toList();
    }
}
