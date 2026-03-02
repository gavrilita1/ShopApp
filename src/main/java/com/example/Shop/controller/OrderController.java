package com.example.Shop.controller;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequestDTO;
import com.example.Shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Marks this class as a REST API controller.
// Spring automatically converts responses to JSON.
@RequestMapping("/api/orders")
// Base URL for all endpoints in this controller.
@RequiredArgsConstructor
// Lombok generates constructor for final fields (dependency injection)
public class OrderController {

    // Service layer containing business logic
    private final OrderService service;

    /**
     * CREATE ORDER
     *
     * HTTP: POST /api/orders
     *
     * INPUT (Request Body JSON):
     * {
     *   "userId": 1,
     *   "productIds": [1,2,3]
     * }
     *
     * PROCESS:
     * Controller receives HTTP request ->
     * converts JSON into OrderRequestDTO ->
     * delegates logic to Service layer.
     *
     * OUTPUT:
     * OrderDTO returned as JSON response.
     */
    @PostMapping
    public OrderDTO saveOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        return service.createOrder(orderRequestDTO);
    }

    /**
     * GET ALL ORDERS
     *
     * HTTP: GET /api/orders
     *
     * INPUT: none
     * OUTPUT: List<OrderDTO> as JSON array
     */
    @GetMapping
    public List<OrderDTO> getAllOrders(){
        return service.getAllOrders();
    }
}