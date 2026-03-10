package com.example.Shop.dto;

public record ReviewRequestDto(
        Long userId,
        Long productId,
        Integer rating,
        String comment
) { }
