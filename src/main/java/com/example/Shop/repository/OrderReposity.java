package com.example.Shop.repository;

import com.example.Shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReposity extends JpaRepository<Order,Long> {
}
