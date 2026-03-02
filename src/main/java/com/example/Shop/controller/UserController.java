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

    // Delegates operations to service layer
    private final UserService service;

    /**
     * CREATE USER
     *
     * POST /api/users
     *
     * JSON body -> UserRequestDTO automatically
     */
    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO dto){
        return service.createUser(dto);
    }

    /**
     * GET USER BY ID
     *
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id){
        return service.getById(id);
    }

    /**
     * GET ALL USERS
     *
     * GET /api/users
     */
    @GetMapping
    public List<UserResponseDTO> getAll(){
        return service.getAll();
    }

    /**
     * UPDATE USER
     *
     * PUT /api/users/{id}
     *
     * Receives:
     *  - id from URL
     *  - updated fields from request body
     */
    @PutMapping("/{id}")
    public UserResponseDTO update(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO dto){
        return service.update(id, dto);
    }

    /**
     * DELETE USER
     *
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}