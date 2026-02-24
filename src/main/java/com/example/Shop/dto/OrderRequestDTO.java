package com.example.Shop.dto;

import java.util.List;

public record OrderRequestDTO(Long userId, List<Long> productIds) {
}
