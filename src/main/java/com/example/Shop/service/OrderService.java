package com.example.Shop.service;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequest;
import com.example.Shop.entity.Order;
import com.example.Shop.entity.Product;
import com.example.Shop.entity.User;
import com.example.Shop.repository.OrderRepository;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDTO createOrder(OrderRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow();
        List<Product> products = productRepository.findAllById(request.productIds());

        Double total = products.stream().mapToDouble(Product::getPrice).sum();

        Order order = new Order(null, user, products, total, new Date());
        Order saved = orderRepository.save(order);

        return new OrderDTO(
                saved.getId(),
                user.getUsername(),
                products.stream().map(Product::getName).toList(),
                total,
                saved.getCreatedAt()
        );
    }

    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(o -> new OrderDTO(
                        o.getId(),
                        o.getUser().getUsername(),
                        o.getProducts().stream().map(Product::getName).toList(),
                        o.getTotal(),
                        o.getCreatedAt()))
                .toList();
    }
}