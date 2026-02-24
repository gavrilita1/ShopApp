package com.example.Shop.controller;

import com.example.Shop.dto.UserDTO;
import com.example.Shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @PostMapping
    public UserDTO create(@RequestBody UserDTO dto) { return service.save(dto); }
    @GetMapping
    public List<UserDTO> getAll() { return service.findAll(); }
}