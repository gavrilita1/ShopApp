package com.example.Shop.repository;

import com.example.Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByPriceGreaterThan(Double price);

    @Query("SELECT p FROM Product p where p.stock < :stock")
    List<Product> findLowStock(Integer stock);

    @Query(value = "SELECT * FROM products where name LIKE %:name%", nativeQuery = true)
    List<Product> searchByNameNative(String name);

}
