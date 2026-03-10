package com.example.Shop.dto;

import java.util.Date;

public record ReviewResposeDto(
        Long id,
        String username,
        String productname,
        Integer rating,
        String comment,
        Date createdAt
) { }
