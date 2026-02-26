package com.example.Shop.controller;

import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.dto.UserUpdateDTO;
import com.example.Shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO dto){
        return service.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id){
        return service.getById(id);
    }

    @GetMapping
    public List<UserResponseDTO> getAll(){
        return service.getAll();
    }

    @PutMapping
    public UserResponseDTO update(@PathVariable Long id, @RequestBody UserUpdateDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
