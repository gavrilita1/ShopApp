package com.example.Shop.controller;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequestDTO;
import com.example.Shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public OrderDTO saveOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        return service.createOrder(orderRequestDTO);
    }

    @GetMapping
    public List<OrderDTO> getAllOrders(){
        return service.getAllOrders();
    }

}
