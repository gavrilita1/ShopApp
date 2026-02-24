package com.example.Shop.dto;

import java.util.Date;
import java.util.List;

public record OrderDTO(Long id, String customerName, List<String> productNames, Double total, Date createdAt) {
}
