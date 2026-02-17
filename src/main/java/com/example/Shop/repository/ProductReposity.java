package com.example.Shop.repository;

import com.example.Shop.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReposity extends CrudRepository<Product,Long> {
}
