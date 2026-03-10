package com.example.Shop.service;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequestDTO;
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

    // Repository used to persist and retrieve Order entities
    private final OrderRepository orderRepository;

    // Repository used to fetch the user that places the order
    private final UserRepository userRepository;

    // Repository used to load products included in the order
    private final ProductRepository productRepository;

    /**
     * Creates a new order.
     *
     * INPUT:
     *  - OrderRequestDTO containing:
     *      userId -> the customer placing the order
     *      productIds -> list of products to be purchased
     *
     * PROCESS:
     *  1. Fetch user from DB
     *  2. Fetch all selected products
     *  3. Compute total price
     *  4. Create Order entity
     *  5. Persist order
     *  6. Map entity -> DTO
     *
     * OUTPUT:
     *  - OrderDTO returned to controller/API
     */
    @Transactional
    public OrderDTO createOrder(OrderRequestDTO request){

        // Load user or fail if user does not exist
        User user = userRepository.findById(request.userId())
                .orElseThrow(RuntimeException::new);

        // Load all products by their IDs
        List<Product> products = productRepository.findAllById(request.productIds());

        // Calculate total order price by summing product prices
        Double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // Create new Order entity (id = null because DB generates it)
        Order order = new Order(null, user, products, total, new Date());

        // Save order into database
        Order savedOrder = orderRepository.save(order);

        // Map entity data into DTO (API response object)
        return new OrderDTO(
                savedOrder.getId(),
                user.getUsername(),
                products.stream()
                        .map(Product::getName) // extract only product names for response
                        .toList(),
                total,
                savedOrder.getCreatedAt()
        );
    }

    /**
     * Returns all orders from database mapped as DTOs.
     *
     * INPUT: none
     * PROCESS: fetch -> map entity to DTO
     * OUTPUT: List<OrderDTO>
     */
    public List<OrderDTO> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(o -> new OrderDTO(
                        o.getId(),
                        o.getUser().getUsername(),
                        o.getProducts().stream()
                                .map(Product::getName)
                                .toList(),
                        o.getTotal(),
                        o.getCreatedAt()
                ))
                .toList();
    }
}