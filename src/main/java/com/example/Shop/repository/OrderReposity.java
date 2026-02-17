package com.example.Shop.repository;

import com.example.Shop.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReposity extends CrudRepository<Order,Long> {
}
