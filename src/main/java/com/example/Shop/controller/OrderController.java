package com.example.Shop.controller;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequest;
import com.example.Shop.entity.Order;
import com.example.Shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/orders") @RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    @PostMapping public OrderDTO create(@RequestBody OrderRequest request) { return service.createOrder(request); }

    @GetMapping
    public List<OrderDTO> getAll() { return service.getAll(); }
}