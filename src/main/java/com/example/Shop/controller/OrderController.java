package com.example.Shop.controller;

import com.example.Shop.entity.Order;
import com.example.Shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Order saveOrder(@RequestBody Map<String, Object> body){
        Long userID = Long.valueOf(body.get("userId").toString());
        List<Integer> productsIdsInteger = (List<Integer>) body.get("productIds");
        List<Long> productIds = productsIdsInteger.stream()
                .map(Long::valueOf)
                .toList();
        return service.createOrder(userID, productIds);
    }

}
