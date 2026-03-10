package com.example.Shop.controller;

import com.example.Shop.dto.ReviewRequestDto;
import com.example.Shop.dto.ReviewResposeDto;
import com.example.Shop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ReviewResposeDto create(@RequestBody ReviewRequestDto dto){
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ReviewResposeDto getById(@PathVariable Long id){
        return service.getById(id);
    }

    @GetMapping
    public List<ReviewResposeDto> getAll(){
        return service.getAllReviews();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}
