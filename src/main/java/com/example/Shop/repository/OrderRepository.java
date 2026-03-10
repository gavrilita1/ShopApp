package com.example.Shop.repository;

import com.example.Shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(
            """
            SELECT COUNT(o) > 0
            FROM Order o
            JOIN o.products p
            WHERE o.user.id = :userId
            AND p.id = :productId
            """
    )
    boolean existsOrderWithProduct(Long userId, Long productId);

}
