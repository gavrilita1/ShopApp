package com.example.Shop.dto;

import java.util.List;

public record OrderRequest(Long userId, List<Long> productIds) {}
